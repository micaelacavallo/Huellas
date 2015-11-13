package com.example.micaela.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.micaela.huellas.R;

public class AdicionalesViewHolder extends RecyclerView.ViewHolder  {


    private TextView textViewTitulo;
    private TextView textViewDescripcion;
    private ImageView imageViewFoto;
    private View cardEstado;
    private ImageView imageViewComentar;
    private TextView textViewComentar;


    public AdicionalesViewHolder(View itemView) {
        super(itemView);
        textViewTitulo = (TextView) itemView.findViewById(R.id.textView_titulo);
        textViewDescripcion = (TextView) itemView.findViewById(R.id.textView_descripcion);
        imageViewFoto = (ImageView) itemView.findViewById(R.id.imageView_foto);
        cardEstado = itemView.findViewById(R.id.card_estado);
        cardEstado.setVisibility(View.GONE);
        imageViewComentar = (ImageView)itemView.findViewById(R.id.imageView_comentar);
        textViewComentar = (TextView)itemView.findViewById(R.id.textView_comentar);
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
