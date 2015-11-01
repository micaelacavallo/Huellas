package com.example.micaela.db.Controladores;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.micaela.db.Enums.CComentarios;
import com.example.micaela.db.Enums.CEstados;
import com.example.micaela.db.Enums.CPersonas;
import com.example.micaela.db.Enums.Clases;
import com.example.micaela.db.Interfaces.IDBLocal;
import com.example.micaela.db.Interfaces.IGeneral;
import com.example.micaela.db.clases.Comentarios;
import com.example.micaela.db.clases.Estados;
import com.example.micaela.db.clases.Personas;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Horacio on 10/10/2015.
 */
public class IGeneralImpl implements IGeneral, IDBLocal{

    @Override
    public Estados getEstado(String situacion) throws ParseException {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.ESTADOS);
        query.whereEqualTo(CEstados.SITUACION, situacion);
        query.whereEqualTo(CEstados.SOLUCIONADO, false);
        Estados estado = null;
        try{ParseObject object = query.getFirst();

         estado = new Estados(object.getObjectId(), object.getInt(CEstados.ID_ESTADO), object.getBoolean(CEstados.SOLUCIONADO), object.getString(CEstados.SITUACION));
        }
        catch(ParseException e)
        {
            e.fillInStackTrace();
        }

        return estado;
    }

    @Override
    public Personas getPersona(String email) throws ParseException {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.PERSONAS);
        query.whereEqualTo(CPersonas.EMAIL, email);
        Personas persona = null;
        try{
            ParseObject object = query.getFirst();
            persona = new Personas(object.getObjectId(), object.getInt(CPersonas.ID_PERSONA), object.getString(CPersonas.EMAIL),object.getString(CPersonas.NOMBRE), object.getString(CPersonas.APELLIDO), object.getString(CPersonas.TELEFONO), object.getBoolean(CPersonas.ADMINISTRADOR));
        }
        catch(ParseException e)
        {
            e.fillInStackTrace();
        }

        return persona;
    }

    @Override
    public List<Comentarios> getComentarios(List<ParseObject> listComentarios, ParseObject object ) {

        Comentarios comentario = null;
        ParseObject objectAux = null;
        Personas persona = null;
        List<Comentarios> comentarios = new ArrayList<Comentarios>();

        for(ParseObject objectComentario : listComentarios)
        {
            objectAux = object.getParseObject(CComentarios.ID_PERSONA);
            persona = new Personas(objectAux.getObjectId(), objectAux.getInt(CPersonas.ID_PERSONA), objectAux.getString(CPersonas.EMAIL), objectAux.getString(CPersonas.NOMBRE), objectAux.getString(CPersonas.APELLIDO), objectAux.getString(CPersonas.TELEFONO), objectAux.getBoolean(CPersonas.ADMINISTRADOR));
            comentario = new Comentarios(objectComentario.getObjectId(), objectComentario.getInt(CComentarios.ID_COMENTARIO), objectComentario.getString(CComentarios.COMENTARIO), persona, objectComentario.getDate(CComentarios.FECHA));
            comentarios.add(comentario);
        }

        return comentarios;
    }

    @Override
    public ParseObject agregarComentario(String comentario, String email, Context context) throws ParseException {

        ParseObject object = new ParseObject(Clases.COMENTARIOS);
        object.put(CComentarios.ID_COMENTARIO, getUltimoComentarioInsertado());
        object.put(CComentarios.COMENTARIO, comentario);
        object.put(CComentarios.FECHA, new Date());
        Personas persona = getPersona(email);
        object.put(CComentarios.ID_PERSONA, ParseObject.createWithoutData(Clases.PERSONAS, String.valueOf(persona.getObjectId())));
        if(internet(context)) {
            saveInBackground(object);
        }
        else
        {
            saveEventually(object);
        }
        pinObjectInBackground(object);
        ParseObject objectComentario = getComentarioById(getUltimoObjectId());

        return objectComentario;
    }

    @Override
    public ParseObject getComentarioById(String objectId) throws ParseException {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.COMENTARIOS);
        query.whereEqualTo(CComentarios.OBJECT_ID, objectId);
       // Comentarios comentario = null;
        ParseObject objectAux = null;

        try {
            objectAux = query.getFirst();
         /*   ParseObject object = objectAux.getParseObject("personas");
            Personas persona = new Personas(object.getObjectId(), object.getInt(CPersonas.ID_PERSONA), object.getString(CPersonas.EMAIL), object.getString(CPersonas.NOMBRE), object.getString(CPersonas.APELLIDO), object.getString(CPersonas.TELEFONO), object.getBoolean(CPersonas.ADMINISTRADOR));
            comentario = new Comentarios(objectAux.getObjectId(), objectAux.getInt(CComentarios.ID_COMENTARIO), objectAux.getString(CComentarios.COMENTARIO), persona, objectAux.getDate(CComentarios.FECHA));*/
        }
        catch (ParseException e)
        {
            e.fillInStackTrace();
        }
        return objectAux;

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
    public void save(ParseObject object) {

    }

    @Override
    public void delete(ParseObject object) {

    }

    @Override
    public void checkInternetGet(ParseQuery<ParseObject> query) {

    }

    public int getUltimoComentarioInsertado() throws ParseException {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.COMENTARIOS);
        query.orderByDescending(CComentarios.ID_COMENTARIO);
        int idUltimo = 0;
        //capturar si no hay nada insertado
        ParseObject objectAux = query.getFirst();
        idUltimo = objectAux.getInt(CComentarios.ID_COMENTARIO) + 1;
        return idUltimo;

    }


    public String getUltimoObjectId() throws ParseException {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.COMENTARIOS);
        query.orderByDescending(CComentarios.ID_COMENTARIO);
        String objectId = null;
        try {
            ParseObject objectAux = query.getFirst();
            objectId = objectAux.getObjectId();
        }
        catch (ParseException e)
        {
            e.fillInStackTrace();
        }

        return objectId;
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
    public void queryFromLocalDatastore(ParseQuery query) {
        query.fromLocalDatastore();
    }

    @Override
    public void saveEventually(ParseObject object) {
        object.saveEventually();
    }

    @Override
    public void saveInBackground(ParseObject object) {

        object.saveInBackground();
    }

    @Override
    public void deleteEventually(ParseObject object) {
        object.deleteEventually();
    }

    @Override
    public void deleteInBackground(ParseObject object) {
        object.deleteInBackground();
    }

    @Override
    public void cargarDBLocal() throws ParseException {

    }
}