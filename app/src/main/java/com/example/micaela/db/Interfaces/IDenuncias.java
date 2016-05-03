package com.example.micaela.db.Interfaces;

import android.content.Context;

import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.db.Modelo.MotivoDenuncia;
import com.parse.ParseException;

import java.util.List;


/**
 * Created by Quimey on 19/09/2015.
 */
public interface IDenuncias {

    public void denunciar(String id, String motivo) throws ParseException;
    public MotivoDenuncia getMotivoDenuncia(String motivo);
    public List<MotivoDenuncia> getMotivoDenuncias();
    public void borrarDenuncia(String denunciaObjectId);
}
