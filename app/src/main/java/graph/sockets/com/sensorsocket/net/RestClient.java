package graph.sockets.com.sensorsocket.net;

import java.util.ArrayList;
import java.util.Map;

import graph.sockets.com.sensorsocket.models.SensorRange;
import rx.Observable;

/**
 * Created by sumandas on 04/12/2016.
 */

public class RestClient {

    private RestApi mRestApi;

    public RestClient(RestApi restApi){
        mRestApi=restApi;
    }

    public Observable<ArrayList<String>> getSensors() {
        return mRestApi.getSensors();
    }

    public Observable<Map<String,SensorRange>> getSensorRange(){
        return mRestApi.getSensorRanges();
    }


}
