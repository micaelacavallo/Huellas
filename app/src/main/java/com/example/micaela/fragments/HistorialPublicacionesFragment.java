package com.example.micaela.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.example.micaela.adapters.AdicionalesAdapter;
import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.Constants;

import java.util.List;

/**
 * Created by Micaela on 05/05/2016.
 */
public class HistorialPublicacionesFragment extends BaseFragment {
    private View mRootView;
    private Spinner mSpinnerTipoPublicacion;

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView =  inflater.inflate(R.layout.fragment_historial_publicaciones, container, false);

        mSpinnerTipoPublicacion = (Spinner) mRootView.findViewById(R.id.spinner_tipo_publi);

        return mRootView;
    }


}
