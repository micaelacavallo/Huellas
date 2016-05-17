package com.managerapp.db.Controladores;

import android.content.Context;

import com.managerapp.db.DAO.PerdidosDAO;
import com.managerapp.db.Interfaces.IPerdidos;
import com.managerapp.db.Modelo.Colores;
import com.managerapp.db.Modelo.Edades;
import com.managerapp.db.Modelo.Especies;
import com.managerapp.db.Modelo.Perdidos;
import com.managerapp.db.Modelo.Razas;
import com.managerapp.db.Modelo.Sexos;
import com.managerapp.db.Modelo.Tamaños;
import com.parse.ParseException;
import com.parse.ParseObject;

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
    public void savePerdido(Perdidos perdido) throws ParseException {
     mPerdidosDAO.savePerdido(perdido);
    }

    @Override
    public String getInsertedID(String email) throws ParseException {
      return  mPerdidosDAO.getInsertedID(email);
    }


    @Override
    public void editPerdido(Perdidos perdido) throws ParseException {
        mPerdidosDAO.editPerdido(perdido);
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
    public void cargarDBLocalListaPerdidos(Context context) throws ParseException {
        mPerdidosDAO.cargarDBLocalListaPerdidos(context);
    }

    @Override
    public void cargarDBLocalCaracteristicasPerdidos(Context context) {
        mPerdidosDAO.cargarDBLocalCaracteristicasPerdidos(context);
    }

    @Override
    public List<Perdidos> getPublicacionPerdidosByEmail(String email) throws ParseException {
        return mPerdidosDAO.getPublicacionPerdidosByEmail(email);
    }

    @Override
    public List<Perdidos> getPublicacionPerdidosPropiasById(String personaObjectId) throws ParseException {
        return  mPerdidosDAO.getPublicacionPerdidosPropiasById(personaObjectId);
    }

    @Override
    public Perdidos getPublicacionPerdidosById(String objectId) throws ParseException {
        return mPerdidosDAO.getPublicacionPerdidosById(objectId);
    }

    @Override
    public ParseObject getParseObjectById(String objectId){
        return mPerdidosDAO.getParseObjectById(objectId);
    }

    @Override
    public void bloquearPerdido(String objectId) {
        mPerdidosDAO.bloquearPerdido(objectId);
    }
}
