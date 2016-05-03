package com.managerapp.db.Controladores;

import android.content.Context;

import com.managerapp.db.DAO.GeneralDAO;
import com.managerapp.db.Interfaces.IGeneral;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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