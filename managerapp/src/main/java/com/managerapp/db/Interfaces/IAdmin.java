package com.managerapp.db.Interfaces;

import com.parse.ParseException;
import com.parse.ParseObject;

/**
 * Created by Micaela on 17/05/2016.
 */
public interface IAdmin {

    public int login(String nombre, String contraseña);
    public void bloquearPersona(String objectId) throws ParseException, java.text.ParseException;
    public void save(ParseObject object);
    public void confirmarDenunciaPublicacion(String publicacionObjectId, String tabla) throws ParseException;

}
