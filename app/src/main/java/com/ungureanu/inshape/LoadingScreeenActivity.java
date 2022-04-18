package com.ungureanu.inshape;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class LoadingScreeenActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView textView;
    private final String TAG = LoadingScreeenActivity.class.getSimpleName();
    private final String progress = "progress";
    private final String[] Tips = {"Avoid ego lifting",
    "Don't forget your towel",
    "Stay hydrated",
    "Work hard",
    "Stay strong",
    "Progress comes in small steps"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screeen);

        setGUI();
        progressBarAnimation();

        TextView txtCount = (TextView) findViewById(R.id.idTips);

        final int min = 0;
        final int max = 5;
        final int random_tip1 = new Random().nextInt((max - min) + 1) + min;
        int random_tip2 = new Random().nextInt((max - min) + 1) + min;
        while (random_tip2 == random_tip1){
            random_tip2 = new Random().nextInt((max - min) + 1) + min;
        }

        final int secs = 5;
        int finalRandom_tip = random_tip2;
        new CountDownTimer((secs +1) * 1000, 1000) // Wait 5 secs, tick every 1 sec
        {
            @Override
            public final void onTick(final long millisUntilFinished)
            {
                int verify = (int) (millisUntilFinished * .001f);
                if(verify > 2){
                    txtCount.setText(Tips[random_tip1]);
                }else
                {
                    txtCount.setText(Tips[finalRandom_tip]);
                }

            }
            @Override
            public final void onFinish()
            {
                txtCount.setText("GO!");
            }
        }.start();
    }

    private void setGUI(){
        //Log.d(TAG, "setGUI: called");
        setProgressBarGUI();
        setTextViewGUI();
    }

    private void setProgressBarGUI(){
        //Log.d(TAG, "setProgressBarGUI: called");
        this.progressBar = (ProgressBar) findViewById(R.id.idProgressBar);
        this.progressBar.setMax(100);
        this.progressBar.setScaleY(3f);
    }

    private void setTextViewGUI(){
        //Log.d(TAG, "setTextViewGUI: called");
        this.textView = (TextView) findViewById(R.id.idTextView);
    }

    private void progressBarAnimation(){
        //Log.d(TAG, "progressBarAnimation: called");

        ObjectAnimator animator = ObjectAnimator.ofInt(progressBar, progress, 0, 100);
        animator.setDuration(5000);
        animator.setInterpolator(new DecelerateInterpolator());


        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //Log.d(TAG, "onAnimationEnd: called");
                goToMainMenu();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }

        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Log.d(TAG, "onAnimationUpdate: called");
                textView.setText(animator.getAnimatedValue().toString() + " %");
            }

        });
        animator.setStartDelay(1500);

        animator.start();
    }

    private void goToMainMenu(){
        //Log.d(TAG, "goToMainMenu: called");

        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}