package com.managerapp.db.Modelo;

/**
 * Created by quimeyarozarena on 3/5/16.
 */
public class MotivoDenuncia {

    private String mObjectId;
    private String mMotivo;


    public MotivoDenuncia() {
    }

    public MotivoDenuncia(String mObjectId, String mMotivo) {
        this.mObjectId = mObjectId;
        this.mMotivo = mMotivo;
    }

    public String getmObjectId() {
        return mObjectId;
    }

    public void setmObjectId(String mObjectId) {
        this.mObjectId = mObjectId;
    }

    public String getmMotivo() {
        return mMotivo;
    }

    public void setmMotivo(String mMotivo) {
        this.mMotivo = mMotivo;
    }
}
