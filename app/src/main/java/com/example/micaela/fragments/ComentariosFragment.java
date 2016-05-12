package com.example.micaela.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.micaela.HuellasApplication;
import com.example.micaela.adapters.ComentariosAdapter;
import com.example.micaela.db.Controladores.IAdicionalesImpl;
import com.example.micaela.db.Controladores.IPerdidosImpl;
import com.example.micaela.db.Interfaces.IAdicionales;
import com.example.micaela.db.Interfaces.IPerdidos;
import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.db.Modelo.Comentarios;
import com.example.micaela.db.Modelo.Perdidos;
import com.example.micaela.db.Modelo.Personas;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.Constants;
import com.parse.ParseException;

import java.util.Date;


public class ComentariosFragment extends BaseFragment {
    private View mRootView;
    private ImageView mButtonSend;
    private EditText mEditTextComentario;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ComentariosAdapter mAdapter;
    private Adicionales mAdicional;
    private Perdidos mPerdido;
    private IPerdidos mIPerdidosImpl;
    private IAdicionales mIAdicionalesImpl;
    private String mFromFragment = "";
    private boolean mFromDetalle = false;
    private AdapterCallback mAdapterCallback;

    private boolean mFromPush;

    public interface AdapterCallback {
        void updateDataSetAdapterComentarios(Comentarios comentario, Object object);
    }


    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_comentarios, container, false);
        mIPerdidosImpl = new IPerdidosImpl(getBaseActivity());
        mIAdicionalesImpl = new IAdicionalesImpl(getBaseActivity());
        inicializarSwipeRefresh();

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Intent intent = getBaseActivity().getIntent();
        mFromPush = intent.getBooleanExtra(Constants.PUSH_OPEN, false);
        mFromFragment = intent.getStringExtra(Constants.FROM_FRAGMENT);

        if (mFromPush) {
            new AsyncTaskGetPerdidoById().execute(intent.getStringExtra(Constants.OBJETO_ID));
        } else {
            mFromDetalle = intent.getBooleanExtra(Constants.FROM_DETALLE, false);
            if (Constants.PERDIDOS.equals(mFromFragment)) {
                mPerdido = intent.getParcelableExtra(Constants.COMENTARIOS_LIST);
                if (mPerdido.isSolucionado()) {
                    mRootView.findViewById(R.id.linear_escribir_comentario).setVisibility(View.GONE);
                }
                mAdapter = new ComentariosAdapter(mPerdido.getComentarios(), getBaseActivity());

            } else {
                mAdicional = intent.getParcelableExtra(Constants.COMENTARIOS_LIST);
                mAdapter = new ComentariosAdapter(mAdicional.getComentarios(), getBaseActivity());
            }
        }

        if (mFromFragment.equals(Constants.PERDIDOS)) {
            mAdapterCallback = PerdidosFragment.getInstance();
        } else {
            if (mFromFragment.equals(Constants.ADICIONALES_DONACIONES)) {
                mAdapterCallback = DonacionesFragment.getInstance();
            } else {
                mAdapterCallback = InformacionUtilFragment.getInstance();
            }
        }

        mRecyclerView.setAdapter(mAdapter);
        mEditTextComentario = (EditText) mRootView.findViewById(R.id.editText_comentario);
        mEditTextComentario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    mButtonSend.setVisibility(View.GONE);
                } else {
                    mButtonSend.setVisibility(View.VISIBLE);
                }
            }
        });
        mButtonSend = (ImageView) mRootView.findViewById(R.id.button_send);
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonSend.setVisibility(View.GONE);
                Comentarios comentarios = new Comentarios("", mEditTextComentario.getText().toString(),
                        new Personas(HuellasApplication.getInstance().getProfileEmailFacebook()), new Date(), false, false);
                comentarios.getPersona().setNombre(HuellasApplication.getInstance().getProfileNameFacebook());
                comentarios.getPersona().setmFoto(HuellasApplication.getInstance().getProfileImageFacebook());
                new AsyncTaskSaveComentario().execute(comentarios);
            }
        });

        return mRootView;
    }

    @Override
    public boolean onBackPressed() {
        if (mFromPush) {
            if (mFromFragment.equals(Constants.PERDIDOS)) {
                getBaseActivity().goToDetalleActivity(mPerdido, mFromFragment);
            } else {
                getBaseActivity().goToDetalleActivity(mAdicional, mFromFragment);
            }
            return true;
        } else {
            return false;
        }
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


    private class AsyncTaskSaveComentario extends AsyncTask<Comentarios, Void, Comentarios> {
        private boolean error = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mEditTextComentario.clearFocus();
            getBaseActivity().hideKeyboard();
        }

        @Override
        protected Comentarios doInBackground(Comentarios... params) {
            try {
                if (Constants.PERDIDOS.equals(mFromFragment)) {
                    mIPerdidosImpl.AgregarComentarioPerdido(mPerdido.getObjectId(), params[0].getComentario(), HuellasApplication.getInstance().getProfileEmailFacebook());
                } else {
                    mIAdicionalesImpl.AgregarComentarioAdicional(mAdicional.getObjectId(), params[0].getComentario(), HuellasApplication.getInstance().getProfileEmailFacebook());
                }
            } catch (Exception e) {
                error = true;

            }

            return params[0];
        }

        @Override
        protected void onPostExecute(Comentarios comentarios) {
            super.onPostExecute(comentarios);
            if (!error) {
                mEditTextComentario.setText("");
                AdapterCallback adapterCallbackDetalle;

                if (Constants.PERDIDOS.equals(mFromFragment)) {
                    mPerdido.getComentarios().add(comentarios);
                    mAdapterCallback.updateDataSetAdapterComentarios(comentarios, mPerdido);
                    if (mFromDetalle) {
                        adapterCallbackDetalle = DetallePublicacionFragment.getInstance();
                        adapterCallbackDetalle.updateDataSetAdapterComentarios(comentarios, mPerdido);
                    }
                } else {
                    mAdicional.getComentarios().add(comentarios);
                    mAdapterCallback.updateDataSetAdapterComentarios(comentarios, mAdicional);
                    if (mFromDetalle) {
                        adapterCallbackDetalle = DetallePublicacionFragment.getInstance();
                        adapterCallbackDetalle.updateDataSetAdapterComentarios(comentarios, mAdicional);
                    }
                }
                mAdapter.notifyDataSetChanged();
                Toast.makeText(getBaseActivity(), "El comentario ha sido publicado.", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getBaseActivity(), "El comentario no ha sido publicado.", Toast.LENGTH_SHORT).show();
                mButtonSend.setVisibility(View.VISIBLE);
            }
        }
    }

}
