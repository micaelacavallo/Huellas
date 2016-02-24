package com.example.micaela.db.Modelo;

/**
 * Created by Quimey on 13/09/2015.
 */
public class Estados {

    private int mIdEstado;
    private String mSituacion;
    private String mObjectId;

    public Estados(){

    }

    public Estados(String situacion) {
        this.mSituacion = situacion;
    }

    public Estados(String objectId, int idEstado, String situacion) {
        this.mIdEstado = idEstado;
        this.mSituacion = situacion;
        this.mObjectId = objectId;
    }

    public int getIdEstado() {
        return mIdEstado;
    }

    public void setIdEstado(int idEstado) {
        this.mIdEstado = idEstado;
    }


    public String getSituacion() {
        return mSituacion;
    }

    public void setSituacion(String situacion) {
        this.mSituacion = situacion;
    }

    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String mObjectId) {
        this.mObjectId = mObjectId;
    }
}
