package com.managerapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.managerapp.R;


public class ComentariosViewHolder  extends RecyclerView.ViewHolder {

    private TextView mTextViewFecha;
    private ImageView mImageViewFoto;
    private TextView mTextViewNombre;
    private TextView mTextViewComentario;
    private View mViewLine;

    public ComentariosViewHolder(View itemView) {
        super(itemView);
        mViewLine= itemView.findViewById(R.id.view_line);
        mTextViewFecha = (TextView) itemView.findViewById(R.id.textView_fecha);
        mTextViewNombre = (TextView) itemView.findViewById(R.id.textView_nombre_persona);
        mTextViewComentario = (TextView) itemView.findViewById(R.id.textView_comentario);
        mImageViewFoto = (ImageView) itemView.findViewById(R.id.imageView_foto_persona);
    }

    public View getmViewLine() {
        return mViewLine;
    }

    public TextView getTextViewFecha() {
        return mTextViewFecha;
    }

    public ImageView getImageViewFoto() {
        return mImageViewFoto;
    }

    public TextView getTextViewNombre() {
        return mTextViewNombre;
    }

    public TextView getTextViewComentario() {
        return mTextViewComentario;
    }
}
