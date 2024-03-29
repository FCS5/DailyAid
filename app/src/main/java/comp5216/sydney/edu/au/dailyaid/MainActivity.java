package comp5216.sydney.edu.au.dailyaid;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import comp5216.sydney.edu.au.dailyaid.contentProvider.DARequest;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DAUser;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidDao;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidDatabase;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidRequest;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidUser;
import comp5216.sydney.edu.au.dailyaid.databinding.ActivityNavigatorBinding;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "0";
    DailyAidDao dao;
    DailyAidDatabase db;
    int userId;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    List<String> documentId = new ArrayList<String>();
    List<DAUser> dauser = new ArrayList<DAUser>();
    ViewModel mViewModel;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build();
        signInLauncher.launch(signInIntent);



        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firestore and the main RecyclerView
        initFirestore();
    }



    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();

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

    // Choose authentication providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(
//            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String userId = user.getUid();
//            String email = user.getEmail();
//            String displayname = user.getDisplayName();
//            Log.d(TAG, "DocumentSnapshot added with ID: " );

            // check whether the user is in our firebase
            DocumentReference docRef = mFirestore.collection("users").document(userId);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // do nothing
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                            // create new user
                            addNewUser();
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });




            // start navigation
            Intent intent = new Intent(this,Navigator.class);
            startActivity(intent);










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



    /* dealing with selected items in the menu */
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//
//            case R.id.logout:
                // add data in firebase
//                DAUser user1 = new DAUser();
//                mFirestore.collection("users")
//                        .add(user1)
//                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w(TAG, "Error adding document", e);
//                            }
//                        });

                // get data by id == 1
//                mFirestore.collection("users")
//                        .whereEqualTo("id", 1)
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if(task.isSuccessful()){
//                                    for (QueryDocumentSnapshot document : task.getResult()){
//                                        Log.d(TAG, document.getId()+"=>" + document.getData());
//                                        documentId.add(document.getId());
//                                        Log.d(TAG, Integer.toString(documentId.size()));
//                                    }
//                                    Log.d(TAG, Integer.toString(documentId.size()));
//                                    for(String docu : documentId){
//                                        // Transfer to class format
//                                        DocumentReference docRef =
//                                                mFirestore.collection("users").document(docu);
//                                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                            @Override
//                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                                Log.d(TAG, "Success!!!!!!!");
//                                                dauser.add(documentSnapshot.toObject(DAUser.class));
//                                                Log.d(TAG, Integer.toString(dauser.size()));
//                                            }
//                                        });
//                                    }
//                                    // why size == 0 **************************************
//                                    Log.d(TAG, Integer.toString(dauser.size()));
//                                } else {
//                                    Log.d(TAG, "Error getting document: ", task.getException());
//                                }
//                            }
//                        });





                // get all idea from db
//                mFirestore.collection("users")
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        Log.d(TAG, document.getId() + " => " + document.getData());
//                                    }
//                                } else {
//                                    Log.d(TAG, "Error getting documents: ", task.getException());
//                                }
//                            }
//                        });

//
//                AuthUI.getInstance()
//                        .signOut(this)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            public void onComplete(@NonNull Task<Void> task) {
//                                // ...
//                            }
//                        });
//                Toast.makeText(this, "Log out successful", Toast.LENGTH_SHORT).show();
//                Intent signInIntent = AuthUI.getInstance()
//                        .createSignInIntentBuilder()
//                        .setAvailableProviders(providers)
//                        .build();
//                signInLauncher.launch(signInIntent);
//                return true;
//
//            default: return super.onOptionsItemSelected(item);
//        }
//    }

    /** Gets distance between 2 users
     * location1 is the user's location
     * location2 is the request's location. */
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

    /** Get the request nearby */
    public List<DailyAidRequest> getNearbyRequest(String userLocation){
        List<DailyAidRequest> sourceRequests = (List<DailyAidRequest>) dao.getAllRequests();
        List<DailyAidRequest> returnRequests = new ArrayList<DailyAidRequest>();
        if (sourceRequests != null){
            for (DailyAidRequest dar :sourceRequests){
                // <5km
                if (getDistance(dar.getLocation() , userLocation) <= 5 && getDistance(dar.getLocation() , userLocation) > 0){
                    returnRequests.add(dar);
                }
            }
            return returnRequests;
        } else {
            return null;
        }
    }

    /** Get request detail*/
    public String getRequestDetail(DailyAidRequest request){
        return request.getDescription();
    }









    /** Get the user's location
     ************************************************/
    public String getDeviceLocation(){
        return "111,222";
    }

    /** Create new request and upload to firebase */







    /** Add a new user */
    public void addNewUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // id, name and email from google
            String uid = user.getUid();
            String name = user.getDisplayName();
            String email = user.getEmail();
            // default
            int numSuccess = 0;
            int numFailed = 0;
            int credit = 100;
            int numPosted = 0;
            DAUser newUser = new DAUser(uid,name,numSuccess,numFailed,credit,numPosted,email);
            mFirestore.collection("users").document(uid)
                    .set(newUser)
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
    }

    /** get profile detail */
    public void getProfile(){
        DailyAidUser usr = dao.getUser(userId);
        //**********************************************
        usr.getCredit();
        usr.getUserName();
        usr.getNumFail();
        usr.getNumPosted();
        usr.getNumSuccess();
    }



}