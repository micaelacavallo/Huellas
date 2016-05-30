package com.example.micaela.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.micaela.HuellasApplication;
import com.example.micaela.activities.BaseActivity;
import com.example.micaela.activities.ComentariosActivity;
import com.example.micaela.activities.DetallePublicacionActivity;
import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.Constants;
import com.example.micaela.utils.CustomDialog;

import java.util.List;


public class AdicionalesAdapter extends RecyclerView.Adapter<AdicionalesViewHolder> {
    List<Adicionales> mAdicionales;
    private Context mContext;
    private PopupMenu mPopupMenu;
    private String mTipoPublicacion;
    private Fragment mFragment;
    private PopupMenuCallback mPopupMenuCallback;

    public AdicionalesAdapter(List<Adicionales> adicionales, Context context, Fragment fragment, String tipoPublicacion) {
        mAdicionales = adicionales;
        mContext = context;
        mFragment = fragment;
        mTipoPublicacion = tipoPublicacion;
    }

    public interface PopupMenuCallback {
        void onClickItem(int idItem, Adicionales adicional);
    }

    @Override
    public AdicionalesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_animales_item, viewGroup, false); //ver ok
        mPopupMenuCallback = (PopupMenuCallback) mFragment;
        return new AdicionalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdicionalesViewHolder holder, final int position) {
        holder.getTextViewTitulo().setText(mAdicionales.get(position).getTitulo());
        holder.getTextViewDescripcion().setText(mAdicionales.get(position).getDescripcion());

        byte[] foto = mAdicionales.get(position).getFoto();
        if (foto != null) {
            holder.getImageViewFoto().setImageBitmap(((BaseActivity) mContext).convertFromByteToBitmap(foto));
        }

        try {
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
        } catch (NullPointerException e) {
            holder.getmTextViewComentarios().setVisibility(View.GONE);
        }

        holder.getViewComentar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((BaseActivity) mContext).internet()) {
                Intent intent = new Intent(mContext, ComentariosActivity.class);
                intent.putExtra(Constants.COMENTARIOS_LIST, mAdicionales.get((int) holder.getCardContainer().getTag()));

                if (mTipoPublicacion.equals(Constants.ADICIONALES_DONACIONES)) {
                    intent.putExtra(Constants.FROM_FRAGMENT, Constants.ADICIONALES_DONACIONES);
                } else {
                    intent.putExtra(Constants.FROM_FRAGMENT, Constants.ADICIONALES_INFO);
                }
                mContext.startActivity(intent);
            } else {
                CustomDialog.showConnectionDialog(mContext);
            }
            }
        });

        holder.getCardContainer().setTag(position);
        holder.getCardContainer().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(mContext, DetallePublicacionActivity.class);
                    intent.putExtra(Constants.OBJETO_PERDIDO, mAdicionales.get((int) view.getTag()));
                    if (mTipoPublicacion.equals(Constants.ADICIONALES_DONACIONES)) {
                        intent.putExtra(Constants.FROM_FRAGMENT, Constants.ADICIONALES_DONACIONES);
                    } else {
                        intent.putExtra(Constants.FROM_FRAGMENT, Constants.ADICIONALES_INFO);
                    }
                    mContext.startActivity(intent);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });


        holder.getmImageViewOpciones().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(position);
                setUpPopupMenu(v, position);
            }
        });

        holder.getTextViewHora().setText(((BaseActivity) mContext).getPublicationTime(mAdicionales.get(position).getFecha()));

    }

    private void setUpPopupMenu(final View view, final int position) {
        mPopupMenu = new PopupMenu(mContext, view);
        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (((BaseActivity) mContext).internet()) {
                    mPopupMenuCallback.onClickItem(item.getItemId(), mAdicionales.get((Integer) view.getTag()));
                } else {
                    CustomDialog.showConnectionDialog(mContext);
                }
                return true;
            }
        });
        mPopupMenu.inflate(R.menu.menu_popup);
        Menu menu = mPopupMenu.getMenu();
        menu.removeItem(R.id.item_solucionado);
        if (!mAdicionales.get(position).getPersona().getEmail().equals(HuellasApplication.getInstance().getProfileEmailFacebook())) {
            menu.removeItem(R.id.item_editar);
            menu.removeItem(R.id.item_eliminar);
        } else {
            menu.removeItem(R.id.item_reportar_publicacion);
            menu.removeItem(R.id.item_reportar_usuario);
        }
        mPopupMenu.show();
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
