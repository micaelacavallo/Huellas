package com.managerapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.managerapp.R;
import com.managerapp.activities.BaseActivity;
import com.managerapp.db.Modelo.Comentarios;
import com.managerapp.utils.CircleImageTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by micaela.cavallo on 4/29/2016.
 */
public class ComentariosAdapter extends RecyclerView.Adapter<ComentariosViewHolder> {
    List<Comentarios> mComentarios;
    private Context mContext;

    public ComentariosAdapter(List<Comentarios> comentarios, Context context) {
        mComentarios = comentarios;
        mContext = context;
    }

    @Override
    public ComentariosViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_comentarios_item, viewGroup, false);
        return new ComentariosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ComentariosViewHolder holder, final int position) {


        if (position == 0) {
            holder.getmViewLine().setVisibility(View.GONE);
        }
        else {
            holder.getmViewLine().setVisibility(View.VISIBLE);
        }


        String mFoto = mComentarios.get(position).getPersona().getmFoto();
        if (mFoto == null || mFoto.equals("")) {
            Picasso.with(mContext).load(R.mipmap.placeholder).transform(new CircleImageTransform()).into(holder.getImageViewFoto());
        } else {
            Picasso.with(mContext).load(Uri.parse(mFoto)).placeholder(R.mipmap.placeholder).transform(new CircleImageTransform()).into(holder.getImageViewFoto());
        }
        holder.getTextViewComentario().setText(mComentarios.get(position).getComentario());
        holder.getTextViewFecha().setText(((BaseActivity) mContext).getFormattedDate2(mComentarios.get(position).getFecha()));
        holder.getTextViewNombre().setText(mComentarios.get(position).getPersona().getNombre());
    }


    @Override
    public int getItemCount() {
        if (mComentarios != null) {
            return mComentarios.size();
        } else {
            return 0;
        }
    }
}
