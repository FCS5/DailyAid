package comp5216.sydney.edu.au.dailyaid.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.validator.TestClassValidator;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

import comp5216.sydney.edu.au.dailyaid.MainActivity;
import comp5216.sydney.edu.au.dailyaid.Navigator;
import comp5216.sydney.edu.au.dailyaid.R;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DARequest;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DAUser;
import comp5216.sydney.edu.au.dailyaid.databinding.RequestDetailBinding;

public class RequestDetailFragment extends AppCompatActivity {

    final String TAG = "requestDetail";
    private RequestDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



//        profileViewModel profileViewModel =
//                new ViewModelProvider(this).get(profileViewModel.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_detail);
        binding = RequestDetailBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        Toast.makeText(this,"The request has been accepted.", Toast.LENGTH_LONG);

        TextView requestName = (TextView) findViewById(R.id.detailRequest);
        TextView detailRequester = (TextView) findViewById(R.id.detailRequester);
        TextView detailAccepter = binding.detailAccepter;
        TextView detailType = (TextView) findViewById(R.id.detailType);
        TextView detailDescription = (TextView) findViewById(R.id.detailDescription);

        int id = getIntent().getExtras().getInt("requestID");

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = mFirestore.collection("requests").document(Integer.toString(id));
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DARequest request = documentSnapshot.toObject(DARequest.class);
                requestName.setText(request.getRequestName());
                detailType.setText(request.getType());
                detailDescription.setText(request.getDescription());
                DocumentReference docR =
                        mFirestore.collection("users").document(request.getRequesterId());
                docR.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DAUser user = documentSnapshot.toObject(DAUser.class);
                        detailRequester.setText(String.valueOf(user.getUserName()));
                    }
                });

            }

        });


        Button accept = (Button) findViewById(R.id.accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // revise the information in firebase, add the accepter
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    String uid = user.getUid();
                    // get request from database
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            DARequest request = documentSnapshot.toObject(DARequest.class);
                            request.setAccepterId(uid);
                            // set the request by id
                            mFirestore.collection("requests").document(Integer.toString(id))
                                    .set(request)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Snackbar.make(view,"Success",
                                                    Snackbar.LENGTH_LONG).setAction("action",null).show();
                                            finish();
                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error deleting document", e);
                                        }
                                    });

                        }
                    });
                }

            }
        });


        ImageButton goBack = (ImageButton) findViewById(R.id.goBack);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





//        final TextView textView = binding.detailName;
//        profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
    }





}
