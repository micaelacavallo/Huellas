package com.example.micaela.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.micaela.db.clases.Perdidos;
import com.example.micaela.huellas.R;
import com.squareup.picasso.Picasso;

import java.net.ContentHandler;
import java.util.List;


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
    public void onBindViewHolder(AnimalesViewHolder holder, int position) {
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
        Picasso.with(mContext).load(mPerdidos.get(position).getFoto().toString()).into(holder.getImageViewFoto());

    }


    @Override
    public int getItemCount() {
        return mPerdidos.size();
    }
}