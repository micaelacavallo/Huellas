package com.example.micaela.db.Interfaces;

import android.content.Context;

import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.db.Modelo.Denuncias;
import com.example.micaela.db.Modelo.MotivoDenuncia;
import com.parse.ParseException;

import java.util.List;


/**
 * Created by Quimey on 19/09/2015.
 */
public interface IDenuncias {

    public void denunciar(String id, String motivo, String tabla) throws ParseException;
    public MotivoDenuncia getMotivoDenuncia(String motivo); //admin
    public List<MotivoDenuncia> getMotivoDenuncias(); //admin
    public void borrarDenuncia(String denunciaObjectId); //admin
    public void confirmarDenuncia(String denunciaObjectId) throws ParseException; //admin
    public Denuncias getDenunciaById(String objectId); //admin
    public List<Denuncias> getDenuncias() throws ParseException; //admin
}
