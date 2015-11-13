package com.example.micaela.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.micaela.db.clases.Adicionales;
import com.example.micaela.db.clases.Perdidos;
import com.example.micaela.huellas.R;

import java.util.List;

/**
 * Created by Horacio on 13/11/2015.
 */
public class AdicionalesAdapter extends RecyclerView.Adapter<AdicionalesViewHolder> {
    List<Adicionales> mAdicionales;
    private int mCurrentPage;
    private Context mContext;

    public AdicionalesAdapter(List<Adicionales> adicionales, int currentPage, Context context) {
        mAdicionales = adicionales;
        mCurrentPage = currentPage;
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
/*        String title = mPerdidos.get(position).getRaza().getRaza() + " " + mPerdidos.get(position).getColor().getColor();
        holder.getTextViewTitulo().setText(title);
        holder.getTextViewDescripcion().setText(mPerdidos.get(position).getDescripcion());


        if (mCurrentPage == 0) {
            holder.getCardEstado().setVisibility(View.VISIBLE);
            if (mPerdidos.get(position).getEstado().getSituacion().equals(mContext.getString(R.string.buscado_minus))) {
                holder.getTextViewEstado().setText(mContext.getString(R.string.buscado_mayus));
                holder.getTextViewEstado().setBackgroundResource(R.color.orange_light);
            } else {
                holder.getTextViewEstado().setText(mContext.getString(R.string.encontrado_mayus));
                holder.getTextViewEstado().setBackgroundResource(R.color.blue_light);
            }
        }
        else {
            holder.getCardEstado().setVisibility(View.INVISIBLE);
        }

        holder.getImageViewFoto().setImageResource(R.mipmap.dog);
*/
    }


    @Override
    public int getItemCount() {
        return mAdicionales.size();
    }
}
