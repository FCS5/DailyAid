package comp5216.sydney.edu.au.dailyaid.contentProvider;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {DailyAidUser.class, DailyAidRequest.class}, version = 1,exportSchema=false)
public abstract class DailyAidDatabase extends RoomDatabase {
    public static final String DAILYAID_DATABASE_NAME = "DailyAid_database";

    public abstract DailyAidDao dailyAidDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile DailyAidDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static DailyAidDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DailyAidDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    DailyAidDatabase.class, DAILYAID_DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
