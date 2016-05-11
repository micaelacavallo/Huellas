package com.example.micaela.db.DAO;

import android.content.Context;

import com.example.micaela.HuellasApplication;
import com.example.micaela.db.Constantes.CAdicionales;
import com.example.micaela.db.Constantes.CColores;
import com.example.micaela.db.Constantes.CComentarios;
import com.example.micaela.db.Constantes.CEdades;
import com.example.micaela.db.Constantes.CEspecies;
import com.example.micaela.db.Constantes.CEstados;
import com.example.micaela.db.Constantes.CPerdidos;
import com.example.micaela.db.Constantes.CPersonas;
import com.example.micaela.db.Constantes.CRazas;
import com.example.micaela.db.Constantes.CSexos;
import com.example.micaela.db.Constantes.CTamaños;
import com.example.micaela.db.Constantes.Clases;
import com.example.micaela.db.Controladores.IComentariosImpl;
import com.example.micaela.db.Controladores.IEstadosImpl;
import com.example.micaela.db.Controladores.IGeneralImpl;
import com.example.micaela.db.Controladores.IPersonasImpl;
import com.example.micaela.db.Interfaces.IComentarios;
import com.example.micaela.db.Interfaces.IDB;
import com.example.micaela.db.Interfaces.IEstados;
import com.example.micaela.db.Interfaces.IGeneral;
import com.example.micaela.db.Interfaces.IPerdidos;
import com.example.micaela.db.Interfaces.IPersonas;
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
import com.example.micaela.utils.Constants;
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

public class PerdidosDAO extends IGeneralImpl implements IPerdidos, IDB {

    private ParseObject perdidosObject;
    private Context context;
    private ParseQuery<ParseObject> query;
    private List<Perdidos> perdidos;
    private ParseObject objectAux;
    private Razas raza;
    private Colores color;
    private Sexos sexo;
    private Tamaños tamaño;
    private Edades edad;
    private Especies especie;
    private Personas persona;
    private Estados estado;
    private ParseFile foto;
    private List<ParseObject> listParseObject;
    private ParseRelation objectRelation;
    private ArrayList<Comentarios> comentarios;
    private Perdidos perdido;
    private List<Razas> razas;
    private List<Tamaños> tamaños;
    private List<Especies> especies;
    private List<Colores> colores;
    private List<Sexos> sexos;
    private List<Edades> edades;
    private IGeneral iGeneral;
    private IPersonas iPersona;
    private IComentarios iComentarios;
    private IEstados iEstado;
    private IPersonas mPersonasImpl;

    public PerdidosDAO(Context context) {
        super(context);
        this.context = context;
        init(context);

    }

    public void init(Context context) {
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
        iGeneral = new IGeneralImpl(context);
        iPersona = new IPersonasImpl(context);
        iComentarios = new IComentariosImpl(context);
        iEstado = new IEstadosImpl(context);
        mPersonasImpl = new IPersonasImpl(context);
    }

    public List<ParseObject> findQuery() {
        try {
            listParseObject = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listParseObject;

    }

    public List<Perdidos> getListPerdidos(List<ParseObject> listParseObject) throws ParseException {

        if (listParseObject.size() > 0) {
            for (ParseObject object : listParseObject) {

                perdido = getPerdido(object);
                perdidos.add(perdido);
            }
        }

        return perdidos;
    }

    public Perdidos getPerdido(ParseObject object) {

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
        persona = new Personas(objectAux.getObjectId(), objectAux.getString(CPersonas.EMAIL), objectAux.getString(CPersonas.NOMBRE), objectAux.getString(CPersonas.TELEFONO), objectAux.getBoolean(CPersonas.ADMINISTRADOR), objectAux.getBoolean(CPersonas.BLOQUEADO), objectAux.getString(CPersonas.CONTRASEÑA), objectAux.getString(CPersonas.FOTO));

        objectAux = object.getParseObject(CPerdidos.ID_ESTADO);
        estado = new Estados(objectAux.getObjectId(), objectAux.getString(CEstados.SITUACION));

        if (internet(context)) {
            try {
                objectRelation = object.getRelation(CPerdidos.COMENTARIOS);

                comentarios = iComentarios.getComentarios(objectRelation.getQuery().addAscendingOrder(CComentarios.FECHA).find(), object);

            } catch (Exception e) {
                comentarios = null;
            }
        } else {
            comentarios = null;
        }

        foto = object.getParseFile(CPerdidos.FOTOS);
        byte[] image = null;
        if (foto != null) {
            try {
                image = foto.getData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        perdido = new Perdidos(object.getObjectId(), edad, raza, especie, tamaño, color, sexo, estado, persona, object.getDate(CPerdidos.FECHA), object.getString(CPerdidos.UBICACION), object.getString(CPerdidos.TITULO), object.getString(CPerdidos.DESCRIPCION), image, comentarios, object.getBoolean(CPerdidos.SOLUCIONADO), object.getBoolean(CPerdidos.BLOQUEADO));
        return perdido;
    }

    @Override
    public List<Perdidos> getPerdidos() throws ParseException {

        perdidos.clear();

        try {
            listParseObject = getPerdidosParseObject();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        iGeneral.startAlert();

        return getListPerdidos(listParseObject);
    }

    public ParseQuery<ParseObject> getQueryPerdidos() {

        query = ParseQuery.getQuery(Clases.PERDIDOS);
        query.include(CPerdidos.ID_RAZA);
        query.include(CPerdidos.ID_COLOR);
        query.include(CPerdidos.ID_SEXO);
        query.include(CPerdidos.ID_TAMAÑO);
        query.include(CPerdidos.ID_EDAD);
        query.include(CPerdidos.ID_ESPECIE);
        query.include(CPerdidos.ID_ESTADO);
        query.include(CPerdidos.ID_PERSONA);

        return query;
    }

    public List<ParseObject> getPerdidosParseObject() throws ParseException {

        perdidos.clear();

        query = getQueryPerdidos();
        query.whereEqualTo(CPerdidos.SOLUCIONADO, false);
        query.whereEqualTo(CPerdidos.BLOQUEADO, false);
        query.orderByDescending(CPerdidos.FECHA);
        checkInternetGet(query);

        listParseObject = findQuery();

        return listParseObject;
    }


    @Override
    public void savePerdido(Perdidos perdido) throws ParseException {
        savePerdidoParseObject(perdido);
    }

    public String getInsertedID(Date date) throws ParseException {
        query = ParseQuery.getQuery(Clases.PERDIDOS);
        query.whereEqualTo(CPerdidos.FECHA, date);
        checkInternetGet(query);
        if (query.count() != 0) {
            return query.getFirst().getObjectId();
        } else {
            return "";
        }
    }

    public void savePerdidoParseObject(Perdidos perdido) throws ParseException {
        perdidosObject.put(CPerdidos.TITULO, perdido.getTitulo());
        perdidosObject.put(CPerdidos.DESCRIPCION, perdido.getDescripcion());
        perdidosObject.put(CPerdidos.FECHA, perdido.getFecha());
        perdidosObject.put(CPerdidos.FOTOS, new ParseFile("picture.jpg", perdido.getFoto()));
        perdidosObject.put(CPerdidos.UBICACION, perdido.getUbicacion());
        perdidosObject.put(CPerdidos.SOLUCIONADO, false);
        perdidosObject.put(CPerdidos.BLOQUEADO, false);


        try {
            raza = getRaza(perdido.getRaza().getRaza());
            color = getColor(perdido.getColor().getColor());
            sexo = getSexo(perdido.getSexo().getSexo());
            tamaño = getTamaño(perdido.getTamaño().getTamaño());
            edad = getEdad(perdido.getEdad().getEdad());
            especie = getEspecie(perdido.getEspecie().getEspecie());
            estado = iEstado.getEstado(perdido.getEstado().getSituacion());
            persona = iPersona.getPersonabyEmail(perdido.getPersona().getEmail());
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
        perdidosObject.put(CPerdidos.UBICACION, perdido.getUbicacion());
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
    public void editPerdido(Perdidos perdidoAux) throws ParseException {

        ParseObject objectPerdido;
        query = ParseQuery.getQuery(Clases.PERDIDOS);
        query.whereEqualTo(CPerdidos.OBJECT_ID, perdidoAux.getObjectId());

        try {
            if (query.count() != 0) {
                objectPerdido = query.getFirst();
                objectPerdido.put(CPerdidos.TITULO, perdidoAux.getTitulo());
                objectPerdido.put(CPerdidos.DESCRIPCION, perdidoAux.getDescripcion());
                objectPerdido.put(CPerdidos.FOTOS, new ParseFile("picture.jpg", perdidoAux.getFoto()));
                objectPerdido.put(CPerdidos.OBJECT_ID, perdidoAux.getObjectId());
                objectPerdido.put(CPerdidos.UBICACION, perdidoAux.getUbicacion());

                try {
                    raza = getRaza(perdidoAux.getRaza().getRaza());
                    color = getColor(perdidoAux.getColor().getColor());
                    sexo = getSexo(perdidoAux.getSexo().getSexo());
                    tamaño = getTamaño(perdidoAux.getTamaño().getTamaño());
                    edad = getEdad(perdidoAux.getEdad().getEdad());
                    especie = getEspecie(perdidoAux.getEspecie().getEspecie());
                    estado = iEstado.getEstado(perdidoAux.getEstado().getSituacion());
                    persona = iPersona.getPersonabyEmail(perdidoAux.getPersona().getEmail());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                objectPerdido.put(CPerdidos.ID_RAZA, ParseObject.createWithoutData(Clases.RAZAS, String.valueOf(raza.getObjectId())));
                objectPerdido.put(CPerdidos.ID_COLOR, ParseObject.createWithoutData(Clases.COLORES, String.valueOf(color.getObjectId())));
                objectPerdido.put(CPerdidos.ID_SEXO, ParseObject.createWithoutData(Clases.SEXOS, String.valueOf(sexo.getObjectId())));
                objectPerdido.put(CPerdidos.ID_TAMAÑO, ParseObject.createWithoutData(Clases.TAMAÑOS, String.valueOf(tamaño.getObjectId())));
                objectPerdido.put(CPerdidos.ID_EDAD, ParseObject.createWithoutData(Clases.EDADES, String.valueOf(edad.getObjectId())));
                objectPerdido.put(CPerdidos.ID_ESPECIE, ParseObject.createWithoutData(Clases.ESPECIES, String.valueOf(especie.getObjectId())));
                objectPerdido.put(CPerdidos.ID_ESTADO, ParseObject.createWithoutData(Clases.ESTADOS, String.valueOf(estado.getObjectId())));
                objectPerdido.put(CPerdidos.ID_PERSONA, ParseObject.createWithoutData(Clases.PERSONAS, String.valueOf(persona.getObjectId())));

                save(objectPerdido);
            }
        } catch (ParseException e) {
            e.fillInStackTrace();
        }

    }


    @Override
    public Razas getRaza(String raza) throws ParseException {

        query = ParseQuery.getQuery(Clases.RAZAS);
        query.include(CRazas.ID_ESPECIE);
        query.whereEqualTo(CRazas.RAZA, raza);
        checkInternetGet(query);
        Razas razaObject = null;
        try {
            if (query.count() != 0) {
                objectAux = query.getFirst();
                if (!raza.equals("Otra")) {
                    if (!raza.equals("Mestizo")) {
                        ParseObject object = objectAux.getParseObject(CPerdidos.ID_ESPECIE);
                        especie = new Especies(object.getString(CEspecies.ESPECIE), object.getObjectId());
                    }
                }
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
            if (query.count() != 0) {
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
            if (query.count() != 0) {
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
            if (query.count() != 0) {
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
            if (query.count() != 0) {
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
            if (query.count() != 0) {
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
            if (objectAux == null) {
                especie = null;
            } else {
                especie = new Especies(objectAux.getString(CEspecies.ESPECIE), objectAux.getObjectId());
            }
            raza = new Razas(object.getString(CRazas.RAZA), object.getObjectId(), especie);
            razas.add(raza);
        }

        return razas;
    }

    public List<ParseObject> getRazasParseObject() {

        query = ParseQuery.getQuery(Clases.RAZAS);
        query.include(CRazas.ID_ESPECIE);
        checkInternetGet(query);

        listParseObject = findQuery();

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

        listParseObject = findQuery();

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

        listParseObject = findQuery();

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

        listParseObject = findQuery();

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

        listParseObject = findQuery();

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

        listParseObject = findQuery();

        return listParseObject;
    }


    @Override
    public void deletePerdido(String objectId) throws ParseException {

        query = ParseQuery.getQuery(Clases.PERDIDOS);
        query.whereEqualTo(CPerdidos.OBJECT_ID, objectId);
        checkInternetGet(query);
        try {
            if (query.count() != 0) {
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

        ParseObject parseObjectComentario = agregarComentario(comentario, email);
        objectAux = getParseObjectById(perdidoObjectId);
        objectRelation = objectAux.getRelation(CPerdidos.COMENTARIOS);
        objectRelation.add(parseObjectComentario);
        save(objectAux);
        pushNotification(perdidoObjectId, email);
    }

    public ParseObject agregarComentario(String comentario, String email) throws ParseException {

        ParseObject object = new ParseObject(Clases.COMENTARIOS);
        object.put(CComentarios.COMENTARIO, comentario);
        object.put(CComentarios.LEIDO, false);
        object.put(CComentarios.FECHA, new Date());
        object.put(CComentarios.BLOQUEADO, false);
        persona = mPersonasImpl.getPersonabyEmail(email);
        object.put(CComentarios.ID_PERSONA, ParseObject.createWithoutData(Clases.PERSONAS, String.valueOf(persona.getObjectId())));
        save(object);
        ParseObject objectComentario = iComentarios.getComentarioById(getUltimoObjectId(Clases.COMENTARIOS));

        return objectComentario;
    }

    public void pushNotification(String perdidoObjectId, String mailLogueado) {
        if (!HuellasApplication.getInstance().getProfileEmailFacebook().equals("")) {
            List<String> emails = new ArrayList<String>();
            try {
                perdido = getPublicacionPerdidosById(perdidoObjectId);
            } catch (ParseException e) {
                e.printStackTrace();
            }
           /* if (!perdido.getPersona().getEmail().equals(mailLogueado))*/
            emails.add(perdido.getPersona().getEmail());
            for (Comentarios comentarioAux : perdido.getComentarios()) { //email de las personas que comentaron
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
                object2.put("alert", HuellasApplication.getInstance().getProfileNameFacebook() + " comentó una publicación.");
                object2.put(Constants.OBJETO_ID, perdido.getObjectId());
                object2.put(Constants.FROM_FRAGMENT, Constants.PERDIDOS);
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

    @Override
    public ParseObject getParseObjectById(String objectId) {

        query = ParseQuery.getQuery(Clases.PERDIDOS);
        query.whereEqualTo(CAdicionales.OBJECT_ID, objectId);
        checkInternetGet(query);

        try {
            if (query.count() != 0) {
                objectAux = query.getFirst();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return objectAux;

    }

    @Override
    public void bloquearPerdido(String objectId) {
        query = ParseQuery.getQuery(Clases.PERDIDOS);
        query.whereEqualTo(CPerdidos.OBJECT_ID, objectId);

        try {
            if (query.count() != 0) {
                objectAux = query.getFirst();
                objectAux.put(CPerdidos.BLOQUEADO, true);
                save(objectAux);
            }
        } catch (ParseException e) {
            e.fillInStackTrace();
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
    public void saveInBackground(ParseObject object) {
        object.saveInBackground();
    }

    @Override
    public void deleteInBackground(ParseObject object) {
        object.deleteInBackground();
    }

    @Override
    public void cargarDBLocalListaPerdidos(Context context) throws ParseException {

        if (internet(context)) {
            ParseObject.pinAllInBackground(getPerdidosParseObject());

        }
    }

    @Override
    public void cargarDBLocalCaracteristicasPerdidos(Context context) {
            if (internet(context)) {
                ParseObject.pinAllInBackground(getRazasParseObject());
                ParseObject.pinAllInBackground(getColoresParseObject());
                ParseObject.pinAllInBackground(getSexosParseObject());
                ParseObject.pinAllInBackground(getTamañosParseObject());
                ParseObject.pinAllInBackground(getEdadesParseObject());
                ParseObject.pinAllInBackground(getEspeciesParseObject());
            }
    }

    @Override
    public List<Perdidos> getPublicacionPerdidosByEmail(String email) throws ParseException {

        persona = iPersona.getPersonabyEmail(email);
        ParseObject obj = ParseObject.createWithoutData(Clases.PERSONAS, persona.getObjectId());

        query = getQueryPerdidos();
        query.whereEqualTo(CPerdidos.OBJECT_ID, obj);
        query.whereEqualTo(CPerdidos.SOLUCIONADO, true);
        query.orderByDescending(CPerdidos.FECHA);
        checkInternetGet(query);

        listParseObject = findQuery();

        return getListPerdidos(listParseObject);
    }

    @Override
    public List<Perdidos> getPublicacionPerdidosPropiasById(String personaObjectId) throws ParseException {

        ParseObject obj = ParseObject.createWithoutData(Clases.PERSONAS, personaObjectId);

        query = getQueryPerdidos();
        query.whereEqualTo(CPerdidos.OBJECT_ID, obj);
        query.orderByDescending(CPerdidos.FECHA);
        checkInternetGet(query);

        listParseObject = findQuery();

        return getListPerdidos(listParseObject);
    }

    @Override
    public Perdidos getPublicacionPerdidosById(String objectId) throws ParseException {

        query = getQueryPerdidos();
        query.whereEqualTo(CPerdidos.OBJECT_ID, objectId);
        checkInternetGet(query);

        try {
            if (query.count() != 0) {
                objectAux = query.getFirst();
                perdido = getPerdido(objectAux);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return perdido;

    }
}

