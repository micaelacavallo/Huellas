package com.example.micaela.db.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Quimey on 13/09/2015.
 */

public class Edades implements Parcelable{

    private int mIdEdad;
    private String mEdad;
    private String mObjectId;


    public Edades() {
    }

    public Edades(String edad) {
        this.mEdad = edad;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mIdEdad);
        dest.writeString(this.mEdad);
        dest.writeString(this.mObjectId);
    }

    protected Edades(Parcel in) {
        this.mIdEdad = in.readInt();
        this.mEdad = in.readString();
        this.mObjectId = in.readString();
    }

    public static final Parcelable.Creator<Edades> CREATOR = new Parcelable.Creator<Edades>() {
        public Edades createFromParcel(Parcel source) {
            return new Edades(source);
        }

        public Edades[] newArray(int size) {
            return new Edades[size];
        }
    };
}
