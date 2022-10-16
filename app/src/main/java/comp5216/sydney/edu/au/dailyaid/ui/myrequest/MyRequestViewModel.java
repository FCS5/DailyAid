package comp5216.sydney.edu.au.dailyaid.ui.myrequest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyRequestViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MyRequestViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is myRequest fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}