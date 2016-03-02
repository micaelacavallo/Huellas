package com.example.micaela.db.DAO;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.micaela.db.Enums.CComentarios;
import com.example.micaela.db.Enums.CEstados;
import com.example.micaela.db.Enums.CPersonas;
import com.example.micaela.db.Enums.CRazas;
import com.example.micaela.db.Enums.Clases;
import com.example.micaela.db.Interfaces.IDBLocal;
import com.example.micaela.db.Interfaces.IGeneral;
import com.example.micaela.db.Modelo.Comentarios;
import com.example.micaela.db.Modelo.Estados;
import com.example.micaela.db.Modelo.Personas;
import com.example.micaela.db.Modelo.Razas;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by quimey.arozarena on 12/22/2015.
 */
public class GeneralDAO implements IGeneral, IDBLocal {

    private Context context;
    private ParseQuery<ParseObject> query;

    public GeneralDAO() {
    }

    public GeneralDAO(Context context) {
        this.context = context;
    }

    @Override
    public Estados getEstado(String situacion) throws ParseException {

        query = ParseQuery.getQuery(Clases.ESTADOS);
        query.whereEqualTo(CEstados.SITUACION, situacion);
        Estados estado = null;
        try {
            ParseObject object = query.getFirst();
            estado = new Estados(object.getObjectId(), object.getInt(CEstados.ID_ESTADO), object.getString(CEstados.SITUACION));
        } catch (ParseException e) {
            e.fillInStackTrace();
        }

        return estado;
    }


    @Override
    public Personas getPersona(String email) throws ParseException {

        query = ParseQuery.getQuery(Clases.PERSONAS);
        query.whereEqualTo(CPersonas.EMAIL, email);
        Personas persona = null;
        try {
            ParseObject object = query.getFirst();
            persona = new Personas(object.getObjectId(), object.getInt(CPersonas.ID_PERSONA), object.getString(CPersonas.EMAIL), object.getString(CPersonas.NOMBRE), object.getString(CPersonas.APELLIDO), object.getString(CPersonas.TELEFONO), object.getBoolean(CPersonas.ADMINISTRADOR));
        } catch (ParseException e) {
            e.fillInStackTrace();
        }

        return persona;
    }

    @Override
    public List<Comentarios> getComentarios(List<ParseObject> listComentarios, ParseObject object) {

        Comentarios comentario = null;
        ParseObject objectAux = null;
        Personas persona = null;
        List<Comentarios> comentarios = null;

        if (listComentarios != null) {
            comentarios = new ArrayList<Comentarios>();
            for (ParseObject objectComentario : listComentarios) {
                objectAux = object.getParseObject(CComentarios.ID_PERSONA);
                persona = new Personas(objectAux.getObjectId(), objectAux.getInt(CPersonas.ID_PERSONA), objectAux.getString(CPersonas.EMAIL), objectAux.getString(CPersonas.NOMBRE), objectAux.getString(CPersonas.APELLIDO), objectAux.getString(CPersonas.TELEFONO), objectAux.getBoolean(CPersonas.ADMINISTRADOR));
                comentario = new Comentarios(objectComentario.getObjectId(), objectComentario.getInt(CComentarios.ID_COMENTARIO), objectComentario.getString(CComentarios.COMENTARIO), persona, objectComentario.getDate(CComentarios.FECHA));
                comentarios.add(comentario);
            }
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
        if (internet(context)) {
            saveInBackground(object);
        } else {
            saveEventually(object);
        }
        pinObjectInBackground(object);
        ParseObject objectComentario = getComentarioById(getUltimoObjectId());

        return objectComentario;
    }

    @Override
    public ParseObject getComentarioById(String objectId) throws ParseException {

        query = ParseQuery.getQuery(Clases.COMENTARIOS);
        query.whereEqualTo(CComentarios.OBJECT_ID, objectId);
        // Comentarios comentario = null;
        ParseObject objectAux = null;

        try {
            objectAux = query.getFirst();
         /*   ParseObject object = objectAux.getParseObject("personas");
            Personas persona = new Personas(object.getObjectId(), object.getInt(CPersonas.ID_PERSONA), object.getString(CPersonas.EMAIL), object.getString(CPersonas.NOMBRE), object.getString(CPersonas.APELLIDO), object.getString(CPersonas.TELEFONO), object.getBoolean(CPersonas.ADMINISTRADOR));
            comentario = new Comentarios(objectAux.getObjectId(), objectAux.getInt(CComentarios.ID_COMENTARIO), objectAux.getString(CComentarios.COMENTARIO), persona, objectAux.getDate(CComentarios.FECHA));*/
        } catch (ParseException e) {
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

    @Override
    public List<Estados> getEstados() {
        query = ParseQuery.getQuery(Clases.ESTADOS);
        List<Estados> listEstados = null;
        List<ParseObject> listParseObject = null;
        Estados estado = null;
        checkInternetGet(query);
        try {
            listParseObject = query.find();
        } catch (ParseException e) {
            Toast.makeText(context, "no existe", Toast.LENGTH_LONG);
        }

        for (ParseObject object : listParseObject) {
            estado = new Estados(object.getObjectId(), object.getInt(CEstados.ID_ESTADO), object.getString(CEstados.SITUACION));
            listEstados.add(estado);
        }

        return listEstados;
    }

    public int getUltimoComentarioInsertado() throws ParseException {

        query = ParseQuery.getQuery(Clases.COMENTARIOS);
        query.orderByDescending(CComentarios.ID_COMENTARIO);
        int idUltimo = 0;
        //capturar si no hay nada insertado
        ParseObject objectAux = query.getFirst();
        idUltimo = objectAux.getInt(CComentarios.ID_COMENTARIO) + 1;
        return idUltimo;

    }


    public String getUltimoObjectId() throws ParseException {

        query = ParseQuery.getQuery(Clases.COMENTARIOS);
        query.orderByDescending(CComentarios.ID_COMENTARIO);
        String objectId = null;
        try {
            ParseObject objectAux = query.getFirst();
            objectId = objectAux.getObjectId();
        } catch (ParseException e) {
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
    public void cargarDBLocal(Context context) throws ParseException {

    }
}
