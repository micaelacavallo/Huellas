package com.example.micaela.db.Controladores;

import android.content.Context;
import android.widget.Toast;

import com.example.micaela.db.Enums.CAdicionales;
import com.example.micaela.db.Enums.CColores;
import com.example.micaela.db.Enums.CEstados;
import com.example.micaela.db.Enums.CPersonas;
import com.example.micaela.db.Enums.Clases;
import com.example.micaela.db.Enums.Keys;
import com.example.micaela.db.Interfaces.IAdicionales;
import com.example.micaela.db.Interfaces.IDBLocal;
import com.example.micaela.db.clases.Adicionales;
import com.example.micaela.db.clases.Colores;
import com.example.micaela.db.clases.Comentarios;
import com.example.micaela.db.clases.Estados;
import com.example.micaela.db.clases.Personas;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.List;

import bolts.Task;


/**
 * Created by Quimey on 19/09/2015.
 */
public class IAdicionalesImpl extends IGeneralImpl implements IAdicionales, IDBLocal {

    ParseObject adicionalObject;
    Context context;

    ParseQuery<ParseObject> query;
    ParseObject objectAux;
    Personas persona;
    Estados estado;
    ArrayList<String> fotos;
    List<ParseObject> listParseObject;
    ParseRelation objectRelation;
    List<Comentarios> comentarios;
    List<Adicionales> adicionales = new ArrayList<Adicionales>();
    Adicionales adicional = null;
    Comentarios comentario;

    public IAdicionalesImpl(Context context)
    {
        Parse.enableLocalDatastore(context);
        Parse.initialize(context, Keys.APPLICATION_ID, Keys.CLIENT_ID);
        this.context = context;
        init();
    }

    public void init()
    {
        adicionalObject = new ParseObject(Clases.ADICIONALES);
        query = null;
        objectAux = null;
        persona = null;
        estado = null;
        comentario = null;
        fotos = null;
        listParseObject = null;
        objectRelation = null;
        comentarios = new ArrayList<Comentarios>();
        adicionales = new ArrayList<Adicionales>();
        adicional = null;
        comentarios = new ArrayList<Comentarios>();

    }

    public void saveColor(Colores color)
    {
        objectAux = new ParseObject(Clases.COLORES);
        objectAux.put(CColores.ID_COLOR, color.getIdColor());
        objectAux.put(CColores.COLOR, color.getColor());
        save(objectAux);
    }

    @Override
    public List<Adicionales> getAdicionales() throws ParseException {

        query = ParseQuery.getQuery(Clases.ADICIONALES);
        query.include(CAdicionales.ID_PERSONA);
        query.include(CAdicionales.ID_ESTADO);
        checkInternetGet(query);

        try{
            listParseObject = query.find();

            if(listParseObject.size() > 0){
                for(ParseObject object : listParseObject)
                {
                    objectAux = object.getParseObject(CAdicionales.ID_PERSONA);
                    persona = new Personas(objectAux.getObjectId(), objectAux.getInt(CPersonas.ID_PERSONA), objectAux.getString(CPersonas.EMAIL), objectAux.getString(CPersonas.NOMBRE), objectAux.getString(CPersonas.APELLIDO), objectAux.getString(CPersonas.TELEFONO), objectAux.getBoolean(CPersonas.ADMINISTRADOR));
                    objectAux = object.getParseObject(CAdicionales.ID_ESTADO);
                    estado = new Estados(objectAux.getObjectId(), objectAux.getInt(CEstados.ID_ESTADO), objectAux.getBoolean(CEstados.SOLUCIONADO), objectAux.getString(CEstados.SITUACION));
                    objectRelation = object.getRelation(CAdicionales.ID_COMENTARIOS);
                    comentarios = getComentarios(objectRelation.getQuery().find(), object);

                    fotos = (ArrayList<String>) object.get(CAdicionales.FOTOS);
                    adicional = new Adicionales(object.getInt(CAdicionales.ID_ADICIONAL), persona, estado, object.getDate(CAdicionales.FECHA), object.getString(CAdicionales.TITULO), object.getString(CAdicionales.DESCRIPCION), fotos, comentarios);
                    adicionales.add(adicional);
                }
            }
        }
        catch(ParseException e)
        {
            Toast.makeText(context, "no existe", Toast.LENGTH_LONG);
        }

        return adicionales;
    }

    @Override
    public Adicionales getAdicionalById(int idAdicional) {

        query = ParseQuery.getQuery(Clases.ADICIONALES);
        query.include(CAdicionales.ID_PERSONA);
        query.include(CAdicionales.ID_ESTADO);
        query.whereEqualTo(CAdicionales.ID_ADICIONAL, idAdicional);
        checkInternetGet(query);

        try{
            ParseObject object = query.getFirst();

             objectAux = object.getParseObject(CAdicionales.ID_PERSONA);
             persona = new Personas(objectAux.getObjectId(), objectAux.getInt(CPersonas.ID_PERSONA), objectAux.getString(CPersonas.EMAIL), objectAux.getString(CPersonas.NOMBRE), objectAux.getString(CPersonas.APELLIDO), objectAux.getString(CPersonas.TELEFONO), objectAux.getBoolean(CPersonas.ADMINISTRADOR));
             objectAux = object.getParseObject(CAdicionales.ID_ESTADO);
             estado = new Estados(objectAux.getObjectId(), objectAux.getInt(CEstados.ID_ESTADO), objectAux.getBoolean(CEstados.SOLUCIONADO), objectAux.getString(CEstados.SITUACION));
             objectRelation = object.getRelation(CAdicionales.ID_COMENTARIOS);
             comentarios = getComentarios(objectRelation.getQuery().find(), object);
             fotos = (ArrayList<String>) object.get(CAdicionales.FOTOS);
             adicional = new Adicionales(object.getInt(CAdicionales.ID_ADICIONAL), persona, estado, object.getDate(CAdicionales.FECHA), object.getString(CAdicionales.TITULO), object.getString(CAdicionales.DESCRIPCION), fotos, comentarios);
        }
        catch(ParseException e)
        {
            Toast.makeText(context, "no existe", Toast.LENGTH_LONG);
        }

        return adicional;
    }

    @Override
    public void saveAdicional(Adicionales adicional) {

        //VALIDAR EN FE
        adicionalObject.put(CAdicionales.TITULO, adicional.getTitulo());
        adicionalObject.put(CAdicionales.DESCRIPCION, adicional.getDescripcion());
        adicionalObject.put(CAdicionales.FECHA, adicional.getFecha());
        adicionalObject.put(CAdicionales.FOTOS, adicional.getFotos());

        try {
            adicionalObject.put(CAdicionales.ID_ADICIONAL, getUltimoInsertado());
            persona = getPersona(adicional.getPersona().getEmail());
            estado = getEstado(adicional.getEstado().getSituacion());
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
        adicionalObject.put(CAdicionales.FOTOS, adicional.getFotos());
        adicionalObject.put(CAdicionales.ID_ADICIONAL, adicional.getIdAdicional());
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
    public int getUltimoInsertado() throws ParseException {

        query = ParseQuery.getQuery(Clases.ADICIONALES);
        query.orderByDescending(CAdicionales.ID_ADICIONAL);
        int idUltimo = 0;
        int size = query.count();
        if(size != 0) {
            objectAux = query.getFirst();
            idUltimo = objectAux.getInt(CAdicionales.ID_ADICIONAL) + 1;
        }
        else
        {
            idUltimo = 1;
        }
        return idUltimo;
    }

    @Override
    public void deleteAdicional(String objectId) throws ParseException {

        query = ParseQuery.getQuery(Clases.ADICIONALES);
        query.whereEqualTo(CAdicionales.OBJECT_ID, objectId);
        checkInternetGet(query);
        objectAux = query.getFirst();
        objectRelation = objectAux.getRelation(CAdicionales.ID_COMENTARIOS);
        try{
            listParseObject = objectRelation.getQuery().find();

            for(ParseObject parseObject : listParseObject)
            {
                delete(parseObject);
            }

            delete(objectAux);
        }
        catch(ParseException e)
        {
            Toast.makeText(context, "no existe", Toast.LENGTH_LONG);
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
            objectAux = query.getFirst();
       }
        catch(ParseException e)
        {
            Toast.makeText(context, "no existe", Toast.LENGTH_LONG);
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
    public void cargarDBLocal() throws ParseException {

        adicionales = getAdicionales();
        ParseObject.pinAllInBackground(adicionales);
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
