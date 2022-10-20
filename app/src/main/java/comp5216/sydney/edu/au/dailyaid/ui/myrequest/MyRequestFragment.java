package comp5216.sydney.edu.au.dailyaid.ui.myrequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import comp5216.sydney.edu.au.dailyaid.R;
import comp5216.sydney.edu.au.dailyaid.databinding.FragmentRequestBinding;

public class MyRequestFragment extends Fragment{

    private FragmentRequestBinding binding;
    TabLayout tabLayout;
    ViewPager viewPager;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRequestBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        tabLayout = (TabLayout) root.findViewById(R.id.requestTabLayout);
        viewPager = (ViewPager) root.findViewById(R.id.requestViewPager);

        // add the tabs
        tabLayout.addTab(tabLayout.newTab().setText("Posted"));
        tabLayout.addTab(tabLayout.newTab().setText("Accepted"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final RequestTabAdapter adapter = new RequestTabAdapter( getChildFragmentManager(), this);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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