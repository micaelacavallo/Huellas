package com.managerapp.fragments;

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
import com.managerapp.db.Modelo.Adicionales;
import com.managerapp.db.Modelo.Perdidos;
import com.managerapp.utils.Constants;


public class ComentariosFragment extends BaseFragment {
    private View mRootView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ComentariosAdapter mAdapter;
    private Adicionales mAdicional;
    private Perdidos mPerdido;
    private IPerdidosImpl mIPerdidosImpl;
    private IAdicionalesImpl mIAdicionalesImpl;
    private String mFromFragment = "";



    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_comentarios, container, false);
        inicializarSwipeRefresh();

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mFromFragment = getBaseActivity().getIntent().getStringExtra(Constants.FROM_FRAGMENT);
        if (Constants.PERDIDOS.equals(mFromFragment)) {
            mPerdido = getBaseActivity().getIntent().getParcelableExtra(Constants.COMENTARIOS_LIST);
            mIPerdidosImpl = new IPerdidosImpl(getBaseActivity());
            mAdapter = new ComentariosAdapter(mPerdido.getComentarios(), getBaseActivity());
        } else {
            mAdicional = getBaseActivity().getIntent().getParcelableExtra(Constants.COMENTARIOS_LIST);
            mIAdicionalesImpl = new IAdicionalesImpl(getBaseActivity());
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

}
