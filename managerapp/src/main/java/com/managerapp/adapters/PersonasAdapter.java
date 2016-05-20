package com.managerapp.adapters;

import android.content.Context;
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
import com.managerapp.db.Modelo.Denuncias;
import com.managerapp.db.Modelo.Personas;
import com.managerapp.utils.CircleImageTransform;
import com.managerapp.utils.CustomDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Micaela on 20/05/2016.
 */
public class PersonasAdapter extends RecyclerView.Adapter<PersonasViewHolder> {
    private List<Denuncias> mDenuncias;
    private List<Personas> mPersonas;
    private Context mContext;
    private Fragment mFragment;
    private PopupMenuCallback mPopupMenuCallback;
    private PopupMenu mPopupMenu;

    public PersonasAdapter(List<Denuncias> denuncias, List<Personas> personas, Context context, Fragment fragment) {
        mDenuncias = denuncias;
        mContext = context;
        mPersonas = personas;
        mFragment = fragment;
    }

    public interface PopupMenuCallback {
        void onClickItem(int idItem, Denuncias denuncia);
    }

    @Override
    public PersonasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_personas_denunciadas_item, parent, false);
        mPopupMenuCallback = (PopupMenuCallback) mFragment;
        return new PersonasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonasViewHolder holder, final int position) {
        Picasso.with(mContext).load(mPersonas.get(position).getmFoto()).transform(new CircleImageTransform()).placeholder(R.mipmap.placeholder).into(holder.getmImageViewFoto());
        if (!mPersonas.get(position).getTelefono().equals("")) {
            holder.getmTextViewTelefono().setVisibility(View.VISIBLE);
            holder.getmTextViewTelefono().setText(mPersonas.get(position).getTelefono());
        } else {
            holder.getmTextViewTelefono().setVisibility(View.GONE);
        }

        holder.getmTextViewEmail().setText(mPersonas.get(position).getEmail());
        holder.getmTextViewNombre().setText(mPersonas.get(position).getNombre());
        holder.getmImageViewMoreOptions().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(position);
                setUpPopupMenu(v);
            }
        });
    }

    private void setUpPopupMenu(final View view) {
        mPopupMenu = new PopupMenu(mContext, view);
        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (((BaseActivity) mContext).internet()) {
                    mPopupMenuCallback.onClickItem(item.getItemId(), mDenuncias.get((Integer) view.getTag()));
                } else {
                    CustomDialog.showConnectionDialog(mContext);
                }
                return true;
            }
        });
        mPopupMenu.inflate(R.menu.menu_popup);
        Menu menu = mPopupMenu.getMenu();
        menu.findItem(R.id.item_solucionado).setVisible(false);
        mPopupMenu.show();
    }

    @Override
    public int getItemCount() {
        if (mDenuncias == null) {
            return 0;
        } else {
            return mDenuncias.size();
        }
    }
}
