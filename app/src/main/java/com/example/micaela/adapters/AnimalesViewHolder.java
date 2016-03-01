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
    private ImageView mImageViewComentar;
    private TextView mTextViewComentar;
    private View mCardContainer;

    public AnimalesViewHolder(View itemView) {
        super(itemView);
        mTextViewTitulo = (TextView) itemView.findViewById(R.id.textView_titulo);
        mTextViewEstado = (TextView) itemView.findViewById(R.id.textView_estado);
        mTextViewDescripcion = (TextView) itemView.findViewById(R.id.textView_descripcion);
        mImageViewFoto = (ImageView) itemView.findViewById(R.id.imageView_foto);
        mCardEstado = itemView.findViewById(R.id.card_estado);
        mTextViewHora = (TextView) itemView.findViewById(R.id.textView_hora);
        mImageViewComentar = (ImageView) itemView.findViewById(R.id.imageView_comentar);
        mTextViewComentar = (TextView) itemView.findViewById(R.id.textView_comentar);
        mCardContainer = itemView.findViewById(R.id.cardView_container);
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

    public TextView getTextViewComentar() {
        return mTextViewComentar;
    }

    public TextView getTextViewHora() {
        return mTextViewHora;
    }

    public View getCardContainer() {
        return mCardContainer;
    }
}

