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
    public MotivoDenuncia getMotivoDenuncia(String motivo);
    public List<MotivoDenuncia> getMotivoDenuncias();
    public void borrarDenuncia(String denunciaObjectId);
    public void confirmarDenuncia(String denunciaObjectId) throws ParseException;
    public Denuncias getDenunciaById(String objectId);
    public List<Denuncias> getDenuncias() throws ParseException;
}

