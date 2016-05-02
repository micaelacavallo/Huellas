package com.example.micaela.db.Controladores;

import android.content.Context;

import com.example.micaela.db.DAO.EstadosDAO;
import com.example.micaela.db.DAO.GeneralDAO;
import com.example.micaela.db.Interfaces.IEstados;
import com.example.micaela.db.Modelo.Estados;
import com.parse.ParseException;

import java.util.List;

/**
 * Created by quimey.arozarena on 5/2/2016.
 */
public class IEstadosImpl implements IEstados {

    private EstadosDAO mEstadosDAO;

    public IEstadosImpl(){}

    public IEstadosImpl(Context context){

        mEstadosDAO = new EstadosDAO(context);
    }

    @Override
    public List<Estados> getEstados() {

        return mEstadosDAO.getEstados();
    }

    @Override
    public void cambiarEstado(String idpublicacion, boolean estado) {
        mEstadosDAO.cambiarEstado(idpublicacion, estado);
    }


    @Override
    public Estados getEstado(String situacion) throws ParseException {
        return mEstadosDAO.getEstado(situacion);
    }
}
