package hu.newtonsapple.andor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import hu.newtonsapple.andor.Classes.Global;

public class InfoActivity extends AppCompatActivity {

    TextView infoTV;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Global.setFullScreen(getWindow());
        overridePendingTransition(R.anim.scale_from_corner, R.anim.scale_to_corner);
        infoTV = (TextView) findViewById(R.id.infoTV);
        backButton = (ImageView) findViewById(R.id.backArrow);
        infoTV.setText(readText());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackToMenu();
            }
        });

    }

    private String readText() {

        String text = "";
        try {
            InputStream is = getAssets().open("info.txt");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            text = new String(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return text;
    }

    @Override
    public void onBackPressed() {
        BackToMenu();
    }

    private void BackToMenu() {
        Intent menu = new Intent(InfoActivity.this, MainActivity.class);
        startActivity(menu);
        finish();
    }
}
