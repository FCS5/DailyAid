package comp5216.sydney.edu.au.dailyaid.contentProvider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface DailyAidDao {
    // User
    @Query("select * from dailyAidUsers")
    LiveData<List<DailyAidUser>> getAllUsers();

    // get by user name
    @Query("select * from dailyAidUsers where userName=:name")
    List<DailyAidUser> getUser(String name);

    // get by user id
    @Query("select * from dailyAidUsers where userId=:id")
    List<DailyAidUser> getUser(int id);

    @Insert
    void addUser(DailyAidUser user);

    @Query("delete from dailyAidUsers where userName=:name")
    void deleteUserByName(String name);

    @Query("delete from dailyAidUsers where userId=:id")
    void deleteUserById(int id);

    // REQUEST
    @Query("select * from dailyAidRequest where completed is 'false'")
    LiveData<List<DailyAidRequest>> getAllRequests();

    // get by request id
    @Query("select * from dailyAidRequest where requestId=:id")
    List<DailyAidRequest> getRequest(int id);

    // get by request type
    @Query("select * from dailyAidRequest where type=:type and completed is 'false'")
    List<DailyAidRequest> getRequestByType(String type);

    @Query("select * from dailyAidRequest where type=:type and location=:location and completed is 'false'")
    List<DailyAidRequest> getRequestByType(String type, String location);

    @Insert
    void addRequest(DailyAidRequest request);

    @Query("delete from dailyAidRequest where requestId=:id")
    void deleteRequestById(int id);

}
