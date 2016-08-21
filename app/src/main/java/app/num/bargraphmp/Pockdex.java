package app.num.bargraphmp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class Pockdex extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pockdex);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.v("Power", "wait");

                Intent intent = new Intent();
                intent.setClass(Pockdex.this,Power.class);
                startActivity(intent);

            }
        }, 3000);
    }

}
