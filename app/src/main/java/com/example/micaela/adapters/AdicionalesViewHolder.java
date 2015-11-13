package com.example.micaela.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.micaela.huellas.R;

/**
 * Created by Horacio on 13/11/2015.
 */
public class AdicionalesViewHolder extends RecyclerView.ViewHolder  {


    private TextView textViewTitulo;
    private TextView textViewEstado;
    private TextView textViewDescripcion;
    private ImageView imageViewFoto;
    private View cardEstado;
    private ImageView imageViewComentar;
    private TextView textViewComentar;


    public AdicionalesViewHolder(View itemView) {
        super(itemView);
        textViewTitulo = (TextView) itemView.findViewById(R.id.textView_titulo);
        textViewEstado = (TextView) itemView.findViewById(R.id.textView_estado);
        textViewDescripcion = (TextView) itemView.findViewById(R.id.textView_descripcion);
        imageViewFoto = (ImageView) itemView.findViewById(R.id.imageView_foto);
        cardEstado = itemView.findViewById(R.id.card_estado);
        imageViewComentar = (ImageView)itemView.findViewById(R.id.imageView_comentar);
        textViewComentar = (TextView)itemView.findViewById(R.id.textView_comentar);
    }

    public View getCardEstado() {
        return cardEstado;
    }

    public TextView getTextViewTitulo() {
        return textViewTitulo;
    }

    public TextView getTextViewEstado() {
        return textViewEstado;
    }

    public TextView getTextViewDescripcion() {
        return textViewDescripcion;
    }

    public ImageView getImageViewFoto() {
        return imageViewFoto;
    }

    public ImageView getImageViewComentar() {
        return imageViewComentar;
    }

    public TextView getTextViewComentar() {
        return textViewComentar;
    }
}
