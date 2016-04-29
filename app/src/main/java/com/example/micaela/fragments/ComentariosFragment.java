package com.example.micaela.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.micaela.huellas.R;


public class ComentariosFragment extends BaseFragment {
    private View mRootView;

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_detalle_publicacion, container, false);

        return mRootView;
    }
}
