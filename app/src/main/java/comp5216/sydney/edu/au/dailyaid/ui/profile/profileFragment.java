package comp5216.sydney.edu.au.dailyaid.ui.profile;

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
import comp5216.sydney.edu.au.dailyaid.contentProvider.DAUser;
import comp5216.sydney.edu.au.dailyaid.databinding.FragmentProfileBinding;

public class profileFragment extends Fragment {

    final String TAG = "profile";
    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        profileViewModel profileViewModel =
//                new ViewModelProvider(this).get(profileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView detailName = binding.detailName;
        TextView detailCredit = binding.detailCredit;
        TextView detailSuccess = binding.detailSuccess;
        TextView detailFailed = binding.detailFailed;
        TextView detailPosted = binding.detailPosted;
        TextView detailEmail = binding.detailEmail;
        ImageView verify = binding.verify;

        // need to get the information from db
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, uid and email
            String name = user.getDisplayName();
            String uid = user.getUid();
            String email = user.getEmail();

            detailEmail.setText(email);
            detailName.setText(name);

            FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
            DocumentReference docRef = mFirestore.collection("users").document(uid);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    DAUser usr = documentSnapshot.toObject(DAUser.class);
                    detailCredit.setText(String.valueOf(usr.getCredit()));
                    detailSuccess.setText(String.valueOf(usr.getNumSuccess()));
                    detailFailed.setText(String.valueOf(usr.getNumFail()));
                    detailPosted.setText(String.valueOf(usr.getNumPosted()));
                }

            });
            verify.setVisibility(View.INVISIBLE);

        }
        Button logOut = binding.logOut;
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go back to MainActivity to log out?
                AuthUI.getInstance()
                        .signOut(view.getContext())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {

                                Intent intent = new Intent(root.getContext(),
                                        MainActivity.class);
                                startActivity(intent);
                            }
                        });
            }
        });


        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}