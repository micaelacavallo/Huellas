package com.example.micaela;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.micaela.utils.Constants;
import com.facebook.FacebookSdk;
import com.parse.Parse;


public class HuellasApplication extends Application{

    private static HuellasApplication instance;

    public static HuellasApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(getApplicationContext(), Constants.APPLICATION_ID, Constants.CLIENT_ID);


        FacebookSdk.sdkInitialize(getApplicationContext());
    }


    public void saveAccessTokenFacebook(String token) {
        SharedPreferences.Editor editor = HuellasApplication.getInstance().getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.ACCESS_TOKEN_FCB, token);
        editor.commit();
    }

    public String getAccessTokenFacebook () {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.ACCESS_TOKEN_FCB, "");
    }

}