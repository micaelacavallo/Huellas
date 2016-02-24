package com.example.micaela.db.Modelo;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Quimey on 13/09/2015.
 */
@ParseClassName("Razas")
public class Razas extends ParseObject {

    private int mIdRaza;
    private String mRaza;
    private String mObjectId;


    public Razas() {
    }

    public Razas(String raza) {
        this.mRaza = raza;
    }

    public Razas(int idRaza, String raza, String objectId) {
        this.mIdRaza = idRaza;
        this.mRaza = raza;
        this.mObjectId = objectId;
    }

    public int getIdRaza() {
        return mIdRaza;
    }

    public void setIdRaza(int idRaza) {
        this.mIdRaza = idRaza;
    }

    public String getRaza() {
        return mRaza;
    }

    public void setRaza(String raza) {
        this.mRaza = raza;
    }

    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String mObjectId) {
        this.mObjectId = mObjectId;
    }
}
