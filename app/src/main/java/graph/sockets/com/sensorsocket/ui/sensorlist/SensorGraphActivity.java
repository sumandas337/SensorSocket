package graph.sockets.com.sensorsocket.ui.sensorlist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import graph.sockets.com.sensorsocket.R;
import graph.sockets.com.sensorsocket.application.SocketApplication;
import graph.sockets.com.sensorsocket.models.Sensor;
import graph.sockets.com.sensorsocket.models.SensorData;
import graph.sockets.com.sensorsocket.models.SensorInit;
import graph.sockets.com.sensorsocket.ui.sensorlist.presenter.SensorGraphPresenter;
import graph.sockets.com.sensorsocket.utils.SensorUtils;
import io.socket.client.Socket;

/**
 * Created by sumandas on 26/03/2017.
 */

public class SensorGraphActivity extends AppCompatActivity implements ISensorContract.ISensorGraphView {

    @Inject
    Socket mSocket;

    @Inject
    SensorGraphPresenter mSensorGraphPresenter;

    @BindView(R.id.line_chart_recent)
    LineChart mChartRecent;

    @BindView(R.id.line_chart_minute)
    LineChart mChartMinute;

    private Sensor mSensor;


    Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_layout);

        mSensor = getIntent().getParcelableExtra("sensor");

        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mSensor.mSensorName);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        mCalendar = Calendar.getInstance();
        mCalendar.setTimeZone(TimeZone.getDefault());

        ((SocketApplication) getApplication())
                .getmApplicationComponent()
                .injectSensorGraphActivity(this);

        mSensorGraphPresenter.create();
        initializeChart(mChartMinute);
        initializeChart(mChartRecent);


    }

    private void initializeChart(LineChart lineChart){
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);

        lineChart.setDragDecelerationFrictionCoef(0.9f);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerDragEnabled(true);

        lineChart.setPinchZoom(true);
        lineChart.setBackgroundColor(Color.WHITE);

        lineChart.animateX(2500);

        Legend l = lineChart.getLegend();

        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaximum(200.0f);
        leftAxis.setAxisMinimum(0);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaximum(200.0f);
        rightAxis.setAxisMinimum(0);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(true);
        rightAxis.setGranularityEnabled(false);
    }

    @Inject
    public void setUp() {
        mSensorGraphPresenter.setMvpView(this);
        mSensorGraphPresenter.setmSensor(mSensor);
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

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onSensorDataInit(SensorInit sensorInit) {
        runOnUiThread(() -> {
            setData(sensorInit.minute, SensorUtils.SensorMessageType.INIT,mChartMinute);
            setData(sensorInit.recent, SensorUtils.SensorMessageType.INIT,mChartRecent);
            for (SensorData sensorData : sensorInit.minute) {
                Log.e("MINUTE", "key :" + sensorData.mKey + " value:" + sensorData.mValue);
            }
            for (SensorData sensorData : sensorInit.recent) {
                Log.e("RECENT", "key :" + sensorData.mKey + " value:" + sensorData.mValue);
            }
        });

    }

    @Override
    public void onSensorDataUpdate(SensorData sensorData) {
        runOnUiThread(() -> {
            Log.e("UPDATE " + sensorData.mScale, "key :" + sensorData.mKey + " value:" + sensorData.mValue);
            ArrayList<SensorData> data = new ArrayList();
            data.add(sensorData);
            setData(data, SensorUtils.SensorMessageType.UPDATE,
                    sensorData.mScale.equals("minute")?mChartMinute:mChartRecent);
        });

    }

    @Override
    public void onSensorDataDelete(SensorData sensorData) {
        runOnUiThread(() -> {
            Log.e("DELETE " + sensorData.mScale, "key :" + sensorData.mKey + " value:" + sensorData.mValue);
            ArrayList<SensorData> data = new ArrayList();
            data.add(sensorData);
            setData(data, SensorUtils.SensorMessageType.DELETE,
                    sensorData.mScale.equals("minute")?mChartMinute:mChartRecent);
        });

    }

    private LineDataSet getDataSet(ArrayList<Entry> yVals) {
        LineDataSet set;
        set = new LineDataSet(yVals, "DataSet");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.MAGENTA);
        set.setLineWidth(2f);
        set.setCircleRadius(3f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setDrawCircleHole(false);
        return set;

    }

    private int getXIndex(long mKey){
        return (int)mKey % SensorUtils.NO_DATA_POINTS;
    }

    private void setData(ArrayList<SensorData> data, @SensorUtils.SensorMessageType int type,LineChart chart) {
        ArrayList<Entry> yVals1 = new ArrayList();
        LineDataSet set;

        for (int i = 0; i < data.size(); i++) {
            SensorData datapoint = data.get(i);

            if (!TextUtils.isEmpty(datapoint.mValue)) {
                yVals1.add(new Entry(getXIndex(datapoint.mKey), (int) Double.parseDouble(datapoint.mValue)));
            }

        }

        if (type == SensorUtils.SensorMessageType.INIT) {
            if (chart.getData() != null &&
                    chart.getData().getDataSetCount() > 0) {
                set = (LineDataSet) chart.getData().getDataSetByIndex(0);
                set.setValues(yVals1);
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
            } else {
                set = getDataSet(yVals1);
                LineData linedata = new LineData(set);
                linedata.setValueTextColor(Color.BLUE);
                linedata.setValueTextSize(9f);

                chart.setData(linedata);
                chart.notifyDataSetChanged();
                chart.invalidate();
            }
        } else if (type == SensorUtils.SensorMessageType.UPDATE) {
            SensorData datapoint = data.get(0);
            if (chart.getLineData() != null) {
                int index =getXIndex(datapoint.mKey);
                LineDataSet lineDataSet =(LineDataSet) chart.getData().getDataSetByIndex(0);
                if(index< lineDataSet.getEntryCount()){
                    chart.getLineData().getDataSetByIndex(0).removeEntry(getXIndex(datapoint.mKey));
                }
                chart.getLineData().addEntry(new Entry(getXIndex(datapoint.mKey),
                                (int) Double.parseDouble(datapoint.mValue)), 0);
                Log.e("ADD"," add index "+getXIndex(datapoint.mKey));
            } else {
                ArrayList<Entry> yVal =new ArrayList<>();
                yVal.add(new Entry(getXIndex(datapoint.mKey),(int) Double.parseDouble(datapoint.mValue)));
                set = getDataSet(yVal);

                LineData linedata = new LineData(set);
                linedata.setValueTextColor(Color.BLUE);
                linedata.setValueTextSize(9f);
                chart.setData(linedata);
            }
            chart.notifyDataSetChanged();
            chart.invalidate();

        } else if (type == SensorUtils.SensorMessageType.DELETE) {
            SensorData datapoint = data.get(0);
            if (chart.getData() != null) {
                Log.e("DELETE"," delete index "+getXIndex(datapoint.mKey));
                LineDataSet lineDataSet =(LineDataSet) mChartRecent.getData().getDataSetByIndex(0);
                int index =getXIndex(datapoint.mKey);
                if(index< lineDataSet.getEntryCount()){
                    chart.getData().getDataSetByIndex(0).removeEntry(index);
                    chart.notifyDataSetChanged();
                    chart.invalidate();
                }

            }
        }
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorGraphPresenter.destroy();
    }

}
