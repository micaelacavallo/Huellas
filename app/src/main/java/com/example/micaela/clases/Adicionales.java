package com.example.micaela.clases;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Quimey on 13/09/2015.
 */
public class Adicionales {

    private int mIdAdicional;
    private Personas mPersona;
    private Estados mEstado;
    private Date mFecha;
    private String mTitulo;
    private String mDescripcion;
    private List<String> mFotos;
    private List<Comentarios> mComentarios;

    public Adicionales() {
        mPersona = new Personas();
        mEstado = new Estados();
        mFotos = new ArrayList<String>();
        mComentarios = new ArrayList<Comentarios>();
    }

    public Adicionales(int idAdicional, Personas persona, Estados estado, Date fecha, String titulo, String descripcion, List<String> fotos, List<Comentarios> comentarios) {
        this.mIdAdicional = idAdicional;
        this.mPersona = persona;
        this.mEstado = estado;
        this.mFecha = fecha;
        this.mTitulo = titulo;
        this.mDescripcion = descripcion;
        this.mFotos = fotos;
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

    public List<String> getFotos() {
        return mFotos;
    }

    public void setFotos(List<String> fotos) {
        this.mFotos = fotos;
    }

    public List<Comentarios> getComentarios() {
        return mComentarios;
    }

    public void setComentarios(List<Comentarios> comentarios) {
        this.mComentarios = comentarios;
    }
}
