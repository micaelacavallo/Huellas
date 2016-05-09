package com.example.micaela.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by micaela.cavallo on 5/9/2016.
 */
public class CustomDialog {

    public static void showDialog(Context con){
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setMessage("Para continuar necesitás una conexión a internet. Activá WIFI o la red móvil y volvé a intentarlo.");
        builder.setTitle("Error de conexión");

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
