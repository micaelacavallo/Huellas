package com.managerapp.db.DAO;

import android.content.Context;

import com.managerapp.db.Constantes.CPersonas;
import com.managerapp.db.Constantes.Clases;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;

/**
 * Created by quimey.arozarena on 3/21/2016.
 */
public class AdminDAO {

    private ParseQuery<ParseObject> query;
    private ParseObject objectAux;
    private GeneralDAO mGeneralDAO;
    private Context context;


    public AdminDAO(Context context){
        this.context = context;
        mGeneralDAO = new GeneralDAO(context);
        objectAux = null;


    }

    public int login(String nombre, String contraseña){

        int result = 0;
        query = ParseQuery.getQuery(Clases.PERSONAS);
        query.whereEqualTo(CPersonas.NOMBRE, nombre);
        query.whereEqualTo(CPersonas.ADMINISTRADOR, true);
        checkInternetGet(query);

        try {
            if(query.count() == 0) {
                result = -1;
            }
            else{
                query = ParseQuery.getQuery(Clases.PERSONAS);
                query.whereEqualTo(CPersonas.NOMBRE, nombre);
                query.whereNotEqualTo(CPersonas.CONTRASEÑA, contraseña);
                checkInternetGet(query);
                if(query.count() != 0) {
                    result = -2;
                }
            }
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        return result;
    }


    public void bloquearPersona(String objectId) throws ParseException {

        ParseObject point = ParseObject.createWithoutData(Clases.PERSONAS, objectId);
        point.put(CPersonas.BLOQUEADO, true);
        save(point);

    }

    public void save(ParseObject object) {

        if(internet(context)) {
            object.saveInBackground();
        }
        else
        {
            object.saveEventually();
        }

        object.pinInBackground();
    }

    public void checkInternetGet(ParseQuery<ParseObject> query) {
        if (!internet(context)) {
            query.fromLocalDatastore();
        }
    }

    public boolean internet(Context context) {
        mGeneralDAO = new GeneralDAO(context);
        return mGeneralDAO.internet(context);
    }
}
