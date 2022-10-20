package comp5216.sydney.edu.au.dailyaid.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import comp5216.sydney.edu.au.dailyaid.R;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DARequest;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidRequest;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidUser;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidViewModel;

public class HomeEmergencyFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RequestRecyclerAdapter adapter;
    private DailyAidViewModel instanceDailyAidViewModel;



    public HomeEmergencyFragment() {
        // Required empty public constructor
    }

    public static HomeEmergencyFragment newInstance() {
        return new HomeEmergencyFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_emergency, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.request_Emergency_Recycle_List);
        layoutManager =new LinearLayoutManager(view.getContext());
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


//        DailyAidRequest request = new DailyAidRequest(
//                "name",1, 1,"good job","here","test",false);

//        instanceDailyAidViewModel.createNewRequest(request);



        // implement viewmodel to access room database
//        instanceDailyAidViewModel.getListAllRequests().observe(getViewLifecycleOwner(),newData->{
//            Log.i("newData", String.valueOf(newData));
//            adapter.setRequestsList(newData);
//            adapter.notifyDataSetChanged();
//        });

        // implement viewmodel to access firebase
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("requests")
                .whereEqualTo("type", "Emergency")
                .whereEqualTo("completed",false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DARequest> emergencyRequests = new ArrayList<DARequest>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DARequest request = document.toObject(DARequest.class);
                                emergencyRequests.add(request);

                                Log.d("EmergencyList",
                                        document.getId() + " => " + document.getData());
                            }
                            List<DARequest> showRequests = new ArrayList<DARequest>();
                            for(DARequest emergency : emergencyRequests){
                                if(getDistance(devicelocation,emergency.getLocation())<=5 && getDistance(devicelocation,emergency.getLocation())){
                                    showRequests.add(emergency);
                                }
                            }
                            adapter.setRequestsList(showRequests);
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("EmergencyList", "Error getting documents: ", task.getException());
                        }
                    }
                });



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