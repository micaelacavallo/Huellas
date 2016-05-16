package com.example.micaela.db.AlarmManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.micaela.db.Controladores.IAdicionalesImpl;
import com.example.micaela.db.Controladores.IPerdidosImpl;
import com.example.micaela.db.Interfaces.IAdicionales;
import com.example.micaela.db.Interfaces.IPerdidos;
import com.parse.ParseException;

/**
 * Created by quimey.arozarena on 3/21/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private IAdicionales mAdicionales;
    private IPerdidos mPerdidos;

    public AlarmReceiver(){}


    @Override
    public void onReceive(Context context, Intent intent) {
        mAdicionales = new IAdicionalesImpl(context);
        mPerdidos = new IPerdidosImpl(context);
        try {
            mAdicionales.cargarDBLocalDonaciones(context);
            mAdicionales.cargarDBLocalInfoUtil(context);
            mPerdidos.cargarDBLocalListaPerdidos(context);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

