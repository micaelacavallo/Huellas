package com.example.micaela.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.huellas.R;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoViewHolder> {
    List<Adicionales> mAdicionales;
    private Context mContext;

    public InfoAdapter(List<Adicionales> adicionales, Context context) {
        mAdicionales = adicionales;
        mContext = context;
    }

    @Override
    public InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_info_item, parent, false);
        return new InfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InfoViewHolder holder, int position) {
        if (position == 1 || position == 3 || position == 4) {
            holder.getImageViewFoto().setImageResource(R.mipmap.info1);
        }

        else {
            holder.getImageViewFoto().setImageResource(R.mipmap.info2);
        }

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
