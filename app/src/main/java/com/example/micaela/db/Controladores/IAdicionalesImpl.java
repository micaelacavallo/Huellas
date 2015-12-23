package com.example.micaela.db.Controladores;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.micaela.db.DAO.AdicionalesDAO;
import com.example.micaela.db.Enums.CAdicionales;
import com.example.micaela.db.Enums.CColores;
import com.example.micaela.db.Enums.CEstados;
import com.example.micaela.db.Enums.CPersonas;
import com.example.micaela.db.Enums.Clases;
import com.example.micaela.db.Interfaces.IAdicionales;
import com.example.micaela.db.Interfaces.IDBLocal;
import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.db.Modelo.Colores;
import com.example.micaela.db.Modelo.Comentarios;
import com.example.micaela.db.Modelo.Estados;
import com.example.micaela.db.Modelo.Personas;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Quimey on 19/09/2015.
 */
public class IAdicionalesImpl implements IAdicionales {

    private AdicionalesDAO mAdicionalesDAO;

    public IAdicionalesImpl(Context context) {
        mAdicionalesDAO = new AdicionalesDAO(context);
    }

    @Override
    public List<Adicionales> getDonaciones() throws ParseException {
        return mAdicionalesDAO.getDonaciones();
    }

    @Override
    public List<Adicionales> getInfoUtil() throws ParseException {
        return mAdicionalesDAO.getInfoUtil();
    }

    @Override
    public Adicionales getAdicionalById(int idAdicional) {
        return mAdicionalesDAO.getAdicionalById(idAdicional);
    }

    @Override
    public void saveAdicional(Adicionales adicional) {
        mAdicionalesDAO.saveAdicional(adicional);
    }


    public ParseObject cargarAdicional(Adicionales adicional) {
        return mAdicionalesDAO.cargarAdicional(adicional);
    }

    @Override
    public void editAdicional(Adicionales adicional) throws ParseException {
        mAdicionalesDAO.editAdicional(adicional);
    }

    @Override
    public int getUltimoInsertado() throws ParseException {
        return mAdicionalesDAO.getUltimoInsertado();
    }

    @Override
    public void deleteAdicional(String objectId) throws ParseException {
        mAdicionalesDAO.deleteAdicional(objectId);
    }

    @Override
    public void AgregarComentarioAdicional(String adicionalObjectId, String comentarioText, String email) throws ParseException {
        mAdicionalesDAO.AgregarComentarioAdicional(adicionalObjectId, comentarioText, email);
    }

    @Override
    public void cargarDBLocal(Context context) throws ParseException {
        mAdicionalesDAO.cargarDBLocal(context);
    }

}
