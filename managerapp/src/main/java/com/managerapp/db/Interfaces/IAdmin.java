package com.managerapp.db.Interfaces;

import com.parse.ParseObject;

/**
 * Created by Micaela on 17/05/2016.
 */
public interface IAdmin {

    public boolean login(String nombre, String contraseña);
    public void bloquearPersona(String objectId) throws java.text.ParseException;
    public void save(ParseObject object);

}
