package graph.sockets.com.sensorsocket.application;


import graph.sockets.com.sensorsocket.database.SensorDbManager;
import graph.sockets.com.sensorsocket.net.RestClient;
import graph.sockets.com.sensorsocket.ui.sensorlist.SensorGraphActivity;
import graph.sockets.com.sensorsocket.ui.sensorlist.SensorListActivity;
import graph.sockets.com.sensorsocket.utils.Navigator;
import graph.sockets.com.sensorsocket.utils.RxBus;

/**
 * Created by sumandas on 29/01/2017.
 */


public interface IAppComponent {

    RestClient getRestClient();

    RxBus getRxBus();

    Navigator getNavigator();

    SensorDbManager getSensorManager();

    void injectSensorListActivity(SensorListActivity sensorListActivity);

    void injectSensorGraphActivity(SensorGraphActivity sensorGraphActivity);
}


