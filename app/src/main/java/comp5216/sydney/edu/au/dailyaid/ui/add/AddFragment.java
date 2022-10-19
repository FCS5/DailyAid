package comp5216.sydney.edu.au.dailyaid.ui.add;

import static java.sql.Types.NULL;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import comp5216.sydney.edu.au.dailyaid.Navigator;
import comp5216.sydney.edu.au.dailyaid.R;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidDatabase;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidRequest;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidViewModel;
import comp5216.sydney.edu.au.dailyaid.databinding.FragmentAddBinding;
import comp5216.sydney.edu.au.dailyaid.databinding.FragmentHomeBinding;

public class AddFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentAddBinding binding;
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
                locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(1* 10);
                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult == null) {
                            return;
                        }
                        for (Location location : locationResult.getLocations()) {
                            if (location != null) {
                                wayLatitude = location.getLatitude();
                                wayLongitude = location.getLongitude();

                                currentLocation = String.valueOf(location.getLatitude()) + ","+String.valueOf(location.getLongitude());

                                Log.d("CurrentLocation",String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude));
                                mFusedLocationClient.removeLocationUpdates(locationCallback);


                                // completed
                                DailyAidRequest  newRequest = new DailyAidRequest(title,uid,"",description,currentLocation,type,completed);
                                instanceDailyAidViewModel.createNewRequest(newRequest);
                                Intent intent = new Intent(root.getContext(), Navigator.class);

                                startActivity(intent);

                            }
                        }
                    }
                };



                // get location permission
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // reuqest for permission
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            locationRequestCode);

                } else {
                    // already permission granted
                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,null);
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