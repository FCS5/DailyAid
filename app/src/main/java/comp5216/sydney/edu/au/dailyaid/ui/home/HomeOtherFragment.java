package comp5216.sydney.edu.au.dailyaid.ui.home;

import static com.google.android.gms.location.Granularity.GRANULARITY_PERMISSION_LEVEL;
import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import comp5216.sydney.edu.au.dailyaid.R;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DARequest;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeOtherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeOtherFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RequestRecyclerAdapter adapter;
    private DailyAidViewModel instanceDailyAidViewModel;
    private FusedLocationProviderClient mFusedLocationClient;
    private CurrentLocationRequest.Builder currentLocationRequestBuilder;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private String devicelocation;



    public HomeOtherFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    // create new instance for tab
    public static HomeOtherFragment newInstance() {
        return new HomeOtherFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_other, container, false);
    }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    recyclerView = view.findViewById(R.id.request_Emergency_Recycle_List);
    layoutManager = new LinearLayoutManager(view.getContext());
    recyclerView.setLayoutManager(layoutManager);

    adapter = new RequestRecyclerAdapter();
    recyclerView.setAdapter(adapter);

    //        DailyAidUser user = new DailyAidUser(
    //                "userName","adawdqw",true,
    //                0,0,100,0);
    //
    //        instanceDailyAidViewModel.createNewUser(user);
    //

    instanceDailyAidViewModel = new ViewModelProvider(this).get(DailyAidViewModel.class);

    // get device location
    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    currentLocationRequestBuilder =
        new CurrentLocationRequest.Builder()
            .setDurationMillis(10 * 1000)
            .setPriority(PRIORITY_HIGH_ACCURACY)
            .setMaxUpdateAgeMillis(5 * 1000)
            .setGranularity(GRANULARITY_PERMISSION_LEVEL);
    CurrentLocationRequest currentLocationRequest = currentLocationRequestBuilder.build();
    // get location permission
    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
      // reuqest for permission
      ActivityCompat.requestPermissions(
          getActivity(),
          new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
          },
          locationRequestCode);

    } else {
      //            mFusedLocationClient.requestLocationUpdates(locationRequest,
      // locationCallback,null);

      // already permission granted and get current location
      mFusedLocationClient
          .getCurrentLocation(currentLocationRequest, null)
          .addOnSuccessListener(
              result -> {
                devicelocation =
                    String.valueOf(result.getLatitude())
                        + ","
                        + String.valueOf(result.getLongitude());
                Log.i("currentLocationHERE", devicelocation);
                // implement viewmodel to access firebase
                FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
                mFirestore
                    .collection("requests")
                    .whereEqualTo("type", "Other")
                    .whereEqualTo("completed", false)
                    .get()
                    .addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                          @Override
                          public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                              List<DARequest> emergencyRequests = new ArrayList<DARequest>();
                              for (QueryDocumentSnapshot document : task.getResult()) {
                                DARequest request = document.toObject(DARequest.class);
                                emergencyRequests.add(request);

                                Log.d(
                                    "OtherList",
                                    document.getId() + " => " + document.getData());
                              }
                              List<DARequest> showRequests = new ArrayList<DARequest>();
                              for (DARequest emergency : emergencyRequests) {
                                if (getDistance(devicelocation, emergency.getLocation()) <= 5
                                    && getDistance(devicelocation, emergency.getLocation()) >= 0) {

                                  showRequests.add(emergency);
                                }
                              }
                              adapter.setRequestsList(showRequests);
                              adapter.notifyDataSetChanged();
                            } else {
                              Log.d(
                                  "EmergencyList",
                                  "Error getting documents: ",
                                  task.getException());
                            }
                          }
                        });
              })
          .addOnFailureListener(
              e -> {
                e.printStackTrace();
              });
    }
        }

    public double getDistance(String location1, String location2){
        String[] st1 = location1.split(",");
        String[] st2 = location2.split(",");
        //0 latitude ; 1 longitude
        double lat1 = Double.parseDouble(st1[0]);
        double lon1 = Double.parseDouble(st1[1]);
        double lat2 = Double.parseDouble(st2[0]);
        double lon2 = Double.parseDouble(st2[1]);
        // radius of earth in Km from google search
        final int Radius = 6371;
        double a = Math.toRadians(lat1 - lat2);
        double b = Math.toRadians(lon1 - lon2);
        double s = 2*Math.asin(Math.sqrt(Math.sin(a/2)*Math.sin(a/2)+Math.cos(lat1)*Math.cos(lat2)*
                Math.sin(b/2)*Math.sin(b/2)))*Radius;
        return s;
    }


}