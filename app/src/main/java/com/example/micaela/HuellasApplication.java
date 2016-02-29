package com.example.micaela;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.db.Modelo.Colores;
import com.example.micaela.db.Modelo.Edades;
import com.example.micaela.db.Modelo.Especies;
import com.example.micaela.db.Modelo.Perdidos;
import com.example.micaela.db.Modelo.Razas;
import com.example.micaela.db.Modelo.Sexos;
import com.example.micaela.db.Modelo.Tamaños;
import com.example.micaela.utils.Constants;
import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseObject;


public class HuellasApplication extends Application{

    private static HuellasApplication instance;

    public static HuellasApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        initParse();

        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    public void initParse()
    {

        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(getApplicationContext(), Constants.APPLICATION_ID, Constants.CLIENT_ID);
    }


    public void saveAccessTokenFacebook(String token) {
        SharedPreferences.Editor editor = HuellasApplication.getInstance().getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.ACCESS_TOKEN_FCB, token);
        editor.apply();
    }

    public String getAccessTokenFacebook () {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.ACCESS_TOKEN_FCB, "");
    }

    public void saveProfileFacebook(Uri image, String name) {
        SharedPreferences.Editor editor = HuellasApplication.getInstance().getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.PROFILE_IMAGE, image.toString());
        editor.putString(Constants.PROFILE_NAME, name);
        editor.apply();
    }

    public void clearProfileFacebook () {
        SharedPreferences.Editor editor = HuellasApplication.getInstance().getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.PROFILE_IMAGE, "");
        editor.putString(Constants.PROFILE_NAME, "");
        editor.putString(Constants.ACCESS_TOKEN_FCB, "");
        editor.apply();
    }

    public String getProfileNameFacebook () {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.PROFILE_NAME, "");
    }

    public String getProfileImageFacebook () {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.PROFILE_IMAGE, "");
    }

}
