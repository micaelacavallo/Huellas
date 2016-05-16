package com.example.micaela.PushNotifications;

/**
 * Created by quimey.arozarena on 3/11/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.micaela.activities.ComentariosActivity;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.Constants;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomPushReceiver extends ParsePushBroadcastReceiver {
    JSONObject pushData = null;

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
        } else {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        }
    }


    @Override
    protected void onPushOpen(Context context, Intent intent) {
        try {
            pushData = new JSONObject(intent.getStringExtra(KEY_PUSH_DATA));
            Intent pushIntent = new Intent(context, ComentariosActivity.class);

            String fromFragment = pushData.getString(Constants.FROM_FRAGMENT);
            pushIntent.putExtra(Constants.FROM_FRAGMENT, fromFragment);
            pushIntent.putExtra(Constants.PUSH_OPEN, true);
            pushIntent.putExtra(Constants.OBJETO_ID, pushData.getString(Constants.OBJETO_ID));
            pushIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(pushIntent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
