package com.example.micaela.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.micaela.activities.BaseActivity;
import com.example.micaela.activities.DetallePublicacionActivity;
import com.example.micaela.db.Modelo.Perdidos;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.Constants;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AnimalesAdapter extends RecyclerView.Adapter<AnimalesViewHolder> {
    List<Perdidos> mPerdidos;
    private Context mContext;

    public AnimalesAdapter(List<Perdidos> perdidos, Context context) {
        mPerdidos = perdidos;
        mContext = context;
    }

    @Override
    public AnimalesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_animales_item, viewGroup, false);
        return new AnimalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnimalesViewHolder holder, final int position) {
        String title = mPerdidos.get(position).getRaza().getRaza() + " " + mPerdidos.get(position).getColor().getColor();
        holder.getTextViewTitulo().setText(title);
        holder.getTextViewDescripcion().setText(mPerdidos.get(position).getDescripcion());

        if (mPerdidos.get(position).getEstado().getSituacion().equals(mContext.getString(R.string.buscado_minus))) {
            holder.getTextViewEstado().setText(mContext.getString(R.string.buscado_mayus));
            holder.getTextViewEstado().setBackgroundResource(R.color.orange_light);
        } else {
            holder.getTextViewEstado().setText(mContext.getString(R.string.encontrado_mayus));
            holder.getTextViewEstado().setBackgroundResource(R.color.blue_light);
        }

        holder.getImageViewFoto().setImageBitmap(((BaseActivity)mContext).convertFromByteToBitmap(mPerdidos.get(position).getFoto()));
        holder.getTextViewHora().setText(((BaseActivity) mContext).getPublicationTime(mPerdidos.get(position).getFecha()));

        holder.getCardContainer().setTag(position);
        holder.getCardContainer().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetallePublicacionActivity.class);
                intent.putExtra(Constants.OBJETO_PERDIDO, mPerdidos.get((int)view.getTag()));
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mPerdidos.size();
    }
}