package hu.newtonsapple.andor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.rm.rmswitch.RMSwitch;

public class OptionsActivity extends AppCompatActivity {

    RMSwitch vibrate, music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.scale_from_corner, R.anim.scale_to_corner);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = prefs.edit();
        vibrate = (RMSwitch) findViewById(R.id.vibrate);
        music = (RMSwitch) findViewById(R.id.music);

        vibrate.setChecked(prefs.getBoolean("vibrate", false));

        vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vibrate.isChecked()){
                    editor.putBoolean("vibrate", false);
                    editor.commit();
                    vibrate.setChecked(prefs.getBoolean("vibrate", false));
                }else{
                    editor.putBoolean("vibrate", true);
                    editor.commit();
                    vibrate.setChecked(prefs.getBoolean("vibrate", false));
                }
            }
        });


    }
    @Override
    public void onBackPressed() {
        Intent menu = new Intent(OptionsActivity.this,MainActivity.class);
        startActivity(menu);
        finish();
    }
}
