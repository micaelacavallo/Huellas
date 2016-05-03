package com.managerapp.db.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Quimey on 13/09/2015.
 */
public class Razas implements Parcelable{

    private String mRaza;
    private String mObjectId;
    private Especies mEspecie;


    public Razas() {
    }

    public Razas(String raza) {
        this.mRaza = raza;
    }

    public Razas(String raza, String objectId, Especies especie) {
        this.mRaza = raza;
        this.mObjectId = objectId;
        this.mEspecie = especie;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mObjectId);
        dest.writeString(this.mRaza);
    }

    protected Razas(Parcel in) {
        this.mObjectId = in.readString();
        this.mRaza = in.readString();
    }

    public static final Creator<Razas> CREATOR = new Creator<Razas>() {
        public Razas createFromParcel(Parcel source) {
            return new Razas(source);
        }

        public Razas[] newArray(int size) {
            return new Razas[size];
        }
    };

    public String getmRaza() {
        return mRaza;
    }

    public void setmRaza(String mRaza) {
        this.mRaza = mRaza;
    }

    public String getmObjectId() {
        return mObjectId;
    }

    public void setmObjectId(String mObjectId) {
        this.mObjectId = mObjectId;
    }

    public Especies getmEspecie() {
        return mEspecie;
    }

    public void setmEspecie(Especies mEspecie) {
        this.mEspecie = mEspecie;
    }
}
