package com.managerapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.managerapp.R;
import com.managerapp.activities.BaseActivity;
import com.managerapp.activities.DetallePublicacionActivity;
import com.managerapp.db.Modelo.Denuncias;
import com.managerapp.db.Modelo.Perdidos;
import com.managerapp.fragments.PublicacionesDenunciadasFragment;
import com.managerapp.utils.Constants;
import com.managerapp.utils.CustomDialog;

import java.util.List;


public class AnimalesAdapter extends RecyclerView.Adapter<AnimalesViewHolder> {
    List<Perdidos> mPerdidos;
    private Context mContext;
    private PopupMenu mPopupMenu;
    private Fragment mFragment;
    private PopupMenuCallback mPopupMenuCallback;
    private List<Denuncias> mDenuncias;


    public AnimalesAdapter(List<Perdidos> perdidos, Context context, Fragment fragment) {
        mPerdidos = perdidos;
        mContext = context;
        mFragment = fragment;
    }

    public AnimalesAdapter(List<Perdidos> perdidos, List<Denuncias> denuncias, Context context, Fragment fragment) {
        mPerdidos = perdidos;
        mContext = context;
        mFragment = fragment;
        mDenuncias = denuncias;
    }

    public interface PopupMenuCallback {
        void onClickItem (int idItem, Perdidos perdido, Denuncias denuncia);
    }

    @Override
    public AnimalesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_animales_item, viewGroup, false);
        mPopupMenuCallback = (PopupMenuCallback) mFragment;
        return new AnimalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AnimalesViewHolder holder, final int position) {
        String raza = mPerdidos.get(position).getRaza().getRaza();
        if (raza.equals("Otra")) {
            raza = "";
        }
        String title = mPerdidos.get(position).getEspecie().getEspecie() + " " + raza + " " + mPerdidos.get(position).getColor().getColor();
        holder.getTextViewTitulo().setText(title);
        holder.getTextViewDescripcion().setText(mPerdidos.get(position).getDescripcion());
        holder.getCardEstado().setVisibility(View.VISIBLE);

        if (mPerdidos.get(position).getEstado().getSituacion().equals(mContext.getString(R.string.buscado_minus))) {
            holder.getTextViewEstado().setText(mContext.getString(R.string.buscado_mayus));
            holder.getTextViewEstado().setBackgroundResource(R.color.orange_light);
        } else {
            if (mPerdidos.get(position).getEstado().getSituacion().equals(mContext.getString(R.string.encontrado_minus))) {
                holder.getTextViewEstado().setText(mContext.getString(R.string.encontrado_mayus));
                holder.getTextViewEstado().setBackgroundResource(R.color.blue_light);
            } else {
                if (mPerdidos.get(position).getEstado().getSituacion().equals(mContext.getString(R.string.adopcion_minus))) {
                    holder.getTextViewEstado().setText(mContext.getString(R.string.adopcion_mayus));
                    holder.getTextViewEstado().setBackgroundResource(R.color.green_light);
                }
            }
        }

        byte[] foto = mPerdidos.get(position).getFoto();
        if (foto != null) {
            holder.getImageViewFoto().setImageBitmap(((BaseActivity) mContext).convertFromByteToBitmap(foto));
        } else {
            holder.getImageViewFoto().setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.placeholder));
        }
        try {
            int cantidadComentarios = mPerdidos.get(position).getComentarios().size();
            if (cantidadComentarios > 0) {
                if (cantidadComentarios == 1) {
                    holder.getmTextViewComentarios().setText(mContext.getString(R.string.un_comentario));
                } else {
                    holder.getmTextViewComentarios().setText(String.format(mContext.getString(R.string.comentarios_cantidad), cantidadComentarios));
                }
                holder.getmTextViewComentarios().setVisibility(View.VISIBLE);
                holder.getmLineSeparator().setVisibility(View.VISIBLE);
            } else {
                holder.getmLineSeparator().setVisibility(View.GONE);
                holder.getmTextViewComentarios().setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            holder.getmLineSeparator().setVisibility(View.GONE);
            holder.getmTextViewComentarios().setVisibility(View.GONE);
        }

        holder.getTextViewHora().setText(((BaseActivity) mContext).getPublicationTime(mPerdidos.get(position).getFecha()));
        holder.getCardContainer().setTag(position);
        holder.getCardContainer().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetallePublicacionActivity.class);
                intent.putExtra(Constants.FROM_FRAGMENT, Constants.PERDIDOS);
                intent.putExtra(Constants.OBJETO_PERDIDO, mPerdidos.get((int) view.getTag()));
                mContext.startActivity(intent);
            }
        });


        holder.getmImageViewOpciones().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(position);
                setUpPopupMenu(v, position);
            }
        });


        if (mFragment instanceof PublicacionesDenunciadasFragment) {
            holder.getDenunciasContainer().setVisibility(View.VISIBLE);
            holder.getDenunciasCount().setText(String.valueOf(mDenuncias.get(position).getContador()));
            holder.getDenunciasMotivo().setText(mDenuncias.get(position).getMmotivoDenuncia().getmMotivo());
        }
    }


    private void setUpPopupMenu(final View view, int position) {
        mPopupMenu = new PopupMenu(mContext, view);
        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (((BaseActivity) mContext).internet()) {
                    if (mDenuncias != null) {
                        mPopupMenuCallback.onClickItem(item.getItemId(), mPerdidos.get((Integer) view.getTag()), mDenuncias.get((Integer) view.getTag()));
                    }
                    else {
                        mPopupMenuCallback.onClickItem(item.getItemId(), mPerdidos.get((Integer) view.getTag()), null);

                    }
                } else {
                    CustomDialog.showConnectionDialog(mContext);
                }
                return true;
            }
        });
        mPopupMenu.inflate(R.menu.menu_popup);
        Menu menu = mPopupMenu.getMenu();
        if (mFragment instanceof PublicacionesDenunciadasFragment) {
            menu.findItem(R.id.item_cancelar_denuncia).setVisible(true);
            menu.findItem(R.id.item_solucionado).setVisible(false);
        } else {
            menu.findItem(R.id.item_cancelar_denuncia).setVisible(false);
        }
        mPopupMenu.show();
    }


    @Override
    public int getItemCount() {
        if (mPerdidos != null) {
            return mPerdidos.size();
        } else {
            return 0;
        }
    }
}