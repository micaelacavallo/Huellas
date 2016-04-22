package com.example.micaela.db.DAO;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.micaela.db.AlarmManager.AlarmReceiver;
import com.example.micaela.db.Constantes.CComentarios;
import com.example.micaela.db.Constantes.CDenuncias;
import com.example.micaela.db.Constantes.CEstados;
import com.example.micaela.db.Constantes.CMotivo_denuncia;
import com.example.micaela.db.Constantes.CPerdidos;
import com.example.micaela.db.Constantes.CPersonas;
import com.example.micaela.db.Constantes.Clases;
import com.example.micaela.db.Interfaces.IDBLocal;
import com.example.micaela.db.Interfaces.IGeneral;
import com.example.micaela.db.Modelo.Comentarios;
import com.example.micaela.db.Modelo.Estados;
import com.example.micaela.db.Modelo.MotivoDenuncia;
import com.example.micaela.db.Modelo.Personas;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SendCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
            if(query.count() != 0) {
                ParseObject object = query.getFirst();
                estado = new Estados(object.getObjectId(), object.getString(CEstados.SITUACION));
            }
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
            if(query.count() != 0) {
                ParseObject object = query.getFirst();
                persona = new Personas(object.getObjectId(), object.getString(CPersonas.EMAIL), object.getString(CPersonas.NOMBRE), object.getString(CPersonas.APELLIDO), object.getString(CPersonas.TELEFONO), object.getBoolean(CPersonas.ADMINISTRADOR), object.getBoolean(CPersonas.BLOQUEADO),  object.getString(CPersonas.CONTRASEÑA));
            }
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
                persona = new Personas(objectAux.getObjectId(), objectAux.getString(CPersonas.EMAIL), objectAux.getString(CPersonas.NOMBRE), objectAux.getString(CPersonas.APELLIDO), objectAux.getString(CPersonas.TELEFONO), objectAux.getBoolean(CPersonas.ADMINISTRADOR), objectAux.getBoolean(CPersonas.BLOQUEADO), objectAux.getString(CPersonas.CONTRASEÑA));
                comentario = new Comentarios(objectComentario.getObjectId(), objectComentario.getString(CComentarios.COMENTARIO), persona, objectComentario.getDate(CComentarios.FECHA));
                comentarios.add(comentario);
            }
        }
            return comentarios;
    }

    @Override
    public ParseObject agregarComentario(String comentario, String email, Context context) throws ParseException {

        ParseObject object = new ParseObject(Clases.COMENTARIOS);
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

        //push notification
        // Create our Installation query
        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("user", "Quimey");

        // Send push notification to query

        JSONObject object2 = new JSONObject();
        try {
            object2.put("title", "se ha agregado un comentario en una publicacion");
            object2.put("description", "traer comentario de la publicacion");
            ParsePush push = new ParsePush();
            push.setQuery(pushQuery); // Set our Installation query
            push.setData(object2);
            push.sendInBackground(new SendCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e)
        {e.printStackTrace();}


        return objectComentario;
    }

    @Override
    public ParseObject getComentarioById(String objectId) throws ParseException {

        query = ParseQuery.getQuery(Clases.COMENTARIOS);
        query.whereEqualTo(CComentarios.OBJECT_ID, objectId);
        // Comentarios comentario = null;
        ParseObject objectAux = null;

        try {
            if(query.count() != 0) {
                objectAux = query.getFirst();
         /*   ParseObject object = objectAux.getParseObject("personas");
            Personas persona = new Personas(object.getObjectId(), object.getInt(CPersonas.ID_PERSONA), object.getString(CPersonas.EMAIL), object.getString(CPersonas.NOMBRE), object.getString(CPersonas.APELLIDO), object.getString(CPersonas.TELEFONO), object.getBoolean(CPersonas.ADMINISTRADOR));
            comentario = new Comentarios(objectAux.getObjectId(), objectAux.getInt(CComentarios.ID_COMENTARIO), objectAux.getString(CComentarios.COMENTARIO), persona, objectAux.getDate(CComentarios.FECHA));*/

            }
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
        List<Estados> listEstados = new ArrayList<>();
        List<ParseObject> listParseObject = null;
        Estados estado = null;
        checkInternetGet(query);
        try {
            listParseObject = query.find();
        } catch (ParseException e) {
            Toast.makeText(context, "no existe", Toast.LENGTH_LONG);
        }

        for (ParseObject object : listParseObject) {
            estado = new Estados(object.getObjectId(), object.getString(CEstados.SITUACION));
            listEstados.add(estado);
        }

        return listEstados;
    }

    @Override
    public void cambiarEstado(String idpublicacion, boolean estado) {

        query = ParseQuery.getQuery(Clases.PERDIDOS);
        query.whereEqualTo(CPerdidos.OBJECT_ID, idpublicacion);
        ParseObject objectAux = null;

        try {
            if(query.count() != 0) {
                objectAux = query.getFirst();
                objectAux.put(CPerdidos.SOLUCIONADO, estado);
                if (internet(context)) {
                    saveInBackground(objectAux);
                } else {
                    saveEventually(objectAux);
                }
                pinObjectInBackground(objectAux);
            }
        } catch (ParseException e) {
            e.fillInStackTrace();
        }
    }

    @Override
    public void denunciar(String id, String motivo) throws ParseException {

        query = ParseQuery.getQuery(Clases.DENUNCIAS);
        query.whereEqualTo(CDenuncias.ID, id);

        if(query.count() == 0) {

            MotivoDenuncia motivoDenuncia = this.getMotivoDenuncia(motivo);

            ParseObject objectAux = new ParseObject(Clases.DENUNCIAS);
            objectAux.put(CDenuncias.FECHA, new Date());
            objectAux.put(CDenuncias.ID, id);
            objectAux.put(CDenuncias.IS_USER, false);
            objectAux.put(CDenuncias.MOTIVO_DENUNCIA, ParseObject.createWithoutData(Clases.MOTIVODENUNCIA, String.valueOf(motivoDenuncia.getmObjectId())));

            if (internet(context)) {
                saveInBackground(objectAux);
            } else {
                saveEventually(objectAux);
            }
            pinObjectInBackground(objectAux);
        }

    }

    @Override
    public MotivoDenuncia getMotivoDenuncia(String motivo) {

        query = ParseQuery.getQuery(Clases.MOTIVODENUNCIA);
        query.whereEqualTo(CMotivo_denuncia.MOTIVO, motivo);
        MotivoDenuncia motivoDenuncia = null;
        try {
            if(query.count() != 0) {
                ParseObject object = query.getFirst();
                motivoDenuncia = new MotivoDenuncia(object.getObjectId(), object.getString(CMotivo_denuncia.MOTIVO));
            }
        } catch (ParseException e) {
            e.fillInStackTrace();
        }

        return motivoDenuncia;
    }

    @Override
    public List<MotivoDenuncia> getMotivoDenuncias() {

        query = ParseQuery.getQuery(Clases.MOTIVODENUNCIA);
        List<MotivoDenuncia> listMotivoDenuncia = new ArrayList<MotivoDenuncia>();
        List<ParseObject> listParseObject = null;
        MotivoDenuncia motivoDenuncia = null;
        checkInternetGet(query);
        try {
            listParseObject = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (ParseObject object : listParseObject) {
            motivoDenuncia = new MotivoDenuncia(object.getObjectId(), object.getString(CMotivo_denuncia.MOTIVO));
            listMotivoDenuncia.add(motivoDenuncia);
        }

        return listMotivoDenuncia;
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

    public String getUltimoObjectId() throws ParseException {

        query = ParseQuery.getQuery(Clases.COMENTARIOS);
        query.orderByDescending(CComentarios.CREATEDAT);
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
