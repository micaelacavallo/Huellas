package com.example.micaela.db.Controladores;

import android.content.Context;

import com.example.micaela.db.DAO.ComentariosDAO;
import com.example.micaela.db.Interfaces.IComentarios;
import com.example.micaela.db.Modelo.Comentarios;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quimey.arozarena on 5/2/2016.
 */
public class IComentariosImpl implements IComentarios{

    private ComentariosDAO mComentarioDAO;

    public IComentariosImpl(){}

    public IComentariosImpl(Context context){

        mComentarioDAO = new ComentariosDAO(context);
    }
    @Override
    public ArrayList<Comentarios> getComentarios(List<ParseObject> listComentarios, ParseObject object ) {
        return mComentarioDAO.getComentarios(listComentarios, object);
    }

    @Override
    public ParseObject agregarComentario(String comentario, String email, Context context) throws ParseException {
        return mComentarioDAO.agregarComentario(comentario, email, context);
    }

    @Override
    public ParseObject getComentarioById(String objectId) throws ParseException {
        return mComentarioDAO.getComentarioById(objectId);
    }


    @Override
    public List<Comentarios> getComentariosNoLeidos(String userObjectId) {
        return mComentarioDAO.getComentariosNoLeidos(userObjectId);
    }

    @Override
    public void cambiarLeidoComentario(String comentarioObjectId, boolean leido) {
        mComentarioDAO.cambiarLeidoComentario(comentarioObjectId, leido);
    }

    @Override
    public void borrarComentario(String objectId) {
        mComentarioDAO.borrarComentario(objectId);
    }
}
