package comp5216.sydney.edu.au.dailyaid.contentProvider;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="dailyAidUsers")
public class DailyAidUser {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (name="userId")
    private int id;

    @NonNull
    @ColumnInfo(name="userName")
    private String userName;

    @NonNull
    @ColumnInfo(name="hashedPassword")
    private String hashedPassword;

    @NonNull
    @ColumnInfo(name="isVerified")
    private boolean isVerified;

    @NonNull
    @ColumnInfo(name="numSuccess")
    private int numSuccess;

    @NonNull
    @ColumnInfo(name="numFail")
    private int numFail;

    @NonNull
    @ColumnInfo(name="credit")
    private int credit;

    @NonNull
    @ColumnInfo(name="numPosted")
    private int numPosted;


    public DailyAidUser( @NonNull String userName, @NonNull String hashedPassword,
                         boolean isVerified, int numSuccess, int numFail, int credit, int numPosted) {
        this.userName = userName;
        this.hashedPassword = hashedPassword;
        this.isVerified = isVerified;
        this.numSuccess = numSuccess;
        this.numFail = numFail;
        this.credit = credit;
        this.numPosted = numPosted;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    @NonNull
    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(@NonNull String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public int getNumSuccess() {
        return numSuccess;
    }

    public void setNumSuccess(int numSuccess) {
        this.numSuccess = numSuccess;
    }

    public int getNumFail() {
        return numFail;
    }

    public void setNumFail(int numFail) {
        this.numFail = numFail;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getNumPosted() {
        return numPosted;
    }

    public void setNumPosted(int numPosted) {
        this.numPosted = numPosted;
    }
}
