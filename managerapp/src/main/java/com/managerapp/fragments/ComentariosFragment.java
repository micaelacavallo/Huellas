package com.managerapp.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.managerapp.R;
import com.managerapp.adapters.ComentariosAdapter;
import com.managerapp.db.Controladores.IAdicionalesImpl;
import com.managerapp.db.Controladores.IPerdidosImpl;
import com.managerapp.db.Interfaces.IAdicionales;
import com.managerapp.db.Interfaces.IPerdidos;
import com.managerapp.db.Modelo.Adicionales;
import com.managerapp.db.Modelo.Perdidos;
import com.managerapp.utils.Constants;
import com.parse.ParseException;


public class ComentariosFragment extends BaseFragment {
    private View mRootView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ComentariosAdapter mAdapter;
    private Adicionales mAdicional;
    private Perdidos mPerdido;
    private IPerdidos mIPerdidosImpl;
    private IAdicionales mIAdicionalesImpl;
    private String mFromFragment = "";



    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_comentarios, container, false);
        mIPerdidosImpl = new IPerdidosImpl(getBaseActivity());
        mIAdicionalesImpl = new IAdicionalesImpl(getBaseActivity());
        inicializarSwipeRefresh();

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Intent intent = getBaseActivity().getIntent();
        mFromFragment = intent.getStringExtra(Constants.FROM_FRAGMENT);

        if (Constants.PERDIDOS.equals(mFromFragment)) {
            mPerdido = intent.getParcelableExtra(Constants.COMENTARIOS_LIST);
            mAdapter = new ComentariosAdapter(mPerdido.getComentarios(), getBaseActivity());

        } else {
            mAdicional = intent.getParcelableExtra(Constants.COMENTARIOS_LIST);
            mAdapter = new ComentariosAdapter(mAdicional.getComentarios(), getBaseActivity());
        }

        mRecyclerView.setAdapter(mAdapter);
        return mRootView;
    }

    @Override
    public boolean onBackPressed() {
            return false;
    }

    private void inicializarSwipeRefresh() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTaskTraerComentarios().execute();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent);
    }

    private class AsyncTaskTraerComentarios extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            if (Constants.PERDIDOS.equals(mFromFragment)) {
                try {
                    mPerdido = mIPerdidosImpl.getPublicacionPerdidosById(mPerdido.getObjectId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                mAdicional = mIAdicionalesImpl.getAdicionalById(mAdicional.getObjectId());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mSwipeRefreshLayout.setRefreshing(false);
            mAdapter.notifyDataSetChanged();
        }
    }


    private class AsyncTaskGetPerdidoById extends AsyncTask<String, Void, Void> {
        private boolean error = false;

        @Override
        protected Void doInBackground(String... params) {
            if (mFromFragment.equals(Constants.PERDIDOS)) {
                try {
                    mPerdido = mIPerdidosImpl.getPublicacionPerdidosById(params[0]);
                } catch (ParseException e) {
                    e.printStackTrace();
                    error = true;
                }
            } else {
                mAdicional = mIAdicionalesImpl.getAdicionalById(params[0]);
                if (mAdicional == null) {
                    error = true;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getBaseActivity().hideOverlay();
            if (error) {
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBaseActivity().loadMainScreen();
                    }
                };
                getBaseActivity().showMessageOverlay("Hubo un problema, por favor intente nuevamente", listener);
            } else {
                if (mFromFragment.equals(Constants.PERDIDOS)) {
                    mAdapter = new ComentariosAdapter(mPerdido.getComentarios(), getBaseActivity());
                } else {
                    mAdapter = new ComentariosAdapter(mAdicional.getComentarios(), getBaseActivity());
                }
                mRecyclerView.setAdapter(mAdapter);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getBaseActivity().showOverlay(getString(R.string.cargando_publicaciones_mensaje));
        }
    }
}
