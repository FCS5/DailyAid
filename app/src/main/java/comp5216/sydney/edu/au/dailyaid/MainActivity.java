package comp5216.sydney.edu.au.dailyaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.ColumnInfo;

import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidDao;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidDatabase;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidRequest;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidRequestRepository;
import comp5216.sydney.edu.au.dailyaid.contentProvider.DailyAidUser;

public class MainActivity extends AppCompatActivity {

    DailyAidDao dao;
    DailyAidDatabase db;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /** Gets distance between 2 users
     * location1 is the user's location
     * location2 is the request's location. */
    public double getDistance(String location1, String location2){
        //********************************************************
        // need to revise by location format in the db
        String[] st1 = location1.split(",");
        String[] st2 = location2.split(",");
        //0 latitude ; 1 longitude
        double lat1 = Double.parseDouble(st1[0]);
        double lon1 = Double.parseDouble(st1[1]);
        double lat2 = Double.parseDouble(st2[0]);
        double lon2 = Double.parseDouble(st2[1]);
        // radius of earth in Km from google search
        final int Radius = 6371;
        double a = Math.toRadians(lat1 - lat2);
        double b = Math.toRadians(lon1 - lon2);
        double s = 2*Math.asin(Math.sqrt(Math.sin(a/2)*Math.sin(a/2)+Math.cos(lat1)*Math.cos(lat2)*
                Math.sin(b/2)*Math.sin(b/2)))*Radius;
        return s;
    }

    /** Get the request nearby */
    public List<DailyAidRequest> getNearbyRequest(String userLocation){
        List<DailyAidRequest> sourceRequests = (List<DailyAidRequest>) dao.getAllRequests();
        List<DailyAidRequest> returnRequests = new ArrayList<DailyAidRequest>();
        if (sourceRequests != null){
            for (DailyAidRequest dar :sourceRequests){
                // <1km
                if (getDistance(dar.getLocation() , userLocation) <= 1){
                    returnRequests.add(dar);
                }
            }
            return returnRequests;
        } else {
            return null;
        }
    }

    /** Get request detail*/
    public String getRequestDetail(DailyAidRequest request){
        return request.getDescription();
    }

    /** Update the status of request after accepted */
    public void updateRequestStatusAfterAccepted(int requestId, int accepterId){
        DailyAidRequest request = dao.getRequest(requestId);
        request.setAccepterId(accepterId);
        dao.deleteRequestById(requestId);
        dao.addRequest(request);
    }

    /** Update the status of request after cancelled */
    public void updateRequestStatusAfterCancelled(int requestId, int accepterId){
        DailyAidRequest request = dao.getRequest(requestId);
        request.setAccepterId(0);
        dao.deleteRequestById(requestId);
        dao.addRequest(request);
    }

    /** get posted requests by userId */
    public List<DailyAidRequest> getPostedRequests(int userId){
        List<DailyAidRequest> sourceRequests = (List<DailyAidRequest>) dao.getAllRequests();
        List<DailyAidRequest> returnRequests = new ArrayList<DailyAidRequest>();
        if(sourceRequests != null){
            for (DailyAidRequest dar :sourceRequests){
                if(userId == dar.getRequesterId()){
                    returnRequests.add(dar);
                }
            }
            return returnRequests;
        } else {
            return null;
        }
    }

    /** get accepted requests by userId */
    public List<DailyAidRequest> getAcceptedRequests(int userId){
        List<DailyAidRequest> sourceRequests = (List<DailyAidRequest>) dao.getAllRequests();
        List<DailyAidRequest> returnRequests = new ArrayList<DailyAidRequest>();
        if(sourceRequests != null){
            for (DailyAidRequest dar :sourceRequests){
                if(userId == dar.getAccepterId()){
                    returnRequests.add(dar);
                }
            }
            return returnRequests;
        } else {
            return null;
        }
    }

    /** Get the user's location
     ************************************************/
    public String getDeviceLocation(){
        return "111,222";
    }

    /** Create new request */
    // default
    String requestName;
    int requesterId;
    int accepterId = 0;
    String description;
    String location;
    String type;
    boolean completed = false;
    public boolean addNewRequest(){
        //*******************************************
        // limit of requests like word number limit
        if(true){
            DailyAidRequest newRequest = new DailyAidRequest(requestName,requesterId,accepterId,
                    description,location,type,completed);
            dao.addRequest(newRequest);
            return true;
        }else{
           return false;
        }
    }

    /** signIn */
    public boolean signIn(String username , String password){
        List<DailyAidUser> sourceUsrList = (List<DailyAidUser>) dao.getAllUsers();
        if(sourceUsrList != null){
            for(DailyAidUser usr :sourceUsrList){
                if(usr.getUserName() == username){
                    if(usr.getHashedPassword() == password){
                        // verified
                        userId = usr.getId();
                        return true;
                    } else {
                        // wrong password
                        return false;
                    }
                }
            }
            // no such usr in db
        } else {
            // New db with no users
        }
        return false;
    }

    /** register */
    // default
    private String userName;
    private String password;
    private boolean isVerified = false;
    private int numSuccess = 0;
    private int numFail = 0;
    private int credit = 100;
    private int numPosted = 0;
    public boolean register(){
        //*******************************************
        // limit of requests like word number limit
        if(true){
            DailyAidUser usr = new DailyAidUser(userName,password,isVerified,numSuccess,numFail,
                    credit,numPosted);
            dao.addUser(usr);
            return true;
        }else{
            return false;
        }
    }

    /** get profile detail */
    public void getProfile(){
        DailyAidUser usr = dao.getUser(userId);
        //**********************************************
        usr.getCredit();
        usr.getUserName();
        usr.getNumFail();
        usr.getNumPosted();
        usr.getNumSuccess();
    }

    /** cancel request as a poster */
    public void cancelByPoster(int requestId){
        DailyAidUser usr = dao.getUser(userId);
        DailyAidRequest request = dao.getRequest(requestId);
        if(request.getRequesterId() == userId){
            if (request.getAccepterId() != 0){
                // inform the accepter
                //*********************************************
            } else {
                dao.deleteRequestById(requestId);
            }
        } else {
            // have no authority to operate others' request
            //**************************************************
        }

    }

    /** cancel request as a accepter */
    public void cancelByAccepter(int requestId){
        DailyAidUser usr = dao.getUser(userId);
        // credit of accepter -10
        usr.setCredit(usr.getCredit()-10);
        updateRequestStatusAfterCancelled(requestId,userId);
    }
}