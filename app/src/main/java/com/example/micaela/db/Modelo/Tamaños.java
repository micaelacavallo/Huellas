package com.example.micaela.db.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Quimey on 13/09/2015.
 */

public class Tamaños {

    private int mIdTamaño;
    private String mTamaño;
    private String mObjectId;


    public Tamaños() {
    }

    public Tamaños(String tamaño) {
        this.mTamaño = tamaño;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mObjectId);
        dest.writeString(this.mTamaño);
        dest.writeInt(this.mIdTamaño);
    }

    protected Tamaños(Parcel in) {
        this.mObjectId = in.readString();
        this.mTamaño = in.readString();
        this.mIdTamaño = in.readInt();
    }

    public static final Parcelable.Creator<Tamaños> CREATOR = new Parcelable.Creator<Tamaños>() {
        public Tamaños createFromParcel(Parcel source) {
            return new Tamaños(source);
        }

        public Tamaños[] newArray(int size) {
            return new Tamaños[size];
        }
    };
}
