package com.example.micaela.db.Modelo;

import android.net.Uri;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Quimey on 13/09/2015.
 */
@ParseClassName("Adicionales")
public class Adicionales extends ParseObject {

    private int mIdAdicional;
    private String objectId;
    private Personas mPersona;
    private Estados mEstado;
    private Date mFecha;
    private String mTitulo;
    private String mDescripcion;
    private Uri mFoto;
    private boolean mDonacion;
    private List<Comentarios> mComentarios;

    public Adicionales() {
        mPersona = new Personas();
        mEstado = new Estados();
        mFoto = null;
        mComentarios = new ArrayList<Comentarios>();
    }
    public Adicionales(int idAdicional, Personas persona, Estados estado, Date fecha, String titulo, String descripcion, Uri foto, boolean donacion, List<Comentarios> comentarios) {
        this.mIdAdicional = idAdicional;
        this.mPersona = persona;
        this.mEstado = estado;
        this.mFecha = fecha;
        this.mTitulo = titulo;
        this.mDescripcion = descripcion;
        this.mFoto = foto;
        this.mDonacion = donacion;
        this.mComentarios = comentarios;
    }

    public int getIdAdicional() {
        return mIdAdicional;
    }

    public void setIdAdicional(int idAdicional) {
        this.mIdAdicional = idAdicional;
    }

    public Personas getPersona() {
        return mPersona;
    }

    public void setPersona(Personas persona) {
        this.mPersona = persona;
    }

    public Estados getEstado() {
        return mEstado;
    }

    public void setEstado(Estados estado) {
        this.mEstado = estado;
    }

    public Date getFecha() {
        return mFecha;
    }

    public void setFecha(Date fecha) {
        this.mFecha = fecha;
    }

    public String getTitulo() {
        return mTitulo;
    }

    public void setTitulo(String titulo) {
        this.mTitulo = titulo;
    }

    public String getDescripcion() {
        return mDescripcion;
    }

    public void setDescripcion(String descripcion) {
        this.mDescripcion = descripcion;
    }

    public Uri getFoto() {
        return mFoto;
    }

    public void setFoto(Uri foto) {
        this.mFoto = foto;
    }

    public List<Comentarios> getComentarios() {
        return mComentarios;
    }

    public void setComentarios(List<Comentarios> comentarios) {
        this.mComentarios = comentarios;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public boolean isDonacion() {
        return mDonacion;
    }

    public void setDonacion(boolean mDonacion) {
        this.mDonacion = mDonacion;
    }
}
