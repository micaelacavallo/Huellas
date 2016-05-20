package com.managerapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.managerapp.HuellasApplication;
import com.managerapp.R;
import com.managerapp.activities.PrincipalActivity;
import com.managerapp.adapters.AdicionalesAdapter;
import com.managerapp.adapters.AnimalesAdapter;
import com.managerapp.db.Controladores.IDenunciasImpl;
import com.managerapp.db.Interfaces.IDenuncias;
import com.managerapp.db.Modelo.Adicionales;
import com.managerapp.db.Modelo.Denuncias;
import com.managerapp.db.Modelo.Perdidos;
import com.managerapp.utils.Constants;
import com.parse.ParseException;

import java.util.List;

/**
 * Created by micaela.cavallo on 5/19/2016.
 */
public class PublicacionesDenunciadasFragment extends BaseFragment implements AnimalesAdapter.PopupMenuCallback, AdicionalesAdapter.PopupMenuCallback {
    private List<Perdidos> mPerdidos;
    private List<Adicionales> mDonaciones;
    private List<Adicionales> mInfoUtil;
    private List<Denuncias> mDenunciasPerdidos;
    private List<Denuncias> mDenunciasDonaciones;
    private List<Denuncias> mDenunciasInfoUtil;
    private List<Denuncias> mDenuncias;

    private AnimalesAdapter mAnimalesAdapter;
    private AdicionalesAdapter mDonacionesAdapter;
    private AdicionalesAdapter mInfoUtilAdapter;

    private RecyclerView mRecyclerView;
    private Spinner mSpinnerTipoPublicacion;
    private TextView mViewEmptyPublicaciones;
    private IDenuncias mIDenuncias;
    private View mRootView;

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_publicaciones_denunciadas, container, false);

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list_recycler_view);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(getContext());
        mViewEmptyPublicaciones = (TextView) mRootView.findViewById(R.id.empty_publicaciones);
        mRecyclerView.setLayoutManager(LinearLayoutManager);
        mSpinnerTipoPublicacion = (Spinner) mRootView.findViewById(R.id.spinner_tipo_publi);
        mSpinnerTipoPublicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppCompatTextView textViewItem = ((AppCompatTextView) view);
                textViewItem.setTextAppearance(getBaseActivity(), R.style.condensed_normal_19);
                setUpAdapters(textViewItem.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mIDenuncias = new IDenunciasImpl(getBaseActivity());
        new AsyncTaskGetDenunciados().execute();
        return mRootView;
    }

    private void setUpAdapters(String selectedItem) {

        switch (selectedItem) {
            case Constants.TABLA_PERDIDOS:
                mViewEmptyPublicaciones.setVisibility(View.GONE);
                mAnimalesAdapter = new AnimalesAdapter(mPerdidos, getBaseActivity(), PublicacionesDenunciadasFragment.this);
                mRecyclerView.setAdapter(mAnimalesAdapter);
                if (mAnimalesAdapter.getItemCount() == 0) {
                    mViewEmptyPublicaciones.setVisibility(View.VISIBLE);
                } else {
                    mViewEmptyPublicaciones.setVisibility(View.GONE);
                }
                break;
            case Constants.ADICIONALES_DONACIONES:
                mDonacionesAdapter = new AdicionalesAdapter(mDonaciones, getBaseActivity(), PublicacionesDenunciadasFragment.this, Constants.ADICIONALES_DONACIONES);
                mRecyclerView.setAdapter(mDonacionesAdapter);
                if (mDonacionesAdapter.getItemCount() == 0) {
                    mViewEmptyPublicaciones.setVisibility(View.VISIBLE);
                } else {
                    mViewEmptyPublicaciones.setVisibility(View.GONE);
                }
                break;
            case Constants.ADICIONALES_INFO:
                mInfoUtilAdapter = new AdicionalesAdapter(mInfoUtil, getBaseActivity(), PublicacionesDenunciadasFragment.this, Constants.ADICIONALES_INFO);
                mRecyclerView.setAdapter(mInfoUtilAdapter);
                if (mInfoUtilAdapter.getItemCount() == 0) {
                    mViewEmptyPublicaciones.setVisibility(View.VISIBLE);
                } else {
                    mViewEmptyPublicaciones.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }


    @Override
    public void onClickItem(int idItem, final Adicionales adicional, final String tabla) {
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
                                new AsyncTaskBloquear().execute(adicional.getObjectId(), tabla);
                                break;
                        }
                    }
                });
                break;
        }

    }

    @Override
    public void onClickItem(int idItem, Perdidos perdido, String tabla) {

    }

    private class AsyncTaskBloquear extends AsyncTask<String, Void, Void> {
        private boolean error = false;
        private String objectID;
        private String tabla;

        @Override
        protected Void doInBackground(String... params) {
            objectID = params[0];
            tabla = params[1];
            try {
                mIDenuncias.confirmarDenuncia(objectID);
            } catch (ParseException e) {
                error = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ((PrincipalActivity) getBaseActivity()).closeDialog();
            if (!error) {
                switch (tabla) {
                    case Constants.TABLA_PERDIDOS:
                        break;
                    case Constants.TABLA_ADICIONALES:
                        break;
                }
                List<Perdidos> perdidos = HuellasApplication.getInstance().getmPerdidos();
                for (int x = 0; x < perdidos.size(); x++) {
                    if (objectID.equals(perdidos.get(x).getObjectId())) {
                        perdidos.remove(x);
                    }
                }
              //  mAdapterAnimales.notifyDataSetChanged();
                Toast.makeText(getBaseActivity(), "Publicación bloqueada con éxito!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseActivity(), "No se pudo bloquear la publicación. Intente de nuevo más tarde", Toast.LENGTH_LONG).show();
            }

        }

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
                error = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!error) {
                getBaseActivity().hideOverlay();
            } else {
                getBaseActivity().showMessageOverlay("Problema inesperado. Intente nuevamente", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getBaseActivity().finish();
                    }
                });
            }
        }
    }
}
