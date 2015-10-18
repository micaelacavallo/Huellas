package com.example.micaela.db.clases;

/**
 * Created by Quimey on 13/09/2015.
 */
public class Estados {

    private int mIdEstado;
    private boolean mSolucionado;
    private String mSituacion;
    private String mObjectId;

    public Estados(){

    }

    public Estados(String objectId, int idEstado, boolean solucionado, String situacion) {
        this.mIdEstado = idEstado;
        this.mSolucionado = solucionado;
        this.mSituacion = situacion;
        this.mObjectId = objectId;
    }

    public int getIdEstado() {
        return mIdEstado;
    }

    public void setIdEstado(int idEstado) {
        this.mIdEstado = idEstado;
    }

    public boolean isSolucionado() {
        return mSolucionado;
    }

    public void setSolucionado(boolean solucionado) {
        this.mSolucionado = solucionado;
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
