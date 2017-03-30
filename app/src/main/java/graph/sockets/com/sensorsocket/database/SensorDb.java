package graph.sockets.com.sensorsocket.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by sumandas on 24/03/2017.
 */

@Database(name = SensorDb.NAME, version = SensorDb.VERSION)
public class SensorDb {
    public static final String NAME = "SensorDb";
    public static final int VERSION = 1;
}
