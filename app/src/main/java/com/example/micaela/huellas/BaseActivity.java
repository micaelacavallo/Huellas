package com.example.micaela.huellas;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.micaela.Controladores.IAdicionalesImpl;
import com.example.micaela.Controladores.IPerdidosImpl;
import com.example.micaela.Interfaces.IAdicionales;
import com.example.micaela.clases.Adicionales;
import com.example.micaela.clases.Colores;
import com.example.micaela.clases.Comentarios;
import com.example.micaela.clases.Edades;
import com.example.micaela.clases.Especies;
import com.example.micaela.clases.Estados;
import com.example.micaela.clases.Perdidos;
import com.example.micaela.clases.Razas;
import com.example.micaela.clases.Sexos;
import com.example.micaela.clases.Tamaños;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    Toolbar mToolbar;
    ViewGroup mainContainer;
    ViewGroup containerLayout;
    IPerdidosImpl mIPerdidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        mainContainer = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_base, null);

        setupToolbar(mainContainer);
        containerLayout = (ViewGroup) mainContainer.findViewById(R.id.container_base);
        ViewGroup content = (ViewGroup) getLayoutInflater().inflate(layoutResID, containerLayout, false);
        containerLayout.addView(content);

        super.setContentView(mainContainer);

        mIPerdidos = new IPerdidosImpl(this);
        try {
            mIPerdidos.AgregarComentarioPerdido("0ywWYmi7Nt", "AGREGANDO COMENTARIO PERDIDO", "admin@gmail.com");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void setupToolbar(ViewGroup mainContainer) {
        mToolbar = (Toolbar) mainContainer.findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        // If it is not Dashboard, set up button for header
        if (!(this instanceof PrincipalActivity)) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }
//
//    public void setToolbarTitle(String title) {
//        TextView loyalTV = (TextView) mToolbar.findViewById(R.id.txt_toolbar1);
//        TextView titleTV = (TextView) mToolbar.findViewById(R.id.toolbar_fragment_title);
//
//        if (TextUtils.isEmpty(title)) {
//            loyalTV.setVisibility(View.VISIBLE);
//            titleTV.setVisibility(View.GONE);
//        } else {
//            loyalTV.setVisibility(View.GONE);
//            titleTV.setVisibility(View.VISIBLE);
//            titleTV.setText(title);
//        }
//    }
}
