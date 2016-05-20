package com.managerapp.db.Controladores;

import android.content.Context;

import com.managerapp.db.DAO.AdminDAO;
import com.managerapp.db.Interfaces.IAdmin;
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

    @Override
    public void confirmarDenunciaPublicacion(String publicacionObjectId, String tabla) throws com.parse.ParseException {
        mAdminDAO.confirmarDenunciaPublicacion(publicacionObjectId, tabla);
    }
}
