package comp5216.sydney.edu.au.dailyaid.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
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
import comp5216.sydney.edu.au.dailyaid.contentProvider.DARequest;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DAUser;
import comp5216.sydney.edu.au.dailyaid.databinding.RequestDetailBinding;

public class RequestDetailFragment extends Fragment{

    final String TAG = "requestDetail";
    private RequestDetailBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        profileViewModel profileViewModel =
//                new ViewModelProvider(this).get(profileViewModel.class);

        binding = RequestDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView requestName = binding.detailRequest;
        TextView detailRequester = binding.detailRequester;
        TextView detailAccepter = binding.detailAccepter;
        TextView detailType = binding.detailType;
        TextView detailDescription = binding.detailDescription;

        int id = getActivity().getIntent().getExtras().getInt("requestID");

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = mFirestore.collection("requests").document(Integer.toString(id));
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DARequest request = documentSnapshot.toObject(DARequest.class);
                requestName.setText(request.getRequestName());
                detailRequester.setText(String.valueOf(request.getRequesterId()));
                detailType.setText(request.getType());
                detailDescription.setText(request.getDescription());
            }

        });

        Button accept = binding.accept;
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
                            request.setAccepterId(Integer.parseInt(uid));
                            // delete the request by id
                            mFirestore.collection("requests").document(Integer.toString(id))
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error deleting document", e);
                                        }
                                    });
                            // add the request by id
                            mFirestore.collection("requests")
                                    .add(request)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                        }

                    });





                    // add new into request








                    // delete the request
                }

            }
        });

//        final TextView textView = binding.detailName;
//        profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    List<AuthUI.IdpConfig> providers = Arrays.asList(
//            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == -1) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String userId = user.getUid();
//            String email = user.getEmail();
//            String displayname = user.getDisplayName();
//            Log.d(TAG, "DocumentSnapshot added with ID: " );


            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build();
            signInLauncher.launch(signInIntent);
            Log.d(TAG, "Try again. " );
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
