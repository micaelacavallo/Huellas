package com.example.micaela;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.db.Modelo.Colores;
import com.example.micaela.db.Modelo.Edades;
import com.example.micaela.db.Modelo.Especies;
import com.example.micaela.db.Modelo.Estados;
import com.example.micaela.db.Modelo.MotivoDenuncia;
import com.example.micaela.db.Modelo.Perdidos;
import com.example.micaela.db.Modelo.Razas;
import com.example.micaela.db.Modelo.Sexos;
import com.example.micaela.db.Modelo.Tamaños;
import com.example.micaela.utils.Constants;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.Parse;
import com.parse.ParseInstallation;

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
    private List<Perdidos> mMisSolucionados = null;
    private List<Adicionales> mDonaciones = new ArrayList<>();
    private List<Adicionales> mInfoUtil = new ArrayList<>();
    private List<MotivoDenuncia> mMotivosDenuncia = new ArrayList<>();

    public static HuellasApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        initParse();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    public List<Adicionales> getInfoUtil() {
        return mInfoUtil;
    }

    public List<Perdidos> getmMisSolucionados() {
        return mMisSolucionados;
    }

    public void setmMisSolucionados(List<Perdidos> mMisSolucionados) {
        this.mMisSolucionados = mMisSolucionados;
    }

    public void setInfoUtil(List<Adicionales> infoUtil) {
        mInfoUtil = infoUtil;
    }

    public List<Adicionales> getDonaciones() {
        return mDonaciones;
    }

    public void setDonaciones(List<Adicionales> donaciones) {
        mDonaciones = donaciones;
    }

    public List<MotivoDenuncia> getmMotivosDenuncia() {
        return mMotivosDenuncia;
    }

    public void setmMotivosDenuncia(List<MotivoDenuncia> mMotivosDenuncia) {
        this.mMotivosDenuncia = mMotivosDenuncia;
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

        //push notifications
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("email", getProfileEmailFacebook());
        installation.saveEventually();

    }


    public void saveAccessTokenFacebook(String token) {
        SharedPreferences.Editor editor = HuellasApplication.getInstance().getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.ACCESS_TOKEN_FCB, token);
        editor.apply();
    }

    public String getAccessTokenFacebook() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.ACCESS_TOKEN_FCB, "");
    }

    public void saveProfileFacebook(Uri image, String name, String email, String location, String gender) {
        SharedPreferences.Editor editor = HuellasApplication.getInstance().getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.PROFILE_IMAGE, image.toString());
        editor.putString(Constants.PROFILE_NAME, name);
        editor.putString(Constants.PROFILE_EMAIL, email);
        editor.putString(Constants.PROFILE_LOCATION, location);
        editor.putString(Constants.PROFILE_GENDER, gender);
        editor.apply();
    }

    public void clearProfileFacebook() {
        SharedPreferences.Editor editor = HuellasApplication.getInstance().getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.PROFILE_IMAGE, "");
        editor.putString(Constants.PROFILE_NAME, "");
        editor.putString(Constants.PROFILE_EMAIL, "");
        editor.putString(Constants.PROFILE_LOCATION, "");
        editor.putString(Constants.PROFILE_GENDER, "");
        editor.putString(Constants.PROFILE_BIRTHDAY, "");
        editor.putString(Constants.ACCESS_TOKEN_FCB, "");
        editor.apply();
    }


    public void saveProfileTelefono (String telefono) {
        SharedPreferences.Editor editor = HuellasApplication.getInstance().getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.PROFILE_TELEFONO, telefono);
        editor.apply();
    }

    public String getProfileTelefono () {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.PROFILE_TELEFONO, "");
    }

    public String getProfileNameFacebook() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.PROFILE_NAME, "");
    }

    public String getProfileImageFacebook() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.PROFILE_IMAGE, "");
    }

    public String getProfileLocationFacebook() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.PROFILE_LOCATION, "");
    }

    public String getProfileGenderFacebook() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.PROFILE_GENDER, "");
    }

    public String getProfileEmailFacebook() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.PROFILE_EMAIL, "");
    }
}
