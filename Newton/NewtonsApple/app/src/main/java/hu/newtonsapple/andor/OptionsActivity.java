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
import android.widget.RadioButton;
import android.widget.TextView;

import com.rm.rmswitch.RMSwitch;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import hu.newtonsapple.andor.Classes.Global;

public class OptionsActivity extends AppCompatActivity {

    RMSwitch vibrate;
    TextView tabletTV, vibrateTV, title, sensTV, inputTV;
    ImageView backArrow;
    Typeface tf;
    DiscreteSeekBar sensBar;
    RadioButton arrows,sensor;

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
        sensTV = (TextView) findViewById(R.id.sensTV);
        inputTV = (TextView) findViewById(R.id.inputTV);
        backArrow = (ImageView) findViewById(R.id.backArrow);
        sensBar = (DiscreteSeekBar) findViewById(R.id.sensBar);

        arrows = (RadioButton) findViewById(R.id.arrows);
        sensor = (RadioButton) findViewById(R.id.sensor);
        tf = Typeface.createFromAsset(getAssets(),"font.ttf");
        TextView[] tvs = {vibrateTV,tabletTV,title,sensTV, inputTV};
        for (TextView tv : tvs) tv.setTypeface(tf);
        arrows.setTypeface(tf);
        sensor.setTypeface(tf);

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
        sensBar.setProgress(prefs.getInt("sensValue",1));

        sensBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                editor.putInt("sensValue",value).commit();
            }
            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

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
