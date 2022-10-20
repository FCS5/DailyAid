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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import comp5216.sydney.edu.au.dailyaid.R;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DARequest;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidViewModel;
import comp5216.sydney.edu.au.dailyaid.ui.home.RequestRecyclerAdapter;

public class RequestPostFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RequestRecyclerAdapter adapter;
    private DailyAidViewModel instanceDailyAidViewModel;



    public RequestPostFragment() {
        // Required empty public constructor
    }

    public static RequestPostFragment newInstance() {
        return new RequestPostFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_post, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.request_Post_Recycle_List);
        layoutManager =new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RequestRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        // initialize the room view model
        instanceDailyAidViewModel = new ViewModelProvider(this).get(DailyAidViewModel.class);



        // implement viewmodel to access room database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = "default";
        if (user != null) {
            uid = user.getUid();
        }
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("requests")
                .whereEqualTo("requesterId", uid)
                .whereEqualTo("completed",false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DARequest> postRequests = new ArrayList<DARequest>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DARequest request = document.toObject(DARequest.class);
                                postRequests.add(request);
                                adapter.setRequestsList(postRequests);
                                adapter.notifyDataSetChanged();
                                Log.d("EmergencyList",
                                        document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("EmergencyList", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


}
