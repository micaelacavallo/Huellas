package com.managerapp.db.Interfaces;

import com.managerapp.db.Modelo.Denuncias;
import com.managerapp.db.Modelo.MotivoDenuncia;
import com.parse.ParseException;

import java.util.List;


/**
 * Created by Quimey on 19/09/2015.
 */
public interface IDenuncias {

    public void denunciar(String id, String motivo, String tabla) throws ParseException;
    public MotivoDenuncia getMotivoDenuncia(String motivo); //admin
    public List<MotivoDenuncia> getMotivoDenuncias(); //admin
    public void borrarDenuncia(String denunciaObjectId) throws ParseException; //admin
    public void confirmarDenuncia(String denunciaObjectId) throws ParseException; //admin
    public Denuncias getDenunciaById(String objectId); //admin
    public Denuncias getDenunciaByIdRef(String refObjectId);
    public List<Denuncias> getDenuncias() throws ParseException; //admin
}
