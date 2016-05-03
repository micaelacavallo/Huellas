package com.example.micaela.db.Controladores;

import android.content.Context;

import com.example.micaela.db.DAO.GeneralDAO;
import com.example.micaela.db.Interfaces.IGeneral;
import com.example.micaela.db.Modelo.Comentarios;
import com.example.micaela.db.Modelo.Estados;
import com.example.micaela.db.Modelo.MotivoDenuncia;
import com.example.micaela.db.Modelo.Personas;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Horacio on 10/10/2015.
 */
public class IGeneralImpl implements IGeneral {

    private GeneralDAO mGeneralDAO;

    public IGeneralImpl(){}

    public IGeneralImpl(Context context){

        mGeneralDAO = new GeneralDAO(context);
    }

    @Override
    public boolean internet(Context context) {
        mGeneralDAO = new GeneralDAO(context);
        return mGeneralDAO.internet(context);
    }


    @Override
    public void save(ParseObject object) {
        mGeneralDAO.save(object);

    }

    @Override
    public void delete(ParseObject object) {
        mGeneralDAO.delete(object);
    }

    @Override
    public void startAlert() {
        mGeneralDAO.startAlert();
    }

    @Override
    public String getUltimoObjectId(String clase) {
        return mGeneralDAO.getUltimoObjectId(clase);
    }

    @Override
    public void checkInternetGet(ParseQuery<ParseObject> query) {
        mGeneralDAO.checkInternetGet(query);
    }

}