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
import com.managerapp.db.Controladores.IAdminImpl;
import com.managerapp.db.Interfaces.IAdicionales;
import com.managerapp.db.Interfaces.IAdmin;
import com.managerapp.db.Modelo.Adicionales;
import com.managerapp.utils.Constants;
import com.parse.ParseException;

import java.util.List;

public class InformacionUtilFragment extends BaseFragment implements AdicionalesAdapter.PopupMenuCallback {

    private RecyclerView mRecyclerView;
    private IAdicionales mIAdicionalesImpl;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    AdicionalesAdapter mAdapterAdicionales;

    private static InformacionUtilFragment mInstanceInfo;

    private boolean mFromSwipeRefresh = false;
    private IAdmin mIDenuncias;

    public static InformacionUtilFragment getInstance() {
        if (mInstanceInfo == null) {
            mInstanceInfo = new InformacionUtilFragment();
        }
        return mInstanceInfo;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_info_util, container, false);
        mIAdicionalesImpl = new IAdicionalesImpl(getActivity().getApplicationContext());
        mIDenuncias = new IAdminImpl(getBaseActivity());
        inicializarSwipeRefresh(mRootView);
        inicializarRecycler(mRootView);

        new AsyncTaskAdicionales().execute();
        return mRootView;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }


    private void inicializarSwipeRefresh(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTaskAdicionales().execute();
                mFromSwipeRefresh = true;
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent);
    }


    private void inicializarRecycler(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mSwipeRefreshLayout.isRefreshing();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mInstanceInfo = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(false);
        if (mAdapterAdicionales == null) {
            List<Adicionales> adicionales = HuellasApplication.getInstance().getInfoUtil();
            mAdapterAdicionales = new AdicionalesAdapter(adicionales, getBaseActivity(), InformacionUtilFragment.this, Constants.ADICIONALES_INFO);
            mRecyclerView.setAdapter(mAdapterAdicionales);
        } else {
            mAdapterAdicionales.notifyDataSetChanged();
        }
    }


    @Override
    public void onClickItem(int idItem, final Adicionales adicional, String tabla) {
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
                                new AsyncTaskBloquearInfoUtil().execute(adicional);
                                break;
                        }
                    }
                };
                ((PrincipalActivity) getBaseActivity()).showNormalDialog(getBaseActivity().getString(R.string.dialog_bloquear_descripcion), onClickEliminarListener);

                break;
        }
    }

    private class AsyncTaskBloquearInfoUtil extends AsyncTask<Adicionales, Void, Void> {
        private boolean error = false;
        private Adicionales adicional = null;

        @Override
        protected Void doInBackground(Adicionales... params) {
            adicional = params[0];
            try {
                mIDenuncias.confirmarDenunciaPublicacion(adicional.getObjectId(), Constants.TABLA_ADICIONALES);
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
                List<Adicionales> adicionales = HuellasApplication.getInstance().getInfoUtil();
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

    private class AsyncTaskAdicionales extends AsyncTask<Void, Void, List<Adicionales>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Adicionales> adicionalesList) {
            super.onPostExecute(adicionalesList);
            HuellasApplication.getInstance().setInfoUtil(adicionalesList);
            if (mFromSwipeRefresh) {
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapterAdicionales.notifyDataSetChanged();
                mFromSwipeRefresh = false;
            } else {
                mAdapterAdicionales = new AdicionalesAdapter(HuellasApplication.getInstance().getInfoUtil(), getContext(), InformacionUtilFragment.this, Constants.ADICIONALES_INFO);
                mRecyclerView.setAdapter(mAdapterAdicionales);
                ((PrincipalActivity) getBaseActivity()).setCountInfoLoaded();
            }
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
