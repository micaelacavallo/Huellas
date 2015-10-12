package com.example.micaela.Interfaces;

import com.example.micaela.clases.Comentarios;
import com.example.micaela.clases.Estados;
import com.example.micaela.clases.Personas;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Horacio on 10/10/2015.
 */
public interface IGeneral {

    public Estados getEstado(String situacion) throws ParseException;
    public Personas getPersona(String email) throws ParseException;
    public List<Comentarios> getComentarios(List<ParseObject> listComentarios, ParseObject object);
    public ParseObject agregarComentario(String comentario, String email) throws ParseException;
    public ParseObject getComentarioById(String objectId) throws ParseException;
}
