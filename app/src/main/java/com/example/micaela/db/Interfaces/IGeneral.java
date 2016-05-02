package com.example.micaela.db.Interfaces;

import android.content.Context;

import com.example.micaela.db.Modelo.Comentarios;
import com.example.micaela.db.Modelo.Estados;
import com.example.micaela.db.Modelo.MotivoDenuncia;
import com.example.micaela.db.Modelo.Personas;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Horacio on 10/10/2015.
 */
public interface IGeneral {

    public boolean internet(Context context);
    public void save(ParseObject object);
    public void delete(ParseObject object);
    public void startAlert();

}
