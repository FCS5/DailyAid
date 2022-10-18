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
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String userId = user.getUid();
            String email = user.getEmail();
            String displayname = user.getDisplayName();
            Log.d(TAG, "DocumentSnapshot added with ID: " );

        // start navigation
            Intent intent = new Intent(this,Navigator.class);
            startActivity(intent);









            // Initialize Firestore and the main RecyclerView
            initFirestore();
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

    /** Update the status of request after accepted */
    public void updateRequestStatusAfterAccepted(int requestId, int accepterId){
        DailyAidRequest request = dao.getRequest(requestId);
        request.setAccepterId(accepterId);
        dao.deleteRequestById(requestId);
        dao.addRequest(request);
    }

    /** Update the status of request after cancelled */
    public void updateRequestStatusAfterCancelled(int requestId, int accepterId){
        DailyAidRequest request = dao.getRequest(requestId);
        request.setAccepterId(0);
        dao.deleteRequestById(requestId);
        dao.addRequest(request);
    }

    /** get posted requests by userId */
    public List<DailyAidRequest> getPostedRequests(int userId){
        List<DailyAidRequest> sourceRequests = (List<DailyAidRequest>) dao.getAllRequests();
        List<DailyAidRequest> returnRequests = new ArrayList<DailyAidRequest>();
        if(sourceRequests != null){
            for (DailyAidRequest dar :sourceRequests){
                if(userId == dar.getRequesterId()){
                    returnRequests.add(dar);
                }
            }
            return returnRequests;
        } else {
            return null;
        }
    }

    /** get accepted requests by userId */
    public List<DailyAidRequest> getAcceptedRequests(int userId){
        List<DailyAidRequest> sourceRequests = (List<DailyAidRequest>) dao.getAllRequests();
        List<DailyAidRequest> returnRequests = new ArrayList<DailyAidRequest>();
        if(sourceRequests != null){
            for (DailyAidRequest dar :sourceRequests){
                if(userId == dar.getAccepterId()){
                    returnRequests.add(dar);
                }
            }
            return returnRequests;
        } else {
            return null;
        }
    }

    /** Get the user's location
     ************************************************/
    public String getDeviceLocation(){
        return "111,222";
    }

    /** Create new request */
    // default
    String requestName;
    int requesterId = userId;
    int accepterId=0;
    String description;
    String location;
    String type;
    boolean completed = false;
    public boolean addNewRequest(){
        if(type.equals("Emergency") || type.equals("Normal")){
            DailyAidRequest newRequest = new DailyAidRequest(requestName,requesterId,accepterId,
                    description,location,type,completed);
            dao.addRequest(newRequest);
            return true;
        }else{
           return false;
        }
    }

    /** signIn */
    public boolean signIn(String username , String password){
        List<DailyAidUser> sourceUsrList = (List<DailyAidUser>) dao.getAllUsers();
        if(sourceUsrList != null){
            for(DailyAidUser usr :sourceUsrList){
                if(usr.getUserName() == username){
                    if(usr.getHashedPassword() == password){
                        // verified
                        userId = usr.getId();
                        return true;
                    } else {
                        // wrong password
                        Toast toast = Toast.makeText(getApplicationContext(),"Wrong password.",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        return false;
                    }
                }
            }
            // no such usr in db
        } else {
            // New db with no users
        }
        Toast toast = Toast.makeText(getApplicationContext(),"No such user.",
                Toast.LENGTH_SHORT);
        toast.show();
        return false;
    }

    /** logOut */
    public boolean logOut(){
        userId = 0;
        Toast toast = Toast.makeText(getApplicationContext(),"You have logged out.",
                Toast.LENGTH_SHORT);
        toast.show();
        return true;
    }

    /** register */
    // default
    private String userName;
    private String password;
    private boolean isVerified = false;
    private int numSuccess = 0;
    private int numFail = 0;
    private int credit = 100;
    private int numPosted = 0;
    public boolean register(){
        // verify password
        boolean upperFlag = false;
        boolean lowerFlag = false;
        boolean numberFlag = false;
        for( int i = 0 ; i < password.length() ; i++){
            char cc = password.charAt(i);
            if (Character.isUpperCase(cc)){
                upperFlag = true;
            }else if (Character.isLowerCase(cc)){
                lowerFlag = true;
            }else if (Character.isDigit(cc)){
                numberFlag = true;
            }
        }
        // Layout give hints************************************************
        // username should contain at least 6 characters
        // password must contain at least a lower case, an upper case and a number and the length
        // should longer than 6
        if(userName.length() >= 6 && password.length() > 6 && upperFlag && lowerFlag && numberFlag){
            DailyAidUser usr = new DailyAidUser(userName,password,isVerified,numSuccess,numFail,
                    credit,numPosted);
            dao.addUser(usr);
            return true;
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),"invalid format",
                    Toast.LENGTH_SHORT);
            toast.show();
            return false;
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

    /** cancel request as a poster */
    public void cancelByPoster(int requestId){
        DailyAidUser usr = dao.getUser(userId);
        DailyAidRequest request = dao.getRequest(requestId);
        if(request.getRequesterId() == userId){
            if (request.getAccepterId() != 0){
                // inform the accepter
                //*********************************************
            } else {
                dao.deleteRequestById(requestId);
            }
        } else {
            // have no authority to operate others' request
            Toast toast = Toast.makeText(getApplicationContext(),"You don't have the right to " +
                    "modify requests " +
                    "created by others", Toast.LENGTH_LONG);
            toast.show();
        }

    }

    /** cancel request as a accepter */
    public void cancelByAccepter(int requestId){
        DailyAidUser usr = dao.getUser(userId);
        // credit of accepter -10
        usr.setCredit(usr.getCredit()-10);
        updateRequestStatusAfterCancelled(requestId,userId);
    }
}