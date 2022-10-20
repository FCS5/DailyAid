package comp5216.sydney.edu.au.dailyaid.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HomeTabAdapter extends FragmentPagerAdapter
{
    HomeFragment context;
    int totalTabs;

    public HomeTabAdapter(@NonNull FragmentManager fm, HomeFragment context) {
        super(fm);
        this.context = context;
    }


    // this is for  fragment tabs
    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return HomeEmergencyFragment.newInstance();

            case 1:
                return HomeOtherFragment.newInstance();

            default:
                return HomeEmergencyFragment.newInstance();
        }

    }


    @Override
    public int getCount() {
        return 2;
    }
}