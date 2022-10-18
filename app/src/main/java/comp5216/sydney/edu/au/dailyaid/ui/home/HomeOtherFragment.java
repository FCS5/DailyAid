package comp5216.sydney.edu.au.dailyaid.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import comp5216.sydney.edu.au.dailyaid.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeOtherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeOtherFragment extends Fragment {

    public HomeOtherFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    // create new instance for tab
    public static HomeOtherFragment newInstance() {
        return new HomeOtherFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_other, container, false);
    }




}