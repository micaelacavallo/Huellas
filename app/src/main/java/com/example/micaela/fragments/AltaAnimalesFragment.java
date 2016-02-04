package com.example.micaela.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.micaela.huellas.R;

public class AltaAnimalesFragment extends BaseFragment{
    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alta_animales, container, false);

        return view;
    }
}
