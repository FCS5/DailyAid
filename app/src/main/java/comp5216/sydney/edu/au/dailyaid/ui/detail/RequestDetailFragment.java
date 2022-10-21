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

import java.io.IOException;
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
        TextView detailAccepter = (TextView) findViewById(R.id.detailAccepter);
        TextView detailType = (TextView) findViewById(R.id.detailType);
        TextView detailDescription = (TextView) findViewById(R.id.detailDescription);
        Button accept = (Button) findViewById(R.id.accept);
        Button cancel = (Button) findViewById(R.id.cancel);

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
                detailRequester.setText(request.getRequesterId());
                detailAccepter.setText(request.getAccepterId());
                // current user is the requester
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(detailRequester.getText().toString().equals(user.getUid())){
                    accept.setText("Complete");
                    // Exist accepter
                    if(detailAccepter.length() != 0){
                        // complete button
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // update request information (completed -> true)
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        DARequest request = documentSnapshot.toObject(DARequest.class);
                                        request.setCompleted(true);
                                        // set the request by id
                                        mFirestore.collection("requests").document(Integer.toString(id))
                                                .set(request)
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
                                        // update accepter information (credit+1, success+1)
                                        mFirestore.collection("users").document(request.getAccepterId())
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        DAUser user =
                                                                documentSnapshot.toObject(DAUser.class);
                                                        user.setNumSuccess(user.getNumSuccess()+1);
                                                        user.setCredit(user.getCredit()+1);
                                                        // upload accepter
                                                        mFirestore.collection("users").document(request.getAccepterId())
                                                                .set(user)
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
                                });
                            }
                        });
                        // cancel button
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // cancel the request
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                // delete request in firebase
                                docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // update poster information (credit-1)
                                                mFirestore.collection("users")
                                                        .document(user.getUid())
                                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                DAUser currentUser =
                                                                        documentSnapshot.toObject(DAUser.class);
                                                                currentUser.setCredit(currentUser.getCredit()-1);
                                                                mFirestore.collection("users")
                                                                        .document(user.getUid())
                                                                        .set(currentUser)
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
                                                        });
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
                    // No accepter
                    else{
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // delete the request
                                docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        finish();
                                    }
                                });
                            }
                        });
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // update request information (completed -> true)
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        DARequest request = documentSnapshot.toObject(DARequest.class);
                                        request.setCompleted(true);
                                        // set the request by id
                                        mFirestore.collection("requests").document(Integer.toString(id))
                                                .set(request)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        finish();
                                                        Log.d(TAG, "DocumentSnapshot successfully update!");
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
                        });

                    }
                }
                // current user is the accepter
                if(detailAccepter.getText().toString().equals(user.getUid())){
                    accept.setText("Complete");
                    // cancel button
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // cancel the request
                            // update request information (accepter -> "")
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    DARequest request = documentSnapshot.toObject(DARequest.class);
                                    request.setAccepterId("");
                                    // upload
                                    docRef.set(request)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    // update accepter information ( credit-1, fail+1)
                                                    mFirestore.collection("users")
                                                            .document(user.getUid())
                                                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                    DAUser currentUser = documentSnapshot.toObject(DAUser.class);
                                                                    currentUser.setNumFail(currentUser.getNumFail()+1);
                                                                    currentUser.setCredit(currentUser.getCredit()-1);
                                                                    mFirestore.collection("users")
                                                                            .document(user.getUid())
                                                                            .set(currentUser)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    Snackbar.make(view,"Success",
                                                                                            Snackbar.LENGTH_LONG).setAction("action",null).show();
                                                                                    finish();
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
                                                            });
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
                            });
                        }
                    });
                    // complete button
                    accept.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            // update request information (completed -> true)
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    DARequest request = documentSnapshot.toObject(DARequest.class);
                                    request.setCompleted(true);
                                    // set the request by id
                                    mFirestore.collection("requests").document(Integer.toString(id))
                                            .set(request)
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
                                    // update accepter information (credit+1, success+1)
                                    mFirestore.collection("users").document(request.getAccepterId())
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    DAUser user =
                                                            documentSnapshot.toObject(DAUser.class);
                                                    user.setNumSuccess(user.getNumSuccess()+1);
                                                    user.setCredit(user.getCredit()+1);
                                                    // upload accepter
                                                    mFirestore.collection("users").document(request.getAccepterId())
                                                            .set(user)
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
                            });
                        }
                    });
                }
                // other user
                if(!detailAccepter.getText().toString().equals(user.getUid()) && !detailRequester.getText().toString().equals(user.getUid())){
                    cancel.setVisibility(View.INVISIBLE);
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
                }
                DocumentReference docR =
                        mFirestore.collection("users").document(request.getRequesterId());
                docR.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DAUser user = documentSnapshot.toObject(DAUser.class);
                        detailRequester.setText(String.valueOf(user.getUserName()));

                    }
                });
                try{
                                    DocumentReference docR1 =
                        mFirestore.collection("users").document(request.getAccepterId());
                docR1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                DAUser user = documentSnapshot.toObject(DAUser.class);
                                detailAccepter.setText(String.valueOf(user.getUserName()));
                            }
                        });
                }
                catch (Exception e){
                    e.printStackTrace();
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
