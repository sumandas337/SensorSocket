package graph.sockets.com.sensorsocket.net;

import java.util.ArrayList;
import java.util.Map;

import graph.sockets.com.sensorsocket.models.SensorRange;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by sumandas on 04/12/2016.
 */

public interface RestApi {

    @GET("/sensornames")
    Observable<ArrayList<String>> getSensors();

    @GET("/config")
    Observable<Map<String,SensorRange>> getSensorRanges();
}
