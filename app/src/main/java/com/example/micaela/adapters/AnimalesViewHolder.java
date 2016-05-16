package com.example.micaela.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.micaela.huellas.R;

public class AnimalesViewHolder extends RecyclerView.ViewHolder {

    private TextView mTextViewTitulo;
    private TextView mTextViewEstado;
    private TextView mTextViewDescripcion;
    private ImageView mImageViewFoto;
    private View mCardEstado;
    private TextView mTextViewHora;
    private View mViewComentar;
    private View mCardContainer;
    private TextView mTextViewComentarios;
    private ImageView mImageViewOpciones;
    private View mLineSeparator;

    public AnimalesViewHolder(View itemView) {
        super(itemView);
        mTextViewTitulo = (TextView) itemView.findViewById(R.id.textView_titulo);
        mTextViewEstado = (TextView) itemView.findViewById(R.id.textView_estado);
        mTextViewDescripcion = (TextView) itemView.findViewById(R.id.textView_descripcion);
        mImageViewFoto = (ImageView) itemView.findViewById(R.id.imageView_foto);
        mCardEstado = itemView.findViewById(R.id.card_estado);
        mTextViewHora = (TextView) itemView.findViewById(R.id.textView_hora);
        mViewComentar =  itemView.findViewById(R.id.layout_comentar);
        mCardContainer = itemView.findViewById(R.id.cardView_container);
        mTextViewComentarios = (TextView) itemView.findViewById(R.id.textView_comentarios);
        mImageViewOpciones = (ImageView) itemView.findViewById(R.id.imageView_options);
        mLineSeparator= itemView.findViewById(R.id.view_separator);
    }

    public View getmLineSeparator() {
        return mLineSeparator;
    }

    public ImageView getmImageViewOpciones() {
        return mImageViewOpciones;
    }

    public TextView getmTextViewComentarios() {
        return mTextViewComentarios;
    }

    public View getCardEstado() {
        return mCardEstado;
    }

    public TextView getTextViewTitulo() {
        return mTextViewTitulo;
    }

    public TextView getTextViewEstado() {
        return mTextViewEstado;
    }

    public TextView getTextViewDescripcion() {
        return mTextViewDescripcion;
    }

    public ImageView getImageViewFoto() {
        return mImageViewFoto;
    }

    public View getViewComentar() {
        return mViewComentar;
    }

    public TextView getTextViewHora() {
        return mTextViewHora;
    }

    public View getCardContainer() {
        return mCardContainer;
    }
}

