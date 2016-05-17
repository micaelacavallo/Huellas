package com.managerapp.db.Interfaces;

import com.parse.ParseObject;

/**
 * Created by Micaela on 17/05/2016.
 */
public interface IDB {

        public void pinObjectInBackground(ParseObject object);
        public void unpinObjectInBackground(ParseObject object);
        public void saveInBackground(ParseObject object);
        public void deleteInBackground(ParseObject object);
}
