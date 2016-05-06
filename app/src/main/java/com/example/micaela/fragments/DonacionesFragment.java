package com.example.micaela.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.micaela.activities.AltaAnimalesActivity;
import com.example.micaela.activities.PrincipalActivity;
import com.example.micaela.adapters.AdicionalesAdapter;
import com.example.micaela.db.Controladores.IAdicionalesImpl;
import com.example.micaela.db.Interfaces.IAdicionales;
import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.db.Modelo.Comentarios;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.Constants;
import com.parse.ParseException;

import java.util.List;

public class DonacionesFragment extends BaseFragment implements AltaAnimalesFragment.AdapterCallback, ComentariosFragment.AdapterCallback, AdicionalesAdapter.PopupMenuCallback {
    private RecyclerView mRecyclerView;
    private IAdicionales mIAdicionalesImpl;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private List<Adicionales> adicionales;

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
        setHasOptionsMenu(false);
        new AsyncTaskAdicionales().execute();
        return mRootView;
    }

    @Override
    public boolean onBackPressed() {
        return false;

    }

    private class AsyncTaskDeletePerdido extends AsyncTask<Adicionales, Void, Void> {
        private boolean error = false;
        private Adicionales adicional = null;

        @Override
        protected Void doInBackground(Adicionales... params) {
            adicional = params[0];
            IAdicionalesImpl iAdicionales = new IAdicionalesImpl(getBaseActivity());
            try {
                iAdicionales.deleteAdicional(adicional.getObjectId());
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
                ((PrincipalActivity) getBaseActivity()).closeDialog();
                for (int x = 0; x < adicionales.size(); x++) {
                    if (adicional.getObjectId().equals(adicionales.get(x).getObjectId())) {
                        adicionales.remove(x);
                    }
                }
                mAdapterAdicionales.notifyDataSetChanged();
                Toast.makeText(getBaseActivity(), "Publicación eliminada con éxito!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseActivity(), "No se pudo eliminar la publicación. Intente de nuevo más tarde", Toast.LENGTH_LONG).show();
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


    @Override
    public void addElementAdapterPublicaciones(Object objeto) {
        adicionales.add(0, (Adicionales) objeto);
        mAdapterAdicionales.notifyDataSetChanged();
        Toast.makeText(getBaseActivity(), "Publicación realizada con éxito!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateElementAdapterPublicacion(Object object) {
        for (Adicionales adicional : adicionales) {
            if (adicional.getObjectId().equals(((Adicionales) object).getObjectId())) {
                adicional.setFoto(((Adicionales) object).getFoto());
                adicional.setDescripcion(((Adicionales) object).getDescripcion());
                adicional.setTitulo(((Adicionales) object).getTitulo());
                Toast.makeText(getBaseActivity(), "Publicación editada con éxito!", Toast.LENGTH_LONG).show();
            }
        }

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

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapterAdicionales == null) {
            mAdapterAdicionales = new AdicionalesAdapter(adicionales, getBaseActivity(), DonacionesFragment.this);
            mRecyclerView.setAdapter(mAdapterAdicionales);
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
    public void onClickItem(int idItem, final Adicionales adicional) {
        switch (idItem) {
            case R.id.item_editar:
                Intent intent = new Intent(getBaseActivity(), AltaAnimalesActivity.class);
                intent.putExtra(Constants.FROM_FRAGMENT, Constants.ADICIONALES_DONACIONES);
                intent.putExtra(Constants.ACTION, Constants.EDITAR);
                intent.putExtra(Constants.OBJETO_PERDIDO, adicional);
                getBaseActivity().startActivity(intent);
                break;
            case R.id.item_reportar_publicacion:
                ((PrincipalActivity) getActivity()).showDenunciasDialog(Constants.TABLA_ADICIONALES, adicional.getObjectId());
                break;
            case R.id.item_reportar_usuario:
                ((PrincipalActivity) getActivity()).showDenunciasDialog(Constants.TABLA_PERSONAS, adicional.getPersona().getObjectId());
                break;
            case R.id.item_eliminar:
                View.OnClickListener onClickEliminarListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.textView_cancelar:
                                ((PrincipalActivity) getBaseActivity()).closeDialog();
                                break;
                            case R.id.textView_confirmar:
                                ((PrincipalActivity) getBaseActivity()).showLoadDialog();
                                new AsyncTaskDeletePerdido().execute(adicional);
                                break;
                        }
                    }
                };
                ((PrincipalActivity) getBaseActivity()).showNormalDialog(getBaseActivity().getString(R.string.dialog_eliminar_descripcion), onClickEliminarListener);

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
            adicionales = adicionalesList;
            if (mFromSwipeRefresh) {
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapterAdicionales.notifyDataSetChanged();
                mFromSwipeRefresh = false;
            } else {
                mAdapterAdicionales = new AdicionalesAdapter(adicionales, getContext(), DonacionesFragment.this);
                mRecyclerView.setAdapter(mAdapterAdicionales);
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
