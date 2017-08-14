package hu.newtonsapple.andor.Classes;

import android.app.Application;
import android.media.MediaPlayer;

import com.onurciner.toastox.ToastOX;
import hu.newtonsapple.andor.R;

/**
 * Created by Andor on 2017.08.14..
 */

public class startApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if(!Global.isNetwork(startApp.this))
            ToastOX.error(startApp.this,getResources().getString(R.string.network_nf),1200);
    }
}
