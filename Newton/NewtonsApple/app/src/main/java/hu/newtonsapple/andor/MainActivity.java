package hu.newtonsapple.andor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.onurciner.toastox.ToastOX;

import hu.newtonsapple.andor.Classes.Alerts;
import hu.newtonsapple.andor.Classes.Global;
import hu.newtonsapple.andor.Classes.startApp;

public class MainActivity extends AppCompatActivity {

    ImageButton playButton, optionsButton, toplistButton;
    TextView name;
    MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.scale_from_corner, R.anim.scale_to_corner);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = prefs.edit();
        name = (TextView) findViewById(R.id.name);

        music = MediaPlayer.create(getBaseContext(),R.raw.music);
        music.start();
        music.setLooping(true);


        if (!prefs.getBoolean("firstTime", false)) {
            Alerts.getName(MainActivity.this,editor, name);
            editor.putBoolean("firstTime", true);
            editor.putBoolean("vibrate", true).apply();
        }else{
            name.setText("Szia, "+prefs.getString("name","Játékos")+" jó játékot!");
        }

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerts.getName(MainActivity.this,editor, name);
            }
        });

        playButton = (ImageButton) findViewById(R.id.playButton);
        optionsButton = (ImageButton) findViewById(R.id.optionsButton);
        toplistButton = (ImageButton) findViewById(R.id.creditsButton);

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
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
