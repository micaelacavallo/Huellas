package com.example.micaela.db.DAO;

import android.content.Context;

import com.example.micaela.HuellasApplication;
import com.example.micaela.db.Constantes.CAdicionales;
import com.example.micaela.db.Constantes.CColores;
import com.example.micaela.db.Constantes.CComentarios;
import com.example.micaela.db.Constantes.CEstados;
import com.example.micaela.db.Constantes.CPersonas;
import com.example.micaela.db.Constantes.Clases;
import com.example.micaela.db.Controladores.IComentariosImpl;
import com.example.micaela.db.Controladores.IEstadosImpl;
import com.example.micaela.db.Controladores.IGeneralImpl;
import com.example.micaela.db.Controladores.IPersonasImpl;
import com.example.micaela.db.Interfaces.IAdicionales;
import com.example.micaela.db.Interfaces.IComentarios;
import com.example.micaela.db.Interfaces.IDBLocal;
import com.example.micaela.db.Interfaces.IEstados;
import com.example.micaela.db.Interfaces.IGeneral;
import com.example.micaela.db.Interfaces.IPersonas;
import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.db.Modelo.Colores;
import com.example.micaela.db.Modelo.Comentarios;
import com.example.micaela.db.Modelo.Estados;
import com.example.micaela.db.Modelo.Personas;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SendCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by quimey.arozarena on 12/22/2015.
 */
public class AdicionalesDAO extends IGeneralImpl implements IAdicionales, IDBLocal {


    private ParseObject adicionalObject;
    private Context context;
    private ParseQuery<ParseObject> query;
    private ParseObject objectAux;
    private Personas persona;
    private Estados estado;
    private ParseFile foto;
    private List<ParseObject> listParseObject;
    private ParseRelation objectRelation;
    private ArrayList<Comentarios> comentarios;
    private List<Adicionales> adicionales = new ArrayList<>();
    private Adicionales adicional = null;
    private IGeneral iGeneral;
    private IPersonas iPersona;
    private IComentarios iComentarios;
    private IEstados iEstado;

    public AdicionalesDAO(Context context)
    {
        super(context);
        this.context = context;
        init(context);
    }

    public void init(Context context)
    {
        adicionalObject = ParseObject.create(Clases.ADICIONALES);
        query = null;
        objectAux = null;
        persona = new Personas();
        estado = null;
        foto = null;
        listParseObject = null;
        objectRelation = null;
        comentarios = new ArrayList<>();
        adicionales = new ArrayList<>();
        adicional = null;
        iGeneral = new IGeneralImpl(context);
        iPersona = new IPersonasImpl(context);
        iComentarios = new IComentariosImpl(context);
        iEstado = new IEstadosImpl(context);
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

        return getListAdicional(listParseObject);
    }

    public List<ParseObject> findQuery(){
        try{
            listParseObject = query.find();
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }

        return listParseObject;

    }

    public List<Adicionales> getListAdicional(List<ParseObject> listParseObject) throws ParseException {
        if(listParseObject.size() > 0){
            for(ParseObject object : listParseObject)
            {
                objectAux = object.getParseObject(CAdicionales.ID_PERSONA);
                persona = new Personas(objectAux.getObjectId(), objectAux.getString(CPersonas.EMAIL), objectAux.getString(CPersonas.NOMBRE), objectAux.getString(CPersonas.TELEFONO), objectAux.getBoolean(CPersonas.ADMINISTRADOR), objectAux.getBoolean(CPersonas.BLOQUEADO),  objectAux.getString(CPersonas.CONTRASEÑA), objectAux.getString(CPersonas.FOTO));

                objectAux = object.getParseObject(CAdicionales.ID_ESTADO);
                estado = new Estados(objectAux.getObjectId(), objectAux.getString(CEstados.SITUACION));

                try {
                    objectRelation = object.getRelation(CAdicionales.ID_COMENTARIOS);
                    comentarios = iComentarios.getComentarios(objectRelation.getQuery().find(), object);
                }catch (Exception e)
                {
                    comentarios = null;
                }

                foto = object.getParseFile(CAdicionales.FOTOS);


                byte[] image = null;
                if (foto != null) {
                    image = foto. getData();
                }
                adicional = new Adicionales(object.getObjectId(), persona, estado, object.getDate(CAdicionales.FECHA), object.getString(CAdicionales.TITULO), object.getString(CAdicionales.DESCRIPCION), image, object.getBoolean(CAdicionales.DONACION), comentarios, object.getBoolean(CAdicionales.BLOQUEADO));
                adicionales.add(adicional);
            }
        }
        iGeneral.startAlert();

        return adicionales;
    }

    public ParseQuery<ParseObject> getQueryForInfoUtil(){
        query = ParseQuery.getQuery(Clases.ADICIONALES);
        query.include(CAdicionales.ID_PERSONA);
        query.include(CAdicionales.ID_ESTADO);
        query.whereEqualTo(CAdicionales.DONACION, false);
        query.whereEqualTo(CAdicionales.BLOQUEADO, false);

        return query;
    }

    public List<ParseObject> getInfoUtilParseObject() throws ParseException {

        query = getQueryForInfoUtil();
        query.orderByDescending(CAdicionales.FECHA);
        checkInternetGet(query);

        listParseObject = findQuery();

        return listParseObject;
    }


    @Override
    public List<Adicionales> getDonaciones() throws ParseException {

        adicionales.clear();

        listParseObject = getDonacionesParseObject();

      return getListAdicional(listParseObject);
    }

    public ParseQuery<ParseObject> getQueryForDonaciones(){
        query = ParseQuery.getQuery(Clases.ADICIONALES);
        query.include(CAdicionales.ID_PERSONA);
        query.include(CAdicionales.ID_ESTADO);
        query.whereEqualTo(CAdicionales.DONACION, true);
        query.whereEqualTo(CAdicionales.BLOQUEADO, false);

        return query;
    }

    public List<ParseObject> getDonacionesParseObject() throws ParseException {

        query = getQueryForDonaciones();

        listParseObject = findQuery();

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
                persona = new Personas(objectAux.getObjectId(), objectAux.getString(CPersonas.EMAIL), objectAux.getString(CPersonas.NOMBRE), objectAux.getString(CPersonas.TELEFONO), objectAux.getBoolean(CPersonas.ADMINISTRADOR), objectAux.getBoolean(CPersonas.BLOQUEADO),  objectAux.getString(CPersonas.CONTRASEÑA), objectAux.getString(CPersonas.FOTO));
                objectAux = object.getParseObject(CAdicionales.ID_ESTADO);
                estado = new Estados(objectAux.getObjectId(), objectAux.getString(CEstados.SITUACION));
                objectRelation = object.getRelation(CAdicionales.ID_COMENTARIOS);
                comentarios = iComentarios.getComentarios(objectRelation.getQuery().find(), object);
                foto = object.getParseFile(CAdicionales.FOTOS);
                byte[] image = null;
                if (foto != null) {
                    image = foto.getData();
                }
                adicional = new Adicionales(object.getObjectId(), persona, estado, object.getDate(CAdicionales.FECHA), object.getString(CAdicionales.TITULO), object.getString(CAdicionales.DESCRIPCION), image, object.getBoolean(CAdicionales.DONACION), comentarios, object.getBoolean(CAdicionales.BLOQUEADO));
            }
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }

        return adicional;
    }

    @Override
    public Adicionales saveAdicional(Adicionales adicional) throws ParseException {

        //VALIDAR EN FE
        adicionalObject.put(CAdicionales.TITULO, adicional.getTitulo());
        adicionalObject.put(CAdicionales.DESCRIPCION, adicional.getDescripcion());
        adicionalObject.put(CAdicionales.FECHA, adicional.getFecha());
        adicionalObject.put(CAdicionales.FOTOS, new ParseFile("picture.jpg", adicional.getFoto()));


        adicionalObject.put(CAdicionales.DONACION, adicional.isDonacion());

        try {
            persona = iPersona.getPersonabyEmail(adicional.getPersona().getEmail());
            estado = iEstado.getEstado("-");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        adicionalObject.put(CAdicionales.ID_ESTADO, ParseObject.createWithoutData(Clases.ESTADOS, String.valueOf(estado.getObjectId())));
        adicionalObject.put(CAdicionales.ID_PERSONA, ParseObject.createWithoutData(Clases.PERSONAS, String.valueOf(persona.getObjectId())));
        save(adicionalObject);

        return getAdicionalById(getUltimoObjectId(Clases.ADICIONALES));
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

            ParseObject parseObjectComentario = agregarComentario(comentarioText, email);
            objectAux = getParseObjectById(adicionalObjectId);
            objectRelation = objectAux.getRelation(CAdicionales.ID_COMENTARIOS);
            objectRelation.add(parseObjectComentario);
            save(objectAux);
            pushNotification(adicionalObjectId, email);
    }

    public ParseObject agregarComentario(String comentario, String email) throws ParseException {

        ParseObject object = new ParseObject(Clases.COMENTARIOS);
        object.put(CComentarios.COMENTARIO, comentario);
        object.put(CComentarios.LEIDO, false);
        object.put(CComentarios.FECHA, new Date());
        persona = iPersona.getPersonabyEmail(email);
        object.put(CComentarios.ID_PERSONA, ParseObject.createWithoutData(Clases.PERSONAS, String.valueOf(persona.getObjectId())));
        save(object);
        ParseObject objectComentario = iComentarios.getComentarioById(getUltimoObjectId(Clases.COMENTARIOS));

        return objectComentario;
    }

    public void pushNotification(String adicionalObjectId, String mailLogueado){

        if(!HuellasApplication.getInstance().getProfileEmailFacebook().equals("")) {
            List<String> emails = new ArrayList<String>();
            adicional = getAdicionalById(adicionalObjectId);

            if (!adicional.getPersona().getEmail().equals(mailLogueado))
                emails.add(adicional.getPersona().getEmail());
            for (Comentarios comentarioAux : adicional.getComentarios()) { //email de las personas que comentaron
                if (!comentarioAux.getPersona().getEmail().equals(mailLogueado)) {
                    emails.add(comentarioAux.getPersona().getEmail());
                }
            }

            JSONObject object2 = new JSONObject();
            try {

                // Create our Installation query
                ParseQuery pushQuery = ParseInstallation.getQuery();

                pushQuery.whereContainedIn("email", emails);
                object2.put("title", "Nuevo comentario");
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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

    public List<Adicionales> getDonacionesById(String userId) throws ParseException {

        ParseObject obj = ParseObject.createWithoutData(Clases.PERSONAS,userId);

        query = getQueryForDonaciones();
        query.whereEqualTo(CAdicionales.ID_PERSONA, obj);
        query.orderByDescending(CAdicionales.FECHA);
        checkInternetGet(query);

        listParseObject = findQuery();

        return getListAdicional(listParseObject);
    }


    private List<Adicionales> getInfoUtilById(String userId) throws ParseException {

        ParseObject obj = ParseObject.createWithoutData(Clases.PERSONAS,userId);

        query = getQueryForInfoUtil();
        query.whereEqualTo(CAdicionales.ID_PERSONA, obj);
        query.orderByDescending(CAdicionales.FECHA);
        checkInternetGet(query);

        listParseObject = findQuery();

        return getListAdicional(listParseObject);
    }



    @Override
    public List<Adicionales> getPublicacionesAdicionalesPropias(String objectId) {

        List<Adicionales> listAdicionales = null;
        try {
            List<Adicionales> donaciones = getDonacionesById(objectId);

            List<Adicionales> infoUtil = getInfoUtilById(objectId);

            listAdicionales = new ArrayList<Adicionales>(donaciones);
            listAdicionales.addAll(infoUtil);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listAdicionales;

    }
}
