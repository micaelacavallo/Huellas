package com.example.micaela.Interfaces;

import com.example.micaela.clases.Adicionales;
import com.example.micaela.clases.Comentarios;
import com.example.micaela.clases.Estados;
import com.example.micaela.clases.Personas;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;


/**
 * Created by Quimey on 19/09/2015.
 */
public interface IAdicionales {

    public List<Adicionales> getAdicionales() throws ParseException;
    public Adicionales getAdicionalById(int idAdicional);
    public void saveAdicional(Adicionales adicional);
    public void editAdicional(Adicionales adicional);
    public int getUltimoInsertado() throws ParseException;
    public void deleteAdicional(String objectId) throws  ParseException;
    public void AgregarComentarioAdicional(String adicionalObjectId, String comentario, String email) throws ParseException;

}
