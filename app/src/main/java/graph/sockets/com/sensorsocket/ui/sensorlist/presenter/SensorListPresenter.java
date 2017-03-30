package graph.sockets.com.sensorsocket.ui.sensorlist.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import javax.inject.Inject;

import graph.sockets.com.sensorsocket.base.IBaseView;
import graph.sockets.com.sensorsocket.database.SensorDbManager;
import graph.sockets.com.sensorsocket.models.Sensor;
import graph.sockets.com.sensorsocket.models.SensorRange;
import graph.sockets.com.sensorsocket.net.RestClient;
import graph.sockets.com.sensorsocket.ui.sensorlist.ISensorContract;
import graph.sockets.com.sensorsocket.ui.sensorlist.adapter.SensorListAdapter;
import graph.sockets.com.sensorsocket.utils.SensorUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sumandas on 25/03/2017.
 */

public class SensorListPresenter implements ISensorContract.ISensorListPresenter, SensorListAdapter.ISensorsActionListener {

    private ISensorContract.ISensorListView mView;

    private RestClient mRestClient;

    private SensorDbManager mSensorManager;

    @Inject
    public SensorListPresenter(RestClient restClient, SensorDbManager sensorDbManager){
        mRestClient = restClient;
        mSensorManager =sensorDbManager;
    }


    @Override
    public void setMvpView(IBaseView baseView) {
        mView = (ISensorContract.ISensorListView) baseView;
    }

    @Override
    public void create() {
        loadSensors();
    }

    @Override
    public void destroy() {

    }

    @Override
    public IBaseView getView() {
        return mView;
    }

    @Override
    public void onSensorClicked(Sensor sensor) {
        mView.onSensorClicked(sensor);
    }

    @Override
    public void loadSensors() {
        mView.showLoading();

        Observable<Map<String,SensorRange>>  networkAndSave = mRestClient.getSensorRange()
                .doOnNext(sensorRangeMap ->
                        mSensorManager.persistSensors(sensorRangeMap));

        Observable<ArrayList<Sensor>> network =
                networkAndSave.map(sensorRangeMap -> SensorUtils.getSensorRanges(sensorRangeMap));
        Observable<ArrayList<Sensor>> db = mSensorManager.getSensors();

        Observable<ArrayList<Sensor>> concatObservable=getConcatObservable(network, db);

        concatObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Sensor>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onSensorListLoadFaile(e.getMessage());
                        mView.hideLoading();
                    }

                    @Override
                    public void onNext(ArrayList<Sensor> contacts) {
                        Collections.sort(contacts, new Sensor.NameAscendingComparator());
                        mView.onSensorListLoaded(contacts);
                        mView.hideLoading();
                    }
                });
    }

    public Observable<ArrayList<Sensor>> getConcatObservable(Observable<ArrayList<Sensor>> network, Observable<ArrayList<Sensor>> db) {

        return Observable.concat(db, network).first(sensors -> {
            if (sensors == null || sensors.isEmpty()) {
                return false;
            } else {
                return true;
            }
        });
    }

}
