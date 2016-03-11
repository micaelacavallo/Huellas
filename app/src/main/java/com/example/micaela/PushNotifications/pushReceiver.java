package com.example.micaela.PushNotifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by quimey.arozarena on 3/11/2016.
 */
public class pushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
 /*       Bundle extras = intent.getExtras();
        String message = extras != null ? extras.getString("com.parse.Data") : "";
        JSONObject jObject;
        try {
            jObject = new JSONObject(message);
            Toast toast = Toast.makeText(context, jObject.getString("alert")+ jObject.getString("title")+jObject.getString("description"), Toast.LENGTH_LONG);
            toast.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    ------*/

        IntentFilter intentFilter = new IntentFilter("MyAction");
        BroadcastReceiver pushReceiver;
        pushReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                String message = extras != null ? extras.getString("com.parse.Data") : "";
                JSONObject jObject;
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        context.registerReceiver(pushReceiver, intentFilter);
    }
}