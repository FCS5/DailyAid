package comp5216.sydney.edu.au.dailyaid.ui.myrequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import comp5216.sydney.edu.au.dailyaid.databinding.FragmentMyrequestBinding;

public class MyRequestFragment extends Fragment {

    private FragmentMyrequestBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyRequestViewModel myRequestViewModel =
                new ViewModelProvider(this).get(MyRequestViewModel.class);

        binding = FragmentMyrequestBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMyrequest;
        myRequestViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}