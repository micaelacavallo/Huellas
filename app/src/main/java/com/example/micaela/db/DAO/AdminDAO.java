package com.example.micaela.db.DAO;

import android.content.Context;

import com.example.micaela.db.Constantes.CPersonas;
import com.example.micaela.db.Constantes.Clases;
import com.example.micaela.db.Modelo.Personas;
import com.example.micaela.db.Modelo.Sexos;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

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


    }
    public boolean login(String nombre, String contraseña){

        boolean exist = false;
        query = ParseQuery.getQuery(Clases.PERSONAS);
        query.whereEqualTo(CPersonas.NOMBRE, nombre);
        query.whereEqualTo(CPersonas.CONTRASEÑA, contraseña);
        checkInternetGet(query);

        try {
            if(query.count() != 0) {
                objectAux = query.getFirst();
                exist = true;
            }
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        return exist;
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
