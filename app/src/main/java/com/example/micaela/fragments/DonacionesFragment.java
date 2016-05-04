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
import android.widget.TextView;
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

    private View mDialogContainer;
    private TextView mTextViewDialogMsg;
    private TextView mTextViewConfirmar;
    private TextView mTextViewCancelar;
    private boolean isDialogOpen = false;
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
        mDialogContainer = mRootView.findViewById(R.id.layout_dialog_container);
        mTextViewCancelar = (TextView)mRootView.findViewById(R.id.textView_cancelar);
        mTextViewConfirmar = (TextView)mRootView.findViewById(R.id.textView_confirmar);
        mTextViewDialogMsg = (TextView)mRootView.findViewById(R.id.textView_confirmar_mensaje);


        setHasOptionsMenu(false);
        new AsyncTaskAdicionales().execute();
        return mRootView;
    }

    @Override
    public boolean onBackPressed() {
        if (isDialogOpen) {
            mDialogContainer.setVisibility(View.GONE);
            isDialogOpen = false;
            return  true;
        }
        else {
            return false;
        }
    }

    private class AsyncTaskDeletePerdido extends AsyncTask<Adicionales, Adicionales, Adicionales> {
        private boolean error = false;

        @Override
        protected Adicionales doInBackground(Adicionales... params) {
            IAdicionalesImpl iAdicionales = new IAdicionalesImpl(getBaseActivity());
            try {
                iAdicionales.deleteAdicional(params[0].getObjectId());
            } catch (ParseException e) {
                getBaseActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        error = true;
                    }
                });

            }
            return params[0];
        }

        @Override
        protected void onPostExecute(Adicionales adicional) {
            super.onPostExecute(adicional);
            if (!error) {
                mDialogContainer.setVisibility(View.GONE);
                isDialogOpen = false;
                for (int x = 0; x < adicionales.size(); x++) {
                    if (adicional.getObjectId().equals(adicionales.get(x).getObjectId())) {
                        adicionales.remove(x);
                    }
                }
                mAdapterAdicionales.notifyDataSetChanged();
                Toast.makeText(getBaseActivity(), "Publicación eliminada con éxito!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseActivity(), "No se pudo eliminar la publicación", Toast.LENGTH_LONG).show();
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
            }
        }

        mAdapterAdicionales.notifyDataSetChanged();
        Toast.makeText(getBaseActivity(), "Publicación editada con éxito!", Toast.LENGTH_LONG).show();
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
                break;
            case R.id.item_reportar_usuario:
                break;
            case R.id.item_eliminar:
                mDialogContainer.setVisibility(View.VISIBLE);
                isDialogOpen = true;
                mTextViewDialogMsg.setText("¿Está seguro que desea eliminar la publicación?");
                mTextViewCancelar.setVisibility(View.VISIBLE);
                break;
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.textView_cancelar:
                        isDialogOpen = false;
                        mDialogContainer.setVisibility(View.GONE);
                        break;
                    case R.id.textView_confirmar:
                        new AsyncTaskDeletePerdido().execute(adicional);


                        break;
                }
            }
        };
        mTextViewCancelar.setOnClickListener(onClickListener);
        mTextViewConfirmar.setOnClickListener(onClickListener);
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
            }
            else {
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
