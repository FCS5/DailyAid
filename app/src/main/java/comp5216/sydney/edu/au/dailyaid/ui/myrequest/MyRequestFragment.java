package comp5216.sydney.edu.au.dailyaid.ui.myrequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import comp5216.sydney.edu.au.dailyaid.R;
import comp5216.sydney.edu.au.dailyaid.databinding.FragmentMyrequestBinding;

public class MyRequestFragment extends Fragment{

    private FragmentMyrequestBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyrequestBinding.inflate(inflater, container, false);
        View root = binding.getRoot();






        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}