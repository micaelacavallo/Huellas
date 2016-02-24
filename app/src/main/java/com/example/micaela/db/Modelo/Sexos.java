package com.example.micaela.db.Modelo;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Quimey on 13/09/2015.
 */
@ParseClassName("Sexos")
public class Sexos extends ParseObject {

    private int mIdSexo;
    private String mSexo;
    private String mObjectId;


    public Sexos() {
    }

    public Sexos(String sexo) {
        this.mSexo = sexo;
    }

    public Sexos(int idSexo, String sexo, String objectId) {
        this.mIdSexo = idSexo;
        this.mSexo = sexo;
        this.mObjectId = objectId;
    }

    public int getIdSexo() {return mIdSexo;}

    public void setIdSexo(int idSexo) {
        this.mIdSexo = idSexo;
    }

    public String getSexo() {
        return mSexo;
    }

    public void setSexo(String sexo) {
        this.mSexo = sexo;
    }

    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String mObjectId) {
        this.mObjectId = mObjectId;
    }
}
