package com.managerapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.managerapp.db.Modelo.Adicionales;
import com.managerapp.db.Modelo.Colores;
import com.managerapp.db.Modelo.Edades;
import com.managerapp.db.Modelo.Especies;
import com.managerapp.db.Modelo.Estados;
import com.managerapp.db.Modelo.Perdidos;
import com.managerapp.db.Modelo.Razas;
import com.managerapp.db.Modelo.Sexos;
import com.managerapp.db.Modelo.Tamaños;
import com.managerapp.utils.Constants;
import com.parse.Parse;

import java.util.ArrayList;
import java.util.List;


public class HuellasApplication extends Application {

    private static HuellasApplication instance;

    private List<Razas> mRazas = new ArrayList<>();
    private List<Especies> mEspecies = new ArrayList<>();
    private List<Tamaños> mTamanios = new ArrayList<>();
    private List<Edades> mEdades = new ArrayList<>();
    private List<Colores> mColores = new ArrayList<>();
    private List<Estados> mEstados = new ArrayList<>();
    private List<Sexos> mSexos = new ArrayList<>();
    private List<Perdidos> mPerdidos = new ArrayList<>();
    private List<Adicionales> mDonaciones = new ArrayList<>();
    private List<Adicionales> mInfoUtil = new ArrayList<>();

    public static HuellasApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        initParse();
    }

    public List<Adicionales> getDonaciones() {
        return mDonaciones;
    }

    public void setDonaciones(List<Adicionales> mDonaciones) {
        this.mDonaciones = mDonaciones;
    }

    public List<Adicionales> getInfoUtil() {
        return mInfoUtil;
    }

    public void setInfoUtil(List<Adicionales> mInfoUtil) {
        this.mInfoUtil = mInfoUtil;
    }

    public List<Perdidos> getmPerdidos() {
        return mPerdidos;
    }

    public void setmPerdidos(List<Perdidos> mPerdidos) {
        this.mPerdidos = mPerdidos;
    }

    public List<Sexos> getmSexos() {
        return mSexos;
    }

    public void setmSexos(List<Sexos> mSexos) {
        this.mSexos = mSexos;
    }

    public List<Razas> getmRazas() {
        return mRazas;
    }

    public void setmRazas(List<Razas> mRazas) {
        this.mRazas = mRazas;
    }

    public List<Especies> getmEspecies() {
        return mEspecies;
    }

    public void setmEspecies(List<Especies> mEspecies) {
        this.mEspecies = mEspecies;
    }

    public List<Tamaños> getmTamanios() {
        return mTamanios;
    }

    public void setmTamanios(List<Tamaños> mTamanios) {
        this.mTamanios = mTamanios;
    }

    public List<Edades> getmEdades() {
        return mEdades;
    }

    public void setmEdades(List<Edades> mEdades) {
        this.mEdades = mEdades;
    }

    public List<Colores> getmColores() {
        return mColores;
    }

    public void setmColores(List<Colores> mColores) {
        this.mColores = mColores;
    }

    public List<Estados> getmEstados() {
        return mEstados;
    }

    public void setmEstados(List<Estados> mEstados) {
        this.mEstados = mEstados;
    }

    public void initParse() {

        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(getApplicationContext(), Constants.APPLICATION_ID, Constants.CLIENT_ID);

    }

    public void clearProfile() {
        SharedPreferences.Editor editor = HuellasApplication.getInstance().getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.USER, "");
        editor.putString(Constants.PASS, "");
        editor.apply();
    }

    public boolean isLoggedIn () {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE);
        String user = sharedPreferences.getString(Constants.USER, "");
        if (user.equals("")) {
            return false;
        }
        else {
            return true;
        }
    }

    public void saveProfile (String user, String pass) {
        SharedPreferences.Editor editor = HuellasApplication.getInstance().getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.USER, user);
        editor.putString(Constants.PASS, pass);
        editor.apply();
    }


}
