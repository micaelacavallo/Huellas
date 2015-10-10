package com.example.micaela.Interfaces;

import com.example.micaela.clases.Estados;
import com.example.micaela.clases.Personas;
import com.parse.ParseException;

/**
 * Created by Horacio on 10/10/2015.
 */
public interface IGeneral {

    public Estados getEstado(String situacion) throws ParseException;
    public Personas getPersona(String email) throws ParseException;
}
