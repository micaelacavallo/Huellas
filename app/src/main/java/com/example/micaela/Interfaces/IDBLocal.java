package com.example.micaela.Interfaces;

import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Horacio on 27/9/2015.
 */
public interface IDBLocal {

    public void pinObjectInBackground(ParseObject object);
    public void queryFromLocalDatastore(ParseQuery query);
}
