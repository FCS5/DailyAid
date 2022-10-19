package comp5216.sydney.edu.au.dailyaid.contentProvider;



public class DARequest {

    private int id;
    private String requestName;
    private int requesterId;
    private int accepterId;
    private String description;
    private String location;
    private String type;
    private boolean completed;

    public DARequest( String requestName, int requesterId,
                            int accepterId,
                            String description, String location,
                            String type, boolean completed) {
        this.requestName = requestName;
        this.requesterId = requesterId;
        this.accepterId = accepterId;
        this.description = description;
        this.location = location;
        this.type = type;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public int getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(int requesterId) {
        this.requesterId = requesterId;
    }

    public int getAccepterId() {
        return accepterId;
    }

    public void setAccepterId(int accepterId) {
        this.accepterId = accepterId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

