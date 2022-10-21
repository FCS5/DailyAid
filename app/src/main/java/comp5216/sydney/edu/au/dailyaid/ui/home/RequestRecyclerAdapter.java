package comp5216.sydney.edu.au.dailyaid.ui.home;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import comp5216.sydney.edu.au.dailyaid.R;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DARequest;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidRequest;
import comp5216.sydney.edu.au.dailyaid.ui.detail.RequestDetailFragment;



public class RequestRecyclerAdapter extends RecyclerView.Adapter<RequestRecyclerAdapter.ViewHolder> {

    private List<DARequest> requestsList;
    private String TAG = "RequestRecycleAdapter";
    private Geocoder geocoder;
    public  RequestRecyclerAdapter(){};




  public void setRequestsList(List<DARequest> newData) {
    requestsList = newData;
  }


  @NonNull
  @Override
  public RequestRecyclerAdapter.ViewHolder onCreateViewHolder(
      @NonNull ViewGroup parent, int viewType) {
    View v =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.request_layout, parent, false); // inflate recycler view layout
      geocoder = new Geocoder(v.getContext(), Locale.getDefault());


      return new ViewHolder(v);
  }

    @Override
    public void onBindViewHolder(@NonNull RequestRecyclerAdapter.ViewHolder holder, int position) {
        holder.title.setText(requestsList.get(position).getRequestName());
        holder.description.setText(requestsList.get(position).getDescription().substring(0, Math.min(requestsList.get(position).getDescription().length(), 30)));
        try{
            String[] strLocation = requestsList.get(position).getLocation().split(",");
            double lat = Double.parseDouble(strLocation[0]);
            double longt = Double.parseDouble(strLocation[1]);
            List<Address> addressList =
                    geocoder.getFromLocation(lat, longt, 1);
            // set current location

            String strNum =  addressList.get(0).getFeatureName();
            String strName =  addressList.get(0).getThoroughfare();
            String city =  addressList.get(0).getLocality();
            String postCode =  addressList.get(0).getPostalCode();

            String street="";
            if (strNum != null){
                street += strNum+ " ";
            }
            if(strName !=null){
                street += strName+ " ";
            }
            if(city !=null){
                street += city+ " ";
            }
            if(postCode !=null){
                street += postCode;
            }
            holder.location.setText(street);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        int id = requestsList.get(position).getId();
        String requesterId = requestsList.get(position).getRequesterId();
        final int fPosition = position;
        holder.itemView.setOnClickListener((v -> {
            // click to go to task detail page
            // first check whether there is a accepter

//            Toast.makeText(v,"The request has been accepted.",
//                    Toast.LENGTH_LONG);


            FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
            DocumentReference docRef =
                    mFirestore.collection("requests").document(Integer.toString(id));
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    DARequest request = documentSnapshot.toObject(DARequest.class);
                    String uid;
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    uid = user.getUid();
                    assert request != null;
                    if(request.getAccepterId().length() != 0 && ! request.getAccepterId().equals(uid)){
                        // Toast
//                        Snackbar.make(v,"The request has been accepted.",
//                                Snackbar.LENGTH_LONG).setAction("action",null).show();
                        Intent intent = new Intent(v.getContext(), RequestDetailFragment.class);
                        intent.putExtra("requestID",id);
                        v.getContext().startActivity(intent);
                    } else{
                        // start activity
                        Intent intent = new Intent(v.getContext(), RequestDetailFragment.class);
                        intent.putExtra("requestID",id);
                        v.getContext().startActivity(intent);
                    }
                }
            });



        }));
  }

  @Override
  public int getItemCount() {
    if (requestsList == null) return 0;
    else return requestsList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView description;
    public TextView location;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      title = itemView.findViewById(R.id.request_title);
      description = itemView.findViewById(R.id.request_description);
      location = itemView.findViewById(R.id.request_location);
    }
  }
}
