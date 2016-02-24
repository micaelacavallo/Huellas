package com.example.micaela.db.Modelo;

import android.net.Uri;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Quimey on 13/09/2015.
 */
@ParseClassName("Perdidos")
public class Perdidos extends ParseObject {

    private int mIdPerdido;
    private Edades mEdad;
    private Razas mRaza;
    private Especies mEspecie;
    private Tamaños mTamaño;
    private Colores mColor;
    private Sexos mSexo;
    private Estados mEstado;
    private Personas mPersona; //persona que publica
    private Date mFecha;
    private ParseGeoPoint mUbicacion;
    private String mTitulo;
    private String mDescripcion;
    private byte[] mFoto;
    private List<Comentarios> mComentarios;
    private boolean mSolucionado;
    private String mObjectId;


    public Perdidos() {
        mEdad = new Edades();
        mRaza = new Razas();
        mEspecie = new Especies();
        mTamaño = new Tamaños();
        mColor = new Colores();
        mSexo = new Sexos();
        mPersona = new Personas();
        mEstado = new Estados();
       // mFotos = new ParseFile();
        mComentarios = new ArrayList<Comentarios>();
    }

    public Perdidos(int idPerdido, Edades edad, Razas raza, Especies especie, Tamaños tamaño, Colores color, Sexos sexo, Estados estado, Personas persona, Date fecha, ParseGeoPoint ubicacion, String titulo, String descripcion, byte[] foto, List<Comentarios> comentarios, boolean mSolucionado) {
        this.mIdPerdido = idPerdido;
        this.mEdad = edad;
        this.mRaza = raza;
        this.mEspecie = especie;
        this.mTamaño = tamaño;
        this.mColor = color;
        this.mSexo = sexo;
        this.mEstado = estado;
        this.mPersona = persona;
        this.mFecha = fecha;
        this.mUbicacion = ubicacion;
        this.mTitulo = titulo;
        this.mDescripcion = descripcion;
        this.mFoto = foto;
        this.mComentarios = comentarios;
        this.mSolucionado = mSolucionado;
    }


    public int getIdPerdido() {
        return mIdPerdido;
    }

    public void setIdPerdido(int mIdPerdido) {
        this.mIdPerdido = mIdPerdido;
    }

    public Edades getEdad() {
        return mEdad;
    }

    public void setEdad(Edades mEdad) {
        this.mEdad = mEdad;
    }

    public Razas getRaza() {
        return mRaza;
    }

    public void setRaza(Razas mRaza) {
        this.mRaza = mRaza;
    }

    public Especies getEspecie() {
        return mEspecie;
    }

    public void setEspecie(Especies mEspecie) {
        this.mEspecie = mEspecie;
    }

    public Tamaños getTamaño() {
        return mTamaño;
    }

    public void setTamaño(Tamaños mTamaño) {
        this.mTamaño = mTamaño;
    }

    public Colores getColor() {
        return mColor;
    }

    public void setColor(Colores mColor) {
        this.mColor = mColor;
    }

    public Sexos getSexo() {
        return mSexo;
    }

    public void setSexo(Sexos mSexo) {
        this.mSexo = mSexo;
    }

    public Estados getEstado() {
        return mEstado;
    }

    public void setEstado(Estados mEstado) {
        this.mEstado = mEstado;
    }

    public Personas getPersona() {
        return mPersona;
    }

    public void setPersona(Personas mPersona) {
        this.mPersona = mPersona;
    }

    public Date getFecha() {
        return mFecha;
    }

    public void setFecha(Date mFecha) {
        this.mFecha = mFecha;
    }

    public ParseGeoPoint getUbicacion() {
        return mUbicacion;
    }

    public void setUbicacion(ParseGeoPoint mUbicacion) {
        this.mUbicacion = mUbicacion;
    }

    public String getTitulo() {
        return mTitulo;
    }

    public void setTitulo(String mTitulo) {
        this.mTitulo = mTitulo;
    }

    public String getDescripcion() {
        return mDescripcion;
    }

    public void setDescripcion(String mDescripcion) {
        this.mDescripcion = mDescripcion;
    }

    public byte[] getFoto() {
        return mFoto;
    }

    public void setFoto(byte[] mFoto) {
        this.mFoto = mFoto;
    }

    public List<Comentarios> getComentarios() {
        return mComentarios;
    }

    public void setComentarios(List<Comentarios> mComentarios) {
        this.mComentarios = mComentarios;
    }

    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String mObjectId) {
        this.mObjectId = mObjectId;
    }

    public boolean isSolucionado() {
        return mSolucionado;
    }

    public void setSolucionado(boolean solucionado) {
        mSolucionado = solucionado;
    }

}
