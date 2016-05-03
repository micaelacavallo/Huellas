package com.managerapp.db.Interfaces;

import android.content.Context;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Horacio on 27/9/2015.
 */
public interface IDBLocal {

    public void pinObjectInBackground(ParseObject object);
    public void unpinObjectInBackground(ParseObject object);
    public void queryFromLocalDatastore(ParseQuery query);
    public void saveEventually(ParseObject object);
    public void saveInBackground(ParseObject object);
    public void deleteEventually(ParseObject object);
    public void deleteInBackground(ParseObject object);
    public void cargarDBLocal(Context context) throws ParseException;

}
