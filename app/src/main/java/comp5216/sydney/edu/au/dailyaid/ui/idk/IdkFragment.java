package comp5216.sydney.edu.au.dailyaid.ui.idk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import comp5216.sydney.edu.au.dailyaid.databinding.FragmentIdkBinding;

public class IdkFragment extends Fragment {

    private FragmentIdkBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        IdkViewModel idkViewModel =
                new ViewModelProvider(this).get(IdkViewModel.class);

        binding = FragmentIdkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textIdk;
        idkViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}