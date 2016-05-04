package com.example.micaela.db.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Quimey on 13/09/2015.
 */

public class Sexos implements Parcelable {

    private String mSexo;
    private String mObjectId;


    public Sexos() {
    }

    public Sexos(String sexo) {
        this.mSexo = sexo;
    }

    public Sexos(String sexo, String objectId) {
        this.mSexo = sexo;
        this.mObjectId = objectId;
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
        dest.writeString(this.mSexo);
        dest.writeString(this.mObjectId);
    }

    protected Sexos(Parcel in) {
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


    public static int returnPositionElement (List<Sexos> sexos, String sexo) {
        int position = -1;
        for (int x = 0; x<sexos.size(); x++) {
            if (sexos.get(0).getSexo().equals(sexo)) {
                position = x;
            }
        }
        return position;
    }
}
