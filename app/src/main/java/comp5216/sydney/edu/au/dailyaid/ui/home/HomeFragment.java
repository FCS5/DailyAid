package comp5216.sydney.edu.au.dailyaid.ui.home;

import static com.google.android.gms.location.Granularity.GRANULARITY_COARSE;
import static com.google.android.gms.location.Granularity.GRANULARITY_FINE;
import static com.google.android.gms.location.Granularity.GRANULARITY_PERMISSION_LEVEL;
import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;
import static com.google.android.gms.location.Priority.PRIORITY_LOW_POWER;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import comp5216.sydney.edu.au.dailyaid.R;
import comp5216.sydney.edu.au.dailyaid.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    TabLayout homeTabLayout;
    ViewPager viewPager;
    private TextView textLocation;

    // position
    private FragmentHomeBinding binding;
    private FusedLocationProviderClient mFusedLocationClient;
    private CurrentLocationRequest.Builder currentLocationRequestBuilder;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // fragment inflate
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        textLocation = root.findViewById(R.id.homeLocationID);


        homeTabLayout = (TabLayout) root.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) root.findViewById(R.id.viewPager);

        // add the tabs
        homeTabLayout.addTab(homeTabLayout.newTab().setText("Emergency"));
        homeTabLayout.addTab(homeTabLayout.newTab().setText("Others"));
        homeTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final HomeTabAdapter adapter = new HomeTabAdapter( getChildFragmentManager(), this);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(homeTabLayout));

        homeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });



        // location fetch using gps
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        currentLocationRequestBuilder = new CurrentLocationRequest.Builder().
                setDurationMillis(10*1000).setPriority(PRIORITY_HIGH_ACCURACY).setMaxUpdateAgeMillis(5*1000).setGranularity(GRANULARITY_PERMISSION_LEVEL);
        CurrentLocationRequest currentLocationRequest =  currentLocationRequestBuilder.build();

//        LocationRequest.Builder builder =
//                new LocationRequest.Builder(PRIORITY_LOW_POWER,1000)
//                        .setDurationMillis(20*1000)
//                        .setMaxUpdateAgeMillis(0)
//                        .setMaxUpdates(5)
//                        .setMinUpdateIntervalMillis(4000);
//        locationRequest = builder.build();
//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult == null) {
//                    return;
//                }
//                for (Location location : locationResult.getLocations()) {
//                    if (location != null) {
//                        wayLatitude = location.getLatitude();
//                        wayLongitude = location.getLongitude();
//                        Log.i("currentVal",String.valueOf(wayLatitude)+","+String.valueOf(wayLongitude));
//                        Geocoder geocoder = new Geocoder(root.getContext(), Locale.getDefault());
//                        try{
//                            List<Address> addressList =
//                                    geocoder.getFromLocation(wayLatitude, wayLongitude, 1);
//                            // set current location
//                            String street = addressList.get(0).getFeatureName() + " " + addressList.get(0).getThoroughfare() + " " + addressList.get(0).getLocality()+ " "+ addressList.get(0).getPostalCode();
//
//                            textLocation.setText(street);
//                        }
//                        catch (IOException e){
//                            e.printStackTrace();
//                        }
//
//                    }
//                }
//            }
//        };



        // get location permission
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // reuqest for permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);

        } else {
//            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,null);

            // already permission granted and get current location
            mFusedLocationClient.getCurrentLocation(currentLocationRequest, null)
                    .addOnSuccessListener(result->{
                        String myCurrentLocation = result.toString();
                        // change current location as readable address
                        Geocoder geocoder = new Geocoder(root.getContext(), Locale.getDefault());
                        try{
                            List<Address> addressList =
                                    geocoder.getFromLocation(result.getLatitude(), result.getLongitude(), 1);
                            // set current location
                            String street = addressList.get(0).getFeatureName() + " " + addressList.get(0).getThoroughfare() + " " + addressList.get(0).getLocality()+ " "+ addressList.get(0).getPostalCode();

                            textLocation.setText(street);
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                        Log.i("currentLocationHERE",myCurrentLocation);

                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                    });

        }


        return root;
    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}