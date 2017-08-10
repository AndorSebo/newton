package hu.newtonsapple.andor.Classes;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hu.newtonsapple.andor.MainActivity;
import hu.newtonsapple.andor.R;

/**
 * Created by Andor on 2017.08.08..
 */

public class Alerts {

    public static void alertToMenu(final Context context, final ObjectAnimator appleAnimator){
        SweetAlertDialog toMenu;

        if(appleAnimator != null && appleAnimator.isRunning())
            appleAnimator.pause();
        else if (appleAnimator != null)
            appleAnimator.resume();

        toMenu = new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        toMenu.setTitleText("Biztosan ki szeretnél lépni?");
        toMenu.setContentText("")
                .setCancelText("Nem")
                .setConfirmText("Igen")
                .setCustomImage(R.drawable.sadapple)
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Intent menu = new Intent(context ,MainActivity.class);
                        context.startActivity(menu);
                        ((Activity)context).finish();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        if(appleAnimator != null)
                            appleAnimator.resume();
                        sDialog.dismissWithAnimation();
                    }
                }).show();
        toMenu.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
    }
    public static void alertToEnd(final Context context, int point){
        SweetAlertDialog toMenu;
        toMenu = new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        toMenu.setTitleText("Vége a játéknak!");
        toMenu.setContentText("A pontszámod: "+String.valueOf(point))
                .setCancelText("Vissza a menübe")
                .setConfirmText("Új játék")
                .setCustomImage(R.drawable.sadapple)
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Intent reload = ((Activity) context).getIntent();
                        ((Activity) context).finish();
                        context.startActivity(reload);
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Intent menu = new Intent(context ,MainActivity.class);
                        context.startActivity(menu);
                        ((Activity)context).finish();
                    }
                })
                .show();
        toMenu.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
    }
}
