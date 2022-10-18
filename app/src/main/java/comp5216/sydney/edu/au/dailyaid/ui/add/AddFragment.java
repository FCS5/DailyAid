package comp5216.sydney.edu.au.dailyaid.ui.add;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import comp5216.sydney.edu.au.dailyaid.databinding.FragmentAddBinding;

public class AddFragment extends Fragment {

    private FragmentAddBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        AddViewModel addViewModel =
//                new ViewModelProvider(this).get(AddViewModel.class);

        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EditText editRequestName = binding.editRequestName;
        String requestName = editRequestName.getText().toString();

        EditText editDescription = binding.editDescription;
        String description = editDescription.getText().toString();

        Button add = binding.addRequest;
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // upload data to db, generate a new request








            }
        });

//        final TextView textView = binding.textAdd;
//        addViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}