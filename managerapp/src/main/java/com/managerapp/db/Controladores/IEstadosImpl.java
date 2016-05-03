package com.managerapp.db.Controladores;

import android.content.Context;

import com.managerapp.db.DAO.EstadosDAO;
import com.managerapp.db.Interfaces.IEstados;
import com.managerapp.db.Modelo.Estados;
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
