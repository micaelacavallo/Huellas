package com.example.micaela.db.Interfaces;

import android.content.Context;

import com.example.micaela.db.Modelo.Adicionales;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.Date;
import java.util.List;


/**
 * Created by Quimey on 19/09/2015.
 */
public interface IAdicionales {

    public List<Adicionales> getDonaciones() throws ParseException;
    public List<Adicionales> getInfoUtil() throws ParseException;
    public Adicionales getAdicionalById(String idAdicional);
    public void saveAdicional(Adicionales adicional) throws ParseException;
    public String getInsertedID(Date date) throws ParseException;
    public void editAdicional(Adicionales adicional) throws ParseException;
    public void deleteAdicional(String objectId) throws  ParseException;
    public void AgregarComentarioAdicional(String adicionalObjectId, String comentario, String email) throws ParseException;
    public void cargarDBLocalDonaciones(Context context) throws ParseException;
    public void cargarDBLocalInfoUtil(Context context) throws ParseException;
    public List<Adicionales> getPublicacionesAdicionalesPropias(String objectId);
    public ParseObject getParseObjectById(String objectId);
    public void bloquearAdicional(String objectId);
}
