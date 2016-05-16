package com.example.micaela.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.micaela.fragments.BaseFragment;
import com.example.micaela.huellas.R;

public class ComentariosActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);
        getSupportActionBar().hide();
    }

    @Override
    public int getLayoutBase() {
        return R.layout.activity_base;
    }


    @Override
    public void onBackPressed() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (!((BaseFragment)fragment).onBackPressed()) {
                super.onBackPressed();
            }
        }
    }
}
