package hu.newtonsapple.andor;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    private int life = 3;
    private int point = 0;
    TextView lifeTV, pointTV;
    ImageView rightArrow, leftArrow, newton;
    AnimationDrawable animation;

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

        rightArrow = (ImageView) findViewById(R.id.rightBtn);
        leftArrow = (ImageView) findViewById(R.id.leftBtn);


        rightArrow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        Log.d("Touch","UP");
                        animation.stop();
                        animation.selectDrawable(0);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        Log.d("Touch","DOWN");
                        newton.setScaleX(1f);
                        animation = (AnimationDrawable) newton.getDrawable();
                        animation.start();
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
                        animation.stop();
                        animation.selectDrawable(0);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        Log.d("Touch","DOWN");
                        newton.setScaleX(-1f);
                        animation = (AnimationDrawable) newton.getDrawable();
                        animation.start();
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
