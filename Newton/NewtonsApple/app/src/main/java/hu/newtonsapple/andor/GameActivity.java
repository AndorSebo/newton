package hu.newtonsapple.andor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.hardware.SensorEventListener;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import hu.newtonsapple.andor.Classes.Alerts;
import hu.newtonsapple.andor.Classes.Global;
import hu.newtonsapple.andor.Classes.User;

public class GameActivity extends AppCompatActivity implements SensorEventListener{
    private int life = 3;
    private int point = 0;
    TextView lifeTV, pointTV, counter, heightScoreTV;
    ImageView newton;
    AnimationDrawable animation;
    ObjectAnimator animator;

    ImageView[] apples = new ImageView[11];
    int height, width, fell, heightScore, stepSize;
    ObjectAnimator appleAnimator;
    boolean finished = false, gameover = false, paused = false;
    SharedPreferences prefs;
    CountDownTimer ct;
    final static int speed = 1500;
    DatabaseReference userReference;

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

        myManager.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        Global.setFullScreen(getWindow());

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        lifeTV = (TextView) findViewById(R.id.heartTV);
        lifeTV.setText(String.valueOf(life));

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
        stepSize = size.x/100 - 10;
        if (stepSize < 20)
            stepSize +=20;
        fell = 0;

        heightScore = prefs.getInt("HeightScore",0);

        heightScoreTV.setText(String.valueOf(heightScore));

        ct = new CountDownTimer(4000, 500) {
            public void onTick(long millisUntilFinished) {
                counter.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                counter.setVisibility(View.GONE);
                finished = true;
                //fallLot();
            }
        }.start();
        newton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                animator.pause();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (finished) {
            paused = true;
            Alerts.alertToMenu(GameActivity.this, appleAnimator);
        }else {
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
        }else{
            if (animator != null)
                animator.pause();
            newton.setImageResource(R.drawable.newton_dead);
            if (point > heightScore){
                prefs.edit().putInt("HeightScore",point).apply();
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
        appleAnimator.setDuration(rn.nextInt(200) + speed).start();
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

    private void sendScore(int point){
        String name = prefs.getString("name","Játékos");
        int score = point;
        String id = userReference.push().getKey();
        User user = new User(id,name,score);
        userReference.child(id).setValue(user);

    }

    private void moveRight(){
        if (newton.getX() != width-newton.getWidth()) {
            animator = ObjectAnimator.ofFloat(newton, "x", (width-newton.getWidth()));
            animator.setInterpolator(null);
            newton.setScaleX(1f);
            animation = (AnimationDrawable) newton.getDrawable();
            animation.start();
            if (!animator.isPaused())
                animator.setDuration(speed).start();
            else
                animator.resume();
        }
    }

    private void stopRight(){
        if(animator != null && !animator.isPaused())
            animator.pause();
        animation.selectDrawable(0);
        animation.stop();
    }

    private void moveLeft(){
        if (newton.getX() != 0) {
            animator = ObjectAnimator.ofFloat(newton, "x", 0);
            animator.setInterpolator(null);
            newton.setScaleX(-1f);
            animation = (AnimationDrawable) newton.getDrawable();
            animation.start();
            if (!animator.isPaused())
                animator.setDuration(speed).start();
            else
                animator.resume();
        }
    }

    private void stopLeft(){
        if(animator != null && !animator.isPaused())
            animator.pause();
        animation.stop();
        animation.selectDrawable(0);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (finished && !gameover && !paused)
            if (sensorEvent.values[1] < -1)
                moveRight();
            else if(sensorEvent.values[1] > 1)
                moveLeft();
            else{
                stopLeft();
                stopRight();
            }

    }
}