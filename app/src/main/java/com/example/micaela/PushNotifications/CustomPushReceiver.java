package com.example.micaela.PushNotifications;

/**
 * Created by quimey.arozarena on 3/11/2016.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.micaela.activities.PrincipalActivity;
import com.example.micaela.huellas.R;
import com.parse.ParsePushBroadcastReceiver;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomPushReceiver extends ParsePushBroadcastReceiver {

    private final String TAG = CustomPushReceiver.class.getSimpleName();
    private static final int NOTIFICATION_ID = 1;
    public static int numMessages = 0;

    public CustomPushReceiver() {
        super();
    }

    @Override
    protected Notification getNotification(Context context, Intent intent) {

        JSONObject pushData = null;
        try{
            pushData = new JSONObject(intent.getExtras().getString("com.parse.Data"));
        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }

        if(pushData.has("HOLA!") && pushData.has("primer msj")){

            String titulo = pushData.optString("HOLA!");
            String mensaje = pushData.optString("primer msj");

            Notification notification = new NotificationCompat.Builder(
                    context)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(titulo)
                    .setContentText(mensaje)
                    .build();

            notification.defaults |= Notification.DEFAULT_VIBRATE;

            return notification;
        }else{
            return null;
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            Log.d(TAG, json.getString("alert").toString());

            final String notificationTitle = json.getString("title").toString();
            final String notificationContent = json.getString("alert").toString();
            final String uri = json.getString("uri");

            Intent resultIntent = null;
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);


            resultIntent = new Intent(context, PrincipalActivity.class);
            stackBuilder.addParentStack(PrincipalActivity.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


//Customize your notification - sample code
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(notificationTitle)
                            .setContentText(notificationContent);

            int mNotificationId = 001;
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, builder.build());


        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }

    }
}