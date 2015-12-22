package com.example.micaela.db.Interfaces;

import android.content.Context;

import com.example.micaela.db.Modelo.Adicionales;
import com.parse.ParseException;

import java.util.List;


/**
 * Created by Quimey on 19/09/2015.
 */
public interface IAdicionales {

    public List<Adicionales> getAdicionales() throws ParseException;
    public Adicionales getAdicionalById(int idAdicional);
    public void saveAdicional(Adicionales adicional);
    public void editAdicional(Adicionales adicional) throws ParseException;
    public int getUltimoInsertado() throws ParseException;
    public void deleteAdicional(String objectId) throws  ParseException;
    public void AgregarComentarioAdicional(String adicionalObjectId, String comentario, String email) throws ParseException;
    public void cargarDBLocal(Context context) throws ParseException;


}
