package com.managerapp.activities;

import android.os.Bundle;

import com.managerapp.R;

public class LoginActivity extends BaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
    }


    @Override
    public int getLayoutBase() {
        return R.layout.activity_base;
    }

    @Override
    public void onBackPressed() {
       finishAffinity();
    }
}
