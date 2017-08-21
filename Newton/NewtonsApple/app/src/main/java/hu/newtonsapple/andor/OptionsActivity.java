package hu.newtonsapple.andor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rm.rmswitch.RMSwitch;

import hu.newtonsapple.andor.Classes.Global;

public class OptionsActivity extends AppCompatActivity {

    RMSwitch vibrate;
    TextView tabletTV, vibrateTV, title;
    ImageView backArrow;
    Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        overridePendingTransition(R.anim.scale_from_corner, R.anim.scale_to_corner);

        Global.setFullScreen(getWindow());

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = prefs.edit();
        vibrate = (RMSwitch) findViewById(R.id.vibrate);
        tabletTV = (TextView) findViewById(R.id.tabletTV);
        title = (TextView) findViewById(R.id.settings_title);
        vibrateTV = (TextView) findViewById(R.id.vibrateTV);
        backArrow = (ImageView) findViewById(R.id.backArrow);
        tf = Typeface.createFromAsset(getAssets(),"font.ttf");
        TextView[] tvs = {vibrateTV,tabletTV,title};
        for (TextView tv : tvs) tv.setTypeface(tf);

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

        if(!((Vibrator)getSystemService(Context.VIBRATOR_SERVICE)).hasVibrator()){
            tabletTV.setText(getResources().getString(R.string.tablet));
            vibrate.setChecked(false);
            vibrate.setClickable(false);
            vibrateTV.setEnabled(false);
        }

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackToMenu();
            }
        });
    }
    @Override
    public void onBackPressed() {
        BackToMenu();
    }

    private void BackToMenu(){
        Intent menu = new Intent(OptionsActivity.this,MainActivity.class);
        startActivity(menu);
        finish();
    }
}
