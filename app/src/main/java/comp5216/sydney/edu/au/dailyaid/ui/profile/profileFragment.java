package comp5216.sydney.edu.au.dailyaid.ui.profile;

import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;

import org.junit.validator.TestClassValidator;
import org.w3c.dom.Text;

import comp5216.sydney.edu.au.dailyaid.databinding.FragmentProfileBinding;

public class profileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        profileViewModel profileViewModel =
//                new ViewModelProvider(this).get(profileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // need to get the information from db






        TextView detailName = binding.detailName;
        detailName.setText("abc");

        TextView detailCredit = binding.detailCredit;
        detailCredit.setText("90");

        TextView detailSuccess = binding.detailSuccess;
        detailSuccess.setText("2");

        TextView detailFailed = binding.detailFailed;
        detailFailed.setText("1");

        TextView detailPosted = binding.detailPosted;
        detailPosted.setText("5");

        ImageView verify = binding.verify;
        verify.setVisibility(View.INVISIBLE);

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