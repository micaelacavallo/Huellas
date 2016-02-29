package com.example.micaela.db.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Quimey on 13/09/2015.
 */
public class Razas {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mIdRaza);
        dest.writeString(this.mObjectId);
        dest.writeString(this.mRaza);
    }

    protected Razas(Parcel in) {
        this.mIdRaza = in.readInt();
        this.mObjectId = in.readString();
        this.mRaza = in.readString();
    }

    public static final Parcelable.Creator<Razas> CREATOR = new Parcelable.Creator<Razas>() {
        public Razas createFromParcel(Parcel source) {
            return new Razas(source);
        }

        public Razas[] newArray(int size) {
            return new Razas[size];
        }
    };
}
