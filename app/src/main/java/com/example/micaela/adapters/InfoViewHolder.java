package com.example.micaela.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.micaela.huellas.R;

public class InfoViewHolder extends RecyclerView.ViewHolder  {

    private ImageView imageViewFoto;

    public InfoViewHolder(View itemView) {
        super(itemView);
        imageViewFoto = (ImageView) itemView.findViewById(R.id.imageView_foto);
    }

    public ImageView getImageViewFoto() {
        return imageViewFoto;
    }
}
