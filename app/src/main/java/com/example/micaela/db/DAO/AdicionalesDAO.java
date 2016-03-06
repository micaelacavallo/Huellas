package com.example.micaela.db.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.micaela.db.Controladores.IGeneralImpl;
import com.example.micaela.db.Constantes.CAdicionales;
import com.example.micaela.db.Constantes.CColores;
import com.example.micaela.db.Constantes.CEstados;
import com.example.micaela.db.Constantes.CPersonas;
import com.example.micaela.db.Constantes.Clases;
import com.example.micaela.db.Interfaces.IAdicionales;
import com.example.micaela.db.Interfaces.IDBLocal;
import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.db.Modelo.Colores;
import com.example.micaela.db.Modelo.Comentarios;
import com.example.micaela.db.Modelo.Estados;
import com.example.micaela.db.Modelo.Personas;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quimey.arozarena on 12/22/2015.
 */
public class AdicionalesDAO extends IGeneralImpl implements IAdicionales, IDBLocal {


    ParseObject adicionalObject;
    Context context;

    ParseQuery<ParseObject> query;
    ParseObject objectAux;
    Personas persona;
    Estados estado;
    ParseFile foto;
    List<ParseObject> listParseObject;
    ParseRelation objectRelation;
    List<Comentarios> comentarios;
    List<Adicionales> adicionales = new ArrayList<>();
    Adicionales adicional = null;
    Comentarios comentario;

    public AdicionalesDAO(Context context)
    {
        this.context = context;
        init();
    }

    public void init()
    {
        adicionalObject = ParseObject.create(Clases.ADICIONALES);
        query = null;
        objectAux = null;
        persona = null;
        estado = null;
        comentario = null;
        foto = null;
        listParseObject = null;
        objectRelation = null;
        comentarios = new ArrayList<>();
        adicionales = new ArrayList<>();
        adicional = null;
        comentarios = new ArrayList<>();

    }

    public void saveColor(Colores color)
    {
        objectAux = new ParseObject(Clases.COLORES);
        objectAux.put(CColores.COLOR, color.getColor());
        save(objectAux);
    }

    private ParseObject getEstadoByID(String estadoID) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.ESTADOS);
        query.whereEqualTo(CEstados.OBJECT_ID, estadoID);
        if(query.count() != 0) {
            return query.getFirst();
        }else{return null;}
    }

    @Override
    public List<Adicionales> getInfoUtil() throws ParseException {

        adicionales.clear();

        listParseObject = getInfoUtilParseObject();

        if(listParseObject.size() > 0){
            for(ParseObject object : listParseObject)
            {
                objectAux = object.getParseObject(CAdicionales.ID_PERSONA);
                persona = new Personas(objectAux.getObjectId(), objectAux.getString(CPersonas.EMAIL), objectAux.getString(CPersonas.NOMBRE), objectAux.getString(CPersonas.APELLIDO), objectAux.getString(CPersonas.TELEFONO), objectAux.getBoolean(CPersonas.ADMINISTRADOR), objectAux.getBoolean(CPersonas.BLOQUEADO));

                objectAux = object.getParseObject(CAdicionales.ID_ESTADO);
                estado = new Estados(objectAux.getObjectId(), objectAux.getString(CEstados.SITUACION));

                try {
                    objectRelation = object.getRelation(CAdicionales.ID_COMENTARIOS);
                    comentarios = getComentarios(objectRelation.getQuery().find(), object);
                }catch (Exception e)
                {
                    comentarios = null;
                }

                foto = object.getParseFile(CAdicionales.FOTOS);

                byte[] image = null;
                if (foto != null) {
                    image = foto.getData();
                }
                adicional = new Adicionales(persona, estado, object.getDate(CAdicionales.FECHA), object.getString(CAdicionales.TITULO), object.getString(CAdicionales.DESCRIPCION), image, object.getBoolean(CAdicionales.DONACION), comentarios);
                adicionales.add(adicional);
            }
        }

        return adicionales;
    }

    public List<ParseObject> getInfoUtilParseObject() throws ParseException {

        query = ParseQuery.getQuery(Clases.ADICIONALES);
        query.include(CAdicionales.ID_PERSONA);
        query.include(CAdicionales.ID_ESTADO);
        query.whereEqualTo(CAdicionales.DONACION, false);
        query.orderByDescending(CAdicionales.FECHA);
        //  query.whereEqualTo(CAdicionales.ID_ESTADO, getEstadoByID("D4ATvjnhj4"));
        checkInternetGet(query);

        try{
            listParseObject = query.find();
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }

        return listParseObject;
    }


    @Override
    public List<Adicionales> getDonaciones() throws ParseException {

        adicionales.clear();

        listParseObject = getDonacionesParseObject();

        if(listParseObject.size() > 0){
            for(ParseObject object : listParseObject)
            {
                objectAux = object.getParseObject(CAdicionales.ID_PERSONA);
                persona = new Personas(objectAux.getObjectId(), objectAux.getString(CPersonas.EMAIL), objectAux.getString(CPersonas.NOMBRE), objectAux.getString(CPersonas.APELLIDO), objectAux.getString(CPersonas.TELEFONO), objectAux.getBoolean(CPersonas.ADMINISTRADOR), objectAux.getBoolean(CPersonas.BLOQUEADO));

                objectAux = object.getParseObject(CAdicionales.ID_ESTADO);
                estado = new Estados(objectAux.getObjectId(), objectAux.getString(CEstados.SITUACION));


                try {
                    objectRelation = object.getRelation(CAdicionales.ID_COMENTARIOS);
                    comentarios = getComentarios(objectRelation.getQuery().find(), object);
                }catch (Exception e)
                {
                    comentarios = null;
                }

                foto = object.getParseFile(CAdicionales.FOTOS);
                byte[] image = null;
                if (foto != null) {
                    image = foto.getData();
                }

                adicional = new Adicionales(persona, estado, object.getDate(CAdicionales.FECHA), object.getString(CAdicionales.TITULO), object.getString(CAdicionales.DESCRIPCION), image, object.getBoolean(CAdicionales.DONACION), comentarios);
                adicionales.add(adicional);
            }
        }

        return adicionales;
    }

    public List<ParseObject> getDonacionesParseObject() throws ParseException {

        query = ParseQuery.getQuery(Clases.ADICIONALES);
        query.include(CAdicionales.ID_PERSONA);
        query.include(CAdicionales.ID_ESTADO);
        query.whereEqualTo(CAdicionales.DONACION, true);
        query.orderByDescending(CAdicionales.FECHA);
        //  query.whereEqualTo(CAdicionales.ID_ESTADO, getEstadoByID("D4ATvjnhj4"));
        checkInternetGet(query);

        try{
            listParseObject = query.find();
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }

        return listParseObject;
    }


    @Override
    public Adicionales getAdicionalById(String idAdicional) {

        query = ParseQuery.getQuery(Clases.ADICIONALES);
        query.include(CAdicionales.ID_PERSONA);
        query.include(CAdicionales.ID_ESTADO);
        query.whereEqualTo(CAdicionales.OBJECT_ID, idAdicional);
        checkInternetGet(query);

        try {
            if (query.count() != 0) {
                ParseObject object = query.getFirst();

                objectAux = object.getParseObject(CAdicionales.ID_PERSONA);
                persona = new Personas(objectAux.getObjectId(), objectAux.getString(CPersonas.EMAIL), objectAux.getString(CPersonas.NOMBRE), objectAux.getString(CPersonas.APELLIDO), objectAux.getString(CPersonas.TELEFONO), objectAux.getBoolean(CPersonas.ADMINISTRADOR), objectAux.getBoolean(CPersonas.BLOQUEADO));
                objectAux = object.getParseObject(CAdicionales.ID_ESTADO);
                estado = new Estados(objectAux.getObjectId(), objectAux.getString(CEstados.SITUACION));
                objectRelation = object.getRelation(CAdicionales.ID_COMENTARIOS);
                comentarios = getComentarios(objectRelation.getQuery().find(), object);
                foto = object.getParseFile(CAdicionales.FOTOS);
                byte[] image = null;
                if (foto != null) {
                    image = foto.getData();
                }
                adicional = new Adicionales(persona, estado, object.getDate(CAdicionales.FECHA), object.getString(CAdicionales.TITULO), object.getString(CAdicionales.DESCRIPCION), image, object.getBoolean(CAdicionales.DONACION), comentarios);
            }
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }

        return adicional;
    }

    @Override
    public void saveAdicional(Adicionales adicional) {

        //VALIDAR EN FE
        adicionalObject.put(CAdicionales.TITULO, adicional.getTitulo());
        adicionalObject.put(CAdicionales.DESCRIPCION, adicional.getDescripcion());
        adicionalObject.put(CAdicionales.FECHA, adicional.getFecha());
        adicionalObject.put(CAdicionales.FOTOS, adicional.getFoto());

        try {
            persona = getPersona(adicional.getPersona().getEmail());
            estado = getEstado("-");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        adicionalObject.put(CAdicionales.ID_ESTADO, ParseObject.createWithoutData(Clases.ESTADOS, String.valueOf(estado.getObjectId())));
        adicionalObject.put(CAdicionales.ID_PERSONA, ParseObject.createWithoutData(Clases.PERSONAS, String.valueOf(persona.getObjectId())));
        save(adicionalObject);
    }


    public ParseObject cargarAdicional(Adicionales adicional)
    {
        adicionalObject.put(CAdicionales.TITULO, adicional.getTitulo());
        adicionalObject.put(CAdicionales.DESCRIPCION, adicional.getDescripcion());
        adicionalObject.put(CAdicionales.FECHA, adicional.getFecha());
        adicionalObject.put(CAdicionales.FOTOS, adicional.getFoto());
        adicionalObject.put(CAdicionales.OBJECT_ID, adicional.getObjectId());
        adicionalObject.put(CAdicionales.ID_ESTADO, ParseObject.createWithoutData(Clases.ESTADOS, String.valueOf(adicional.getEstado().getObjectId())));
        adicionalObject.put(CAdicionales.ID_PERSONA, ParseObject.createWithoutData(Clases.PERSONAS, String.valueOf(adicional.getPersona().getObjectId())));

        return adicionalObject;
    }

    @Override
    public void editAdicional(Adicionales adicional) throws ParseException {

        objectAux = cargarAdicional(adicional);
        deleteAdicional(adicional.getObjectId());
        saveAdicional(adicional);
    }

    @Override
    public void deleteAdicional(String objectId) throws ParseException {

        query = ParseQuery.getQuery(Clases.ADICIONALES);
        query.whereEqualTo(CAdicionales.OBJECT_ID, objectId);
        checkInternetGet(query);
        if(query.count() != 0) {
            objectAux = query.getFirst();
            objectRelation = objectAux.getRelation(CAdicionales.ID_COMENTARIOS);
            try {
                listParseObject = objectRelation.getQuery().find();

                for (ParseObject parseObject : listParseObject) {
                    delete(parseObject);
                }

                delete(objectAux);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void AgregarComentarioAdicional(String adicionalObjectId, String comentarioText, String email) throws ParseException {

        ParseObject parseObjectComentario = agregarComentario(comentarioText, email, context);
        objectAux = getParseObjectById(adicionalObjectId);
        objectRelation = objectAux.getRelation(CAdicionales.ID_COMENTARIOS);
        objectRelation.add(parseObjectComentario);
        save(objectAux);
    }

    public ParseObject getParseObjectById(String objectId) {

        query = ParseQuery.getQuery(Clases.ADICIONALES);
        query.whereEqualTo(CAdicionales.OBJECT_ID, objectId);
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
    public void cargarDBLocalDonaciones(Context context) throws ParseException {

        if(internet(context)) {
            ParseObject.pinAllInBackground(getDonacionesParseObject());
        }
    }

    @Override
    public void cargarDBLocalInfoUtil(Context context) throws ParseException {

        if(internet(context)) {
            ParseObject.pinAllInBackground(getInfoUtilParseObject());
        }
    }

    @Override
    public void save(ParseObject object) {

        if(internet(context)) {
            saveInBackground(object);
        }
        else
        {
            saveEventually(object);
        }

        pinObjectInBackground(object);

    }

    @Override
    public void delete(ParseObject object) {

        if(internet(context)) {
            deleteInBackground(objectAux);
        }
        else {
            deleteEventually(objectAux);
        }

        unpinObjectInBackground(objectAux);
    }


    @Override
    public void checkInternetGet(ParseQuery<ParseObject> query)
    {
        if(!internet(context)) {
            query.fromLocalDatastore();
        }
    }
}
