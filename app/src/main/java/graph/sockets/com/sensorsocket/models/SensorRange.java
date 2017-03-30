package graph.sockets.com.sensorsocket.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sumandas on 24/03/2017.
 */

public class SensorRange implements Parcelable {

    @SerializedName("max")
    public float mMax;

    @SerializedName("min")
    public float mMin;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.mMax);
        dest.writeFloat(this.mMin);
    }

    public SensorRange() {
    }

    protected SensorRange(Parcel in) {
        this.mMax = in.readFloat();
        this.mMin = in.readFloat();
    }

    public static final Creator<SensorRange> CREATOR = new Creator<SensorRange>() {
        @Override
        public SensorRange createFromParcel(Parcel source) {
            return new SensorRange(source);
        }

        @Override
        public SensorRange[] newArray(int size) {
            return new SensorRange[size];
        }
    };
}


