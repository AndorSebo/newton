package hu.newtonsapple.andor;

import android.animation.ObjectAnimator;
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

public class GameActivity extends AppCompatActivity {
    private int life = 3;
    private int point = 0;
    TextView lifeTV, pointTV, counter;
    ImageView rightArrow, leftArrow, newton;
    AnimationDrawable animation;
    ObjectAnimator animator;

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
        final int height = size.y;

        new CountDownTimer(4000, 1000) {
            public void onTick(long millisUntilFinished) {
                rightArrow.setEnabled(false);
                leftArrow.setEnabled(false);
                counter.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                rightArrow.setEnabled(true);
                leftArrow.setEnabled(true);
                counter.setVisibility(View.GONE);
            }
        }.start();

        rightArrow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        Log.d("Touch","UP");
                        animator.pause();
                        animation.stop();
                        animation.selectDrawable(0);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        if (newton.getX() != height-(rightArrow.getWidth()/3)){
                            Log.d("Touch","DOWN");
                            animator = ObjectAnimator.ofFloat(newton,"x",height-(rightArrow.getWidth()/3));
                            newton.setScaleX(1f);
                            animation = (AnimationDrawable) newton.getDrawable();
                            animation.start();
                            if(!animator.isPaused())
                                animator.setDuration(1500).start();
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
                        animator.pause();
                        animation.stop();
                        animation.selectDrawable(0);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        Log.d("Touch","DOWN");
                        if (newton.getX() != rightArrow.getWidth()/10){
                            animator = ObjectAnimator.ofFloat(newton,"x",rightArrow.getWidth()/10);
                            newton.setScaleX(-1f);
                            animation = (AnimationDrawable) newton.getDrawable();
                            animation.start();
                            if(!animator.isPaused())
                                animator.setDuration(1500).start();
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
        Intent menu = new Intent(GameActivity.this,MainActivity.class);
        startActivity(menu);
        finish();
    }
}
