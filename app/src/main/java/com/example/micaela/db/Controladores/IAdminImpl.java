package com.example.micaela.db.Controladores;

import android.content.Context;

import com.example.micaela.db.DAO.AdminDAO;
import com.example.micaela.db.Interfaces.IAdmin;
import com.parse.ParseObject;

import java.text.ParseException;

/**
 * Created by quimey.arozarena on 5/9/2016.
 */
public class IAdminImpl implements IAdmin {

    private AdminDAO mAdminDAO;

    public IAdminImpl(){}

    public IAdminImpl(Context context){

        mAdminDAO = new AdminDAO(context);
    }
    @Override
    public int login(String nombre, String contraseña) {
        return mAdminDAO.login(nombre, contraseña);
    }

    @Override
    public void bloquearPersona(String objectId) throws ParseException {
        mAdminDAO.bloquearPersona(objectId);
    }

    @Override
    public void save(ParseObject object) {
        mAdminDAO.save(object);
    }
}
