package com.managerapp.db.Interfaces;

import com.managerapp.db.Modelo.Comentarios;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Quimey on 19/09/2015.
 */
public interface IComentarios {

    public ArrayList<Comentarios> getComentarios(List<ParseObject> listComentarios, ParseObject object);
    //public ParseObject agregarComentario(String publicacionObjectId, String comentario, String email, Context context) throws ParseException;
    public ParseObject getComentarioById(String objectId) throws ParseException;
    public List<Comentarios> getComentariosNoLeidos(String userObjectId);
    public void cambiarLeidoComentario(String comentarioObjectId, boolean leido);
    public void borrarComentario(String objectId);
    public List<Comentarios> getComentariosByPersonaObjectId(String objectId);
    public void bloquearComentario(String objectId);

}
