package graph.sockets.com.sensorsocket.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Comparator;

import graph.sockets.com.sensorsocket.database.SensorDb;

/**
 * Created by sumandas on 24/03/2017.
 */
@Table(database = SensorDb.class)
public class Sensor extends BaseModel implements Parcelable {

    @Column
    @PrimaryKey
    public String mSensorName;

    @Column
    public float mMax;

    @Column
    public float mMin;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mSensorName);
        dest.writeFloat(this.mMax);
        dest.writeFloat(this.mMin);
    }

    public Sensor() {
    }

    public Sensor(String name, float max, float min){
        mSensorName = name;
        mMax = max;
        mMin = min;
    }

    protected Sensor(Parcel in) {
        this.mSensorName = in.readString();
        this.mMax = in.readFloat();
        this.mMin = in.readFloat();
    }

    public static final Creator<Sensor> CREATOR = new Creator<Sensor>() {
        @Override
        public Sensor createFromParcel(Parcel source) {
            return new Sensor(source);
        }

        @Override
        public Sensor[] newArray(int size) {
            return new Sensor[size];
        }
    };

    public static class NameAscendingComparator implements Comparator<Sensor> {
        @Override
        public int compare(Sensor lhs, Sensor rhs) {
            return lhs.mSensorName.trim().compareToIgnoreCase(rhs.mSensorName.trim());
           }
    }
}
