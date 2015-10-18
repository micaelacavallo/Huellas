package com.example.micaela.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.micaela.huellas.R;

import java.util.List;


public class AnimalesAdapter extends RecyclerView.Adapter<AnimalesViewHolder> {
    List<String> mAnimales;
    private Context mContext;
    private int mCurrentPage;

    public AnimalesAdapter(List<String> animales, int currentPage, Context context) {
        mContext = context;
        mAnimales = animales;
        mCurrentPage = currentPage;
    }

    @Override
    public AnimalesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_animales_item, viewGroup, false);
        return new AnimalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnimalesViewHolder holder, int position) {
        holder.getTextViewTitulo().setText("Golden mediano marron");
        holder.getTextViewDescripcion().setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.");

        if (mCurrentPage == 0) {
            holder.getCardEstado().setVisibility(View.VISIBLE);
            if (position == 1 || position == 3 || position == 5) {
                holder.getTextViewEstado().setText("BUSCADO");
                holder.getTextViewEstado().setBackgroundResource(R.color.orange_light);
            } else {
                holder.getTextViewEstado().setText("ENCONTRADO");
                holder.getTextViewEstado().setBackgroundResource(R.color.blue_light);
            }
        }
        else {
            holder.getCardEstado().setVisibility(View.INVISIBLE);
        }

        holder.getImageViewFoto().setImageResource(R.mipmap.dog);

    }


    @Override
    public int getItemCount() {
        return mAnimales.size();
    }
}