package hu.newtonsapple.andor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import hu.newtonsapple.andor.Classes.Alerts;
import hu.newtonsapple.andor.Classes.Global;
import hu.newtonsapple.andor.Classes.User;

public class GameActivity extends AppCompatActivity implements SensorEventListener {
    final static int appleSpeed = 1100;
    final static int userSpeed = 4000;
    TextView lifeTV, pointTV, counter, heightScoreTV;
    ImageView newton, rightArrow, leftArrow;
    AnimationDrawable animation;
    ObjectAnimator animator, appleAnimator;
    ImageView[] apples = new ImageView[14];
    int height, width, fell, heightScore, stepSize;
    boolean finished = false, gameover = false, paused = false, arrows;
    SharedPreferences prefs;
    CountDownTimer ct;
    DatabaseReference userReference;
    Typeface tf;
    AnimatorSet set = new AnimatorSet();
    private int life = 3;
    private int point = 0;
    private double sens = 0.1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        newton = (ImageView) findViewById(R.id.newton);
        animation = (AnimationDrawable) newton.getDrawable();
        animation.stop();
        overridePendingTransition(R.anim.scale_from_corner, R.anim.scale_to_corner);

        SensorManager myManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor mySensor = myManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        tf = Typeface.createFromAsset(getAssets(), "font.ttf");

        myManager.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        Global.setFullScreen(getWindow());

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        lifeTV = (TextView) findViewById(R.id.heartTV);
        lifeTV.setText(String.valueOf(life));

        leftArrow = (ImageView) findViewById(R.id.leftArrow);
        rightArrow = (ImageView) findViewById(R.id.rightArrow);

        userReference = FirebaseDatabase.getInstance().getReference("users");

        pointTV = (TextView) findViewById(R.id.pointTV);
        pointTV.setText(String.valueOf(point));

        heightScoreTV = (TextView) findViewById(R.id.heightTV);

        counter = (TextView) findViewById(R.id.counter);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        height = size.y;
        width = size.x;
        stepSize = size.x / 100 - 10;
        if (stepSize < 20)
            stepSize += 20;
        fell = 0;

        if (prefs.getString("inputType", "Arrows").equals("Arrows")) {
            rightArrow.setVisibility(View.VISIBLE);
            leftArrow.setVisibility(View.VISIBLE);
            arrows = true;
        } else {
            rightArrow.setVisibility(View.GONE);
            leftArrow.setVisibility(View.GONE);
            arrows = false;
        }

        heightScore = prefs.getInt("HeightScore", 0);
        sens = prefs.getInt("sensValue", 1) / 10;

        heightScoreTV.setText(String.valueOf(heightScore));
        TextView[] tvs = {lifeTV, pointTV, heightScoreTV, counter};
        for (TextView tv : tvs) tv.setTypeface(tf);

        ct = new CountDownTimer(4000, 500) {
            public void onTick(long millisUntilFinished) {
                counter.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                counter.setVisibility(View.GONE);
                finished = true;
                inputArrows();
                fallLot();
            }
        }.start();
    }

    private void inputArrows() {
        rightArrow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        leftArrow.setEnabled(true);
                        animator.pause();
                        animation.stop();
                        animation.selectDrawable(0);
                        Log.d("newtonWidth", String.valueOf(newton.getWidth()));
                        break;
                    case MotionEvent.ACTION_DOWN:
                        if (newton.getX() != width - newton.getWidth() - rightArrow.getWidth()) {
                            leftArrow.setEnabled(false);
                            animator = ObjectAnimator.ofFloat(newton, "x", (width - newton.getWidth() - rightArrow.getWidth()));
                            animator.setInterpolator(new LinearOutSlowInInterpolator());
                            newton.setScaleType(ImageView.ScaleType.CENTER);
                            newton.setScaleX(1);
                            animation = (AnimationDrawable) newton.getDrawable();
                            animation.start();
                            if (!animator.isPaused())
                                animator.setDuration(userSpeed).start();
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
                        Log.d("newtonWidth", String.valueOf(newton.getWidth()));
                        break;
                    case MotionEvent.ACTION_DOWN:
                        rightArrow.setEnabled(false);
                        if (newton.getX() != leftArrow.getWidth()) {
                            rightArrow.setEnabled(false);
                            animator = ObjectAnimator.ofFloat(newton, "x", leftArrow.getWidth());
                            animator.setInterpolator(new LinearOutSlowInInterpolator());
                            newton.setScaleX(-1);
                            animation = (AnimationDrawable) newton.getDrawable();
                            animation.start();
                            if (!animator.isPaused())
                                animator.setDuration(userSpeed).start();
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
        if (finished) {
            Alerts.alertToMenu(GameActivity.this, appleAnimator, set);
        } else {
            ct.cancel();
            Intent menu = new Intent(GameActivity.this, MainActivity.class);
            startActivity(menu);
            finish();
        }
    }

    private void fallLot() {
        int[] sldAppleId = new int[2];

        for (int i = 0; i < apples.length; i++) {
            int resID = getResources().getIdentifier("fall" + i, "id", getPackageName());
            apples[i] = (ImageView) findViewById(resID);
        }

        if (life > 0) {
            for (int i = 0; i < sldAppleId.length; i++) {
                sldAppleId[i] = randomApple(apples.length);
                while (sldAppleId[0] == sldAppleId[1])
                    sldAppleId[1] = randomApple(apples.length);
                fallApple(sldAppleId[i]);
            }
        } else {
            if (animator != null)
                animator.pause();
            newton.setImageResource(R.drawable.newton_dead);
            if (point > heightScore) {
                prefs.edit().putInt("HeightScore", point).apply();
                sendScore(point);
            }
            Alerts.alertToEnd(GameActivity.this, point);
            gameover = true;
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
        appleAnimator.setDuration(rn.nextInt(200) + appleSpeed).start();
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
                if (fell == 2) {
                    fell = 0;
                    fallLot();
                }
            }
        });
    }

    private int fallLife() {
        Random rn = new Random();
        return rn.nextInt(101);
    }

    private int randomApple(int length) {
        Random rn = new Random();
        return rn.nextInt(length);
    }

    private boolean collision(ImageView a, ImageView b) {
        Rect appleRect = new Rect();
        a.getHitRect(appleRect);
        Rect playerRect = new Rect();
        b.getHitRect(playerRect);

        return (Rect.intersects(appleRect, playerRect));
    }

    private void sendScore(int point) {
        String name = prefs.getString("name", "Játékos");
        int score = point;
        String id = userReference.push().getKey();
        User user = new User(id, name, score);
        userReference.child(id).setValue(user);

    }

    private void moveRight() {
        if (newton.getX() != width - newton.getWidth() - rightArrow.getWidth()) {
            animator = ObjectAnimator.ofFloat(newton, "x", (width - newton.getWidth() - rightArrow.getWidth()));
            animator.setInterpolator(new LinearOutSlowInInterpolator());
            newton.setScaleX(1f);
            animation = (AnimationDrawable) newton.getDrawable();
            animation.start();
            set.setDuration(userSpeed);
            set.play(animator);
            if (set.isStarted()) {
                set.resume();
            } else if (set.isPaused()) {
                set.end();
                set.play(animator);
                set.start();
            } else {
                set.start();
            }
        }
    }

    private void animatorStop() {
        set.setupStartValues();
        set.cancel();
        animation.selectDrawable(0);
        animation.stop();
    }

    private void moveLeft() {
        if (newton.getX() != leftArrow.getWidth()) {
            if (!set.isStarted())
                set.end();
            animator = ObjectAnimator.ofFloat(newton, "x", leftArrow.getWidth());
            animator.setInterpolator(new LinearOutSlowInInterpolator());
            newton.setScaleType(ImageView.ScaleType.CENTER);
            newton.setScaleX(-1f);
            animation = (AnimationDrawable) newton.getDrawable();
            animation.start();
            set.setDuration(userSpeed);
            set.play(animator);
            if (set.isStarted()) {
                set.resume();
            } else if (set.isPaused()) {
                set.end();
                set.play(animator);
                set.start();
            } else {
                set.start();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (finished && !gameover && !paused && !arrows)
            if (sensorEvent.values[1] < -1 + sens)
                moveRight();
            else if (sensorEvent.values[1] > 1 - sens)
                moveLeft();
            else {
                if (animator != null)
                    animatorStop();
            }

    }
}