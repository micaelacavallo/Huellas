package com.example.micaela.db.Controladores;

import android.content.Context;

import com.example.micaela.db.DAO.DenunciasDAO;
import com.example.micaela.db.Interfaces.IDenuncias;
import com.example.micaela.db.Modelo.Denuncias;
import com.example.micaela.db.Modelo.MotivoDenuncia;
import com.parse.ParseException;

import java.util.List;

/**
 * Created by quimey.arozarena on 5/2/2016.
 */
public class IDenunciasImpl implements IDenuncias {

    private DenunciasDAO mDenunciasDAO;

    public IDenunciasImpl(){}

    public IDenunciasImpl(Context context){

        mDenunciasDAO = new DenunciasDAO(context);
    }

    @Override
    public void denunciar(String id, String motivo, String tabla) throws ParseException {

        mDenunciasDAO.denunciar(id, motivo, tabla);
    }

    @Override
    public MotivoDenuncia getMotivoDenuncia(String motivo) {
        return mDenunciasDAO.getMotivoDenuncia(motivo);
    }

    @Override
    public List<MotivoDenuncia> getMotivoDenuncias() {

        return mDenunciasDAO.getMotivoDenuncias();
    }

    @Override
    public void borrarDenuncia(String denunciaObjectId) {
        mDenunciasDAO.borrarDenuncia(denunciaObjectId);
    }

    @Override
    public void confirmarDenuncia(String denunciaObjectId) throws ParseException {
        mDenunciasDAO.confirmarDenuncia(denunciaObjectId);
    }

    @Override
    public Denuncias getDenunciaById(String objectId) {
        return  mDenunciasDAO.getDenunciaById(objectId);
    }

    @Override
    public List<Denuncias> getDenuncias() throws ParseException {
        return mDenunciasDAO.getDenuncias();
    }

}
