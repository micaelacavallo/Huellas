package com.example.micaela.db.Controladores;

import android.content.Context;

import com.example.micaela.db.DAO.PersonasDAO;
import com.example.micaela.db.Interfaces.IPersonas;
import com.example.micaela.db.Modelo.MotivoDenuncia;
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
}
