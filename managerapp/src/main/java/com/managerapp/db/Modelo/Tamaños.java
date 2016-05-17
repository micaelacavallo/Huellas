package com.managerapp.db.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Quimey on 13/09/2015.
 */

public class Tamaños implements Parcelable{

    private String mTamaño;
    private String mObjectId;


    public Tamaños() {
    }

    public Tamaños(String tamaño) {
        this.mTamaño = tamaño;
    }

    public Tamaños(String tamaño, String objectId) {
        this.mTamaño = tamaño;
        this.mObjectId = objectId;
    }

    public String getTamaño() {
        return mTamaño;
    }

    public void setTamaño(String tamaño) {
        this.mTamaño = tamaño;
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
    }

    protected Tamaños(Parcel in) {
        this.mObjectId = in.readString();
        this.mTamaño = in.readString();
    }

    public static final Creator<Tamaños> CREATOR = new Creator<Tamaños>() {
        public Tamaños createFromParcel(Parcel source) {
            return new Tamaños(source);
        }

        public Tamaños[] newArray(int size) {
            return new Tamaños[size];
        }
    };


    public static int returnPositionElement (List<Tamaños> tamaños, String tamaño) {
        int position = -1;
        for (int x = 0; x<tamaños.size(); x++) {
            if (tamaños.get(x).getTamaño().equals(tamaño)) {
                position = x;
            }
        }
        return position;
    }
}
