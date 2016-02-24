package com.example.micaela.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.micaela.activities.BaseActivity;
import com.example.micaela.activities.PrincipalActivity;
import com.example.micaela.adapters.AnimalesAdapter;
import com.example.micaela.db.Controladores.IPerdidosImpl;
import com.example.micaela.db.Interfaces.IPerdidos;
import com.example.micaela.db.Modelo.Colores;
import com.example.micaela.db.Modelo.Edades;
import com.example.micaela.db.Modelo.Especies;
import com.example.micaela.db.Modelo.Estados;
import com.example.micaela.db.Modelo.Perdidos;
import com.example.micaela.db.Modelo.Personas;
import com.example.micaela.db.Modelo.Razas;
import com.example.micaela.db.Modelo.Sexos;
import com.example.micaela.db.Modelo.Tamaños;
import com.example.micaela.huellas.R;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import java.util.Date;
import java.util.List;


public class PerdidosFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private IPerdidos mIperdidosImpl;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perdidos, container, false);
        mIperdidosImpl = new IPerdidosImpl(getActivity().getApplicationContext());
        ((BaseActivity) getActivity()).showOverlay("Cargando publicaciones...");
        new AsyncTaskPerdidos().execute();
        inicializarSwipeRefresh(view);
        inicializarRecycler(view);


        return view;
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


    private class AsyncTaskPerdidos extends AsyncTask<Void, Void, List<Perdidos>> {

        @Override
        protected void onPostExecute(final List<Perdidos> perdidosList) {
            super.onPostExecute(perdidosList);
            AnimalesAdapter mAdapter = new AnimalesAdapter(perdidosList, getContext());
            mRecyclerView.setAdapter(mAdapter);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((BaseActivity)getActivity()).hideOverlay();
                    Perdidos per = new Perdidos(5000, new Edades("adulto"), new Razas("San Bernardo"), new Especies("conejo"), new Tamaños("grande"), new Colores("marron claro"), new Sexos("macho"), new Estados("buscado"),  new Personas("admin@gmail.com"), new Date(), new ParseGeoPoint(30,30), "holahola", "holahola", null, null, false);
                    mIperdidosImpl.savePerdido(per);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }

        @Override
        protected List<Perdidos> doInBackground(Void... voids) {
            try {
                mIperdidosImpl.cargarDBLocal(getActivity().getBaseContext());
                return mIperdidosImpl.getPerdidos();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;

        }
    }
}
