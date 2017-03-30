package graph.sockets.com.sensorsocket.ui.sensorlist.presenter;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import graph.sockets.com.sensorsocket.base.IBaseView;
import graph.sockets.com.sensorsocket.models.Sensor;
import graph.sockets.com.sensorsocket.models.SensorData;
import graph.sockets.com.sensorsocket.models.SensorInit;
import graph.sockets.com.sensorsocket.ui.sensorlist.ISensorContract;
import graph.sockets.com.sensorsocket.utils.SensorUtils;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by sumandas on 26/03/2017.
 */

public class SensorGraphPresenter implements ISensorContract.ISensorGraphPresenter {

    private Socket mSocket;

    private ISensorContract.ISensorGraphView mView;

    private Sensor mSensor;

    public static final String TAG ="Sensor";

    @Inject
    SensorGraphPresenter(Socket socket){
        mSocket =socket;
    }


    @Override
    public void setMvpView(IBaseView baseView) {
        mView =(ISensorContract.ISensorGraphView)baseView;
    }


    public void setmSensor(Sensor mSensor) {
        this.mSensor = mSensor;
    }


    @Override
    public void create() {
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("data",onMessage);
        mSocket.connect();
    }

    @Override
    public void destroy() {
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("data",onMessage);
    }

    @Override
    public IBaseView getView() {
        return mView;
    }


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mSocket.emit(SensorUtils.SUBSCRIBE,mSensor.mSensorName);
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mSocket.emit(SensorUtils.UNSUBSCRIBE,mSensor.mSensorName);
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

        }
    };

    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data =(JSONObject) args[0];
            String messageType;
            try {
                messageType = data.getString("type");
                if(messageType.equals("init")){
                    ArrayList<SensorData> minutes = new ArrayList<>();
                    ArrayList<SensorData> recent = new ArrayList<>();
                    JSONArray minutesData = data.getJSONArray("minutes");
                    JSONArray recentData = data.getJSONArray("recent");
                    for(int i=0;i<minutesData.length();i++){
                        SensorData sensorData =new SensorData();
                        JSONObject jsonobject = minutesData.getJSONObject(i);
                        sensorData.mKey = jsonobject.getLong("key");
                        sensorData.mValue = jsonobject.getString("val");
                        sensorData.mSensorName = mSensor.mSensorName;
                        sensorData.mScale = "minute";
                        minutes.add(sensorData);
                    }
                    for(int i=0;i<recentData.length();i++){
                        SensorData sensorData =new SensorData();
                        JSONObject jsonobject = recentData.getJSONObject(i);
                        sensorData.mKey = jsonobject.getLong("key");
                        sensorData.mValue = jsonobject.getString("val");
                        sensorData.mSensorName = mSensor.mSensorName;
                        sensorData.mScale = "recent";
                        recent.add(sensorData);
                    }
                    SensorInit sensorInit = new SensorInit();
                    sensorInit.mSensorName = mSensor.mSensorName;
                    sensorInit.minute = minutes;
                    sensorInit.recent =recent;
                    sensorDataInit(sensorInit);
                }else if(messageType.equals("update")){
                    SensorData sensorData =new SensorData();
                    sensorData.mKey = data.getLong("key");
                    sensorData.mValue = data.getString("val");
                    sensorData.mSensorName = data.getString("sensor");
                    sensorData.mScale = data.getString("scale");
                    sensorDataUpdate(sensorData);
                }else if(messageType.equals("delete")){
                    SensorData sensorData =new SensorData();
                    sensorData.mKey = data.getLong("key");
                    sensorData.mValue = data.getString("val");
                    sensorData.mSensorName = data.getString("sensor");
                    sensorData.mScale = data.getString("scale");
                    sensorDataDelete(sensorData);
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                return;
            }
        }
    };

    @Override
    public void sensorDataInit(SensorInit initData) {
        mView.onSensorDataInit(initData);
    }

    @Override
    public void sensorDataUpdate(SensorData sensorData) {
        mView.onSensorDataUpdate(sensorData);
    }

    @Override
    public void sensorDataDelete(SensorData sensorData) {
        mView.onSensorDataDelete(sensorData);
    }
}
