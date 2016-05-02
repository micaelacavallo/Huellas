package com.example.micaela.db.Controladores;

import android.content.Context;

import com.example.micaela.db.DAO.DenunciasDAO;
import com.example.micaela.db.Interfaces.IDenuncias;
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
    public void denunciar(String id, String motivo) throws ParseException {

        mDenunciasDAO.denunciar(id, motivo);
    }

    @Override
    public MotivoDenuncia getMotivoDenuncia(String motivo) {
        return mDenunciasDAO.getMotivoDenuncia(motivo);
    }

    @Override
    public List<MotivoDenuncia> getMotivoDenuncias() {

        return mDenunciasDAO.getMotivoDenuncias();
    }

}
