package comp5216.sydney.edu.au.dailyaid.ui.myrequest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import comp5216.sydney.edu.au.dailyaid.R;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidViewModel;
import comp5216.sydney.edu.au.dailyaid.ui.home.HomeEmergencyFragment;
import comp5216.sydney.edu.au.dailyaid.ui.home.RequestRecyclerAdapter;

public class RequestAcceptFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RequestRecyclerAdapter adapter;
    private DailyAidViewModel instanceDailyAidViewModel;



    public RequestAcceptFragment() {
        // Required empty public constructor
    }

    public static RequestAcceptFragment newInstance() {
        return new RequestAcceptFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_accept, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.request_Accept_Recycle_List);
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
        instanceDailyAidViewModel.getListAllRequests().observe(getViewLifecycleOwner(),newData->{
            Log.i("newData", String.valueOf(newData));
            adapter.setRequestsList(newData);
            adapter.notifyDataSetChanged();
        });


    }
}
