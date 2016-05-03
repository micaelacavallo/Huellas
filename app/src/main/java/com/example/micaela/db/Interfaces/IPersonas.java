package com.example.micaela.db.Interfaces;

import android.content.Context;

import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.db.Modelo.MotivoDenuncia;
import com.example.micaela.db.Modelo.Personas;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


/**
 * Created by Quimey on 19/09/2015.
 */
public interface IPersonas {

    public void denunciar(String id, String motivo) throws ParseException;
    public MotivoDenuncia getMotivoDenuncia(String motivo);
    public List<MotivoDenuncia> getMotivoDenuncias();
    public void checkInternetGet(ParseQuery<ParseObject> query);
    public boolean internet(Context context);
    public void registar(Personas personas);
    public List<Personas> getPersonas();
    public Personas getPersonabyEmail(String email) throws ParseException;
    public Personas getPersonabyId(String objectId) throws ParseException;
    public void editTelefono(String objectId, String telefono);
    public ParseObject getParseObjectById(String objectId);

}
