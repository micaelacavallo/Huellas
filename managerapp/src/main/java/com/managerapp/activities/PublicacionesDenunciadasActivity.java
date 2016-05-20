package com.managerapp.activities;

import android.os.Bundle;

import com.managerapp.R;

/**
 * Created by micaela.cavallo on 5/19/2016.
 */
public class PublicacionesDenunciadasActivity  extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicaciones_denunciadas);
        showUpButton();
    }

    @Override
    public int getLayoutBase() {
        return R.layout.activity_base;
    }

}