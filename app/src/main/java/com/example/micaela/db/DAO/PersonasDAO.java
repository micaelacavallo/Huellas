package com.example.micaela.db.DAO;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.micaela.db.Constantes.CDenuncias;
import com.example.micaela.db.Constantes.CMotivo_denuncia;
import com.example.micaela.db.Constantes.CPersonas;
import com.example.micaela.db.Constantes.Clases;
import com.example.micaela.db.Interfaces.IPersonas;
import com.example.micaela.db.Modelo.MotivoDenuncia;
import com.example.micaela.db.Modelo.Personas;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by quimeyarozarena on 3/5/16.
 */
public class PersonasDAO implements IPersonas {

    private ParseQuery<ParseObject> query;
    private Context context;
    private ParseObject parseObject;
    private List<ParseObject> listParseObject;
    private Personas persona;
    private List<Personas> personas;
    private ParseObject objectAux;


    public PersonasDAO(Context context) {

        this.context = context;
        parseObject = ParseObject.create(Clases.PERSONAS);
        listParseObject = null;
        persona = null;
        objectAux = null;
        personas = new ArrayList<Personas>();

    }


    @Override
    public void denunciar(String id, String motivo) throws ParseException{


        query = ParseQuery.getQuery(Clases.DENUNCIAS);
        query.whereEqualTo(CDenuncias.ID_REFERENCIA, id);

        if(query.count() == 0) {

            MotivoDenuncia motivoDenuncia = this.getMotivoDenuncia(motivo);

            ParseObject objectAux = new ParseObject(Clases.DENUNCIAS);
            objectAux.put(CDenuncias.FECHA, new Date());
            objectAux.put(CDenuncias.ID_REFERENCIA, id);
            objectAux.put(CDenuncias.IS_USER, true);
            objectAux.put(CDenuncias.MOTIVO_DENUNCIA, ParseObject.createWithoutData(Clases.MOTIVODENUNCIA, String.valueOf(motivoDenuncia.getmObjectId())));

            if (internet(context)) {
                objectAux.saveInBackground();
            } else {
                objectAux.saveEventually();
            }

            objectAux.pinInBackground();
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
    public Personas getPersonabyEmail(String email) throws ParseException {

        query = ParseQuery.getQuery(Clases.PERSONAS);
        query.whereEqualTo(CPersonas.EMAIL, email);
        Personas persona = null;
        try {
            if(query.count() != 0) {
                ParseObject object = query.getFirst();
                persona = new Personas(object.getObjectId(), object.getString(CPersonas.EMAIL), object.getString(CPersonas.NOMBRE), object.getString(CPersonas.TELEFONO), object.getBoolean(CPersonas.ADMINISTRADOR), object.getBoolean(CPersonas.BLOQUEADO),  object.getString(CPersonas.CONTRASEÑA), object.getString(CPersonas.FOTO));
            }
        } catch (ParseException e) {
            e.fillInStackTrace();
        }

        return persona;
    }

    @Override
    public Personas getPersonabyId(String objectId) throws ParseException {
        query = ParseQuery.getQuery(Clases.PERSONAS);
        query.whereEqualTo(CPersonas.OBJECT_ID, objectId);
        Personas persona = null;
        try {
            if(query.count() != 0) {
                ParseObject object = query.getFirst();
                persona = new Personas(object.getObjectId(), object.getString(CPersonas.EMAIL), object.getString(CPersonas.NOMBRE), object.getString(CPersonas.TELEFONO), object.getBoolean(CPersonas.ADMINISTRADOR), object.getBoolean(CPersonas.BLOQUEADO),  object.getString(CPersonas.CONTRASEÑA), object.getString(CPersonas.FOTO));
            }
        } catch (ParseException e) {
            e.fillInStackTrace();
        }

        return persona;
    }

    public ParseObject getParseObjectById(String objectId) {

        query = ParseQuery.getQuery(Clases.PERSONAS);
        query.whereEqualTo(CPersonas.OBJECT_ID, objectId);
        checkInternetGet(query);

        try{
            if(query.count() != 0) {
                objectAux = query.getFirst();
            }
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }

        return objectAux;
    }

    @Override
    public void editTelefono(String objectId, String telefono) {

        query = ParseQuery.getQuery(Clases.PERSONAS);
        query.whereEqualTo(CPersonas.OBJECT_ID, objectId);

        try {
            if(query.count() != 0) {
                objectAux = query.getFirst();
                objectAux.put(CPersonas.TELEFONO, telefono);
                save(objectAux);
            }
        } catch (ParseException e) {
            e.fillInStackTrace();
        }
    }

    @Override
    public void checkInternetGet(ParseQuery<ParseObject> query) {
        if (!internet(context)) {
            query.fromLocalDatastore();
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
    public void registar(Personas persona) {

        boolean flag = false;
        personas = this.getPersonas();
        for (int x = 0; x<personas.size();x++) {
            if (persona.getEmail().equals(personas.get(x).getEmail())) {
                flag = true;
            }
        }

        if(!flag)
        {
            parseObject.put(CPersonas.NOMBRE, persona.getNombre());
            parseObject.put(CPersonas.EMAIL, persona.getEmail());
            parseObject.put(CPersonas.TELEFONO, persona.getTelefono());
            parseObject.put(CPersonas.BLOQUEADO, false);
            parseObject.put(CPersonas.ADMINISTRADOR, false);
            parseObject.put(CPersonas.CONTRASEÑA, "-");
            parseObject.put(CPersonas.FOTO, persona.getmFoto());

            save(parseObject);
        }
    }

    @Override
    public List<Personas> getPersonas() {

        query = ParseQuery.getQuery(Clases.PERSONAS);
        checkInternetGet(query);

        try{
            listParseObject = query.find();
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }
        for (ParseObject object : listParseObject) {
            persona = new Personas(object.getObjectId(), object.getString(CPersonas.EMAIL), object.getString(CPersonas.NOMBRE), object.getString(CPersonas.TELEFONO), object.getBoolean(CPersonas.ADMINISTRADOR), object.getBoolean(CPersonas.BLOQUEADO), object.getString(CPersonas.CONTRASEÑA), object.getString(CPersonas.FOTO));
            personas.add(persona);
        }

        return personas;
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

}
