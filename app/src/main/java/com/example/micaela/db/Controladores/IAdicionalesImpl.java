package com.example.micaela.db.Controladores;

import android.content.Context;

import com.example.micaela.db.DAO.AdicionalesDAO;
import com.example.micaela.db.Interfaces.IAdicionales;
import com.example.micaela.db.Modelo.Adicionales;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;


/**
 * Created by Quimey on 19/09/2015.
 */
public class IAdicionalesImpl implements IAdicionales {

    private AdicionalesDAO mAdicionalesDAO;

    public IAdicionalesImpl(Context context) {
        mAdicionalesDAO = new AdicionalesDAO(context);
    }

    @Override
    public List<Adicionales> getDonaciones() throws ParseException {
        return mAdicionalesDAO.getDonaciones();
    }

    @Override
    public List<Adicionales> getInfoUtil() throws ParseException {
        return mAdicionalesDAO.getInfoUtil();
    }

    @Override
    public Adicionales getAdicionalById(String idAdicional) {
        return mAdicionalesDAO.getAdicionalById(idAdicional);
    }

    @Override
    public String saveAdicional(Adicionales adicional) throws ParseException {
        return mAdicionalesDAO.saveAdicional(adicional);
    }


    public ParseObject cargarAdicional(Adicionales adicional) {
        return mAdicionalesDAO.cargarAdicional(adicional);
    }

    @Override
    public void editAdicional(Adicionales adicional) throws ParseException {
        mAdicionalesDAO.editAdicional(adicional);
    }


    @Override
    public void deleteAdicional(String objectId) throws ParseException {
        mAdicionalesDAO.deleteAdicional(objectId);
    }

    @Override
    public void AgregarComentarioAdicional(String adicionalObjectId, String comentarioText, String email) throws ParseException {
        mAdicionalesDAO.AgregarComentarioAdicional(adicionalObjectId, comentarioText, email);
    }

    @Override
    public void cargarDBLocalDonaciones(Context context) throws ParseException {
        mAdicionalesDAO.cargarDBLocalDonaciones(context);
    }

    @Override
    public void cargarDBLocalInfoUtil(Context context) throws ParseException {
        mAdicionalesDAO.cargarDBLocalInfoUtil(context);
    }

    @Override
    public List<Adicionales> getPublicacionesAdicionalesPropias(String objectId) {
        return mAdicionalesDAO.getPublicacionesAdicionalesPropias(objectId);
    }

    @Override
    public ParseObject getParseObjectById(String objectId){
        return getParseObjectById(objectId);
    }

    @Override
    public void bloquearAdicional(String objectId) {
        mAdicionalesDAO.bloquearAdicional(objectId);
    }
}
