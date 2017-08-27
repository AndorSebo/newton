package hu.newtonsapple.andor;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import hu.newtonsapple.andor.Classes.Alerts;
import hu.newtonsapple.andor.Classes.Global;

public class MainActivity extends AppCompatActivity {

    ImageButton playButton, optionsButton, toplistButton, infoButton;
    TextView name;
    Typeface tf;
    ImageView backgroundOne, backgroundTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.scale_from_corner, R.anim.scale_to_corner);

        Global.setFullScreen(getWindow());



        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = prefs.edit();
        backgroundOne = (ImageView) findViewById(R.id.background_one);
        backgroundTwo = (ImageView) findViewById(R.id.background_two);
        backgroundAnimation();
        name = (TextView) findViewById(R.id.name);

        if (!prefs.getBoolean("firstTime", false)) {
            Alerts.getName(MainActivity.this,editor, name);
            editor.putBoolean("firstTime", true);
            editor.putBoolean("vibrate", true).apply();
        }else{
            name.setText("Szia, "+prefs.getString("name","Játékos")+" jó játékot!");
        }
        tf = Typeface.createFromAsset(getAssets(),"font.ttf");
        name.setTypeface(tf);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerts.getName(MainActivity.this,editor, name);
            }
        });

        playButton = (ImageButton) findViewById(R.id.playButton);
        optionsButton = (ImageButton) findViewById(R.id.optionsButton);
        toplistButton = (ImageButton) findViewById(R.id.creditsButton);
        infoButton = (ImageButton) findViewById(R.id.info);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent game = new Intent(MainActivity.this, GameActivity.class);
                startActivity(game);
                finish();
            }
        });
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settings = new Intent(MainActivity.this, OptionsActivity.class);
                startActivity(settings);
                finish();
            }
        });
        toplistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toplist = new Intent(MainActivity.this, ToplistActivity.class);
                startActivity(toplist);
                finish();
            }
        });
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent info = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(info);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void backgroundAnimation(){
        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(10000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float height = backgroundOne.getHeight();
                final float translationY = height * progress;
                backgroundOne.setTranslationY(translationY);
                backgroundTwo.setTranslationY(translationY - height);
            }
        });
        animator.start();
    }

}
