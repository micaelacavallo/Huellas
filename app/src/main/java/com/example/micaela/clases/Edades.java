package com.example.micaela.clases;

/**
 * Created by Quimey on 13/09/2015.
 */
public class Edades {

    private int mIdEdad;
    private String mEdad;
    private String mObjectId;


    public Edades() {
    }

    public Edades(int idEdad, String edad, String objectId) {
        this.mIdEdad = idEdad;
        this.mEdad = edad;
        this.mObjectId = objectId;
    }

    public int getIdEdad() {
        return mIdEdad;
    }

    public void setIdEdad(int idEdad) {
        this.mIdEdad = idEdad;
    }

    public String getEdad() {
        return mEdad;
    }

    public void setEdad(String edad) {
        this.mEdad = edad;
    }

    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String mObjectId) {
        this.mObjectId = mObjectId;
    }
}
