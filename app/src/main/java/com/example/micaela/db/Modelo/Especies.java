package com.example.micaela.db.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Quimey on 13/09/2015.
 */
@ParseClassName("Especies")
public class Especies extends ParseObject {

    private int mIdEspecie;
    private String mEspecie;
    private String mObjectId;


    public Especies() {
    }

    public Especies(String especie) {
        this.mEspecie = especie;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mIdEspecie);
        dest.writeString(this.mEspecie);
        dest.writeString(this.mObjectId);
    }

    protected Especies(Parcel in) {
        this.mIdEspecie = in.readInt();
        this.mEspecie = in.readString();
        this.mObjectId = in.readString();
    }

    public static final Parcelable.Creator<Especies> CREATOR = new Parcelable.Creator<Especies>() {
        public Especies createFromParcel(Parcel source) {
            return new Especies(source);
        }

        public Especies[] newArray(int size) {
            return new Especies[size];
        }
    };
}
