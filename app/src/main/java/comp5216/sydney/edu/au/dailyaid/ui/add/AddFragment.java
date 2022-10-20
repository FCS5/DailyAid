package comp5216.sydney.edu.au.dailyaid.ui.add;

import static com.google.android.gms.location.Granularity.GRANULARITY_PERMISSION_LEVEL;
import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;
import static java.sql.Types.NULL;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import comp5216.sydney.edu.au.dailyaid.Navigator;
import comp5216.sydney.edu.au.dailyaid.R;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DARequest;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DAUser;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidDao;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidDatabase;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidRequest;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidViewModel;
import comp5216.sydney.edu.au.dailyaid.databinding.FragmentAddBinding;
import comp5216.sydney.edu.au.dailyaid.databinding.FragmentHomeBinding;

public class AddFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentAddBinding binding;
    DailyAidDao dao;
    EditText editTitle;
    EditText editDescription;
    double wayLatitude = 0.0;
    double wayLongitude = 0.0;
    private DailyAidViewModel instanceDailyAidViewModel;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    int locationRequestCode = 1000;
    private String currentLocation="";
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final String uid = user.getUid();
    private boolean completed = false;
    private final String TAG = "AddFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        AddViewModel addViewModel =
//                new ViewModelProvider(this).get(AddViewModel.class);

        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        editTitle = root.findViewById(R.id.editRequestName);
        editDescription = root.findViewById(R.id.editDescription);



        Spinner spinner = (Spinner) root.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        container.getContext(), R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);




        EditText editRequestName = binding.editRequestName;
        String requestName = editRequestName.getText().toString();

        EditText editDescription = binding.editDescription;
        String description = editDescription.getText().toString();
        instanceDailyAidViewModel = new ViewModelProvider(this).get(DailyAidViewModel.class);

        Button add = binding.addRequest;
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // upload data to db, generate a new request
                String type = spinner.getSelectedItem().toString();
                String title = editTitle.getText().toString();
                String description = editDescription.getText().toString();


                // position


                // get current location
                // location fetch using gps
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(root.getContext());
                CurrentLocationRequest.Builder currentLocationRequestBuilder = new CurrentLocationRequest.Builder().
                        setDurationMillis(10 * 1000).setPriority(PRIORITY_HIGH_ACCURACY).setMaxUpdateAgeMillis(5 * 1000).setGranularity(GRANULARITY_PERMISSION_LEVEL);
                CurrentLocationRequest currentLocationRequest =  currentLocationRequestBuilder.build();
//                locationRequest = LocationRequest.create();
//                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                locationRequest.setInterval(1* 1000);
//                locationCallback = new LocationCallback() {
//                    @Override
//                    public void onLocationResult(LocationResult locationResult) {
//                        if (locationResult == null) {
//                            return;
//                        }
//                        for (Location location : locationResult.getLocations()) {
//                            if (location != null) {
//
//                                wayLatitude = location.getLatitude();
//                                wayLongitude = location.getLongitude();
//                                try {
//                                    Thread.sleep(2*100);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                currentLocation = String.valueOf(location.getLatitude()) + ","+String.valueOf(location.getLongitude());
//
//                                Log.d("CurrentLocation",String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude));
//                                mFusedLocationClient.removeLocationUpdates(locationCallback);
//
//                            }
//                        }
//
//
//
//
//                    }
//                };



                // get location permission
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // reuqest for permission
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            locationRequestCode);

                } else {
//                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,null);
                    // already permission granted and get current location
                    mFusedLocationClient.getCurrentLocation(currentLocationRequest, null)
                            .addOnSuccessListener(result->{
                                String myCurrentLocation = result.toString();
                                Log.i("currentLocationHERE",myCurrentLocation);
                                String uploadLocation = String.valueOf(result.getLatitude()) + ","+ String.valueOf(result.getLongitude());
                                // change current location as readable address



                                // completed
                                DailyAidRequest  newRequest = new DailyAidRequest(title,uid,"",description,uploadLocation,type,completed);
                                DARequest newDARequest = new DARequest(title,uid,"",description,uploadLocation,type,completed);
                                //Upload to firebse
//                                instanceDailyAidViewModel.createNewRequest(newRequest);

                                try {
                                    // Run a task specified by a Runnable Object asynchronously.
                                    CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
                                        @Override
                                        public void run() {
                                            //read items from database
                                            long id = instanceDailyAidViewModel.createRequest(newRequest);
                                            newDARequest.setId((int)id);
                                            System.out.println("I'll run in a separate thread than the main thread.");
                                        }
                                    });

                                    // Block and wait for the future to complete
                                    future.get();
                                }
                                catch(Exception ex) {
                                    Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
                                }

                                FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
                                // add the request by id
                                mFirestore.collection("requests").document(Integer.toString(newDARequest.getId()))
                                        .set(newDARequest)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // add posted to for the user
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                DocumentReference docRef =
                                                        mFirestore.collection("users")
                                                                .document(user.getUid());
                                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        DAUser user =
                                                                documentSnapshot.toObject(DAUser.class);
                                                        user.setNumPosted(user.getNumPosted()+1);
                                                        // update user information
                                                        mFirestore.collection("users")
                                                                .document(user.getId())
                                                                .set(user)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.w(TAG, "Error writing document", e);
                                                                    }
                                                                });

                                                    }
                                                });
                                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error writing document", e);
                                            }
                                        });

                                Intent intent = new Intent(root.getContext(), Navigator.class);
                                startActivity(intent);
                            })
                            .addOnFailureListener(e -> {
                                e.printStackTrace();
                            });

                }

            }
        });

//        final TextView textView = binding.textAdd;
//        addViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



}