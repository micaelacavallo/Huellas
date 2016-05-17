package com.managerapp.db.Interfaces;

import android.content.Context;

import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Horacio on 10/10/2015.
 */
public interface IGeneral {

    public boolean internet(Context context);
    public void save(ParseObject object);
    public void delete(ParseObject object);
    public void startAlert();
    public String getUltimoObjectId(String clase);
    public void checkInternetGet(ParseQuery<ParseObject> query);
}
