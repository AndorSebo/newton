package hu.newtonsapple.andor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
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
    int height, width, fell;
    ObjectAnimator appleAnimator;
    boolean finished = false;
    SharedPreferences prefs;
    CountDownTimer ct;
    int speed = 1050;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        newton = (ImageView) findViewById(R.id.newton);
        animation = (AnimationDrawable) newton.getDrawable();
        animation.stop();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.scale_from_corner, R.anim.scale_to_corner);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

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
        fell = 0;

        ct = new CountDownTimer(4000, 500) {
            public void onTick(long millisUntilFinished) {
                rightArrow.setEnabled(false);
                leftArrow.setEnabled(false);
                counter.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                rightArrow.setEnabled(true);
                leftArrow.setEnabled(true);
                counter.setVisibility(View.GONE);
                finished = true;
                fallLot();
            }
        }.start();

        rightArrow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        leftArrow.setEnabled(true);
                        animator.pause();
                        animation.stop();
                        animation.selectDrawable(0);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        if (newton.getX() != height - (rightArrow.getWidth() / 3)) {
                            leftArrow.setEnabled(false);
                            animator = ObjectAnimator.ofFloat(newton, "x", height - (rightArrow.getWidth() / 3));
                            newton.setScaleX(1f);
                            animation = (AnimationDrawable) newton.getDrawable();
                            animation.start();
                            if (!animator.isPaused())
                                animator.setDuration(speed).start();
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
                        rightArrow.setEnabled(true);
                        animator.pause();
                        animation.stop();
                        animation.selectDrawable(0);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        rightArrow.setEnabled(false);
                        if (newton.getX() != rightArrow.getWidth() / 10) {
                            animator = ObjectAnimator.ofFloat(newton, "x", rightArrow.getWidth() / 10);
                            newton.setScaleX(-1f);
                            animation = (AnimationDrawable) newton.getDrawable();
                            animation.start();
                            if (!animator.isPaused())
                                animator.setDuration(speed).start();
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
        if (finished)
            Alerts.alertToMenu(GameActivity.this, appleAnimator);
        else {
            ct.cancel();
            Intent menu = new Intent(GameActivity.this, MainActivity.class);
            startActivity(menu);
            finish();
        }
    }

    private void fallLot() {
        int[] sldAppleId = new int[2];

        for (int i = 0; i < apples.length; i++) {
            int resID = getResources().getIdentifier("fall" + (i + 1), "id", getPackageName());
            apples[i] = (ImageView) findViewById(resID);
        }

        if (life > 0) {
            for (int i = 0; i < sldAppleId.length; i++) {
                sldAppleId[i] = randomApple(apples.length);
                while (sldAppleId[0] == sldAppleId[1])
                    sldAppleId[1] = randomApple(apples.length);
                fallApple(sldAppleId[i]);
            }
        }else{
            if (animator != null)
                animator.pause();
            newton.setImageResource(R.drawable.newton_dead);
            Alerts.alertToEnd(GameActivity.this, point);
        }
    }

    private void fallApple(int sldAppleId) {

        final float appleY;
        Random rn = new Random();
        final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        final ImageView apple = apples[sldAppleId];
        appleY = apple.getY();
        final int fallLife = fallLife();

        if (fallLife < 6)
            apple.setBackgroundResource(R.drawable.heart);
        else
            apple.setBackgroundResource(R.drawable.ic_apple);

        appleAnimator = ObjectAnimator.ofFloat(apple, "y", height);
        appleAnimator.setDuration(rn.nextInt(250) + 1050).start();
        final boolean[] collided = {false};
        appleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (collision(apple, newton) && !collided[0]) {
                    if (fallLife > 5) {
                        life--;
                        if (prefs.getBoolean("vibrate", false))
                            v.vibrate(150);
                    } else
                        life++;
                    apple.setBackgroundResource(0);
                    lifeTV.setText(String.valueOf(life));
                    collided[0] = true;

                }
            }
        });
        appleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!collided[0]) {
                    if (fallLife > 5)
                        point++;
                    pointTV.setText(String.valueOf(point));
                }
                apple.setBackgroundResource(0);
                apple.setY(appleY);
                fell++;
                if (fell == 2){
                    fell=0;
                    fallLot();
                }
            }
        });
    }

    private int fallLife(){
        Random rn = new Random();
        return  rn.nextInt(101);
    }

    private int randomApple(int length){
        Random rn = new Random();
        return rn.nextInt(length);
    }

    private boolean collision(ImageView a, ImageView b){
        int crop = 40;
        float bl = a.getY()-crop;
        float bt = a.getX()-crop;
        float br = a.getWidth() + bl-crop;
        float bb = a.getHeight() + bt-crop;
        float pl = b.getY()-crop;
        float pt = b.getX()-crop;
        float pr = b.getWidth() + pl-crop;
        float pb = b.getHeight() + pt-crop;
        if (bl <= pr && bl >= pl && bt >= pt && bt <= pb) {
            return true;

        } else if (br >= pl && br <= pr && bb >= pt && bb <= pb) {
            return true;
        } else if (bt <= pb && bt >= pt && br >= pl && br <= pr) {
            return true;
        } else if (bb >= pt && bb <= pb && bl >= pl && bl <= pr) {
            return true;
        }
        return false;
    }

}
