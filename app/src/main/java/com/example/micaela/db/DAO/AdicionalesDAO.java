package com.example.micaela.db.DAO;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.micaela.db.Controladores.IGeneralImpl;
import com.example.micaela.db.Enums.CAdicionales;
import com.example.micaela.db.Enums.CColores;
import com.example.micaela.db.Enums.CEstados;
import com.example.micaela.db.Enums.CPersonas;
import com.example.micaela.db.Enums.Clases;
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
    List<Adicionales> adicionales = new ArrayList<Adicionales>();
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
        objectAux.put(CColores.ID_COLOR, color.getIdColor());
        objectAux.put(CColores.COLOR, color.getColor());
        save(objectAux);
    }

    private ParseObject getEstadoByID(String estadoID) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.ESTADOS);
        query.whereEqualTo(CEstados.OBJECT_ID, estadoID);
        return query.getFirst();
    }

    @Override
    public List<Adicionales> getInfoUtil() throws ParseException {

        adicionales.clear();

        query = ParseQuery.getQuery(Clases.ADICIONALES);
        query.include(CAdicionales.ID_PERSONA);
        query.include(CAdicionales.ID_ESTADO);
        query.whereEqualTo(CAdicionales.DONACION, false);
        //  query.whereEqualTo(CAdicionales.ID_ESTADO, getEstadoByID("D4ATvjnhj4"));
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


                    try {
                        objectRelation = object.getRelation(CAdicionales.ID_COMENTARIOS);
                        comentarios = getComentarios(objectRelation.getQuery().find(), object);
                    }catch (Exception e)
                    {
                        comentarios = null;
                    }

                    foto = object.getParseFile(CAdicionales.FOTOS);
                    String imageURL = foto.getUrl();
                    Uri imageUri = Uri.parse(imageURL);

                    adicional = new Adicionales(object.getInt(CAdicionales.ID_ADICIONAL), persona, estado, object.getDate(CAdicionales.FECHA), object.getString(CAdicionales.TITULO), object.getString(CAdicionales.DESCRIPCION), imageUri, object.getBoolean(CAdicionales.DONACION), comentarios);
                    adicionales.add(adicional);
                }
            }
        }
        catch(ParseException e)
        {
            Log.e(e.getMessage(), "no existe");
        }

        return adicionales;
    }

    @Override
    public List<Adicionales> getDonaciones() throws ParseException {

        query = ParseQuery.getQuery(Clases.ADICIONALES);
        query.include(CAdicionales.ID_PERSONA);
        query.include(CAdicionales.ID_ESTADO);
        query.whereEqualTo(CAdicionales.DONACION, true);
        //  query.whereEqualTo(CAdicionales.ID_ESTADO, getEstadoByID("D4ATvjnhj4"));
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


                    try {
                        objectRelation = object.getRelation(CAdicionales.ID_COMENTARIOS);
                        comentarios = getComentarios(objectRelation.getQuery().find(), object);
                    }catch (Exception e)
                    {
                        comentarios = null;
                    }

                    foto = object.getParseFile(CAdicionales.FOTOS);
                    String imageURL = foto.getUrl();
                    Uri imageUri = Uri.parse(imageURL);

                    adicional = new Adicionales(object.getInt(CAdicionales.ID_ADICIONAL), persona, estado, object.getDate(CAdicionales.FECHA), object.getString(CAdicionales.TITULO), object.getString(CAdicionales.DESCRIPCION), imageUri, object.getBoolean(CAdicionales.DONACION), comentarios);
                    adicionales.add(adicional);
                }
            }
        }
        catch(ParseException e)
        {
            Log.e(e.getMessage(), "no existe");
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
            foto = object.getParseFile(CAdicionales.FOTOS);
            String imageURL = foto.getUrl();
            Uri imageUri = Uri.parse(imageURL);
            adicional = new Adicionales(object.getInt(CAdicionales.ID_ADICIONAL), persona, estado, object.getDate(CAdicionales.FECHA), object.getString(CAdicionales.TITULO), object.getString(CAdicionales.DESCRIPCION), imageUri, object.getBoolean(CAdicionales.DONACION), comentarios);
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
        adicionalObject.put(CAdicionales.FOTOS, adicional.getFoto());

        try {
            adicionalObject.put(CAdicionales.ID_ADICIONAL, getUltimoInsertado());
            persona = getPersona(adicional.getPersona().getEmail());
            estado = getEstadoNoSolucionado();
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
    public void cargarDBLocal(Context context) throws ParseException {

        if(internet(context)) {
            adicionales = getDonaciones();
            adicional.addAll("info_util", getInfoUtil());
            ParseObject.pinAllInBackground(adicionales);
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
