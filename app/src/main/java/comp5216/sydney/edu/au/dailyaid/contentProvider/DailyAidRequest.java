package comp5216.sydney.edu.au.dailyaid.contentProvider;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName="dailyAidRequest",
        foreignKeys= {
                @ForeignKey(
                        entity = DailyAidUser.class,
                        parentColumns = "userId",
                        childColumns = "requesterId",
                        onDelete = CASCADE
                ),
                @ForeignKey(
                        entity = DailyAidUser.class,
                        parentColumns = "userId",
                        childColumns = "accepterId",
                        onDelete = CASCADE
                )})
public class DailyAidRequest {
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name="requestId")
        private int id;

        @NonNull
        @ColumnInfo(name="requestName")
        private String requestName;

        @ColumnInfo(name="requesterId")
        private int requesterId;

        @ColumnInfo(name="accepterId")
        private int accepterId;

        @NonNull
        @ColumnInfo(name="description")
        private String description;

        @NonNull
        @ColumnInfo(name="location")
        private String location;

        @NonNull
        @ColumnInfo(name="type")
        private String type;

        @NonNull
        @ColumnInfo(name="completed")
        private boolean completed;


        public DailyAidRequest( @NonNull String requestName, int requesterId,
                                int accepterId,
                                @NonNull String description, @NonNull String location,
                                @NonNull String type, boolean completed) {
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

        @NonNull
        public String getRequestName() {
                return requestName;
        }

        public void setRequestName(@NonNull String requestName) {
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

        @NonNull
        public String getDescription() {
                return description;
        }

        public void setDescription(@NonNull String description) {
                this.description = description;
        }

        @NonNull
        public String getLocation() {
                return location;
        }

        public void setLocation(@NonNull String location) {
                this.location = location;
        }

        @NonNull
        public String getType() {
                return type;
        }

        public void setType(@NonNull String type) {
                this.type = type;
        }

        public boolean isCompleted() {
                return completed;
        }

        public void setCompleted(boolean completed) {
                this.completed = completed;
        }
}

