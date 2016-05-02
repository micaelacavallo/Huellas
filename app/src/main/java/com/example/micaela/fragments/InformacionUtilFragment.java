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
import com.example.micaela.adapters.AdicionalesAdapter;
import com.example.micaela.db.Controladores.IAdicionalesImpl;
import com.example.micaela.db.Interfaces.IAdicionales;
import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.db.Modelo.Comentarios;
import com.example.micaela.huellas.R;
import com.parse.ParseException;

import java.util.List;

public class InformacionUtilFragment extends BaseFragment implements AltaAnimalesFragment.AdapterCallback, ComentariosFragment.AdapterCallback {

    private RecyclerView mRecyclerView;
    private IAdicionales mIAdicionalesImpl;
    List<Adicionales> adicionales;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    AdicionalesAdapter mAdapterAdicionales;


    private static InformacionUtilFragment mInstanceInfo;

    public static InformacionUtilFragment getInstance() {
        if (mInstanceInfo == null) {
            mInstanceInfo = new InformacionUtilFragment();
        }
        return mInstanceInfo;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mInstanceInfo = null;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_util, container, false);
        mIAdicionalesImpl = new IAdicionalesImpl(getActivity().getApplicationContext());

        inicializarSwipeRefresh(view);
        inicializarRecycler(view);

        setHasOptionsMenu(false);
        getBaseActivity().showOverlay(getString(R.string.cargando_publicaciones_mensaje));
        new AsyncTaskAdicionales().execute();
        return view;
    }


    private void inicializarSwipeRefresh(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTaskAdicionales().execute();
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
    public void updateDataSetAdapterPublicaciones(Object objeto) {
        adicionales.add(0, (Adicionales) objeto);
        mAdapterAdicionales.notifyDataSetChanged();
    }

    @Override
    public void updateDataSetAdapterComentarios(Comentarios comentario, Object object) {
        for (Adicionales adicional : adicionales) {
            if (adicional.getObjectId().equals(((Adicionales) object).getObjectId())) {
                adicional.getComentarios().add(comentario);
            }
        }
        mAdapterAdicionales.notifyDataSetChanged();
    }

    private class AsyncTaskAdicionales extends AsyncTask<Void, Void, List<Adicionales>> {

        @Override
        protected void onPostExecute(List<Adicionales> adicionalesList) {
            super.onPostExecute(adicionalesList);
            adicionales = adicionalesList;
            mAdapterAdicionales = new AdicionalesAdapter(adicionales, getContext());
            mRecyclerView.setAdapter(mAdapterAdicionales);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((BaseActivity) getActivity()).hideOverlay();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }

        @Override
        protected List<Adicionales> doInBackground(Void... voids) {
            try {
                mIAdicionalesImpl.cargarDBLocalInfoUtil(getBaseActivity());
                return mIAdicionalesImpl.getInfoUtil();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;

        }
    }

}
