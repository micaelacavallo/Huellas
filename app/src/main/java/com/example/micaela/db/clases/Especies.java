package com.example.micaela.db.clases;

/**
 * Created by Quimey on 13/09/2015.
 */
public class Especies {

    private int mIdEspecie;
    private String mEspecie;
    private String mObjectId;


    public Especies() {
    }

    public Especies(int idEspecie, String especie, String objectId) {
        this.mIdEspecie = idEspecie;
        this.mEspecie = especie;
        this.mObjectId = objectId;
    }

    public int getIdEspecie() {
        return mIdEspecie;
    }

    public void setIdEspecie(int idEspecie) {
        this.mIdEspecie = idEspecie;
    }

    public String getEspecie() {
        return mEspecie;
    }

    public void setEspecie(String especie) {
        this.mEspecie = especie;
    }

    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String mObjectId) {
        this.mObjectId = mObjectId;
    }
}
