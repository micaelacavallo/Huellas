package com.managerapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.managerapp.HuellasApplication;
import com.managerapp.R;
import com.managerapp.activities.PrincipalActivity;
import com.managerapp.activities.PublicacionesDenunciadasActivity;
import com.managerapp.adapters.AdicionalesAdapter;
import com.managerapp.adapters.AnimalesAdapter;
import com.managerapp.db.Controladores.IDenunciasImpl;
import com.managerapp.db.Interfaces.IDenuncias;
import com.managerapp.db.Modelo.Adicionales;
import com.managerapp.db.Modelo.Denuncias;
import com.managerapp.db.Modelo.Perdidos;
import com.managerapp.utils.Constants;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micaela.cavallo on 5/19/2016.
 */
public class PublicacionesDenunciadasFragment extends BaseFragment implements AnimalesAdapter.PopupMenuCallback, AdicionalesAdapter.PopupMenuCallback {
    private List<Perdidos> mPerdidos = new ArrayList<>();
    private List<Adicionales> mAdicionales = new ArrayList<>();
    private List<Adicionales> mDonaciones = new ArrayList<>();
    private List<Adicionales> mInfoUtil = new ArrayList<>();
    private List<Denuncias> mDenunciasPerdidos = new ArrayList<>();
    private List<Denuncias> mDenunciasDonaciones = new ArrayList<>();
    private List<Denuncias> mDenunciasInfoUtil = new ArrayList<>();
    private List<Denuncias> mDenuncias = new ArrayList<>();

    private AnimalesAdapter mAnimalesAdapter;
    private AdicionalesAdapter mDonacionesAdapter;
    private AdicionalesAdapter mInfoUtilAdapter;

    private RecyclerView mRecyclerView;
    private Spinner mSpinnerTipoPublicacion;
    private TextView mViewEmptyPublicaciones;
    private IDenuncias mIDenuncias;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mFromSwipeRefresh = false;
    private View mRootView;

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_publicaciones_denunciadas, container, false);
        mAdicionales.addAll(HuellasApplication.getInstance().getDonaciones());
        mAdicionales.addAll(HuellasApplication.getInstance().getInfoUtil());
        inicializarRecycler();
        inicializarSwipeRefresh();
        mViewEmptyPublicaciones = (TextView) mRootView.findViewById(R.id.empty_publicaciones);
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

    private void inicializarSwipeRefresh() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFromSwipeRefresh = true;
                new AsyncTaskGetDenunciados().execute();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent);
    }

    private void inicializarRecycler() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list_recycler_view);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mSwipeRefreshLayout.isRefreshing();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setUpAdapters(String selectedItem) {

        switch (selectedItem) {
            case Constants.TABLA_PERDIDOS:
                mViewEmptyPublicaciones.setVisibility(View.GONE);
                mAnimalesAdapter = new AnimalesAdapter(mPerdidos, mDenunciasPerdidos, getBaseActivity(), PublicacionesDenunciadasFragment.this);
                mRecyclerView.setAdapter(mAnimalesAdapter);
                if (mAnimalesAdapter.getItemCount() == 0) {
                    mViewEmptyPublicaciones.setVisibility(View.VISIBLE);
                } else {
                    mViewEmptyPublicaciones.setVisibility(View.GONE);
                }
                break;
            case Constants.ADICIONALES_DONACIONES:
                mDonacionesAdapter = new AdicionalesAdapter(mDonaciones, mDenunciasDonaciones, getBaseActivity(), PublicacionesDenunciadasFragment.this, Constants.ADICIONALES_DONACIONES);
                mRecyclerView.setAdapter(mDonacionesAdapter);
                if (mDonacionesAdapter.getItemCount() == 0) {
                    mViewEmptyPublicaciones.setVisibility(View.VISIBLE);
                } else {
                    mViewEmptyPublicaciones.setVisibility(View.GONE);
                }
                break;
            case Constants.ADICIONALES_INFO:
                mInfoUtilAdapter = new AdicionalesAdapter(mInfoUtil, mDenunciasInfoUtil, getBaseActivity(), PublicacionesDenunciadasFragment.this, Constants.ADICIONALES_INFO);
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
    public void onClickItem(int idItem, final Adicionales adicional, final Denuncias denuncia) {
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
                                new AsyncTaskBloquear().execute(adicional.getObjectId(), denuncia.getmTabla());
                                break;
                        }
                    }
                });
                break;
            case R.id.item_cancelar_denuncia:
                ((PublicacionesDenunciadasActivity) getBaseActivity()).showNormalDialog(getBaseActivity().getString(R.string.dialog_cancelar_denuncia_descripcion), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.textView_cancelar:
                                ((PublicacionesDenunciadasActivity) getBaseActivity()).closeDialog();
                                break;
                            case R.id.textView_confirmar:
                                ((PublicacionesDenunciadasActivity) getBaseActivity()).showLoadDialog();
                                new AsyncTaskCancelarDenuncia().execute(denuncia.getmObjectId(), denuncia.getmId());
                                break;
                        }
                    }
                });
                break;
        }

    }

    @Override
    public void onClickItem(int idItem, final Perdidos perdido, final Denuncias denuncia) {
        switch (idItem) {
            case R.id.item_bloquear:
                ((PublicacionesDenunciadasActivity) getBaseActivity()).showNormalDialog(getBaseActivity().getString(R.string.dialog_bloquear_descripcion), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.textView_cancelar:
                                ((PublicacionesDenunciadasActivity) getBaseActivity()).closeDialog();
                                break;
                            case R.id.textView_confirmar:
                                ((PublicacionesDenunciadasActivity) getBaseActivity()).showLoadDialog();
                                new AsyncTaskBloquear().execute(denuncia.getmObjectId(), denuncia.getmTabla());
                                break;
                        }
                    }
                });
                break;
            case R.id.item_cancelar_denuncia:
                ((PublicacionesDenunciadasActivity) getBaseActivity()).showNormalDialog(getBaseActivity().getString(R.string.dialog_cancelar_denuncia_descripcion), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.textView_cancelar:
                                ((PublicacionesDenunciadasActivity) getBaseActivity()).closeDialog();
                                break;
                            case R.id.textView_confirmar:
                                ((PublicacionesDenunciadasActivity) getBaseActivity()).showLoadDialog();
                                new AsyncTaskCancelarDenuncia().execute(denuncia.getmObjectId(), denuncia.getmId());
                                break;
                        }
                    }
                });
                break;
        }
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
            ((PublicacionesDenunciadasActivity) getBaseActivity()).closeDialog();
            if (!error) {
                cleanLists(mSpinnerTipoPublicacion.getSelectedItem().toString(), objectID);

                Toast.makeText(getBaseActivity(), "Publicación bloqueada con éxito!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseActivity(), "No se pudo bloquear la publicación. Intente de nuevo más tarde", Toast.LENGTH_LONG).show();
            }

        }

    }

    private class AsyncTaskCancelarDenuncia extends AsyncTask<String, Void, Void> {
        private boolean error = false;
        private String objectIDDenuncia;
        private String objectIDTabla;

        @Override
        protected Void doInBackground(String... params) {
            objectIDDenuncia = params[0];
            objectIDTabla = params[1];
            try {
                mIDenuncias.borrarDenuncia(objectIDDenuncia);
            } catch (ParseException e) {
                error = true;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ((PublicacionesDenunciadasActivity) getBaseActivity()).closeDialog();
            if (!error) {
                cleanLists(mSpinnerTipoPublicacion.getSelectedItem().toString(), objectIDTabla);
                Toast.makeText(getBaseActivity(), "Denuncia cancelada con éxito!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseActivity(), "No se pudo cancelar la denuncia. Intente de nuevo más tarde", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void filterDenuncias() {
        mDenunciasDonaciones.clear();
        mDenunciasInfoUtil.clear();
        mDenunciasPerdidos.clear();
        mPerdidos.clear();
        mDonaciones.clear();
        mInfoUtil.clear();

        for (int x = 0; x < mDenuncias.size(); x++) {
            switch (mDenuncias.get(x).getmTabla()) {
                case Constants.TABLA_PERDIDOS:
                    mDenunciasPerdidos.add(mDenuncias.get(x));
                    for (Perdidos perdido : HuellasApplication.getInstance().getmPerdidos()) {
                        if (mDenuncias.get(x).getmId().equals(perdido.getObjectId())) {
                            mPerdidos.add(perdido);
                        }
                    }
                    break;
                case Constants.TABLA_ADICIONALES:
                    for (Adicionales adicionales : mAdicionales) {
                        if (mDenuncias.get(x).getmId().equals(adicionales.getObjectId())) {
                            if (adicionales.isDonacion()) {
                                mDenunciasDonaciones.add(mDenuncias.get(x));
                                mDonaciones.add(adicionales);
                            } else {
                                mDenunciasInfoUtil.add(mDenuncias.get(x));
                                mInfoUtil.add(adicionales);
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void cleanLists(String tabla, String objectID) {
        switch (tabla) {
            case Constants.TABLA_PERDIDOS:
                for (int x = 0; x < mPerdidos.size(); x++) {
                    if (objectID.equals(mPerdidos.get(x).getObjectId())) {
                        mPerdidos.remove(x);
                        mDenunciasPerdidos.remove(x);
                    }
                }
                break;
            case Constants.ADICIONALES_DONACIONES:
                for (int x = 0; x < mAdicionales.size(); x++) {
                    if (objectID.equals(mAdicionales.get(x).getObjectId())) {
                        if (mAdicionales.get(x).isDonacion()) {
                            mDenunciasDonaciones.remove(x);
                            mDonaciones.remove(x);
                        }
                        mAdicionales.remove(x);
                    }
                }
                break;

            case Constants.ADICIONALES_INFO:
                for (int x = 0; x < mAdicionales.size(); x++) {
                    if (objectID.equals(mAdicionales.get(x).getObjectId())) {
                        if (!mAdicionales.get(x).isDonacion()) {
                            mDenunciasInfoUtil.remove(x);
                            mInfoUtil.remove(x);
                        }
                        mAdicionales.remove(x);
                    }
                }
                break;
        }
        setUpAdapters(mSpinnerTipoPublicacion.getSelectedItem().toString());
    }

    private class AsyncTaskGetDenunciados extends AsyncTask<Void, Void, Void> {
        private boolean error = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!mFromSwipeRefresh) {
                getBaseActivity().showOverlay("Cargando...");
            }
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
                if (mFromSwipeRefresh) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mFromSwipeRefresh = false;
                }
                filterDenuncias();
                setUpAdapters(mSpinnerTipoPublicacion.getSelectedItem().toString());
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
