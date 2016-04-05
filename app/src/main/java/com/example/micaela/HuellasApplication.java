package com.example.micaela;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.example.micaela.utils.Constants;
import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseInstallation;


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

        //push notifications
        // Associate the device with a user
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", getProfileNameFacebook());
        installation.saveInBackground();

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

    public void saveProfileFacebook(Uri image, String name, String email, String location, String gender, String birthday) {
        SharedPreferences.Editor editor = HuellasApplication.getInstance().getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.PROFILE_IMAGE, image.toString());
        editor.putString(Constants.PROFILE_NAME, name);
        editor.putString(Constants.PROFILE_EMAIL, email);
        editor.putString(Constants.PROFILE_LOCATION, location);
        editor.putString(Constants.PROFILE_GENDER, gender);
        editor.putString(Constants.PROFILE_BIRTHDAY, birthday);
        editor.apply();
    }

    public void clearProfileFacebook () {
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

    public String getProfileNameFacebook () {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.PROFILE_NAME, "");
    }

    public String getProfileImageFacebook () {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.PROFILE_IMAGE, "");
    }
    public String getProfileLocationFacebook () {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.PROFILE_LOCATION, "");
    } public String getProfileGenderFacebook () {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.PROFILE_GENDER, "");
    }
    public String getProfileEmailFacebook () {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.PROFILE_EMAIL, "");
    }
    public String getProfileBirthdayFacebook () {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_HUELLAS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.PROFILE_BIRTHDAY, "");
    }

}
