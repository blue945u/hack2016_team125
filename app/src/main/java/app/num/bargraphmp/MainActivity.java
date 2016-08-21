package app.num.bargraphmp;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private GoogleApiClient client;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private Handler handler = new Handler();
    private long startTime;
    private Calendar mCalendar;
    private String str;
    private SimpleDateFormat df;
    public int tempcount;
    public int blood=5; // 怪物的血量
    public int finalcount=blood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Shake+time
        operation();
        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                count -= tempcount;
                finalcount = Math.abs(count-blood);
                if(finalcount <= 0){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,Catch.class);
                    startActivity(intent);
                }
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://app.num.bargraphmp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://app.num.bargraphmp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    public void operation() {
        startTime = System.currentTimeMillis(); //取得目前時間
        handler.removeCallbacks(updateTimer);//設定定時要執行的方法
        handler.postDelayed(updateTimer, 500);//設定Delay的時間
    }

    //固定要執行的方法
    private Runnable updateTimer = new Runnable() {
        public void run() {
            //final TextView time = (TextView) findViewById(R.id.time);
            handler.postDelayed(this,500);
            mCalendar = Calendar.getInstance();
            df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            str = df.format(mCalendar.getTime());
            //time.setText(str);

            //TextView testTextView = (TextView) findViewById(R.id.haha);
            if(finalcount<blood) {
                finalcount++;
                tempcount++;//時間減一秒就加一次這個，因為shaedetector那邊的變數是實際累積的，最後要扣掉tempcount
            }

            HorizontalBarChart barChart = (HorizontalBarChart) findViewById(R.id.chart);

            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(finalcount, 0));

            BarDataSet dataset = new BarDataSet(entries, "");

            ArrayList<String> labels = new ArrayList<String>();
            labels.add("怪物血量");

            // no description text
            barChart.setDescription("");

            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setEnabled(false);
            leftAxis.setDrawGridLines(false);
            leftAxis.setAxisMaxValue(blood);
            leftAxis.setAxisMinValue(0f);
            YAxis rightAxis = barChart.getAxisRight();
            rightAxis.setEnabled(false);
            rightAxis.setDrawGridLines(false);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setEnabled(false);

            BarData data = new BarData(labels, dataset);
            // dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
            barChart.setData(data);
            barChart.invalidate();
            //barChart.animateY(100);
            barChart.setDrawValueAboveBar(false); //讓顯示的數字不會超過bar條
            barChart.getLegend().setEnabled(false); // 把label拿掉

        }
    };
}
