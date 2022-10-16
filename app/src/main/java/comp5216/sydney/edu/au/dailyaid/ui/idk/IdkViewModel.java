package comp5216.sydney.edu.au.dailyaid.ui.idk;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IdkViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public IdkViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is idk fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}