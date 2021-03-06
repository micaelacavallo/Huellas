package com.example.micaela.db.Interfaces;

import android.content.Context;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Horacio on 27/9/2015.
 */
public interface IDB {

    public void pinObjectInBackground(ParseObject object);
    public void unpinObjectInBackground(ParseObject object);
    public void saveInBackground(ParseObject object);
    public void deleteInBackground(ParseObject object);
}
