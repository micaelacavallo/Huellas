package com.example.micaela.Controladores;

import android.content.Context;
import android.graphics.Color;

import com.example.micaela.Enums.CAdicionales;
import com.example.micaela.Enums.CColores;
import com.example.micaela.Enums.CComentarios;
import com.example.micaela.Enums.CEdades;
import com.example.micaela.Enums.CEspecies;
import com.example.micaela.Enums.CEstados;
import com.example.micaela.Enums.CPerdidos;
import com.example.micaela.Enums.CPersonas;
import com.example.micaela.Enums.CRazas;
import com.example.micaela.Enums.CSexos;
import com.example.micaela.Enums.CTamaños;
import com.example.micaela.Enums.Clases;
import com.example.micaela.Enums.Keys;
import com.example.micaela.Interfaces.IDBLocal;
import com.example.micaela.Interfaces.IPerdidos;
import com.example.micaela.clases.Colores;
import com.example.micaela.clases.Comentarios;
import com.example.micaela.clases.Edades;
import com.example.micaela.clases.Especies;
import com.example.micaela.clases.Estados;
import com.example.micaela.clases.Perdidos;
import com.example.micaela.clases.Personas;
import com.example.micaela.clases.Razas;
import com.example.micaela.clases.Sexos;
import com.example.micaela.clases.Tamaños;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Horacio on 5/10/2015.
 */
public class IPerdidosImpl extends IGeneralImpl implements IPerdidos, IDBLocal {

    ParseObject perdidosObject;

    public IPerdidosImpl(Context context)
    {
        Parse.initialize(context, Keys.APPLICATION_ID, Keys.CLIENT_ID);
        perdidosObject = new ParseObject(Clases.PERDIDOS);

    }

        @Override
    public List<Perdidos> getPerdidos() throws ParseException {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.PERDIDOS);
            query.include(CPerdidos.ID_RAZA);
            query.include(CPerdidos.ID_COLOR);
            query.include(CPerdidos.ID_SEXO);
            query.include(CPerdidos.ID_TAMAÑO);
            query.include(CPerdidos.ID_EDAD);
            query.include(CPerdidos.ID_ESPECIE);
            query.include(CPerdidos.ID_ESTADO);
            query.include(CPerdidos.ID_PERSONA);

            List<ParseObject> objects = query.find();
            List<Perdidos> perdidos = new ArrayList<Perdidos>();
            ParseObject objectAux = null;
            Razas raza = null;
            Colores color = null;
            Sexos sexo = null;
            Tamaños tamaño = null;
            Edades edad = null;
            Especies especie = null;
            Personas persona = null;
            Estados estado = null;
            ArrayList<String> fotos = null;
            List<ParseObject> listComentarios = null;
            Comentarios comentario = null;
            ParseRelation objectRelation;
            List<Comentarios> comentarios = new ArrayList<Comentarios>();
            Perdidos perdido = null;


            if(objects.size() > 0){
                for(ParseObject object : objects)
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

                    objectRelation = object.getRelation(CPerdidos.COMENTARIOS);
                    listComentarios = objectRelation.getQuery().find();

                    //verificar que no sea null
                    for(ParseObject objectComentario : listComentarios)
                    {
                        objectAux = object.getParseObject(CComentarios.ID_PERSONA);
                        persona = new Personas(objectAux.getObjectId(), objectAux.getInt(CPersonas.ID_PERSONA), objectAux.getString(CPersonas.EMAIL), objectAux.getString(CPersonas.NOMBRE), objectAux.getString(CPersonas.APELLIDO), objectAux.getString(CPersonas.TELEFONO), objectAux.getBoolean(CPersonas.ADMINISTRADOR));
                        comentario = new Comentarios(objectComentario.getObjectId(), objectComentario.getInt(CComentarios.ID_COMENTARIO), objectComentario.getString(CComentarios.COMENTARIO), persona, objectComentario.getDate(CComentarios.FECHA));
                        comentarios.add(comentario);
                    }

                    fotos = (ArrayList<String>) object.get(CPerdidos.FOTOS);
                    perdido = new Perdidos(object.getInt(CPerdidos.ID_PERDIDO), edad, raza, especie, tamaño, color, sexo, estado, persona, object.getDate(CPerdidos.FECHA), object.getParseGeoPoint(CPerdidos.UBICACION), object.getString(CPerdidos.TITULO), object.getString(CPerdidos.DESCRIPCION), fotos, comentarios);
                    perdidos.add(perdido);
                }
            }

            return perdidos;
    }

    @Override
    public void savePerdido(Perdidos perdido) {

        //VALIDAR EN FE
        perdidosObject.put(CPerdidos.TITULO, perdido.getTitulo());
        perdidosObject.put(CPerdidos.DESCRIPCION, perdido.getDescripcion());
        perdidosObject.put(CPerdidos.FECHA, perdido.getFecha());
        perdidosObject.put(CPerdidos.FOTOS, perdido.getFotos());
        perdidosObject.put(CPerdidos.UBICACION, perdido.getUbicacion());

        Razas raza = null;
        Colores color  = null;
        Sexos sexo  = null;
        Tamaños tamaño  = null;
        Edades edad  = null;
        Especies especie  = null;
        Estados estado = null;
        Personas persona = null;

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

        perdidosObject.saveInBackground();

    }

    @Override
    public int getUltimoInsertado() throws ParseException {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.PERDIDOS);
        query.orderByDescending(CPerdidos.ID_PERDIDO);
        int idUltimo = 0;
        //capturar si no hay nada insertado
        ParseObject object = query.getFirst();
        idUltimo = object.getInt(CPerdidos.ID_PERDIDO) + 1;
        return idUltimo;

    }

    @Override
    public Razas getRaza(String raza) throws ParseException {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.RAZAS);
        query.whereEqualTo(CRazas.RAZA, raza);
        ParseObject object = query.getFirst();

        Razas razaObject = new Razas(object.getInt(CRazas.ID_RAZA), object.getString(CRazas.RAZA), object.getObjectId());

        return razaObject;
    }

    @Override
    public Colores getColor(String color) throws ParseException {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.COLORES);
        query.whereEqualTo(CColores.COLOR, color);
        ParseObject object = query.getFirst();

        Colores colorObject = new Colores(object.getInt(CColores.ID_COLOR), object.getString(CColores.COLOR), object.getObjectId());

        return colorObject;
    }

    @Override
    public Sexos getSexo(String sexo) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.SEXOS);
        query.whereEqualTo(CSexos.SEXO, sexo);
        ParseObject object = query.getFirst();

        Sexos sexoObject = new Sexos(object.getInt(CSexos.ID_SEXO), object.getString(CSexos.SEXO), object.getObjectId());

        return sexoObject;
    }

    @Override
    public Tamaños getTamaño(String tamaño) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.TAMAÑOS);
        query.whereEqualTo(CTamaños.TAMAÑO, tamaño);
        ParseObject object = query.getFirst();

        Tamaños tamañoObject = new Tamaños(object.getInt(CTamaños.ID_TAMAÑO), object.getString(CTamaños.TAMAÑO), object.getObjectId());

        return tamañoObject;
    }

    @Override
    public Edades getEdad(String edad) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.EDADES);
        query.whereEqualTo(CEdades.EDAD, edad);
        ParseObject object = query.getFirst();

        Edades edadObject = new Edades(object.getInt(CEdades.ID_EDAD), object.getString(CEdades.EDAD), object.getObjectId());

        return edadObject;
    }

    @Override
    public Especies getEspecie(String especie) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.ESPECIES);
        query.whereEqualTo(CEspecies.ESPECIE, especie);
        ParseObject object = query.getFirst();

        Especies especieObject = new Especies(object.getInt(CEspecies.ID_ESPECIE), object.getString(CEspecies.ESPECIE), object.getObjectId());

        return especieObject;
    }

    @Override
    public List<Razas> getRazas() {

        List<Razas> razas = new ArrayList<Razas>();
        Razas raza = null;

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.RAZAS);
        List<ParseObject> listParseObject = null;
        try {
            listParseObject = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
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

        List<Colores> colores = new ArrayList<Colores>();
        Colores color = null;

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.COLORES);
        List<ParseObject> listParseObject = null;
        try {
            listParseObject = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
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

        List<Sexos> sexos = new ArrayList<Sexos>();
        Sexos sexo = null;

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.SEXOS);
        List<ParseObject> listParseObject = null;
        try {
            listParseObject = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
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

        List<Tamaños> tamaños = new ArrayList<Tamaños>();
        Tamaños tamaño  = null;

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.TAMAÑOS);
        List<ParseObject> listParseObject = null;
        try {
            listParseObject = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
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

        List<Edades> edades = new ArrayList<Edades>();
        Edades edad= null;

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.EDADES);
        List<ParseObject> listParseObject = null;
        try {
            listParseObject = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
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

        List<Especies> especies = new ArrayList<Especies>();
        Especies especie = null;

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.ESPECIES);
        List<ParseObject> listParseObject = null;
        try {
            listParseObject = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
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

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.PERDIDOS);
        query.whereEqualTo(CPerdidos.OBJECT_ID, objectId);
        ParseObject object = query.getFirst();

        object.deleteInBackground();
    }

    @Override
    public void pinObjectInBackground(ParseObject object) {

    }

    @Override
    public void queryFromLocalDatastore(ParseQuery query) {

    }
}
