package com.managerapp.db.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Quimey on 13/09/2015.
 */
public class Colores implements Parcelable{

    private String mColor;
    private String mObjectId;


    public Colores() {
    }

    public Colores(String color) {
        this.mColor = color;
    }

    public Colores(String color, String objectId) {
        this.mColor = color;
        this.mObjectId = objectId;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        this.mColor = color;
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
        dest.writeString(this.mColor);
        dest.writeString(this.mObjectId);
    }

    protected Colores(Parcel in) {
        this.mColor = in.readString();
        this.mObjectId = in.readString();
    }

    public static final Creator<Colores> CREATOR = new Creator<Colores>() {
        public Colores createFromParcel(Parcel source) {
            return new Colores(source);
        }

        public Colores[] newArray(int size) {
            return new Colores[size];
        }
    };

    public static int returnPositionElement (List<Colores> colores, String color) {
        int position = -1;
        for (int x = 0; x<colores.size(); x++) {
            if (colores.get(x).getColor().equals(color)) {
                position = x;
            }
        }
        return position;
    }
}
