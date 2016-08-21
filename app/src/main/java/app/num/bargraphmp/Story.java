package app.num.bargraphmp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;




public class Story extends AppCompatActivity {
    Animation fade_in, fade_out;
    ViewFlipper viewFlipper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        viewFlipper = (ViewFlipper) this.findViewById(R.id.bckgrndViewFlipper1);
        fade_in = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        viewFlipper.setInAnimation(fade_in);
        viewFlipper.setOutAnimation(fade_out);
        Log.v("Story","begin");

        //sets auto flipping
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(3500);
        // viewFlipper.setInAnimation(this, android.R.anim.slide_in_left); //use either the default slide animation in sdk or create your own ones
        // viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);

        viewFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationEnd(Animation animation) {

                int displayedChild = viewFlipper.getDisplayedChild();
                int childCount = viewFlipper.getChildCount();
                if (displayedChild == childCount -1) {
                    viewFlipper.stopFlipping();
                    Log.v("MainActivity","ok");

                    /**
                     * go to AnimationActivity when last picture is done.
                     */
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("Power", "wait");
                            Intent intent = new Intent();
                            intent.setClass(Story.this,Power.class);
                            startActivity(intent);
                        }
                    }, 2000);

                }
            }
        });
        viewFlipper.startFlipping();


    }
}
