package com.example.micaela.activities;

import android.os.Bundle;

import com.example.micaela.fragments.ComentariosFragment;
import com.example.micaela.huellas.R;

public class ComentariosActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, new ComentariosFragment()).commit();
        showUpButton();
    }

    @Override
    public int getLayoutBase() {
        return R.layout.activity_base;
    }

}
