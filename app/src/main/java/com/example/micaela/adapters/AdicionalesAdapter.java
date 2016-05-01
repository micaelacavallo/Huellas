package com.example.micaela.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.micaela.activities.BaseActivity;
import com.example.micaela.activities.ComentariosActivity;
import com.example.micaela.activities.DetallePublicacionActivity;
import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.Constants;

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
                .inflate(R.layout.recycler_animales_item, viewGroup, false); //ver ok
        return new AdicionalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdicionalesViewHolder holder, int position) {
        holder.getTextViewTitulo().setText(mAdicionales.get(position).getTitulo());
        holder.getTextViewDescripcion().setText(mAdicionales.get(position).getDescripcion());

        byte[] foto = mAdicionales.get(position).getFoto();
        if (foto != null) {
            holder.getImageViewFoto().setImageBitmap(((BaseActivity) mContext).convertFromByteToBitmap(foto));
        }

        int cantidadComentarios = mAdicionales.get(position).getComentarios().size();
        if (cantidadComentarios > 0) {
            if (cantidadComentarios == 1) {
                holder.getmTextViewComentarios().setText(mContext.getString(R.string.un_comentario));
            } else {
                holder.getmTextViewComentarios().setText(String.format(mContext.getString(R.string.comentarios_cantidad), cantidadComentarios));
            }
            holder.getmTextViewComentarios().setVisibility(View.VISIBLE);
        } else {
            holder.getmTextViewComentarios().setVisibility(View.GONE);
        }

        holder.getViewComentar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ComentariosActivity.class);
                intent.putParcelableArrayListExtra(Constants.COMENTARIOS_LIST, mAdicionales.get((int) holder.getCardContainer().getTag()).getComentarios());
                mContext.startActivity(intent);
            }
        });

        holder.getCardContainer().setTag(position);
        holder.getCardContainer().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetallePublicacionActivity.class);
                intent.putExtra(Constants.OBJETO_PERDIDO, mAdicionales.get((int) view.getTag()));
                intent.putExtra(Constants.FROM_FRAGMENT, Constants.ADICIONALES);
                mContext.startActivity(intent);
            }
        });

        holder.getTextViewHora().setText(((BaseActivity) mContext).getPublicationTime(mAdicionales.get(position).getFecha()));

    }


    @Override
    public int getItemCount() {
        if (mAdicionales != null) {
            return mAdicionales.size();
        } else {
            return 0;
        }
    }
}
