package com.managerapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.managerapp.R;


/**
 * Created by micaela.cavallo on 5/9/2016.
 */
public class CustomDialog {

    public static void showDialog(String title, String message, DialogInterface.OnClickListener listener, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setIcon(R.mipmap.ic_launcher);
        if (listener == null) {
            builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

        } else {
            builder.setPositiveButton("Confirmar", listener);
        }
        AlertDialog dialog = builder.create();
        if (!((Activity) context).isFinishing()) {
            dialog.show();
        }
    }

    public static void showConnectionDialog(Context context) {
        showDialog("Para continuar necesitás una conexión a internet. Activá WIFI o la red móvil y volvé a intentarlo.",
                "Error de conexión", null, context);
    }


}
