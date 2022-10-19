package comp5216.sydney.edu.au.dailyaid.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.ui.auth.AuthUI;
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
        ImageView verify = binding.verify;

        // need to get the information from db
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name
            String name = user.getDisplayName();
            String uid = user.getUid();


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






            }
        });

//        final TextView textView = binding.detailName;
//        profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}