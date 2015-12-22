package com.example.micaela.db.Controladores;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.micaela.db.DAO.GeneralDAO;
import com.example.micaela.db.Enums.CComentarios;
import com.example.micaela.db.Enums.CEstados;
import com.example.micaela.db.Enums.CPersonas;
import com.example.micaela.db.Enums.Clases;
import com.example.micaela.db.Interfaces.IDBLocal;
import com.example.micaela.db.Interfaces.IGeneral;
import com.example.micaela.db.Modelo.Comentarios;
import com.example.micaela.db.Modelo.Estados;
import com.example.micaela.db.Modelo.Personas;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Horacio on 10/10/2015.
 */
public class IGeneralImpl implements IGeneral {

    private GeneralDAO mGeneralDAO;

    @Override
    public Estados getEstado(String situacion) throws ParseException {
        return mGeneralDAO.getEstado(situacion);
    }

    @Override
    public Estados getEstadoNoSolucionado() throws ParseException {
        return mGeneralDAO.getEstadoNoSolucionado();
    }

    @Override
    public Personas getPersona(String email) throws ParseException {
        return mGeneralDAO.getPersona(email);
    }

    @Override
    public List<Comentarios> getComentarios(List<ParseObject> listComentarios, ParseObject object ) {
        return mGeneralDAO.getComentarios(listComentarios, object);
    }

    @Override
    public ParseObject agregarComentario(String comentario, String email, Context context) throws ParseException {
        return mGeneralDAO.agregarComentario(comentario, email, context);
    }

    @Override
    public ParseObject getComentarioById(String objectId) throws ParseException {
        return mGeneralDAO.getComentarioById(objectId);
    }

    @Override
    public boolean internet(Context context) {
        return mGeneralDAO.internet(context);
    }


    @Override
    public void save(ParseObject object) {

    }

    @Override
    public void delete(ParseObject object) {

    }

    @Override
    public void checkInternetGet(ParseQuery<ParseObject> query) {

    }

    public int getUltimoComentarioInsertado() throws ParseException {
        return mGeneralDAO.getUltimoComentarioInsertado();
    }


    public String getUltimoObjectId() throws ParseException {
        return mGeneralDAO.getUltimoObjectId();
    }


    @Override
    public void cargarDBLocal() throws ParseException {

    }
}