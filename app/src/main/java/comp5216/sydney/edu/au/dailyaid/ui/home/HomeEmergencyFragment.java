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

import comp5216.sydney.edu.au.dailyaid.R;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidRequest;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidUser;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeEmergencyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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


        DailyAidRequest request = new DailyAidRequest(
                "name",1, 1,"good job","here","test",false);

        instanceDailyAidViewModel.createNewRequest(request);


        // implement viewmodel to access room database
        instanceDailyAidViewModel.getListAllRequests().observe(getViewLifecycleOwner(),newData->{
            Log.i("newData", String.valueOf(newData));
            adapter.setRequestsList(newData);
            adapter.notifyDataSetChanged();
        });


    }
}