package com.example.micaela.db.AlarmManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.micaela.db.Controladores.IAdicionalesImpl;
import com.example.micaela.db.Controladores.IPerdidosImpl;
import com.example.micaela.db.Interfaces.IAdicionales;
import com.example.micaela.db.Interfaces.IPerdidos;
import com.parse.ParseException;

import java.util.Calendar;

/**
 * Created by quimey.arozarena on 3/21/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private IAdicionales mAdicionales;
    private IPerdidos mPerdidos;

    public AlarmReceiver(){}

    public AlarmReceiver(Context context){
        mAdicionales = new IAdicionalesImpl(context);
        mPerdidos = new IPerdidosImpl(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            mAdicionales.cargarDBLocalDonaciones(context);
            mAdicionales.cargarDBLocalInfoUtil(context);
            mPerdidos.cargarDBLocal(context);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

