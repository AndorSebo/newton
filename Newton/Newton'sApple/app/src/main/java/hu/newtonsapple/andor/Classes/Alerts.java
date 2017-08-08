package hu.newtonsapple.andor.Classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hu.newtonsapple.andor.MainActivity;
import hu.newtonsapple.andor.R;

/**
 * Created by Andor on 2017.08.08..
 */

public class Alerts {

    public static void alertToMenu(final Context context){
        SweetAlertDialog toMenu;
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
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }
}
