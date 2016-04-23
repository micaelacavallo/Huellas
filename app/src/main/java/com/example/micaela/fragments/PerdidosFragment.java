package com.example.micaela.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.micaela.HuellasApplication;
import com.example.micaela.activities.LoginActivity;
import com.example.micaela.activities.PrincipalActivity;
import com.example.micaela.adapters.AnimalesAdapter;
import com.example.micaela.db.Controladores.IPerdidosImpl;
import com.example.micaela.db.DAO.GeneralDAO;
import com.example.micaela.db.DAO.PerdidosDAO;
import com.example.micaela.db.Interfaces.IPerdidos;
import com.example.micaela.db.Modelo.Estados;
import com.example.micaela.db.Modelo.Perdidos;
import com.example.micaela.huellas.R;
import com.parse.ParseException;

import java.util.List;


public class PerdidosFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private IPerdidos mIperdidosImpl;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perdidos, container, false);
        mIperdidosImpl = new IPerdidosImpl(getActivity().getApplicationContext());
        inicializarSwipeRefresh(view);
        inicializarRecycler(view);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new AsyncTaskPerdidos().execute();
    }

    private void inicializarSwipeRefresh(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTaskPerdidos().execute();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent);
    }

    private void inicializarRecycler(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if ((newState == RecyclerView.SCROLL_STATE_DRAGGING) || (newState == RecyclerView.SCROLL_STATE_SETTLING)) {
                    ((PrincipalActivity) getBaseActivity()).getActionButton().hide();
                } else {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        ((PrincipalActivity) getBaseActivity()).getActionButton().show();
                    }
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private class AsyncTaskPerdidos extends AsyncTask<Void, Void, List<Perdidos>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getBaseActivity().showOverlay(getString(R.string.cargando_publicaciones_mensaje));
        }

        @Override
        protected void onPostExecute(final List<Perdidos> perdidosList) {
            super.onPostExecute(perdidosList);
            AnimalesAdapter mAdapter = new AnimalesAdapter(perdidosList, getBaseActivity());
            mRecyclerView.setAdapter(mAdapter);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);

                }
            });

            new AsyncTaskPerdidosInfo().execute();
        }

        @Override
        protected List<Perdidos> doInBackground(Void... voids) {
            try {
                mIperdidosImpl.cargarDBLocal(getBaseActivity());
                return mIperdidosImpl.getPerdidos();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;

        }
    }

    private class AsyncTaskPerdidosInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                PerdidosDAO perdidosDAO = new PerdidosDAO(getActivity());
                HuellasApplication.getInstance().setmEspecies(perdidosDAO.getEspecies());
                HuellasApplication.getInstance().setmRazas(perdidosDAO.getRazas());
                HuellasApplication.getInstance().setmColores(perdidosDAO.getColores());
                HuellasApplication.getInstance().setmEdades(perdidosDAO.getEdades());
                HuellasApplication.getInstance().setmTamanios(perdidosDAO.getTamaños());
                HuellasApplication.getInstance().setmSexos(perdidosDAO.getSexos());

                GeneralDAO generalDAO = new GeneralDAO(getBaseActivity());
                List<Estados> estados = generalDAO.getEstados();
                for (int x = 0; x<estados.size(); x++) {
                    if (estados.get(x).getSituacion().equals("-")) {
                        estados.remove(x);
                    }
                }
                HuellasApplication.getInstance().setmEstados(estados);
            }
            catch (Exception e) {
                getBaseActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        View.OnClickListener listener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getBaseActivity().finish();
                                Intent intent = new Intent(getBaseActivity(), LoginActivity.class);
                                startActivity(intent);
                            }
                        };
                        getBaseActivity().showMessageOverlay("Hubo un problema, por favor intente nuevamente", listener);
                    }
                });
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getBaseActivity().hideOverlay();
        }
    }

}
