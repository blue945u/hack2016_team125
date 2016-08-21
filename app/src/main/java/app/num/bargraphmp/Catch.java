package app.num.bargraphmp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class Catch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch);
        Log.v("Shake", "sss");

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //Notification notify = new Notification(android.R.drawable.stat_notify_more, "This is important", System.currentTimeMillis());

        Context context = Catch.this;
        CharSequence title = "Congrats! Gotcha!!!";
        CharSequence details = "Keep Good Sleep Habit :)";
        Intent intent = new Intent(context, Catch.class);//--------------------------?
        PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);

        Notification notify = new NotificationCompat.Builder(context).setTicker("---------------Gotcha----------------")
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setContentTitle(title)
                .setContentText(details)
                .setContentIntent(pending)
                .setAutoCancel(true)
                .build();

        nm.notify(0, notify);
        //notify.sound = Uri.parse("android.resource://com.leebrimelow.status")
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.v("Power", "wait");

                    Intent intent = new Intent();
                    intent.setClass(Catch.this,Pockdex.class);
                    startActivity(intent);

            }
        }, 3000);

    }
}
