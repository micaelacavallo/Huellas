package com.example.micaela.db.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Quimey on 13/09/2015.
 */
public class Personas implements Parcelable {

    private String email;
    private String nombre;
    private String apellido;
    private String telefono;
    private boolean administrador;
    private String objectId;
    private boolean mBloqueado;

    public Personas() {
    }

    public Personas(String email) {
        this.email = email;
    }

    public Personas(String objectId, String email, String nombre, String apellido, String telefono, boolean administrador, boolean bloqueado) {
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.administrador = administrador;
        this.objectId = objectId;
        this.mBloqueado = bloqueado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isAdministrador() {
        return administrador;
    }

    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.nombre);
        dest.writeString(this.apellido);
        dest.writeString(this.telefono);
        dest.writeByte(administrador ? (byte) 1 : (byte) 0);
        dest.writeString(this.objectId);
    }

    protected Personas(Parcel in) {
        this.email = in.readString();
        this.nombre = in.readString();
        this.apellido = in.readString();
        this.telefono = in.readString();
        this.administrador = in.readByte() != 0;
        this.objectId = in.readString();
    }

    public static final Parcelable.Creator<Personas> CREATOR = new Parcelable.Creator<Personas>() {
        public Personas createFromParcel(Parcel source) {
            return new Personas(source);
        }

        public Personas[] newArray(int size) {
            return new Personas[size];
        }
    };

    public boolean isBloqueado() {
        return mBloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.mBloqueado = bloqueado;
    }
}
