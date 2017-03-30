package graph.sockets.com.sensorsocket.ui.sensorlist;

import java.util.ArrayList;

import graph.sockets.com.sensorsocket.base.IBasePresenter;
import graph.sockets.com.sensorsocket.base.IBaseView;
import graph.sockets.com.sensorsocket.models.Sensor;
import graph.sockets.com.sensorsocket.models.SensorData;
import graph.sockets.com.sensorsocket.models.SensorInit;

/**
 * Created by sumandas on 25/03/2017.
 */

public interface ISensorContract {

    interface ISensorListView extends IBaseView {
        void onSensorListLoaded(ArrayList<Sensor> sensors);
        void onSensorListLoadFaile(String message);
        void onSensorClicked(Sensor sensor);
    }

    interface ISensorListPresenter extends IBasePresenter {
        void loadSensors();
    }

    interface ISensorGraphView extends IBaseView{
        void onSensorDataInit(SensorInit initData);
        void onSensorDataUpdate(SensorData sensorData);
        void onSensorDataDelete(SensorData sensorData);
    }

    interface ISensorGraphPresenter extends IBasePresenter {
        void sensorDataInit(SensorInit initData);
        void sensorDataUpdate(SensorData sensorData);
        void sensorDataDelete(SensorData sensorData);
    }
}
