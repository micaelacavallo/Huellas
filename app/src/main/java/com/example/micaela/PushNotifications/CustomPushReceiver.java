package com.example.micaela.PushNotifications;

/**
 * Created by quimey.arozarena on 3/11/2016.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.micaela.activities.ComentariosActivity;
import com.example.micaela.activities.PrincipalActivity;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.Constants;
import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomPushReceiver extends ParsePushBroadcastReceiver {



    public CustomPushReceiver() {

        super();
    }

    @Override
    protected int getSmallIconId(Context context, Intent intent) {
        return R.mipmap.ic_launcher;
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
    protected void onPushOpen(Context context, Intent intent) {

        JSONObject pushData = null;

        try {
            pushData = new JSONObject(intent.getStringExtra(KEY_PUSH_DATA));
            Intent pushIntent = new Intent(context, ComentariosActivity.class);
            pushIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          //  pushIntent.putExtra("intent", pushData.get("intent"));
            context.startActivity(pushIntent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    }
