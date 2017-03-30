package graph.sockets.com.sensorsocket.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sumandas on 26/03/2017.
 */

public class SensorData {

    @SerializedName("key")
    public long mKey;

    @SerializedName("val")
    public String mValue;

    @SerializedName("scale")
    public String mScale;

    @SerializedName("sensor")
    public String mSensorName;

}
