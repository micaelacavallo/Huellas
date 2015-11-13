package com.example.micaela.db.Controladores;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.example.micaela.db.Enums.CAdicionales;
import com.example.micaela.db.Enums.CColores;
import com.example.micaela.db.Enums.CEdades;
import com.example.micaela.db.Enums.CEspecies;
import com.example.micaela.db.Enums.CEstados;
import com.example.micaela.db.Enums.CPerdidos;
import com.example.micaela.db.Enums.CPersonas;
import com.example.micaela.db.Enums.CRazas;
import com.example.micaela.db.Enums.CSexos;
import com.example.micaela.db.Enums.CTamaños;
import com.example.micaela.db.Enums.Clases;
import com.example.micaela.db.Interfaces.IDBLocal;
import com.example.micaela.db.Interfaces.IPerdidos;
import com.example.micaela.db.clases.Colores;
import com.example.micaela.db.clases.Comentarios;
import com.example.micaela.db.clases.Edades;
import com.example.micaela.db.clases.Especies;
import com.example.micaela.db.clases.Estados;
import com.example.micaela.db.clases.Perdidos;
import com.example.micaela.db.clases.Personas;
import com.example.micaela.db.clases.Razas;
import com.example.micaela.db.clases.Sexos;
import com.example.micaela.db.clases.Tamaños;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.List;

public class IPerdidosImpl extends IGeneralImpl implements IPerdidos, IDBLocal {

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


    public IPerdidosImpl(Context context)
    {
        ParseObject.registerSubclass(Colores.class);
        ParseObject.registerSubclass(Edades.class);
        ParseObject.registerSubclass(Especies.class);
        ParseObject.registerSubclass(Razas.class);
        ParseObject.registerSubclass(Sexos.class);
        ParseObject.registerSubclass(Tamaños.class);
        ParseObject.registerSubclass(Perdidos.class);

        this.context = context;
        init();

    }

    public void init()
    {
        perdidosObject = ParseObject.create(Clases.PERDIDOS);
        query = null;
        perdidos = new ArrayList<Perdidos>();
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
        comentarios = new ArrayList<Comentarios>();
        perdido = null;
        razas = new ArrayList<Razas>();
        tamaños = new ArrayList<Tamaños>();
        especies = new ArrayList<Especies>();
        colores = new ArrayList<Colores>();
        sexos = new ArrayList<Sexos>();
        edades = new ArrayList<Edades>();
    }

        @Override
    public List<Perdidos> getPerdidos() throws ParseException {

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
            checkInternetGet(query);

            try{

                listParseObject = query.find();

                if(listParseObject.size() > 0){
                    for(ParseObject object : listParseObject)
                    {
                        objectAux = object.getParseObject(CPerdidos.ID_RAZA);
                        raza = new Razas(objectAux.getInt(CRazas.ID_RAZA), objectAux.getString(CRazas.RAZA), objectAux.getObjectId());

                        objectAux = object.getParseObject(CPerdidos.ID_COLOR);
                        color = new Colores(objectAux.getInt(CColores.ID_COLOR), objectAux.getString(CColores.COLOR), objectAux.getObjectId());

                        objectAux = object.getParseObject(CPerdidos.ID_SEXO);
                        sexo = new Sexos(objectAux.getInt(CSexos.ID_SEXO), objectAux.getString(CSexos.SEXO), objectAux.getObjectId());

                        objectAux = object.getParseObject(CPerdidos.ID_TAMAÑO);
                        tamaño = new Tamaños(objectAux.getInt(CTamaños.ID_TAMAÑO), objectAux.getString(CTamaños.TAMAÑO), objectAux.getObjectId());

                        objectAux = object.getParseObject(CPerdidos.ID_EDAD);
                        edad = new Edades(objectAux.getInt(CEdades.ID_EDAD), objectAux.getString(CEdades.EDAD), objectAux.getObjectId());

                        objectAux = object.getParseObject(CPerdidos.ID_ESPECIE);
                        especie = new Especies(objectAux.getInt(CEspecies.ID_ESPECIE), objectAux.getString(CEspecies.ESPECIE), objectAux.getObjectId());

                        objectAux = object.getParseObject(CPerdidos.ID_PERSONA);
                        persona = new Personas(objectAux.getObjectId(), objectAux.getInt(CPersonas.ID_PERSONA), objectAux.getString(CPersonas.EMAIL), objectAux.getString(CPersonas.NOMBRE), objectAux.getString(CPersonas.APELLIDO), objectAux.getString(CPersonas.TELEFONO), objectAux.getBoolean(CPersonas.ADMINISTRADOR));

                        objectAux = object.getParseObject(CPerdidos.ID_ESTADO);
                        estado = new Estados(objectAux.getObjectId(), objectAux.getInt(CEstados.ID_ESTADO), objectAux.getBoolean(CEstados.SOLUCIONADO), objectAux.getString(CEstados.SITUACION));

                        try{
                        objectRelation = object.getRelation(CPerdidos.COMENTARIOS);
                            List<ParseObject> listComentarios = objectRelation.getQuery().find();
                        comentarios = getComentarios(objectRelation.getQuery().find(), object);}
                            catch (Exception e)
                            {
                                comentarios = null;
                            }


                            foto =  object.getParseFile(CPerdidos.FOTOS);
                            String imageURL = foto.getUrl();
                            Uri imageUri = Uri.parse(imageURL);

                        perdido = new Perdidos(object.getInt(CPerdidos.ID_PERDIDO), edad, raza, especie, tamaño, color, sexo, estado, persona, object.getDate(CPerdidos.FECHA), object.getParseGeoPoint(CPerdidos.UBICACION), object.getString(CPerdidos.TITULO), object.getString(CPerdidos.DESCRIPCION), imageUri, comentarios);
                        perdidos.add(perdido);
                    }
                }
            }
            catch(ParseException e)
            {
                Toast.makeText(context, "no hay perdidos", Toast.LENGTH_LONG);
            }

            return perdidos;
    }

    @Override
    public void savePerdido(Perdidos perdido) {

        //VALIDAR EN FE
        perdidosObject.put(CPerdidos.TITULO, perdido.getTitulo());
        perdidosObject.put(CPerdidos.DESCRIPCION, perdido.getDescripcion());
        perdidosObject.put(CPerdidos.FECHA, perdido.getFecha());
        perdidosObject.put(CPerdidos.FOTOS, perdido.getFoto());
        perdidosObject.put(CPerdidos.UBICACION, perdido.getUbicacion());


        try {
            perdidosObject.put(CPerdidos.ID_PERDIDO, getUltimoInsertado());
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

    public ParseObject cargarPerdido(Perdidos perdido)
    {
        perdidosObject.put(CPerdidos.TITULO, perdido.getTitulo());
        perdidosObject.put(CPerdidos.DESCRIPCION, perdido.getDescripcion());
        perdidosObject.put(CPerdidos.FECHA, perdido.getFecha());
        perdidosObject.put(CPerdidos.FOTOS, perdido.getFoto());
        perdidosObject.put(CPerdidos.UBICACION, perdido.getUbicacion());
        perdidosObject.put(CPerdidos.ID_PERDIDO, perdido.getIdPerdido());
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
    public int getUltimoInsertado() throws ParseException {

        query = ParseQuery.getQuery(Clases.PERDIDOS);
        query.orderByDescending(CPerdidos.ID_PERDIDO);
        int idUltimo = 0;
        int size = query.count();
        if(size != 0) {
            objectAux = query.getFirst();
            idUltimo = objectAux.getInt(CPerdidos.ID_PERDIDO) + 1;
        }
        else
        {
            idUltimo = 1;
        }

        return idUltimo;

    }

    @Override
    public Razas getRaza(String raza) throws ParseException {

        query = ParseQuery.getQuery(Clases.RAZAS);
        query.whereEqualTo(CRazas.RAZA, raza);
        checkInternetGet(query);
        Razas razaObject = null;

        try{
            objectAux = query.getFirst();
            razaObject = new Razas(objectAux.getInt(CRazas.ID_RAZA), objectAux.getString(CRazas.RAZA), objectAux.getObjectId());
        }
        catch(ParseException e)
        {
            Toast.makeText(context, "no existe", Toast.LENGTH_LONG);
        }
        return razaObject;
    }

    @Override
    public Colores getColor(String color) throws ParseException {

        query = ParseQuery.getQuery(Clases.COLORES);
        query.whereEqualTo(CColores.COLOR, color);
        checkInternetGet(query);
        Colores colorObject = null;

        try{
            objectAux = query.getFirst();
            colorObject = new Colores(objectAux.getInt(CColores.ID_COLOR), objectAux.getString(CColores.COLOR), objectAux.getObjectId());
        }
        catch(ParseException e)
        {
            Toast.makeText(context, "no existe", Toast.LENGTH_LONG);
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
            objectAux = query.getFirst();
            sexoObject = new Sexos(objectAux.getInt(CSexos.ID_SEXO), objectAux.getString(CSexos.SEXO), objectAux.getObjectId());
        }
        catch(ParseException e)
        {
            Toast.makeText(context, "no existe", Toast.LENGTH_LONG);
        }
        return sexoObject;
    }

    @Override
    public Tamaños getTamaño(String tamaño) throws ParseException {

        query = ParseQuery.getQuery(Clases.TAMAÑOS);
        query.whereEqualTo(CTamaños.TAMAÑO, tamaño);
        checkInternetGet(query);
        Tamaños tamañoObject = null;
        try{
            objectAux = query.getFirst();
            tamañoObject = new Tamaños(objectAux.getInt(CTamaños.ID_TAMAÑO), objectAux.getString(CTamaños.TAMAÑO), objectAux.getObjectId());
        }
        catch(ParseException e)
        {
            Toast.makeText(context, "no existe", Toast.LENGTH_LONG);
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
            objectAux = query.getFirst();
            edadObject = new Edades(objectAux.getInt(CEdades.ID_EDAD), objectAux.getString(CEdades.EDAD), objectAux.getObjectId());
        }
        catch(ParseException e)
        {
            Toast.makeText(context, "no existe", Toast.LENGTH_LONG);
        }

    return edadObject;
    }

    @Override
    public Especies getEspecie(String especie) throws ParseException {

        query = ParseQuery.getQuery(Clases.ESPECIES);
        query.whereEqualTo(CEspecies.ESPECIE, especie);
        checkInternetGet(query);
        Especies especieObject = null;

        try{
            objectAux = query.getFirst();
            especieObject = new Especies(objectAux.getInt(CEspecies.ID_ESPECIE), objectAux.getString(CEspecies.ESPECIE), objectAux.getObjectId());
        }
        catch(ParseException e)
        {
            Toast.makeText(context, "no existe", Toast.LENGTH_LONG);
        }

        return especieObject;
    }

    @Override
    public List<Razas> getRazas() {

        query = ParseQuery.getQuery(Clases.RAZAS);
        checkInternetGet(query);
        try {
            listParseObject = query.find();
        }
        catch(ParseException e)
        {
            Toast.makeText(context, "no existe", Toast.LENGTH_LONG);
        }

        for(ParseObject object : listParseObject)
        {
            raza = new Razas(object.getInt(CRazas.ID_RAZA), object.getString(CRazas.RAZA), object.getObjectId());
            razas.add(raza);
        }

        return razas;
    }

    @Override
    public List<Colores> getColores() {

        query = ParseQuery.getQuery(Clases.COLORES);
        checkInternetGet(query);
        try {
            listParseObject = query.find();
        }
        catch(ParseException e)
        {
            Toast.makeText(context, "no existe", Toast.LENGTH_LONG);
        }

        for(ParseObject object : listParseObject)
        {
            color = new Colores(object.getInt(CColores.ID_COLOR), object.getString(CColores.COLOR), object.getObjectId());
            colores.add(color);
        }

        return colores;
    }

    @Override
    public List<Sexos> getSexos() {

        query = ParseQuery.getQuery(Clases.SEXOS);
        checkInternetGet(query);
        try {
            listParseObject = query.find();
        }
        catch(ParseException e)
        {
            Toast.makeText(context, "no existe", Toast.LENGTH_LONG);
        }

        for(ParseObject object : listParseObject)
        {
            sexo = new Sexos(object.getInt(CSexos.ID_SEXO), object.getString(CSexos.SEXO), object.getObjectId());
            sexos.add(sexo);
        }

        return sexos;
    }

    @Override
    public List<Tamaños> getTamaños() {

        query = ParseQuery.getQuery(Clases.TAMAÑOS);
        checkInternetGet(query);
        try {
            listParseObject = query.find();
        }
        catch(ParseException e)
        {
            Toast.makeText(context, "no existe", Toast.LENGTH_LONG);
        }

        for(ParseObject object : listParseObject)
        {
            tamaño = new Tamaños(object.getInt(CTamaños.ID_TAMAÑO), object.getString(CTamaños.TAMAÑO), object.getObjectId());
            tamaños.add(tamaño);
        }

        return tamaños;
    }

    @Override
    public List<Edades> getEdades() {

        query = ParseQuery.getQuery(Clases.EDADES);
        checkInternetGet(query);

        try {
            listParseObject = query.find();
        }
        catch(ParseException e)
        {
            Toast.makeText(context, "no existe", Toast.LENGTH_LONG);
        }

        for(ParseObject object : listParseObject)
        {
           edad = new Edades(object.getInt(CEdades.ID_EDAD), object.getString(CEdades.EDAD), object.getObjectId());
            edades.add(edad);
        }

        return edades;
    }

    @Override
    public List<Especies> getEspecies() {

        query = ParseQuery.getQuery(Clases.ESPECIES);
        checkInternetGet(query);
        try {
            listParseObject = query.find();
        }
        catch(ParseException e)
        {
            Toast.makeText(context, "no existe", Toast.LENGTH_LONG);
        }

        for(ParseObject object : listParseObject)
        {
           especie = new Especies(object.getInt(CEspecies.ID_ESPECIE), object.getString(CEspecies.ESPECIE), object.getObjectId());
           especies.add(especie);
        }

        return especies;
    }

    @Override
    public void deletePerdido(String objectId) throws ParseException {

        query = ParseQuery.getQuery(Clases.PERDIDOS);
        query.whereEqualTo(CPerdidos.OBJECT_ID, objectId);
        checkInternetGet(query);
        try {
            objectAux = query.getFirst();
            objectRelation = objectAux.getRelation(CPerdidos.COMENTARIOS);
            listParseObject = objectRelation.getQuery().find();

            for (ParseObject parseObject : listParseObject) {

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

        if(internet(context)) {
            perdidos = getPerdidos();
            razas = getRazas();
            colores = getColores();
            sexos = getSexos();
            tamaños = getTamaños();
            edades = getEdades();
            especies = getEspecies();

            ParseObject.pinAllInBackground(perdidos);
            ParseObject.pinAllInBackground(razas);
            ParseObject.pinAllInBackground(colores);
            ParseObject.pinAllInBackground(sexos);
            ParseObject.pinAllInBackground(tamaños);
            ParseObject.pinAllInBackground(edades);
            ParseObject.pinAllInBackground(especies);
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
