package com.managerapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.managerapp.R;

/**
 * Created by Micaela on 20/05/2016.
 */
public class PersonasViewHolder extends RecyclerView.ViewHolder {

    private TextView mTextViewNombre;
    private ImageView mImageViewFoto;
    private TextView mTextViewEmail;
    private TextView mTextViewTelefono;
    private TextView mTextViewCountDenuncias;
    private TextView mTextViewMotivoDenuncia;
    private ImageView mImageViewMoreOptions;

    public PersonasViewHolder(View itemView) {
        super(itemView);
        mTextViewNombre = (TextView) itemView.findViewById(R.id.textView_nombre_persona);
        mTextViewEmail = (TextView) itemView.findViewById(R.id.textView_mail_persona);
        mTextViewTelefono = (TextView) itemView.findViewById(R.id.textView_telefono_persona);
        mImageViewFoto = (ImageView) itemView.findViewById(R.id.imageView_foto_persona);
        mImageViewMoreOptions = (ImageView) itemView.findViewById(R.id.imageView_options);
        mTextViewCountDenuncias = (TextView) itemView.findViewById(R.id.textView_count_denuncias);
        mTextViewMotivoDenuncia = (TextView) itemView.findViewById(R.id.textView_motivo_denuncia);
    }

    public ImageView getmImageViewMoreOptions() {
        return mImageViewMoreOptions;
    }

    public TextView getmTextViewNombre() {
        return mTextViewNombre;
    }

    public ImageView getmImageViewFoto() {
        return mImageViewFoto;
    }

    public TextView getmTextViewEmail() {
        return mTextViewEmail;
    }

    public TextView getmTextViewTelefono() {
        return mTextViewTelefono;
    }

    public TextView getmTextViewCountDenuncias() {
        return mTextViewCountDenuncias;
    }

    public TextView getmTextViewMotivoDenuncia() {
        return mTextViewMotivoDenuncia;
    }
}
