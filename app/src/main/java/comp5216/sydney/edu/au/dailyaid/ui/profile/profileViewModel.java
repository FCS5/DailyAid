package comp5216.sydney.edu.au.dailyaid.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class profileViewModel extends ViewModel {

    private final MutableLiveData<String> detailName;

    public profileViewModel() {
        detailName = new MutableLiveData<>();
        detailName.setValue("This is profile fragment");
    }

    public LiveData<String> getText() {
        return detailName;
    }


}