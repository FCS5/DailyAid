package comp5216.sydney.edu.au.dailyaid.contentProvider;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class DailyAidRequestRepository {
    private DailyAidDao instanceDailyAidDao;
    private LiveData<List<DailyAidUser>> instanceAllUsers;
    private LiveData<List<DailyAidRequest>> instanceAllRequests;

    DailyAidRequestRepository(Application application) {
        DailyAidDatabase db = DailyAidDatabase.getDatabase(application);
        instanceDailyAidDao = db.dailyAidDao();
        instanceAllUsers = instanceDailyAidDao.getAllUsers();
        instanceAllRequests = instanceDailyAidDao.getAllRequests();
    }
    LiveData<List<DailyAidUser>> getAllUsers() {
        return instanceAllUsers;
    }

    void insert(DailyAidUser user) {
        DailyAidDatabase.databaseWriteExecutor.execute(() -> instanceDailyAidDao.addUser(user));
    }

    void deleteUserById(int id) {
        DailyAidDatabase.databaseWriteExecutor.execute(() -> instanceDailyAidDao.deleteUserById(id));
    }

    LiveData<List<DailyAidRequest>> getAllRequests() {
        return instanceAllRequests;
    }

    void insert(DailyAidRequest request) {
        DailyAidDatabase.databaseWriteExecutor.execute(() -> instanceDailyAidDao.addRequest(request));
    }

    void deleteRequestById(int id) {
        DailyAidDatabase.databaseWriteExecutor.execute(() -> instanceDailyAidDao.deleteRequestById(id));
    }


}
