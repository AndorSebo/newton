package hu.newtonsapple.andor.Classes;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.onurciner.toastox.ToastOX;

import hu.newtonsapple.andor.R;

/**
 * Created by Andor on 2017.08.16..
 */

public class Multidex extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if(!Global.isNetwork(Multidex.this))
            ToastOX.error(Multidex.this,getResources().getString(R.string.network_nf),1200);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
