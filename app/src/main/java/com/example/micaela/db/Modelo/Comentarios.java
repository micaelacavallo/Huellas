package com.example.micaela.db.Modelo;

import java.util.Date;

/**
 * Created by Quimey on 13/09/2015.
 */
public class Comentarios {

    private int mIdComentario;
    private String mComentario;
    private Personas mPersona;
    private Date mFecha;
    private String mObjectId;

    public Comentarios() {
    }

    public Comentarios(String objectId, int idComentario, String comentario, Personas persona, Date fecha) {
        this.mIdComentario = idComentario;
        this.mComentario = comentario;
        this.mPersona = persona;
        this.mFecha = fecha;
        this.mObjectId = objectId;
    }

    public int getIdComentario() {
        return mIdComentario;
    }

    public void setIdComentario(int idComentario) {
        this.mIdComentario = idComentario;
    }

    public String getComentario() {
        return mComentario;
    }

    public void setComentario(String comentario) {
        this.mComentario = comentario;
    }

    public Personas getPersona() {
        return mPersona;
    }

    public void setPersona(Personas persona) {
        this.mPersona = persona;
    }

    public Date getFecha() {
        return mFecha;
    }

    public void setFecha(Date fecha) {
        this.mFecha = fecha;
    }

    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String mObjectId) {
        this.mObjectId = mObjectId;
    }
}
