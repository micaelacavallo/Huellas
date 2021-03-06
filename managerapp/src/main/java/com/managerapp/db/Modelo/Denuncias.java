package com.managerapp.db.Modelo;

import java.util.Date;

/**
 * Created by quimeyarozarena on 3/5/16.
 */
public class Denuncias {

    private String mObjectId;
    private boolean mUser;
    private Date mFecha;
    private String mId;
    private String mTabla;
    private int mContador;
    private MotivoDenuncia MmotivoDenuncia;

    public Denuncias() {
    }

    public Denuncias(String mObjectId, boolean mUser, Date mFecha, String mId, MotivoDenuncia mmotivoDenuncia, String tabla, int contador) {
        this.mObjectId = mObjectId;
        this.mUser = mUser;
        this.mFecha = mFecha;
        this.mId = mId;
        MmotivoDenuncia = mmotivoDenuncia;
        this.mContador = contador;
        this.setmTabla(tabla);
    }

    public String getmObjectId() {
        return mObjectId;
    }

    public void setmObjectId(String mObjectId) {
        this.mObjectId = mObjectId;
    }

    public boolean ismUser() {
        return mUser;
    }

    public void setmUser(boolean mUser) {
        this.mUser = mUser;
    }

    public Date getmFecha() {
        return mFecha;
    }

    public void setmFecha(Date mFecha) {
        this.mFecha = mFecha;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public MotivoDenuncia getMmotivoDenuncia() {
        return MmotivoDenuncia;
    }

    public void setMmotivoDenuncia(MotivoDenuncia mmotivoDenuncia) {
        MmotivoDenuncia = mmotivoDenuncia;
    }

    public String getmTabla() {
        return mTabla;
    }

    public void setmTabla(String mTabla) {
        this.mTabla = mTabla;
    }

    public int getContador() {
        return mContador;
    }

    public void setContador(int mContador) {
        this.mContador = mContador;
    }
}
