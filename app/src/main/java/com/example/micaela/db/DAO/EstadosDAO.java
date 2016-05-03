package com.example.micaela.db.DAO;

import android.content.Context;
import android.widget.Toast;

import com.example.micaela.db.Constantes.CEstados;
import com.example.micaela.db.Constantes.CPerdidos;
import com.example.micaela.db.Constantes.Clases;
import com.example.micaela.db.Controladores.IGeneralImpl;
import com.example.micaela.db.Interfaces.IEstados;
import com.example.micaela.db.Modelo.Estados;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quimey.arozarena on 5/2/2016.
 */
public class EstadosDAO extends IGeneralImpl implements IEstados {

    private Context context;
    private ParseQuery<ParseObject> query;

    public EstadosDAO() {
    }

    public EstadosDAO(Context context) {
        this.context = context;
        query = null;
    }

    @Override
    public Estados getEstado(String situacion) throws ParseException {

        query = ParseQuery.getQuery(Clases.ESTADOS);
        query.whereEqualTo(CEstados.SITUACION, situacion);
        Estados estado = null;
        try {
            if(query.count() != 0) {
                ParseObject object = query.getFirst();
                estado = new Estados(object.getObjectId(), object.getString(CEstados.SITUACION));
            }
        } catch (ParseException e) {
            e.fillInStackTrace();
        }

        return estado;
    }

    @Override
    public List<Estados> getEstados() {
        query = ParseQuery.getQuery(Clases.ESTADOS);
        List<Estados> listEstados = new ArrayList<>();
        List<ParseObject> listParseObject = null;
        Estados estado = null;
        if(internet(context)) {
            try {
                listParseObject = query.find();
            } catch (ParseException e) {
                Toast.makeText(context, "no existe", Toast.LENGTH_LONG);
            }

            for (ParseObject object : listParseObject) {
                estado = new Estados(object.getObjectId(), object.getString(CEstados.SITUACION));
                listEstados.add(estado);
            }
        }
        return listEstados;
    }

    @Override
    public void cambiarEstado(String idpublicacion, boolean estado) {

        query = ParseQuery.getQuery(Clases.PERDIDOS);
        query.whereEqualTo(CPerdidos.OBJECT_ID, idpublicacion);
        ParseObject objectAux = null;

        try {
            if(query.count() != 0) {
                objectAux = query.getFirst();
                objectAux.put(CPerdidos.SOLUCIONADO, estado);
                save(objectAux);
            }
        } catch (ParseException e) {
            e.fillInStackTrace();
        }
    }
}
