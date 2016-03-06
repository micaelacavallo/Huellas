package com.example.micaela.db.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Quimey on 13/09/2015.
 */
public class Adicionales implements Parcelable {

    private String objectId;
    private Personas mPersona;
    private Estados mEstado;
    private Date mFecha;
    private String mTitulo;
    private String mDescripcion;
    private byte[] mFoto;
    private boolean mDonacion;
    private List<Comentarios> mComentarios;

    public Adicionales() {
        mPersona = new Personas();
        mEstado = new Estados();
        mFoto = null;
        mComentarios = new ArrayList<Comentarios>();
    }
    public Adicionales(Personas persona, Estados estado, Date fecha, String titulo, String descripcion, byte[] foto, boolean donacion, List<Comentarios> comentarios) {
        this.mPersona = persona;
        this.mEstado = estado;
        this.mFecha = fecha;
        this.mTitulo = titulo;
        this.mDescripcion = descripcion;
        this.mFoto = foto;
        this.mDonacion = donacion;
        this.mComentarios = comentarios;
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

    public byte[] getFoto() {
        return mFoto;
    }

    public void setFoto(byte[] foto) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.objectId);
        dest.writeParcelable(this.mPersona, 0);
        dest.writeParcelable(this.mEstado, 0);
        dest.writeLong(mFecha != null ? mFecha.getTime() : -1);
        dest.writeString(this.mTitulo);
        dest.writeString(this.mDescripcion);
        dest.writeByteArray(this.mFoto);
        dest.writeByte(mDonacion ? (byte) 1 : (byte) 0);
        dest.writeTypedList(mComentarios);
    }

    protected Adicionales(Parcel in) {
        this.objectId = in.readString();
        this.mPersona = in.readParcelable(Personas.class.getClassLoader());
        this.mEstado = in.readParcelable(Estados.class.getClassLoader());
        long tmpMFecha = in.readLong();
        this.mFecha = tmpMFecha == -1 ? null : new Date(tmpMFecha);
        this.mTitulo = in.readString();
        this.mDescripcion = in.readString();
        this.mFoto = in.createByteArray();
        this.mDonacion = in.readByte() != 0;
        this.mComentarios = in.createTypedArrayList(Comentarios.CREATOR);
    }

    public static final Parcelable.Creator<Adicionales> CREATOR = new Parcelable.Creator<Adicionales>() {
        public Adicionales createFromParcel(Parcel source) {
            return new Adicionales(source);
        }

        public Adicionales[] newArray(int size) {
            return new Adicionales[size];
        }
    };
}
