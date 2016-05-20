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
import com.managerapp.activities.PrincipalActivity;
import com.managerapp.adapters.AdicionalesAdapter;
import com.managerapp.db.Controladores.IAdicionalesImpl;
import com.managerapp.db.Interfaces.IAdicionales;
import com.managerapp.db.Modelo.Adicionales;
import com.managerapp.utils.Constants;
import com.parse.ParseException;

import java.util.List;

public class DonacionesFragment extends BaseFragment implements AdicionalesAdapter.PopupMenuCallback {
    private RecyclerView mRecyclerView;
    private IAdicionales mIAdicionalesImpl;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private AdicionalesAdapter mAdapterAdicionales;

    private static DonacionesFragment mInstanceDonacion;

    private boolean mFromSwipeRefresh = false;

    public static DonacionesFragment getInstance() {
        if (mInstanceDonacion == null) {
            mInstanceDonacion = new DonacionesFragment();
        }
        return mInstanceDonacion;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mInstanceDonacion = null;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_donaciones, container, false);
        mIAdicionalesImpl = new IAdicionalesImpl(getActivity().getApplicationContext());

        inicializarSwipeRefresh(mRootView);
        inicializarRecycler(mRootView);

        new AsyncTaskAdicionales().execute();
        return mRootView;
    }

    @Override
    public boolean onBackPressed() {
        return false;

    }

    private class AsyncTaskBloquearDonacion extends AsyncTask<Adicionales, Void, Void> {
        private boolean error = false;
        private Adicionales adicional = null;

        @Override
        protected Void doInBackground(Adicionales... params) {
            adicional = params[0];
            IAdicionalesImpl iAdicionales = new IAdicionalesImpl(getBaseActivity());
            try {
                iAdicionales.bloquearAdicional(adicional.getObjectId());
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
                List<Adicionales> adicionales = HuellasApplication.getInstance().getDonaciones();
                for (int x = 0; x < adicionales.size(); x++) {
                    if (adicional.getObjectId().equals(adicionales.get(x).getObjectId())) {
                        adicionales.remove(x);
                    }
                }
                mAdapterAdicionales.notifyDataSetChanged();
                Toast.makeText(getBaseActivity(), "Publicación bloqueada con éxito!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseActivity(), "No se pudo bloquear la publicación. Intente de nuevo más tarde", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void inicializarSwipeRefresh(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFromSwipeRefresh = true;
                new AsyncTaskAdicionales().execute();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent);
    }


    private void notifyAdapter(List<Adicionales> adicionales) {
        if (mAdapterAdicionales == null) {
            mAdapterAdicionales = new AdicionalesAdapter(adicionales, getBaseActivity(), DonacionesFragment.this, Constants.ADICIONALES_DONACIONES);

        } else {
            mAdapterAdicionales.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(false);
        if (mAdapterAdicionales == null) {
            List<Adicionales> adicionales = HuellasApplication.getInstance().getDonaciones();
            mAdapterAdicionales = new AdicionalesAdapter(adicionales, getBaseActivity(), DonacionesFragment.this, Constants.ADICIONALES_DONACIONES);
            mRecyclerView.setAdapter(mAdapterAdicionales);
        }
        else {
            mAdapterAdicionales.notifyDataSetChanged();
        }
    }

    private void inicializarRecycler(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_recycler_view);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mSwipeRefreshLayout.isRefreshing();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onClickItem(int idItem, final Adicionales adicional) {
        switch (idItem) {
            case R.id.item_bloquear:
                View.OnClickListener onClickEliminarListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.textView_cancelar:
                                ((PrincipalActivity) getBaseActivity()).closeDialog();
                                break;
                            case R.id.textView_confirmar:
                                ((PrincipalActivity) getBaseActivity()).showLoadDialog();
                                new AsyncTaskBloquearDonacion().execute(adicional);
                                break;
                        }
                    }
                };
                ((PrincipalActivity) getBaseActivity()).showNormalDialog(getBaseActivity().getString(R.string.dialog_bloquear_descripcion), onClickEliminarListener);

                break;
        }
    }

    private class AsyncTaskAdicionales extends AsyncTask<Void, Void, List<Adicionales>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Adicionales> adicionalesList) {
            super.onPostExecute(adicionalesList);
            HuellasApplication.getInstance().setDonaciones(adicionalesList);
            if (mFromSwipeRefresh) {
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapterAdicionales.notifyDataSetChanged();
                mFromSwipeRefresh = false;
            } else {
                mAdapterAdicionales = new AdicionalesAdapter(HuellasApplication.getInstance().getDonaciones(), getContext(), DonacionesFragment.this, Constants.ADICIONALES_DONACIONES);
                mRecyclerView.setAdapter(mAdapterAdicionales);
                ((PrincipalActivity) getBaseActivity()).setCountInfoLoaded();
            }

        }

        @Override
        protected List<Adicionales> doInBackground(Void... voids) {
            try {
                mIAdicionalesImpl.cargarDBLocalDonaciones(getBaseActivity());
                return mIAdicionalesImpl.getDonaciones();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;

        }
    }

}
