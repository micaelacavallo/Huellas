package com.example.micaela.db.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Quimey on 13/09/2015.
 */
public class Estados implements Parcelable {

    private String mSituacion;
    private String mObjectId;

    public Estados(){

    }

    public Estados(String situacion) {
        this.mSituacion = situacion;
    }

    public Estados(String objectId, String situacion) {
        this.mSituacion = situacion;
        this.mObjectId = objectId;
    }

    public String getSituacion() {
        return mSituacion;
    }

    public void setSituacion(String situacion) {
        this.mSituacion = situacion;
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
        dest.writeString(this.mSituacion);
        dest.writeString(this.mObjectId);
    }

    protected Estados(Parcel in) {
        this.mSituacion = in.readString();
        this.mObjectId = in.readString();
    }

    public static final Parcelable.Creator<Estados> CREATOR = new Parcelable.Creator<Estados>() {
        public Estados createFromParcel(Parcel source) {
            return new Estados(source);
        }

        public Estados[] newArray(int size) {
            return new Estados[size];
        }
    };


    public static int returnPositionElement (List<Estados> estados, String estado) {
        int position = -1;
        for (int x = 0; x<estados.size(); x++) {
            if (estados.get(x).getSituacion().equals(estado)) {
                position = x;
            }
        }
        return position;
    }
}
