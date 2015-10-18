package com.example.micaela.clases;

/**
 * Created by Quimey on 13/09/2015.
 */
public class Tamaños {

    private int mIdTamaño;
    private String mTamaño;
    private String mObjectId;


    public Tamaños() {
    }

    public Tamaños(int idTamaño, String tamaño, String objectId) {
        this.mIdTamaño = idTamaño;
        this.mTamaño = tamaño;
        this.mObjectId = objectId;
    }

    public String getTamaño() {
        return mTamaño;
    }

    public void setTamaño(String tamaño) {
        this.mTamaño = tamaño;
    }

    public int getIdTamaño() {
        return mIdTamaño;
    }

    public void setIdTamaño(int idTamaño) {
        this.mIdTamaño = idTamaño;
    }

    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String mObjectId) {
        this.mObjectId = mObjectId;
    }
}
