package hu.newtonsapple.andor;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import hu.newtonsapple.andor.Classes.Alerts;

public class GameActivity extends AppCompatActivity {
    private int life = 3;
    private int point = 0;
    TextView lifeTV, pointTV, counter;
    ImageView rightArrow, leftArrow, newton;
    AnimationDrawable animation;
    ObjectAnimator animator;

    ImageView[] apples = new ImageView[10];
    int height;
    int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        newton = (ImageView) findViewById(R.id.newton);
        animation = (AnimationDrawable) newton.getDrawable();
        animation.stop();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.scale_from_corner, R.anim.scale_to_corner);

        lifeTV = (TextView) findViewById(R.id.heartTV);
        lifeTV.setText(String.valueOf(life));

        pointTV = (TextView) findViewById(R.id.pointTV);
        pointTV.setText(String.valueOf(point));

        counter = (TextView) findViewById(R.id.counter);

        rightArrow = (ImageView) findViewById(R.id.rightBtn);
        leftArrow = (ImageView) findViewById(R.id.leftBtn);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        height = size.y;
        width = size.x;

        new CountDownTimer(4000, 500) {
            public void onTick(long millisUntilFinished) {
                rightArrow.setEnabled(false);
                leftArrow.setEnabled(false);
                counter.setText(String.valueOf(millisUntilFinished / 1000));
            }
            public void onFinish() {
                rightArrow.setEnabled(true);
                leftArrow.setEnabled(true);
                counter.setVisibility(View.GONE);
                fallApple();
            }
        }.start();



        Log.d("Apples",String.valueOf(apples.length));

        rightArrow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        Log.d("Touch","UP");
                        leftArrow.setEnabled(true);
                        animator.pause();
                        animation.stop();
                        animation.selectDrawable(0);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        if (newton.getX() != height-(rightArrow.getWidth()/3)){
                            Log.d("Touch","DOWN");
                            leftArrow.setEnabled(false);
                            animator = ObjectAnimator.ofFloat(newton,"x",height-(rightArrow.getWidth()/3));
                            newton.setScaleX(1f);
                            animation = (AnimationDrawable) newton.getDrawable();
                            animation.start();
                            if(!animator.isPaused())
                                animator.setDuration(1100).start();
                            else
                                animator.resume();
                        }
                        break;
                }
                return true;
            }
        });

        leftArrow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        Log.d("Touch","UP");
                        rightArrow.setEnabled(true);
                        animator.pause();
                        animation.stop();
                        animation.selectDrawable(0);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        Log.d("Touch","DOWN");
                        rightArrow.setEnabled(false);
                        if (newton.getX() != rightArrow.getWidth()/10){
                            animator = ObjectAnimator.ofFloat(newton,"x",rightArrow.getWidth()/10);
                            newton.setScaleX(-1f);
                            animation = (AnimationDrawable) newton.getDrawable();
                            animation.start();
                            if(!animator.isPaused())
                                animator.setDuration(1100).start();
                            else
                                animator.resume();
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
       Alerts.alertToMenu(GameActivity.this);
    }

    private void fallApple(){
        int sldAppleId;
        final ImageView apple;
        final ObjectAnimator appleAnimator;
        for(int i=0; i<apples.length;i++){
            int resID = getResources().getIdentifier("fall"+(i+1), "id", getPackageName());
            apples[i] = (ImageView) findViewById(resID);
        }
       // while(life > 0){
            sldAppleId = randomApple(apples.length);
            apple = apples[5];
            apple.setBackgroundResource(R.drawable.ic_apple);
            appleAnimator = ObjectAnimator.ofFloat(apple,"y",height);
            appleAnimator.setDuration(2000).start();
            appleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    //TODO---- COLLIDER VIZSGÁLAT
                }

            });
        //}
    }

    private int randomApple(int length){
        Random rn = new Random();
        return rn.nextInt(length);
    }
}
