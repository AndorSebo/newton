package hu.newtonsapple.andor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import hu.newtonsapple.andor.Classes.Alerts;

public class MainActivity extends AppCompatActivity {

    ImageButton playButton, optionsButton, toplistButton;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.scale_from_corner, R.anim.scale_to_corner);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = prefs.edit();
        name = (TextView) findViewById(R.id.name);

        if (!prefs.getBoolean("firstTime", false)) {
            Alerts.getName(MainActivity.this,editor, name);
            editor.putBoolean("firstTime", true);
            editor.putBoolean("vibrate", true);
            editor.commit();
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
                Toast.makeText(getBaseContext(),"TOPLIST BUTTON",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
