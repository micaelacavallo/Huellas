package com.example.micaela.activities;

import android.os.Bundle;

import com.example.micaela.fragments.DetallePublicacionFragment;
import com.example.micaela.huellas.R;

public class DetallePublicacionActivity  extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_publicacion);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment, new DetallePublicacionFragment()).commit();
    }
}