package com.managerapp.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.managerapp.R;
import com.managerapp.activities.PrincipalActivity;
import com.managerapp.adapters.AdicionalesAdapter;
import com.managerapp.adapters.AnimalesAdapter;
import com.managerapp.db.Controladores.IDenunciasImpl;
import com.managerapp.db.Interfaces.IDenuncias;
import com.managerapp.db.Modelo.Adicionales;
import com.managerapp.db.Modelo.Denuncias;
import com.managerapp.db.Modelo.Perdidos;
import com.parse.ParseException;

import java.util.List;

/**
 * Created by micaela.cavallo on 5/19/2016.
 */
public class PublicacionesDenunciadasFragment extends BaseFragment implements AnimalesAdapter.PopupMenuCallback, AdicionalesAdapter.PopupMenuCallback {
    private List<Perdidos> mPerdidos;
    private List<Adicionales> mDonaciones;
    private List<Adicionales> mInfoUtil;
    private List<Denuncias> mDenuncias;

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publicaciones_denunciadas, container, false);
        new AsyncTaskGetDenunciados().execute();
        return  view;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onClickItem(int idItem, Adicionales adicional) {
            switch (idItem) {
                case R.id.item_bloquear:
                    ((PrincipalActivity) getBaseActivity()).showNormalDialog(getBaseActivity().getString(R.string.dialog_bloquear_descripcion), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.textView_cancelar:
                                    ((PrincipalActivity) getBaseActivity()).closeDialog();
                                    break;
                                case R.id.textView_confirmar:
                                    ((PrincipalActivity) getBaseActivity()).showLoadDialog();
                            //        new AsyncTaskDeletePerdido().execute(perdido);
                                    break;
                            }
                        }
                    });
                    break;
            }

        }

    @Override
    public void onClickItem(int idItem, Perdidos perdido) {

    }


    private class AsyncTaskGetDenunciados extends AsyncTask<Void, Void, Void> {
        private boolean error = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getBaseActivity().showOverlay("Cargando...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            IDenuncias iDenuncias = new IDenunciasImpl(getBaseActivity());
            try {
               mDenuncias = iDenuncias.getDenuncias();
            } catch (ParseException e) {
                e.printStackTrace();
                error =true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!error) {
                getBaseActivity().hideOverlay();
            }
            else {
                getBaseActivity().showMessageOverlay("Problema inesperado. Intente nuevamente", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent aIntent = new Intent(getBaseActivity(), PrincipalActivity.class);
                        aIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(aIntent);
                    }
                });
            }
        }
    }
}
