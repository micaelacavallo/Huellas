package com.example.micaela.db.DAO;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.micaela.db.AlarmManager.AlarmReceiver;
import com.example.micaela.db.Constantes.CComentarios;
import com.example.micaela.db.Constantes.CPerdidos;
import com.example.micaela.db.Constantes.CPersonas;
import com.example.micaela.db.Constantes.Clases;
import com.example.micaela.db.Controladores.IPersonasImpl;
import com.example.micaela.db.Interfaces.IComentarios;
import com.example.micaela.db.Interfaces.IDBLocal;
import com.example.micaela.db.Interfaces.IGeneral;
import com.example.micaela.db.Interfaces.IPersonas;
import com.example.micaela.db.Modelo.Comentarios;
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
 * Created by quimey.arozarena on 5/2/2016.
 */
public class ComentariosDAO implements IComentarios, IGeneral, IDBLocal {

    private Context context;
    private ParseQuery<ParseObject> query;
    private IPersonas mPersonasImpl;
    private ParseObject objectAux;
    private ParseObject object;
    private List<Comentarios> comentarios;
    private List<ParseObject> listParseObject;
    private Comentarios comentario;
    private Personas persona;

    public ComentariosDAO() {
    }

    public ComentariosDAO(Context context) {
        this.context = context;
        objectAux = null;
        mPersonasImpl = new IPersonasImpl(context);
        comentarios = new ArrayList<Comentarios>();
        listParseObject = null;
        comentario = null;
        persona = null;
        object = null;


    }

    @Override
    public ArrayList<Comentarios> getComentarios(List<ParseObject> listComentarios, ParseObject object) {

        Comentarios comentario = null;
        ParseObject objectAux = null;
        Personas persona = null;
        ArrayList<Comentarios> comentarios = null;

        if (listComentarios != null) {
            comentarios = new ArrayList<>();
            for (ParseObject objectComentario : listComentarios) {
                objectAux = objectComentario.getParseObject(CComentarios.ID_PERSONA);
                persona = new Personas(objectAux.getObjectId(), objectAux.getString(CPersonas.EMAIL), objectAux.getString(CPersonas.NOMBRE), objectAux.getString(CPersonas.TELEFONO), objectAux.getBoolean(CPersonas.ADMINISTRADOR), objectAux.getBoolean(CPersonas.BLOQUEADO), objectAux.getString(CPersonas.CONTRASEÑA), objectAux.getString(CPersonas.FOTO));
                comentario = new Comentarios(objectComentario.getObjectId(), objectComentario.getString(CComentarios.COMENTARIO), persona, objectComentario.getDate(CComentarios.FECHA), objectComentario.getBoolean(CComentarios.LEIDO));
                comentarios.add(comentario);
            }
        }
        return comentarios;
    }

    @Override
    public ParseObject agregarComentario(String comentario, String email, Context context) throws ParseException {

        ParseObject object = new ParseObject(Clases.COMENTARIOS);
        object.put(CComentarios.COMENTARIO, comentario);
        object.put(CComentarios.LEIDO, false);
        object.put(CComentarios.FECHA, new Date());
        persona = mPersonasImpl.getPersonabyEmail(email);
        object.put(CComentarios.ID_PERSONA, ParseObject.createWithoutData(Clases.PERSONAS, String.valueOf(persona.getObjectId())));
        save(object);
        ParseObject objectComentario = getComentarioById(getUltimoObjectId());

        // Send push notification to query

        JSONObject object2 = new JSONObject();
        try {
            List<String> emails = new ArrayList<String>();
            emails.add("micaela.cavallo@outlook.com");
            emails.add("kimy_1_8@hotmail.com");

            // Create our Installation query
            ParseQuery pushQuery = ParseInstallation.getQuery();
            //pushQuery.whereEqualTo("email", persona.getEmail());

            pushQuery.whereContainedIn("email", emails);
            object2.put("title", "Se ha agregado un comentario en una publicacion");
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
        {
            e.printStackTrace();
        }


        return objectComentario;
    }

    @Override
    public ParseObject getComentarioById(String objectId) throws ParseException {

        query = ParseQuery.getQuery(Clases.COMENTARIOS);
        query.include(CComentarios.ID_PERSONA);
        query.whereEqualTo(CComentarios.OBJECT_ID, objectId);
        checkInternetGet(query);

        try {
            if(query.count() != 0) {
                object = query.getFirst();
            }
        } catch (ParseException e) {
            e.fillInStackTrace();
        }
        return object;

    }
    @Override
    public List<Comentarios> getComentariosNoLeidos(String userObjectId) {

        ParseObject obj = ParseObject.createWithoutData(Clases.PERSONAS, userObjectId);

        query = ParseQuery.getQuery(Clases.COMENTARIOS);
        query.include(CComentarios.ID_PERSONA);
        query.whereEqualTo(CComentarios.ID_PERSONA, obj);
        query.whereEqualTo(CComentarios.LEIDO, false);

        checkInternetGet(query);

        try{
            listParseObject = query.find();
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }

        if (listParseObject.size() > 0) {
            for (ParseObject object : listParseObject) {
                objectAux = object.getParseObject(CPerdidos.ID_PERSONA);
                persona = new Personas(objectAux.getObjectId(), objectAux.getString(CPersonas.EMAIL), objectAux.getString(CPersonas.NOMBRE), objectAux.getString(CPersonas.TELEFONO), objectAux.getBoolean(CPersonas.ADMINISTRADOR), objectAux.getBoolean(CPersonas.BLOQUEADO), objectAux.getString(CPersonas.CONTRASEÑA), objectAux.getString(CPersonas.FOTO));

                comentario = new Comentarios(object.getObjectId(), object.getString(CComentarios.COMENTARIO), persona, object.getDate(CComentarios.FECHA), object.getBoolean(CComentarios.LEIDO));
                comentarios.add(comentario);
            }
        }


        return comentarios;
    }

    @Override
    public void cambiarLeidoComentario(String comentarioObjectId, boolean leido) {

        query = ParseQuery.getQuery(Clases.COMENTARIOS);
        query.whereEqualTo(CComentarios.OBJECT_ID, comentarioObjectId);

        try {
            if(query.count() != 0) {
                objectAux = query.getFirst();
                objectAux.put(CComentarios.LEIDO, leido);
                save(objectAux);
            }
        } catch (ParseException e) {
            e.fillInStackTrace();
        }

    }

    @Override
    public void borrarComentario(String objectId) {

        query = ParseQuery.getQuery(Clases.COMENTARIOS);
        query.whereEqualTo(CComentarios.OBJECT_ID, objectId);
        checkInternetGet(query);
        try {
            if(query.count() != 0) {
                objectAux = query.getFirst();
                delete(objectAux);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

        if (internet(context)) {
            saveInBackground(object);
        } else {
            saveEventually(object);
        }

        pinObjectInBackground(object);

    }

    @Override
    public void delete(ParseObject object) {

        if(internet(context)) {
            deleteInBackground(object);
        }
        else {
            deleteEventually(object);
        }

        unpinObjectInBackground(object);

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

    @Override
    public void startAlert() {

        long repeatTime = TimeUnit.MINUTES.toMillis(180);

        AlarmManager service = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, 0);

        //AlarmManager.RTC_WAKEUP se sigue ejecutando por mas que este apagado
        service.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), repeatTime, pending);
    }
}
