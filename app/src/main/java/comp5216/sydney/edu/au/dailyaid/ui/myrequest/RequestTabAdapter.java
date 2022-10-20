package comp5216.sydney.edu.au.dailyaid.ui.myrequest;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class RequestTabAdapter extends FragmentPagerAdapter
{
    MyRequestFragment context;

    public RequestTabAdapter(@NonNull FragmentManager fm, MyRequestFragment context) {
        super(fm);
        this.context = context;
    }


    // this is for  fragment tabs
    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return RequestPostFragment.newInstance();

            case 1:
                return RequestAcceptFragment.newInstance();

            default:
                return RequestPostFragment.newInstance();
        }

    }


    @Override
    public int getCount() {
        return 2;
    }
}