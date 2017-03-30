package graph.sockets.com.sensorsocket.ui.sensorlist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import graph.sockets.com.sensorsocket.R;
import graph.sockets.com.sensorsocket.application.SocketApplication;
import graph.sockets.com.sensorsocket.models.Sensor;
import graph.sockets.com.sensorsocket.net.RestClient;
import graph.sockets.com.sensorsocket.ui.sensorlist.adapter.SensorListAdapter;
import graph.sockets.com.sensorsocket.ui.sensorlist.presenter.SensorListPresenter;
import graph.sockets.com.sensorsocket.utils.Navigator;

/**
 * Created by sumandas on 25/03/2017.
 */

public class SensorListActivity extends AppCompatActivity implements ISensorContract.ISensorListView{

    @Inject
    SensorListPresenter mSensorListPresenter;

    @Inject
    RestClient mRestClient;


    @Inject
    Navigator mNavigator;

    @BindView(R.id.sensor_list_recycler)
    RecyclerView mSensorList;

    //@BindView(R.id.toolbar)
    //Toolbar mToolbar;

    @BindView(R.id.rootView)
    View mRootView;

    private ProgressDialog mProgressDialog;

    private SensorListAdapter mSensorListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        mSensorList.setLayoutManager(layoutManager1);
        mSensorList.setItemAnimator(new DefaultItemAnimator());

        //setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        mProgressDialog = new ProgressDialog(this);

        ((SocketApplication)getApplication())
                .getmApplicationComponent()
                .injectSensorListActivity(this);

        mSensorListPresenter.create();
    }

    @Inject
    public void setUp() {
        mSensorListPresenter.setMvpView(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void showLoading() {
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        if (!isFinishing())
            mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onSensorListLoaded(ArrayList<Sensor> sensors) {
        mSensorListAdapter = new SensorListAdapter(this, sensors, mSensorListPresenter);
        mSensorList.setAdapter(mSensorListAdapter);
        mSensorListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSensorListLoadFaile(String message) {
        Snackbar.make(mRootView, "Error in Loading Sensors", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSensorClicked(Sensor sensor) {
        Intent intent =new Intent(this,SensorGraphActivity.class);
        intent.putExtra("sensor",sensor);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorListPresenter.destroy();
    }

}
