package com.example.micaela.db.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Quimey on 13/09/2015.
 */
public class Comentarios implements Parcelable {

    private String mComentario;
    private Personas mPersona;
    private Date mFecha;
    private String mObjectId;

    public Comentarios() {
    }

    public Comentarios(String objectId, String comentario, Personas persona, Date fecha) {
        this.mComentario = comentario;
        this.mPersona = persona;
        this.mFecha = fecha;
        this.mObjectId = objectId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mComentario);
        dest.writeParcelable(this.mPersona, 0);
        dest.writeLong(mFecha != null ? mFecha.getTime() : -1);
        dest.writeString(this.mObjectId);
    }

    protected Comentarios(Parcel in) {
        this.mComentario = in.readString();
        this.mPersona = in.readParcelable(Personas.class.getClassLoader());
        long tmpMFecha = in.readLong();
        this.mFecha = tmpMFecha == -1 ? null : new Date(tmpMFecha);
        this.mObjectId = in.readString();
    }

    public static final Parcelable.Creator<Comentarios> CREATOR = new Parcelable.Creator<Comentarios>() {
        public Comentarios createFromParcel(Parcel source) {
            return new Comentarios(source);
        }

        public Comentarios[] newArray(int size) {
            return new Comentarios[size];
        }
    };
}
