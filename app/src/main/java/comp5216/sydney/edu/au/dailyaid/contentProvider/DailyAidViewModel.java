package comp5216.sydney.edu.au.dailyaid.contentProvider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DailyAidViewModel extends AndroidViewModel {
    private DailyAidRepository instanceRepository;
    private LiveData<List<DailyAidRequest>> listAllRequests;
    private LiveData<List<DailyAidUser>> listAllUsers;

    public DailyAidViewModel(@NonNull Application application) {
        super(application);
        instanceRepository = new DailyAidRepository(application);
        listAllRequests = instanceRepository.getAllRequests();
        listAllUsers = instanceRepository.getAllUsers();
    }


    // methods
    public LiveData<List<DailyAidRequest>> getListAllRequests(){return listAllRequests;}

    public void createNewRequest(DailyAidRequest request) {
        instanceRepository.insertNewRequest(request);
    }

    public long createRequest(DailyAidRequest request){
        return instanceRepository.insertRequest(request);
    }
    public void createNewUser(DailyAidUser user) {
        instanceRepository.insertNewUser(user);
    }


}
