package comp5216.sydney.edu.au.dailyaid.contentProvider;

public class DAUser {

    private int id = 1;
    private String userName;
    private String hashedPassword;
    private boolean isVerified;
    private int numSuccess;
    private int numFail;
    private int credit;
    private int numPosted;


    public DAUser(){};
    public DAUser( int id, String userName, String hashedPassword,
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
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
