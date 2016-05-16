package com.example.micaela.db.DAO;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.example.micaela.db.AlarmManager.AlarmReceiver;
import com.example.micaela.db.Interfaces.IDB;
import com.example.micaela.db.Interfaces.IGeneral;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.concurrent.TimeUnit;

/**
 * Created by quimey.arozarena on 12/22/2015.
 */
public class GeneralDAO implements IGeneral, IDB {

    private Context context;
    private ParseQuery<ParseObject> query;

    public GeneralDAO() {
    }

    public GeneralDAO(Context context) {
        this.context = context;
        query = null;
    }


    @Override
    public boolean internet(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    public void save(final ParseObject object) {

        if (internet(context)) {
            saveInBackground(object);
        }

        pinObjectInBackground(object);

    }

    @Override
    public void delete(ParseObject object) {

        if(internet(context)) {
            deleteInBackground(object);
        }

        unpinObjectInBackground(object);

    }

    @Override
    public void startAlert() {

        long repeatTime = TimeUnit.MINUTES.toMillis(180);

        AlarmManager service = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, 0);

        //AlarmManager.RTC_WAKEUP se sigue ejecutando por mas que este apagado
        service.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), repeatTime, pending);
    }

    @Override
    public String getUltimoObjectId(String clase) {
        query = ParseQuery.getQuery(clase);
        query.orderByDescending("createdAt");

        String objectId = null;
        try {
            if(query.count() != 0) {
                ParseObject objectAux = query.getFirst();
                objectId = objectAux.getObjectId();
            }
        } catch (ParseException e) {
            e.fillInStackTrace();
        }

        return objectId;
    }

    @Override
    public void checkInternetGet(ParseQuery<ParseObject> query) {
        if (!internet(context)) {
            query.fromLocalDatastore();
        }
    }

    @Override
    public void pinObjectInBackground(ParseObject object) {
        object.pinInBackground();
    }

    @Override
    public void unpinObjectInBackground(ParseObject object) {
        object.unpinInBackground();
    }

    @Override
    public void saveInBackground(ParseObject object) {

        object.saveInBackground();
    }

    @Override
    public void deleteInBackground(ParseObject object) {
        object.deleteInBackground();
    }



}
