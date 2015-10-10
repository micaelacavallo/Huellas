package com.example.micaela.Controladores;

import com.example.micaela.Enums.CEstados;
import com.example.micaela.Enums.CPersonas;
import com.example.micaela.Enums.Clases;
import com.example.micaela.Interfaces.IGeneral;
import com.example.micaela.clases.Estados;
import com.example.micaela.clases.Personas;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Horacio on 10/10/2015.
 */
public class IGeneralImpl implements IGeneral{

    @Override
    public Estados getEstado(String situacion) throws ParseException {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.ESTADOS);
        query.whereEqualTo(CEstados.SITUACION, situacion);
        query.whereEqualTo(CEstados.SOLUCIONADO, false);
        ParseObject object = query.getFirst();

        Estados estado = new Estados(object.getObjectId(), object.getInt(CEstados.ID_ESTADO), object.getBoolean(CEstados.SOLUCIONADO), object.getString(CEstados.SITUACION));

        return estado;
    }

    @Override
    public Personas getPersona(String email) throws ParseException {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Clases.PERSONAS);
        query.whereEqualTo(CPersonas.EMAIL, email);
        ParseObject object = query.getFirst();

        Personas persona = new Personas(object.getObjectId(), object.getInt(CPersonas.ID_PERSONA), object.getString(CPersonas.EMAIL),object.getString(CPersonas.NOMBRE), object.getString(CPersonas.APELLIDO), object.getString(CPersonas.TELEFONO), object.getBoolean(CPersonas.ADMINISTRADOR));

        return persona;
    }
}
