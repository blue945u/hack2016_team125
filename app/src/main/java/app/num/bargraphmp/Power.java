package app.num.bargraphmp;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Power extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public EditText editText;
    public TextView textView;
    public TextView date_display;
    public Button save, load;

    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/aaTutorial";
    public String path_out = Environment.getExternalStorageDirectory().getAbsolutePath();
    public String path_data = Environment.getExternalStorageDirectory().getAbsolutePath();

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    MediaPlayer mysound;
    private int brightness;
    private ContentResolver cResolver;

    private AudioManager audioManager = null;
    public String dateString;
    String asleep="23:00", awake="07:00", deep="6h20m", totalsleepstring="8h00m",powerstring="125";
    int duration = 7;
    String date="08/21";
    static int first=1,power=125;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mysound = MediaPlayer.create(this, R.raw.crop);
        cResolver = getContentResolver();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        Log.v("Power", "Poweropen");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();*/
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView view = (TextView) findViewById(R.id.asleep);
        view.setText(asleep);
        view = (TextView) findViewById(R.id.awake);
        view.setText(awake);
        view = (TextView) findViewById(R.id.deep);
        view.setText(deep);
        view = (TextView) findViewById(R.id.totalsleep);
        view.setText(totalsleepstring);
        view = (TextView) findViewById(R.id.power);
        view.setText(""+ power);


        //buttonLoad();
        Log.v("Power", "Power Loadmini");

        Loaddata();
        Log.v("Power", "Power data");


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.v("Power", "wait");
                if (duration >= 7 && duration <= 8 && first==1) {
                    duration =0;
                    first=0;
                    power = power + 85;
                    Log.v("Power", "Power if duration");
                    Intent intent = new Intent();
                    intent.setClass(Power.this, MainActivity.class);
                    startActivity(intent);

                }
                else
                {
                    playMusic();
                }

            }
        }, 5000);






    }

    public void playMusic(){
        //mysound.setVolume( 1.0f,1.0f );
        mysound.start();
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(10000);

        /*Settings.System.putInt(this.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, 20);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness =0.2f;// 100 / 100.0f;
        getWindow().setAttributes(lp);*/

        //BLACKKKKKKKKKKKKKKKKKKKKKKKKKKKKK

        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        android.provider.Settings.System.putInt(getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS, 1);

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                511, 0);

        //startActivity(new Intent(this, RefreshScreen.class));
    }
    public void Loaddata() {
        //File file = new File(path + "/mili_log.txt");
        //File file = new File(path_data + "/data.txt");




    }

    public void buttonLoad() {
        Log.v("Power", "load start");
        //File file = new File(path + "/mili_log.txt");
        File file = new File(path_out + "/mili_log.txt");
        Log.v("Power", "load middle");
        String[] loadText = Load(file);

        Log.v("Power", "load file");

        String finalString = "";
        String testString = "";

        for (int i = 0; i < loadText.length; i++) {
            if (loadText[i].startsWith("sleep =")) {
                finalString += loadText[i];
                break;
            }
            //finalString += loadText[i] + System.getProperty("line.separator");
        }


        //textView.setText(finalString);

        //String csvLine
        String[] row = finalString.split("\\s+");
        String[] temp = row[20].split(";");
        String wake = temp[0];
        String[] temp2 = row[22].split(";");
        String deep = temp2[0];
        textView.setText(row[9] + " / " + row[16] + " / " + wake + " / " + deep + " /  " + row[24]);
        //intosleeptime, waketime, awake, deep, totalsleep

        asleep = row[9];
        awake = row[16];
        deep = temp2[0];
        totalsleepstring = row[24];
        duration = Integer.parseInt(row[24]);
    }




    public static String[] Load(File file) {
        Log.v("Power", "Load function1");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
        Log.v("Power", "Load function2");

        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        Log.v("Power", "Load function3");
        String test;
        int anzahl = 0;
        try {
            while ((test = br.readLine()) != null) {
                anzahl++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fis.getChannel().position(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] array = new String[anzahl];

        String line;
        int i = 0;
        try {
            while ((line = br.readLine()) != null) {
                array[i] = line;
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return array;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.power, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
