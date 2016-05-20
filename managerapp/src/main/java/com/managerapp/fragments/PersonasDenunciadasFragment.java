package com.managerapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.managerapp.HuellasApplication;
import com.managerapp.R;
import com.managerapp.activities.PersonasDenunciadasActivity;
import com.managerapp.adapters.PersonasAdapter;
import com.managerapp.db.Controladores.IDenunciasImpl;
import com.managerapp.db.Interfaces.IDenuncias;
import com.managerapp.db.Modelo.Denuncias;
import com.managerapp.db.Modelo.Personas;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Micaela on 20/05/2016.
 */
public class PersonasDenunciadasFragment extends BaseFragment implements PersonasAdapter.PopupMenuCallback {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mEmptyLayout;
    private RecyclerView mRecyclerView;
    private View mRootView;
    private List<Denuncias> mDenuncias;
    private List<Personas> mPersonas;
    private PersonasAdapter mAdapter;
    private boolean mFromSwipeRefresh = false;
    private IDenuncias iDenuncias;

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_personas_denunciadas, container, false);
        mEmptyLayout = mRootView.findViewById(R.id.empty_personas_denunciadas);
        inicializarRecycler();
        inicializarSwipeRefresh();
        mDenuncias = new ArrayList<>();
        mPersonas = new ArrayList<>();
        iDenuncias = new IDenunciasImpl(getBaseActivity());
        mAdapter = new PersonasAdapter(mDenuncias, mPersonas, getBaseActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
        new AsyncTaskGetDenuncias().execute();
        return mRootView;
    }

    private void inicializarSwipeRefresh() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFromSwipeRefresh = true;
                new AsyncTaskGetDenuncias().execute();
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

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onClickItem(int idItem, final Denuncias denuncia) {
        switch (idItem) {
            case R.id.item_bloquear:
                ((PersonasDenunciadasActivity) getBaseActivity()).showNormalDialog(getBaseActivity().getString(R.string.dialog_bloquear_persona_descripcion), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.textView_cancelar:
                                ((PersonasDenunciadasActivity) getBaseActivity()).closeDialog();
                                break;
                            case R.id.textView_confirmar:
                                ((PersonasDenunciadasActivity) getBaseActivity()).showLoadDialog();
                                new AsyncTaskBloquearPersona().execute(denuncia.getmObjectId(), denuncia.getmId());
                                break;
                        }
                    }
                });
                break;
            case R.id.item_cancelar_denuncia:
                ((PersonasDenunciadasActivity) getBaseActivity()).showNormalDialog(getBaseActivity().getString(R.string.dialog_cancelar_denuncia_descripcion), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.textView_cancelar:
                                ((PersonasDenunciadasActivity) getBaseActivity()).closeDialog();
                                break;
                            case R.id.textView_confirmar:
                                ((PersonasDenunciadasActivity) getBaseActivity()).showLoadDialog();
                                new AsyncTaskCancelarDenuncia().execute(denuncia.getmObjectId(), denuncia.getmId());
                                break;
                        }
                    }
                });
                break;
        }
    }

    private void cleanLists(String objectID) {
        List<Personas> personas = HuellasApplication.getInstance().getmPersonas();
        for (int x = 0; x < personas.size(); x++) {
            if (personas.get(x).getObjectId().equals(objectID)) {
                personas.remove(x);
            }
        }

        List<Denuncias> denuncias = HuellasApplication.getInstance().getmDenuncias();
        for (int x = 0; x < denuncias.size(); x++) {
            if (denuncias.get(x).getmId().equals(objectID)) {
                denuncias.remove(x);
            }
        }

        filterDenunciasByPersonas();

        adapterNotify();
    }


    public void adapterNotify() {
        mAdapter.notifyDataSetChanged();
        if (mAdapter.getItemCount() == 0) {
            mEmptyLayout.setVisibility(View.VISIBLE);
        } else {
            mEmptyLayout.setVisibility(View.GONE);
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
                iDenuncias.borrarDenuncia(objectIDDenuncia);
            } catch (ParseException e) {
                error = true;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ((PersonasDenunciadasActivity) getBaseActivity()).closeDialog();
            if (!error) {
                cleanLists(objectIDTabla);
                Toast.makeText(getBaseActivity(), "Denuncia cancelada con éxito!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseActivity(), "No se pudo cancelar la denuncia. Intente de nuevo más tarde", Toast.LENGTH_LONG).show();
            }

        }
    }


    private class AsyncTaskBloquearPersona extends AsyncTask<String, Void, Void> {
        private boolean error = false;
        private String objectIDDenuncia;
        private String objectIDTabla;

        @Override
        protected Void doInBackground(String... params) {
            objectIDDenuncia = params[0];
            objectIDTabla = params[1];
            try {
                iDenuncias.confirmarDenuncia(objectIDDenuncia);
            } catch (ParseException e) {
                error = true;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ((PersonasDenunciadasActivity) getBaseActivity()).closeDialog();
            if (!error) {
                cleanLists(objectIDTabla);
                Toast.makeText(getBaseActivity(), "Persona bloqueada con éxito!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseActivity(), "No se pudo bloquear esta persona. Intente de nuevo más tarde", Toast.LENGTH_LONG).show();
            }

        }
    }

    private class AsyncTaskGetDenuncias extends AsyncTask<Void, Void, Void> {
        private boolean error = false;
        private List<Denuncias> denunciasList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!mFromSwipeRefresh) {
                getBaseActivity().showOverlay(getString(R.string.cargando_publicaciones_mensaje));
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                denunciasList = iDenuncias.getDenuncias();
            } catch (ParseException e) {
                error = true;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getBaseActivity().hideOverlay();
            if (error) {
                getBaseActivity().showMessageOverlay("Problema inesperado. Intente nuevamente", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getBaseActivity().finish();
                    }
                });
            } else {
                if (mFromSwipeRefresh) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mFromSwipeRefresh = false;
                }

                HuellasApplication.getInstance().setmDenuncias(denunciasList);
                filterDenunciasByPersonas();
                adapterNotify();
            }
        }

    }

    private void filterDenunciasByPersonas() {
        mDenuncias.clear();
        mPersonas.clear();
        mDenuncias.addAll(HuellasApplication.getInstance().getmDenuncias());
        for (int x = 0; x < mDenuncias.size(); x++) {
            if (!mDenuncias.get(x).ismUser()) {
                mDenuncias.remove(x);
                x--;
            } else {

                for (Personas persona : HuellasApplication.getInstance().getmPersonas()) {
                    if (mDenuncias.get(x).getmId().equals(persona.getObjectId())) {
                        mPersonas.add(persona);
                    }
                }
            }
        }
    }
}
