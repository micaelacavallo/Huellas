package com.example.micaela.db.Modelo;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Quimey on 13/09/2015.
 */
@ParseClassName("Colores")
public class Colores extends ParseObject {

    private int mIdColor;
    private String mColor;
    private String mObjectId;


    public Colores() {
    }

    public Colores(String color) {
        this.mColor = color;
    }

    public Colores(int idColor, String color, String objectId) {
        this.mIdColor = idColor;
        this.mColor = color;
        this.mObjectId = objectId;
    }

    public int getIdColor() {
        return mIdColor;
    }

    public void setIdColor(int idColor) {
        this.mIdColor = idColor;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        this.mColor = color;
    }

    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String mObjectId) {
        this.mObjectId = mObjectId;
    }
}
