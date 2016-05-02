package com.example.micaela.db.Interfaces;

import android.content.Context;

import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.db.Modelo.Estados;
import com.parse.ParseException;

import java.util.List;


/**
 * Created by Quimey on 19/09/2015.
 */
public interface IEstados {

    public Estados getEstado(String situacion) throws ParseException;
    public List<Estados> getEstados ();
    public void cambiarEstado(String idpublicacion, boolean estado);


}
