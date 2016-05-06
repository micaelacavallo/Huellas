package com.example.micaela.activities;

import android.os.Bundle;

import com.example.micaela.fragments.HistorialPublicacionesFragment;
import com.example.micaela.huellas.R;

/**
 * Created by Micaela on 05/05/2016.
 */
public class HistorialPublicacionesActivity extends BaseActivity {
    @Override
    public int getLayoutBase() {
        return R.layout.activity_base;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_datos);
        showUpButton();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, new HistorialPublicacionesFragment()).commit();
    }
}
