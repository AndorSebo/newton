package hu.newtonsapple.andor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    private int life = 3;
    private int point = 0;
    TextView lifeTV, pointTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.scale_from_corner, R.anim.scale_to_corner);

        lifeTV = (TextView) findViewById(R.id.heartTV);
        lifeTV.setText(String.valueOf(life));

        pointTV = (TextView) findViewById(R.id.pointTV);
        pointTV.setText(String.valueOf(point));
    }

    @Override
    public void onBackPressed() {

        Intent menu = new Intent(GameActivity.this,MainActivity.class);
        startActivity(menu);
        finish();
    }
}
