package graph.sockets.com.sensorsocket.ui.sensorlist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import graph.sockets.com.sensorsocket.R;
import graph.sockets.com.sensorsocket.models.Sensor;
import graph.sockets.com.sensorsocket.utils.FontTextView;

/**
 * Created by sumandas on 25/03/2017.
 */

public class SensorListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<Sensor> mSensors;

    private Context mContext;

    private ISensorsActionListener mListener;

    public SensorListAdapter(Context context, @NonNull ArrayList<Sensor> sensors, ISensorsActionListener listener) {
        mSensors = sensors;
        mContext = context;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.item_sensor_view, parent, false);
        return new SensorsViewHolder(mContext, rootView, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Sensor sensor = mSensors.get(position);

        ((SensorsViewHolder) holder).mSensor = sensor;
        ((SensorsViewHolder) holder).mSensorName.setText(sensor.mSensorName);
    }

    @Override
    public int getItemCount() {
        return mSensors.size();
    }

    public static class SensorsViewHolder extends RecyclerView.ViewHolder {

        public Sensor mSensor;
        public FontTextView mSensorName;
        public ImageView mSensorIcon;
        public Context mContext;

        public SensorsViewHolder(Context context, View itemView, ISensorsActionListener listener) {
            super(itemView);
            mSensorName = (FontTextView) itemView.findViewById(R.id.sensor_name);
            mSensorIcon = (ImageView) itemView.findViewById(R.id.sensor_image);
            mContext = context;
            itemView.setOnClickListener(view -> listener.onSensorClicked(mSensor));
        }

    }

    public interface ISensorsActionListener {
        void onSensorClicked(Sensor Sensor);
    }
}