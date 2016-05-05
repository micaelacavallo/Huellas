package com.example.micaela.db.Interfaces;

import android.content.Context;

import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.db.Modelo.Colores;
import com.example.micaela.db.Modelo.Edades;
import com.example.micaela.db.Modelo.Especies;
import com.example.micaela.db.Modelo.MotivoDenuncia;
import com.example.micaela.db.Modelo.Perdidos;
import com.example.micaela.db.Modelo.Razas;
import com.example.micaela.db.Modelo.Sexos;
import com.example.micaela.db.Modelo.Tamaños;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Horacio on 5/10/2015.
 */
public interface IPerdidos {

    public List<Perdidos> getPerdidos() throws ParseException;
    public Perdidos savePerdido(Perdidos perdido) throws ParseException;
    public void editPerdido(Perdidos perdido) throws ParseException;
    public Razas getRaza(String raza) throws ParseException;
    public Colores getColor(String color) throws ParseException;
    public Sexos getSexo(String sexo) throws ParseException;
    public Tamaños getTamaño(String tamaño) throws ParseException;
    public Edades getEdad(String edad) throws ParseException;
    public Especies getEspecie(String especie) throws ParseException;
    public List<Razas> getRazas();
    public List<Colores> getColores();
    public List<Sexos> getSexos();
    public List<Tamaños> getTamaños();
    public List<Edades> getEdades();
    public List<Especies> getEspecies();
    public void deletePerdido(String objectId) throws ParseException;
    public void AgregarComentarioPerdido(String perdidoObjectId, String comentario, String email) throws ParseException;
    public void cargarDBLocal(Context context) throws ParseException;
    public List<Perdidos> getPublicacionesPerdidosPropias(String objectId) throws ParseException;
    public Perdidos getPublicacionPerdidosById(String objectId) throws ParseException;
    public ParseObject getParseObjectById(String objectId);
    public void bloquearPerdido(String objectId);
}
