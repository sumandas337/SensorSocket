package graph.sockets.com.sensorsocket.database;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import graph.sockets.com.sensorsocket.models.Sensor;
import graph.sockets.com.sensorsocket.models.SensorRange;
import graph.sockets.com.sensorsocket.models.Sensor_Table;
import rx.Observable;

/**
 * Created by sumandas on 24/03/2017.
 */

public class SensorDbManager {

    private static volatile SensorDbManager sSensorManager;

    public static SensorDbManager initializeManager() {
        if (sSensorManager == null) {
            synchronized (SensorDbManager.class) {
                if (sSensorManager == null) {
                    sSensorManager = new SensorDbManager();
                }
            }
        }
        return sSensorManager;
    }

    public static SensorDbManager getInstance() {
        if (sSensorManager == null) {
            throw new IllegalStateException("Manager not initialized . Call initializeManager");
        } else {
            return sSensorManager;
        }
    }

    private SensorDbManager(){

    }

    public Observable<ArrayList<Sensor>> getSensors() {
        return Observable.create(subscriber -> {
            List<Sensor> sensors = SQLite.select()
                    .from(Sensor.class)
                    .queryList();
            subscriber.onNext((ArrayList<Sensor>) sensors);
            subscriber.onCompleted();
        });
    }

    public  Sensor getSensorByName(String name) {
        return SQLite.select().from(Sensor.class)
                .where(Sensor_Table.mSensorName.eq(name))
                .querySingle();

    }

    public  void persistSensors(Map<String,SensorRange> sensorMap) {
        for(String sensorName:sensorMap.keySet()){
            SensorRange sensorRange = sensorMap.get(sensorName);
            Sensor sensor =new Sensor(sensorName,sensorRange.mMax,sensorRange.mMin);
            sensor.save();
        }
    }
}
