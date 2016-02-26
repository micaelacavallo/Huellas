package com.example.micaela.db.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Quimey on 13/09/2015.
 */
@ParseClassName("Sexos")
public class Sexos extends ParseObject implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mIdSexo);
        dest.writeString(this.mSexo);
        dest.writeString(this.mObjectId);
    }

    protected Sexos(Parcel in) {
        this.mIdSexo = in.readInt();
        this.mSexo = in.readString();
        this.mObjectId = in.readString();
    }

    public static final Parcelable.Creator<Sexos> CREATOR = new Parcelable.Creator<Sexos>() {
        public Sexos createFromParcel(Parcel source) {
            return new Sexos(source);
        }

        public Sexos[] newArray(int size) {
            return new Sexos[size];
        }
    };
}
