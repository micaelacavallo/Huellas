package com.example.micaela.db.Modelo;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Quimey on 13/09/2015.
 */
public class Perdidos implements Parcelable {

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
    private double mLatitud;
    private double mLongitud;
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

    public Perdidos(int idPerdido, Edades edad, Razas raza, Especies especie, Tamaños tamaño, Colores color, Sexos sexo, Estados estado, Personas persona, Date fecha, double latitud, double logitud, String titulo, String descripcion, byte[] foto, List<Comentarios> comentarios, boolean mSolucionado) {
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
        this.mLatitud = latitud;
        this.mLongitud = logitud;
        this.mTitulo = titulo;
        this.mDescripcion = descripcion;
        this.mFoto = foto;
        this.mComentarios = comentarios;
        this.mSolucionado = mSolucionado;
    }

    public double getLongitud() {
        return mLongitud;
    }

    public void setLongitud(double longitud) {
        mLongitud = longitud;
    }

    public double getLatitud() {
        return mLatitud;
    }

    public void setLatitud(double latitud) {
        mLatitud = latitud;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mIdPerdido);
        dest.writeParcelable(this.mEdad, 0);
        dest.writeParcelable(this.mRaza, 0);
        dest.writeParcelable(this.mEspecie, 0);
        dest.writeParcelable(this.mTamaño, 0);
        dest.writeParcelable(this.mColor, 0);
        dest.writeParcelable(this.mSexo, 0);
        dest.writeParcelable(this.mEstado, 0);
        dest.writeParcelable(this.mPersona, 0);
        dest.writeLong(mFecha != null ? mFecha.getTime() : -1);
        dest.writeDouble(this.mLatitud);
        dest.writeDouble(this.mLongitud);
        dest.writeString(this.mTitulo);
        dest.writeString(this.mDescripcion);
        dest.writeByteArray(this.mFoto);
        dest.writeTypedList(mComentarios);
        dest.writeByte(mSolucionado ? (byte) 1 : (byte) 0);
        dest.writeString(this.mObjectId);
    }

    protected Perdidos(Parcel in) {
        this.mIdPerdido = in.readInt();
        this.mEdad = in.readParcelable(Edades.class.getClassLoader());
        this.mRaza = in.readParcelable(Razas.class.getClassLoader());
        this.mEspecie = in.readParcelable(Especies.class.getClassLoader());
        this.mTamaño = in.readParcelable(Tamaños.class.getClassLoader());
        this.mColor = in.readParcelable(Colores.class.getClassLoader());
        this.mSexo = in.readParcelable(Sexos.class.getClassLoader());
        this.mEstado = in.readParcelable(Estados.class.getClassLoader());
        this.mPersona = in.readParcelable(Personas.class.getClassLoader());
        long tmpMFecha = in.readLong();
        this.mFecha = tmpMFecha == -1 ? null : new Date(tmpMFecha);
        this.mLatitud = in.readDouble();
        this.mLongitud = in.readDouble();
        this.mTitulo = in.readString();
        this.mDescripcion = in.readString();
        this.mFoto = in.createByteArray();
        this.mComentarios = in.createTypedArrayList(Comentarios.CREATOR);
        this.mSolucionado = in.readByte() != 0;
        this.mObjectId = in.readString();
    }

    public static final Parcelable.Creator<Perdidos> CREATOR = new Parcelable.Creator<Perdidos>() {
        public Perdidos createFromParcel(Parcel source) {
            return new Perdidos(source);
        }

        public Perdidos[] newArray(int size) {
            return new Perdidos[size];
        }
    };
}
