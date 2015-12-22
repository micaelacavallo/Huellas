package com.example.micaela.db.Controladores;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.example.micaela.db.DAO.PerdidosDAO;
import com.example.micaela.db.Enums.CAdicionales;
import com.example.micaela.db.Enums.CColores;
import com.example.micaela.db.Enums.CEdades;
import com.example.micaela.db.Enums.CEspecies;
import com.example.micaela.db.Enums.CEstados;
import com.example.micaela.db.Enums.CPerdidos;
import com.example.micaela.db.Enums.CPersonas;
import com.example.micaela.db.Enums.CRazas;
import com.example.micaela.db.Enums.CSexos;
import com.example.micaela.db.Enums.CTamaños;
import com.example.micaela.db.Enums.Clases;
import com.example.micaela.db.Interfaces.IDBLocal;
import com.example.micaela.db.Interfaces.IPerdidos;
import com.example.micaela.db.Modelo.Colores;
import com.example.micaela.db.Modelo.Comentarios;
import com.example.micaela.db.Modelo.Edades;
import com.example.micaela.db.Modelo.Especies;
import com.example.micaela.db.Modelo.Estados;
import com.example.micaela.db.Modelo.Perdidos;
import com.example.micaela.db.Modelo.Personas;
import com.example.micaela.db.Modelo.Razas;
import com.example.micaela.db.Modelo.Sexos;
import com.example.micaela.db.Modelo.Tamaños;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.List;

public class IPerdidosImpl implements IPerdidos {

    private PerdidosDAO mPerdidosDAO;

    public IPerdidosImpl(Context context) {
        mPerdidosDAO = new PerdidosDAO(context);
    }

    @Override
    public List<Perdidos> getPerdidos() throws ParseException {
        return mPerdidosDAO.getPerdidos();
    }

    @Override
    public void savePerdido(Perdidos perdido) {
        mPerdidosDAO.savePerdido(perdido);
    }


    @Override
    public void editPerdido(Perdidos perdido) throws ParseException {
        mPerdidosDAO.editPerdido(perdido);
    }

    @Override
    public int getUltimoInsertado() throws ParseException {
        return mPerdidosDAO.getUltimoInsertado();
    }

    @Override
    public Razas getRaza(String raza) throws ParseException {
        return mPerdidosDAO.getRaza(raza);
    }

    @Override
    public Colores getColor(String color) throws ParseException {
        return mPerdidosDAO.getColor(color);
    }

    @Override
    public Sexos getSexo(String sexo) throws ParseException {
        return mPerdidosDAO.getSexo(sexo);
    }

    @Override
    public Tamaños getTamaño(String tamaño) throws ParseException {
        return mPerdidosDAO.getTamaño(tamaño);
    }

    @Override
    public Edades getEdad(String edad) throws ParseException {
        return mPerdidosDAO.getEdad(edad);
    }

    @Override
    public Especies getEspecie(String especie) throws ParseException {
        return mPerdidosDAO.getEspecie(especie);
    }

    @Override
    public List<Razas> getRazas() {
        return mPerdidosDAO.getRazas();
    }

    @Override
    public List<Colores> getColores() {
        return mPerdidosDAO.getColores();
    }

    @Override
    public List<Sexos> getSexos() {
        return mPerdidosDAO.getSexos();
    }

    @Override
    public List<Tamaños> getTamaños() {
        return mPerdidosDAO.getTamaños();
    }

    @Override
    public List<Edades> getEdades() {
        return mPerdidosDAO.getEdades();
    }

    @Override
    public List<Especies> getEspecies() {
        return mPerdidosDAO.getEspecies();
    }

    @Override
    public void deletePerdido(String objectId) throws ParseException {
        mPerdidosDAO.deletePerdido(objectId);
    }

    @Override
    public void AgregarComentarioPerdido(String perdidoObjectId, String comentario, String email) throws ParseException {
        mPerdidosDAO.AgregarComentarioPerdido(perdidoObjectId, comentario, email);
    }

    @Override
    public void cargarDBLocal(Context context) throws ParseException {
        mPerdidosDAO.cargarDBLocal(context);
    }

}
