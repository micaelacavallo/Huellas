package com.managerapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.managerapp.R;


public class AdicionalesViewHolder extends RecyclerView.ViewHolder  {


    private TextView textViewTitulo;
    private TextView textViewDescripcion;
    private ImageView imageViewFoto;
    private TextView textViewHora;
    private View cardEstado;
    private View viewComentar;
    private TextView mTextViewComentarios;
    private View mCardContainer;
    private ImageView mImageViewOpciones;


    public AdicionalesViewHolder(View itemView) {
        super(itemView);
        textViewTitulo = (TextView) itemView.findViewById(R.id.textView_titulo);
        textViewDescripcion = (TextView) itemView.findViewById(R.id.textView_descripcion);
        imageViewFoto = (ImageView) itemView.findViewById(R.id.imageView_foto);
        textViewHora = (TextView) itemView.findViewById(R.id.textView_hora);
        cardEstado = itemView.findViewById(R.id.card_estado);
        cardEstado.setVisibility(View.GONE);
        viewComentar = itemView.findViewById(R.id.layout_comentar);
        mCardContainer = itemView.findViewById(R.id.cardView_container);
        mTextViewComentarios = (TextView) itemView.findViewById(R.id.textView_comentarios);
        mImageViewOpciones = (ImageView) itemView.findViewById(R.id.imageView_options);
    }

    public ImageView getmImageViewOpciones() {
        return mImageViewOpciones;
    }

    public TextView getmTextViewComentarios() {
        return mTextViewComentarios;
    }
    public View getCardEstado() {
        return cardEstado;
    }

    public TextView getTextViewTitulo() {
        return textViewTitulo;
    }

    public TextView getTextViewDescripcion() {
        return textViewDescripcion;
    }

    public View getViewComentar() {
        return viewComentar;
    }

    public ImageView getImageViewFoto() {
        return imageViewFoto;
    }

    public TextView getTextViewHora() {
        return textViewHora;
    }

    public View getCardContainer() {
        return mCardContainer;
    }
}
