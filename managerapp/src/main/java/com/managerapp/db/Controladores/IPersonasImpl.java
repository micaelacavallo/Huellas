package com.managerapp.db.Controladores;

import android.content.Context;

import com.managerapp.db.DAO.PersonasDAO;
import com.managerapp.db.Interfaces.IPersonas;
import com.managerapp.db.Modelo.MotivoDenuncia;
import com.managerapp.db.Modelo.Personas;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by quimeyarozarena on 3/5/16.
 */
public class IPersonasImpl implements IPersonas {

    private PersonasDAO mPersonasDAO;

    public IPersonasImpl(Context context) {
        mPersonasDAO = new PersonasDAO(context);
    }

    @Override
    public void denunciar(String id, String motivo) throws ParseException {
        mPersonasDAO.denunciar(id, motivo);
    }

    @Override
    public MotivoDenuncia getMotivoDenuncia(String motivo) {
        return mPersonasDAO.getMotivoDenuncia(motivo);
    }

    @Override
    public Personas getPersonabyEmail(String email) throws ParseException {
        return mPersonasDAO.getPersonabyEmail(email);
    }

    @Override
    public Personas getPersonabyId(String objectId) throws ParseException {
        return mPersonasDAO.getPersonabyId(objectId);
    }

    @Override
    public void editTelefono(String objectId, String telefono) {
        mPersonasDAO.editTelefono(objectId, telefono);
    }

    @Override
    public List<MotivoDenuncia> getMotivoDenuncias() {
        return getMotivoDenuncias();
    }

    @Override
    public void checkInternetGet(ParseQuery<ParseObject> query) {

    }

    @Override
    public boolean internet(Context context) {
        return false;
    }

    @Override
    public void registar(Personas personas) {
       mPersonasDAO.registar(personas);
    }

    @Override
    public List<Personas> getPersonas() {
        return mPersonasDAO.getPersonas();
    }

    @Override
    public ParseObject getParseObjectById(String objectId){
        return mPersonasDAO.getParseObjectById(objectId);
    }
}
