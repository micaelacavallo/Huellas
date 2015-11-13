package com.example.micaela.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.micaela.db.clases.Adicionales;
import com.example.micaela.db.clases.Perdidos;
import com.example.micaela.huellas.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdicionalesAdapter extends RecyclerView.Adapter<AdicionalesViewHolder> {
    List<Adicionales> mAdicionales;
    private Context mContext;

    public AdicionalesAdapter(List<Adicionales> adicionales, Context context) {
        mAdicionales = adicionales;
        mContext = context;
    }

    @Override
    public AdicionalesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_info_item, viewGroup, false); //ver ok
        return new AdicionalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdicionalesViewHolder holder, int position) {
        holder.getTextViewTitulo().setText(mAdicionales.get(position).getTitulo());
        holder.getTextViewDescripcion().setText(mAdicionales.get(position).getDescripcion());
        Picasso.with(mContext).load(mAdicionales.get(position).getFoto().toString()).into(holder.getImageViewFoto());

    }


    @Override
    public int getItemCount() {
        if (mAdicionales != null) {
            return mAdicionales.size();
        }
        else {
            return 0;
        }
    }
}
