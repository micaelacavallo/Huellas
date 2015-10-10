package com.example.micaela.Interfaces;

import com.example.micaela.clases.Colores;
import com.example.micaela.clases.Edades;
import com.example.micaela.clases.Especies;
import com.example.micaela.clases.Perdidos;
import com.example.micaela.clases.Razas;
import com.example.micaela.clases.Sexos;
import com.example.micaela.clases.Tamaños;
import com.parse.ParseException;

import java.util.List;

/**
 * Created by Horacio on 5/10/2015.
 */
public interface IPerdidos {

    public List<Perdidos> getPerdidos() throws ParseException;

    public void savePerdido(Perdidos perdido);
    public int getUltimoInsertado() throws ParseException;
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


}
