package com.example.micaela.db.Interfaces;

import android.content.Context;

import com.example.micaela.db.Constantes.CPersonas;
import com.example.micaela.db.Constantes.Clases;
import com.example.micaela.db.Modelo.Adicionales;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.List;


/**
 * Created by Quimey on 19/09/2015.
 */
public interface IAdmin {

    public boolean login(String nombre, String contraseña);
    public void bloquearPersona(String objectId) throws java.text.ParseException;
    public void save(ParseObject object);


}
