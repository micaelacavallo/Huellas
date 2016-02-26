package com.example.micaela.fragments;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.micaela.activities.BaseActivity;
import com.example.micaela.db.Modelo.Perdidos;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.Constants;

public class DetallePublicacionFragment extends BaseFragment {

    ImageView mImageViewFoto;
    TextView mTextViewDescripcion;
    TextView mTextViewFecha;
    TextView mTextViewSexo;

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_publicacion, container, false);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        Perdidos perdidos = getActivity().getIntent().getParcelableExtra(Constants.OBJETO_PERDIDO);

        mImageViewFoto = (ImageView) view.findViewById(R.id.imageView_foto);
        mTextViewDescripcion = (TextView) view.findViewById(R.id.textView_descripcion);
        mTextViewFecha = (TextView) view.findViewById(R.id.textView_fecha);
        mTextViewSexo = (TextView) view.findViewById(R.id.textView_sexo);

        mImageViewFoto.setImageBitmap(((BaseActivity) getActivity()).convertFromByteToBitmap(perdidos.getFoto()));
        mTextViewDescripcion.setText(perdidos.getDescripcion());



        mTextViewFecha.setText(((BaseActivity)getActivity()).getFormattedDate(perdidos.getFecha()));
        mTextViewSexo.setText(perdidos.getSexo().getSexo());



        return view;
    }
}