package com.managerapp.db.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Quimey on 13/09/2015.
 */

public class Especies implements Parcelable{

    private String mEspecie;
    private String mObjectId;


    public Especies() {
    }

    public Especies(String especie) {
        this.mEspecie = especie;
    }

    public Especies(String especie, String objectId) {
        this.mEspecie = especie;
        this.mObjectId = objectId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mEspecie);
        dest.writeString(this.mObjectId);
    }

    protected Especies(Parcel in) {
        this.mEspecie = in.readString();
        this.mObjectId = in.readString();
    }

    public static final Creator<Especies> CREATOR = new Creator<Especies>() {
        public Especies createFromParcel(Parcel source) {
            return new Especies(source);
        }

        public Especies[] newArray(int size) {
            return new Especies[size];
        }
    };
}
