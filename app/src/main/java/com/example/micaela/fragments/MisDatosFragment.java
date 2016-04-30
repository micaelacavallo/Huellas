package com.example.micaela.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.micaela.HuellasApplication;
import com.example.micaela.huellas.R;

/**
 * Created by Micaela on 20/04/2016.
 */
public class MisDatosFragment extends BaseFragment {

    private TextView mTextViewNombre;
    private TextView mTextViewMail;
    private TextView mTextViewGenero;
    private TextView mTextViewCiudad;
    private TextView mTextViewTelefono;

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_datos, container, false);
        String facebookImagen = HuellasApplication.getInstance().getProfileImageFacebook();
        getBaseActivity().setUpCollapsingToolbar(getString(R.string.mis_datos), facebookImagen);

        String telefono = HuellasApplication.getInstance().getProfileTelefono();

        mTextViewNombre = (TextView) view.findViewById(R.id.textView_nombre_perfil);
        mTextViewMail = (TextView) view.findViewById(R.id.textView_mail);
        mTextViewGenero = (TextView) view.findViewById(R.id.textView_genero);
        mTextViewCiudad = (TextView) view.findViewById(R.id.textView_ciudad);
        mTextViewTelefono = (TextView) view.findViewById(R.id.textView_telefono);
        View layoutTelefonoContainer = view.findViewById(R.id.layout_telefono_container);
        if (!telefono.equals("")) {
            layoutTelefonoContainer.setVisibility(View.VISIBLE);
            mTextViewTelefono.setText(telefono);
        }

        mTextViewMail.setText(HuellasApplication.getInstance().getProfileEmailFacebook());
        mTextViewGenero.setText(HuellasApplication.getInstance().getProfileGenderFacebook());
        mTextViewNombre.setText(HuellasApplication.getInstance().getProfileNameFacebook());
        mTextViewCiudad.setText(HuellasApplication.getInstance().getProfileLocationFacebook());
        return view;
    }
}
