package hu.newtonsapple.andor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageButton playButton, optionsButton, creditsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.scale_from_corner, R.anim.scale_to_corner);

        playButton = (ImageButton) findViewById(R.id.playButton);
        optionsButton = (ImageButton) findViewById(R.id.optionsButton);
        creditsButton = (ImageButton) findViewById(R.id.creditsButton);

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
                Toast.makeText(getBaseContext(),"OPTIONS BUTTON",Toast.LENGTH_SHORT).show();
            }
        });
        creditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(),"INFO BUTTON",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
