package graph.sockets.com.sensorsocket.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Map;

import graph.sockets.com.sensorsocket.models.Sensor;
import graph.sockets.com.sensorsocket.models.SensorRange;

/**
 * Created by sumandas on 26/03/2017.
 */

public class SensorUtils {

    public static final String SUBSCRIBE = "subscribe";
    public static final String UNSUBSCRIBE = "unsubscribe";

    public static final int NO_DATA_POINTS =60*60;

    public static ArrayList<Sensor> getSensorRanges(Map<String,SensorRange> sensorRanges){
        ArrayList<Sensor> sensors =new ArrayList<>(sensorRanges.keySet().size());
        for(String sensorName:sensorRanges.keySet()){
            SensorRange sensorRange = sensorRanges.get(sensorName);
            Sensor sensor =new Sensor(sensorName,sensorRange.mMax,sensorRange.mMin);
            sensors.add(sensor);
        }
        return sensors;
    }

    @IntDef({SensorMessageType.INIT,SensorMessageType.UPDATE,SensorMessageType.DELETE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SensorMessageType {
        int INIT =0;
        int UPDATE =1;
        int DELETE =2;

    }


}
