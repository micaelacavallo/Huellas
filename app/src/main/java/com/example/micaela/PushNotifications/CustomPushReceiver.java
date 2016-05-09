package com.example.micaela.PushNotifications;

/**
 * Created by quimey.arozarena on 3/11/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.micaela.activities.ComentariosActivity;
import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.db.Modelo.Perdidos;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.Constants;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomPushReceiver extends ParsePushBroadcastReceiver {



    public CustomPushReceiver() {

        super();
    }

    @Override
    protected int getSmallIconId(Context context, Intent intent) {
        return R.mipmap.ic_huella;
    }

    @Override
    protected Bitmap getLargeIcon(Context context, Intent intent) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        }
        else{
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {

        JSONObject pushData = null;

        try {
            pushData = new JSONObject(intent.getStringExtra(KEY_PUSH_DATA));

            Intent pushIntent = new Intent(context, ComentariosActivity.class);

            String fromFragment =  pushData.getString(Constants.FROM_FRAGMENT);
            pushIntent.putExtra(Constants.FROM_FRAGMENT, fromFragment);

            if (fromFragment.equals(Constants.PERDIDOS)) {
                pushIntent.putExtra(Constants.COMENTARIOS_LIST, (Perdidos) pushData.get(Constants.COMENTARIOS_LIST));
            }
            else {
                pushIntent.putExtra(Constants.COMENTARIOS_LIST, (Adicionales) pushData.get(Constants.COMENTARIOS_LIST));
            }
            context.startActivity(pushIntent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    }
