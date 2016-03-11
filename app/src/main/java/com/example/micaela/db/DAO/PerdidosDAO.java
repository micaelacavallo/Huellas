package com.example.micaela.db.DAO;

import android.content.Context;
import android.widget.Toast;

import com.example.micaela.db.Controladores.IGeneralImpl;
import com.example.micaela.db.Constantes.CAdicionales;
import com.example.micaela.db.Constantes.CColores;
import com.example.micaela.db.Constantes.CEdades;
import com.example.micaela.db.Constantes.CEspecies;
import com.example.micaela.db.Constantes.CEstados;
import com.example.micaela.db.Constantes.CPerdidos;
import com.example.micaela.db.Constantes.CPersonas;
import com.example.micaela.db.Constantes.CRazas;
import com.example.micaela.db.Constantes.CSexos;
import com.example.micaela.db.Constantes.CTamaños;
import com.example.micaela.db.Constantes.Clases;
import com.example.micaela.db.Interfaces.IDBLocal;
import com.example.micaela.db.Interfaces.IPerdidos;
import com.example.micaela.db.Modelo.Colores;
import com.example.micaela.db.Modelo.Comentarios;
import com.example.micaela.db.Modelo.Edades;
import com.example.micaela.db.Modelo.Especies;
import com.example.micaela.db.Modelo.Estados;
import com.example.micaela.db.Modelo.Perdidos;
import com.example.micaela.db.Modelo.Personas;
import com.example.micaela.db.Modelo.Razas;
import com.example.micaela.db.Modelo.Sexos;
import com.example.micaela.db.Modelo.Tamaños;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SendCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quimey.arozarena on 12/22/2015.
 */
public class PerdidosDAO extends IGeneralImpl implements IPerdidos, IDBLocal {

    ParseObject perdidosObject;
    Context context;

    ParseQuery<ParseObject> query;
    List<Perdidos> perdidos;
    ParseObject objectAux;
    Razas raza;
    Colores color;
    Sexos sexo;
    Tamaños tamaño;
    Edades edad;
    Especies especie;
    Personas persona;
    Estados estado;
    ParseFile foto;
    List<ParseObject> listParseObject;
    ParseRelation objectRelation;
    List<Comentarios> comentarios;
    Perdidos perdido;
    List<Razas> razas;
    List<Tamaños> tamaños;
    List<Especies> especies;
    List<Colores> colores;
    List<Sexos> sexos;
    List<Edades> edades;


    public PerdidosDAO(Context context) {

        this.context = context;
        init();

    }

    public void init() {
        perdidosObject = ParseObject.create(Clases.PERDIDOS);
        query = null;
        perdidos = new ArrayList<>();
        objectAux = null;
        raza = null;
        color = null;
        sexo = null;
        tamaño = null;
        edad = null;
        especie = null;
        persona = null;
        estado = null;
        foto = null;
        listParseObject = null;
        objectRelation = null;
        comentarios = new ArrayList<>();
        perdido = null;
        razas = new ArrayList<>();
        tamaños = new ArrayList<>();
        especies = new ArrayList<>();
        colores = new ArrayList<>();
        sexos = new ArrayList<>();
        edades = new ArrayList<>();
    }

    @Override
    public List<Perdidos> getPerdidos() throws ParseException {

        perdidos.clear();

        try {

            listParseObject = getPerdidosParseObject();

            if (listParseObject.size() > 0) {
                for (ParseObject object : listParseObject) {

                    objectAux = object.getParseObject(CPerdidos.ID_COLOR);
                    color = new Colores(objectAux.getString(CColores.COLOR), objectAux.getObjectId());

                    objectAux = object.getParseObject(CPerdidos.ID_SEXO);
                    sexo = new Sexos(objectAux.getString(CSexos.SEXO), objectAux.getObjectId());

                    objectAux = object.getParseObject(CPerdidos.ID_TAMAÑO);
                    tamaño = new Tamaños(objectAux.getString(CTamaños.TAMAÑO), objectAux.getObjectId());

                    objectAux = object.getParseObject(CPerdidos.ID_EDAD);
                    edad = new Edades(objectAux.getString(CEdades.EDAD), objectAux.getObjectId());

                    objectAux = object.getParseObject(CPerdidos.ID_ESPECIE);
                    especie = new Especies(objectAux.getString(CEspecies.ESPECIE), objectAux.getObjectId());

                    objectAux = object.getParseObject(CPerdidos.ID_RAZA);
                    raza = new Razas(objectAux.getString(CRazas.RAZA), objectAux.getObjectId(), especie);

                    objectAux = object.getParseObject(CPerdidos.ID_PERSONA);
                    persona = new Personas(objectAux.getObjectId(), objectAux.getString(CPersonas.EMAIL), objectAux.getString(CPersonas.NOMBRE), objectAux.getString(CPersonas.APELLIDO), objectAux.getString(CPersonas.TELEFONO), objectAux.getBoolean(CPersonas.ADMINISTRADOR), objectAux.getBoolean(CPersonas.BLOQUEADO));

                    objectAux = object.getParseObject(CPerdidos.ID_ESTADO);
                    estado = new Estados(objectAux.getObjectId(),objectAux.getString(CEstados.SITUACION));

                    try {
                        objectRelation = object.getRelation(CPerdidos.COMENTARIOS);
                        comentarios = getComentarios(objectRelation.getQuery().find(), object);

                    } catch (Exception e) {
                        comentarios = null;
                    }


                    foto = object.getParseFile(CPerdidos.FOTOS);
                    byte[] image = null;
                    if (foto != null) {
                        image = foto.getData();
                    }
                    perdido = new Perdidos(edad, raza, especie, tamaño, color, sexo, estado, persona, object.getDate(CPerdidos.FECHA), object.getParseGeoPoint(CPerdidos.UBICACION).getLatitude(), object.getParseGeoPoint(CPerdidos.UBICACION).getLongitude(), object.getString(CPerdidos.TITULO), object.getString(CPerdidos.DESCRIPCION), image, comentarios, object.getBoolean(CPerdidos.SOLUCIONADO), object.getBoolean(CPerdidos.BLOQUEADO));
                    perdidos.add(perdido);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // ParsePush push = new ParsePush(); push.setChannel("commentsChannel"); push.setMessage("HOLAA, publicacion guardada"); push.sendInBackground();

        JSONObject object = new JSONObject();
        try {
            object.put("title", "Hola");
            object.put("description", "click publicacion");
            ParsePush pushMessageClient1 = new ParsePush();
            pushMessageClient1.setData(object);
            pushMessageClient1.setChannel("commentsChannel");
            pushMessageClient1.sendInBackground(new SendCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        //Something wrong
                    }
                }
            });
        } catch (JSONException e)
        {e.printStackTrace();}

        return perdidos;
    }

    public List<ParseObject> getPerdidosParseObject() throws ParseException {

        perdidos.clear();

        query = ParseQuery.getQuery(Clases.PERDIDOS);
        query.include(CPerdidos.ID_RAZA);
        query.include(CPerdidos.ID_COLOR);
        query.include(CPerdidos.ID_SEXO);
        query.include(CPerdidos.ID_TAMAÑO);
        query.include(CPerdidos.ID_EDAD);
        query.include(CPerdidos.ID_ESPECIE);
        query.include(CPerdidos.ID_ESTADO);
        query.include(CPerdidos.ID_PERSONA);
        query.whereEqualTo(CPerdidos.SOLUCIONADO, false);
        query.orderByDescending(CPerdidos.FECHA);
        checkInternetGet(query);

        try {

            listParseObject = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listParseObject;
    }


    @Override
    public void savePerdido(Perdidos perdido) {

        //VALIDAR EN FE
        perdidosObject.put(CPerdidos.TITULO, perdido.getTitulo());
        perdidosObject.put(CPerdidos.DESCRIPCION, perdido.getDescripcion());
        perdidosObject.put(CPerdidos.FECHA, perdido.getFecha());
        perdidosObject.put(CPerdidos.FOTOS, new ParseFile("picture.jpg", perdido.getFoto()));
        perdidosObject.put(CPerdidos.UBICACION, new ParseGeoPoint(perdido.getLatitud(), perdido.getLongitud()));
        perdidosObject.put(CPerdidos.SOLUCIONADO, false);


        try {
            raza = getRaza(perdido.getRaza().getRaza());
            color = getColor(perdido.getColor().getColor());
            sexo = getSexo(perdido.getSexo().getSexo());
            tamaño = getTamaño(perdido.getTamaño().getTamaño());
            edad = getEdad(perdido.getEdad().getEdad());
            especie = getEspecie(perdido.getEspecie().getEspecie());
            estado = getEstado(perdido.getEstado().getSituacion());
            persona = getPersona(perdido.getPersona().getEmail());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        perdidosObject.put(CPerdidos.ID_RAZA, ParseObject.createWithoutData(Clases.RAZAS, String.valueOf(raza.getObjectId())));
        perdidosObject.put(CPerdidos.ID_COLOR, ParseObject.createWithoutData(Clases.COLORES, String.valueOf(color.getObjectId())));
        perdidosObject.put(CPerdidos.ID_SEXO, ParseObject.createWithoutData(Clases.SEXOS, String.valueOf(sexo.getObjectId())));
        perdidosObject.put(CPerdidos.ID_TAMAÑO, ParseObject.createWithoutData(Clases.TAMAÑOS, String.valueOf(tamaño.getObjectId())));
        perdidosObject.put(CPerdidos.ID_EDAD, ParseObject.createWithoutData(Clases.EDADES, String.valueOf(edad.getObjectId())));
        perdidosObject.put(CPerdidos.ID_ESPECIE, ParseObject.createWithoutData(Clases.ESPECIES, String.valueOf(especie.getObjectId())));
        perdidosObject.put(CPerdidos.ID_ESTADO, ParseObject.createWithoutData(Clases.ESTADOS, String.valueOf(estado.getObjectId())));
        perdidosObject.put(CPerdidos.ID_PERSONA, ParseObject.createWithoutData(Clases.PERSONAS, String.valueOf(persona.getObjectId())));

        save(perdidosObject);

    }

    public ParseObject cargarPerdido(Perdidos perdido) {
        perdidosObject.put(CPerdidos.TITULO, perdido.getTitulo());
        perdidosObject.put(CPerdidos.DESCRIPCION, perdido.getDescripcion());
        perdidosObject.put(CPerdidos.FECHA, perdido.getFecha());
        perdidosObject.put(CPerdidos.FOTOS, new ParseFile("picture.jpg", perdido.getFoto()));
        perdidosObject.put(CPerdidos.UBICACION, new ParseGeoPoint(perdido.getLatitud(), perdido.getLongitud()));
        perdidosObject.put(CPerdidos.ID_RAZA, ParseObject.createWithoutData(Clases.RAZAS, perdido.getRaza().getObjectId()));
        perdidosObject.put(CPerdidos.ID_COLOR, ParseObject.createWithoutData(Clases.COLORES, perdido.getColor().getObjectId()));
        perdidosObject.put(CPerdidos.ID_SEXO, ParseObject.createWithoutData(Clases.SEXOS, perdido.getSexo().getObjectId()));
        perdidosObject.put(CPerdidos.ID_TAMAÑO, ParseObject.createWithoutData(Clases.TAMAÑOS, perdido.getTamaño().getObjectId()));
        perdidosObject.put(CPerdidos.ID_EDAD, ParseObject.createWithoutData(Clases.EDADES, perdido.getEdad().getObjectId()));
        perdidosObject.put(CPerdidos.ID_ESPECIE, ParseObject.createWithoutData(Clases.ESPECIES, perdido.getEspecie().getObjectId()));
        perdidosObject.put(CPerdidos.ID_ESTADO, ParseObject.createWithoutData(Clases.ESTADOS, perdido.getEstado().getObjectId()));
        perdidosObject.put(CPerdidos.ID_PERSONA, ParseObject.createWithoutData(Clases.PERSONAS, perdido.getPersona().getObjectId()));

        return perdidosObject;
    }

    @Override
    public void editPerdido(Perdidos perdido) throws ParseException {
        objectAux = cargarPerdido(perdido);
        deletePerdido(perdido.getObjectId());
        savePerdido(perdido);
    }


    @Override
    public Razas getRaza(String raza) throws ParseException {

        query = ParseQuery.getQuery(Clases.RAZAS);
        query.include(CRazas.ID_ESPECIE);
        query.whereEqualTo(CRazas.RAZA, raza);
        checkInternetGet(query);
        Razas razaObject = null;

        try {
            if(query.count() != 0) {
                objectAux = query.getFirst();
                ParseObject object = objectAux.getParseObject(CPerdidos.ID_ESPECIE);
                especie = new Especies(object.getString(CEspecies.ESPECIE), object.getObjectId());

                razaObject = new Razas(objectAux.getString(CRazas.RAZA), objectAux.getObjectId(), especie);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return razaObject;
    }

    @Override
    public Colores getColor(String color) throws ParseException {

        query = ParseQuery.getQuery(Clases.COLORES);
        query.whereEqualTo(CColores.COLOR, color);
        checkInternetGet(query);
        Colores colorObject = null;

        try {
            if(query.count() != 0) {
                objectAux = query.getFirst();
                colorObject = new Colores(objectAux.getString(CColores.COLOR), objectAux.getObjectId());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return colorObject;
    }

    @Override
    public Sexos getSexo(String sexo) throws ParseException {

        query = ParseQuery.getQuery(Clases.SEXOS);
        query.whereEqualTo(CSexos.SEXO, sexo);
        checkInternetGet(query);
        Sexos sexoObject = null;

        try {
            if(query.count() != 0) {
                objectAux = query.getFirst();
                sexoObject = new Sexos(objectAux.getString(CSexos.SEXO), objectAux.getObjectId());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sexoObject;
    }

    @Override
    public Tamaños getTamaño(String tamaño) throws ParseException {

        query = ParseQuery.getQuery(Clases.TAMAÑOS);
        query.whereEqualTo(CTamaños.TAMAÑO, tamaño);
        checkInternetGet(query);
        Tamaños tamañoObject = null;
        try {
            if(query.count() != 0) {
                objectAux = query.getFirst();
                tamañoObject = new Tamaños(objectAux.getString(CTamaños.TAMAÑO), objectAux.getObjectId());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tamañoObject;
    }

    @Override
    public Edades getEdad(String edad) throws ParseException {

        query = ParseQuery.getQuery(Clases.EDADES);
        query.whereEqualTo(CEdades.EDAD, edad);
        checkInternetGet(query);
        Edades edadObject = null;
        try {
            if(query.count() != 0) {
                objectAux = query.getFirst();
                edadObject = new Edades(objectAux.getString(CEdades.EDAD), objectAux.getObjectId());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return edadObject;
    }

    @Override
    public Especies getEspecie(String especie) throws ParseException {

        query = ParseQuery.getQuery(Clases.ESPECIES);
        query.whereEqualTo(CEspecies.ESPECIE, especie);
        checkInternetGet(query);
        Especies especieObject = null;

        try {
            if(query.count() != 0) {
                objectAux = query.getFirst();
                especieObject = new Especies(objectAux.getString(CEspecies.ESPECIE), objectAux.getObjectId());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return especieObject;
    }

    @Override
    public List<Razas> getRazas() {

        listParseObject = getRazasParseObject();

        for (ParseObject object : listParseObject) {
            objectAux = object.getParseObject(CPerdidos.ID_ESPECIE);
            especie = new Especies(object.getString(CEspecies.ESPECIE), object.getObjectId());

            raza = new Razas(object.getString(CRazas.RAZA), object.getObjectId(), especie);
            razas.add(raza);
        }

        return razas;
    }

    public List<ParseObject> getRazasParseObject() {

        query = ParseQuery.getQuery(Clases.RAZAS);
        query.include(CRazas.ID_ESPECIE);
        checkInternetGet(query);
        try {
            listParseObject = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listParseObject;
    }


    @Override
    public List<Colores> getColores() {

        listParseObject = getColoresParseObject();

        for (ParseObject object : listParseObject) {
            color = new Colores(object.getString(CColores.COLOR), object.getObjectId());
            colores.add(color);
        }

        return colores;
    }

    public List<ParseObject> getColoresParseObject() {

        query = ParseQuery.getQuery(Clases.COLORES);
        checkInternetGet(query);
        try {
            listParseObject = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listParseObject;
    }


    @Override
    public List<Sexos> getSexos() {


        listParseObject = getSexosParseObject();

        for (ParseObject object : listParseObject) {
            sexo = new Sexos(object.getString(CSexos.SEXO), object.getObjectId());
            sexos.add(sexo);
        }

        return sexos;
    }

    public List<ParseObject> getSexosParseObject() {

        query = ParseQuery.getQuery(Clases.SEXOS);
        checkInternetGet(query);
        try {
            listParseObject = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listParseObject;
    }


    @Override
    public List<Tamaños> getTamaños() {

        listParseObject = getTamañosParseObject();


        for (ParseObject object : listParseObject) {
            tamaño = new Tamaños(object.getString(CTamaños.TAMAÑO), object.getObjectId());
            tamaños.add(tamaño);
        }

        return tamaños;
    }

    public List<ParseObject> getTamañosParseObject() {

        query = ParseQuery.getQuery(Clases.TAMAÑOS);
        checkInternetGet(query);
        try {
            listParseObject = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listParseObject;
    }


    @Override
    public List<Edades> getEdades() {

        listParseObject = getEdadesParseObject();


        for (ParseObject object : listParseObject) {
            edad = new Edades(object.getString(CEdades.EDAD), object.getObjectId());
            edades.add(edad);
        }

        return edades;
    }

    public List<ParseObject> getEdadesParseObject() {

        query = ParseQuery.getQuery(Clases.EDADES);
        checkInternetGet(query);

        try {
            listParseObject = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listParseObject;
    }


    @Override
    public List<Especies> getEspecies() {


        listParseObject = getEspeciesParseObject();

        for (ParseObject object : listParseObject) {
            especie = new Especies(object.getString(CEspecies.ESPECIE), object.getObjectId());
            especies.add(especie);
        }

        return especies;
    }

    public List<ParseObject> getEspeciesParseObject() {

        query = ParseQuery.getQuery(Clases.ESPECIES);
        checkInternetGet(query);
        try {
            listParseObject = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listParseObject;
    }


    @Override
    public void deletePerdido(String objectId) throws ParseException {

        query = ParseQuery.getQuery(Clases.PERDIDOS);
        query.whereEqualTo(CPerdidos.OBJECT_ID, objectId);
        checkInternetGet(query);
        try {
            if(query.count() != 0) {
                objectAux = query.getFirst();
                objectRelation = objectAux.getRelation(CPerdidos.COMENTARIOS);
                listParseObject = objectRelation.getQuery().find();

                for (ParseObject parseObject : listParseObject) {

                    delete(parseObject);
                }

                delete(objectAux);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void AgregarComentarioPerdido(String perdidoObjectId, String comentario, String email) throws ParseException {

        ParseObject parseObjectComentario = agregarComentario(comentario, email, context);
        objectAux = getParseObjectById(perdidoObjectId);
        objectRelation = objectAux.getRelation(CPerdidos.COMENTARIOS);
        objectRelation.add(parseObjectComentario);
        save(objectAux);
    }

    public ParseObject getParseObjectById(String objectId) {

        query = ParseQuery.getQuery(Clases.PERDIDOS);
        query.whereEqualTo(CAdicionales.OBJECT_ID, objectId);
        checkInternetGet(query);

        try {
            if(query.count() != 0) {
                objectAux = query.getFirst();
            }
        } catch (ParseException e) {
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

        if (internet(context)) {
            ParseObject.pinAllInBackground(getPerdidosParseObject());
            ParseObject.pinAllInBackground(getRazasParseObject());
            ParseObject.pinAllInBackground(getColoresParseObject());
            ParseObject.pinAllInBackground(getSexosParseObject());
            ParseObject.pinAllInBackground(getTamañosParseObject());
            ParseObject.pinAllInBackground(getEdadesParseObject());
            ParseObject.pinAllInBackground(getEspeciesParseObject());
        }
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

        if (internet(context)) {
            deleteInBackground(objectAux);
        } else {
            deleteEventually(objectAux);
        }

        unpinObjectInBackground(objectAux);
    }


    @Override
    public void checkInternetGet(ParseQuery<ParseObject> query) {
        if (!internet(context)) {
            query.fromLocalDatastore();
        }
    }
}
