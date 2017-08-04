package hu.newtonsapple.andor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button playButton, optionsButton, creditsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        playButton = (Button) findViewById(R.id.playButton);
        optionsButton = (Button) findViewById(R.id.optionsButton);
        creditsButton = (Button) findViewById(R.id.creditsButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(),"PLAY BUTTON",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getBaseContext(),"CREDITS BUTTON",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
