package com.example.micaela.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.micaela.db.Controladores.IGeneralImpl;
import com.example.micaela.db.Modelo.Comentarios;
import com.example.micaela.db.Modelo.Personas;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.Constants;

import java.util.ArrayList;
import java.util.Date;


public class ComentariosFragment extends BaseFragment {
    private View mRootView;
    private ImageView mButtonSend;
    private EditText mEditTextComentario;
    private RecyclerView mRecyclerView;
    private ComentariosAdapter mAdapter;
    private ArrayList<Comentarios> mComentarios;
    private IGeneralImpl mGeneralIImpl;

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_comentarios, container, false);
        mComentarios = getBaseActivity().getIntent().getParcelableArrayListExtra(Constants.COMENTARIOS_LIST);

        mGeneralIImpl = new IGeneralImpl(getBaseActivity());
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ComentariosAdapter(mComentarios, getBaseActivity());
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
                        new Personas(HuellasApplication.getInstance().getProfileEmailFacebook()), new Date(), false);
                new AsyncTaskSaveComentario().execute(comentarios);
            }
        });
        return mRootView;
    }

    private class AsyncTaskSaveComentario extends AsyncTask<Comentarios, Void, Comentarios> {
        private boolean error = false;

        @Override
        protected Comentarios doInBackground(Comentarios... params) {
            try {
                mGeneralIImpl.agregarComentario(params[0].getComentario(), HuellasApplication.getInstance().getProfileEmailFacebook(), getBaseActivity());
            } catch (Exception e) {
                getBaseActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mButtonSend.setVisibility(View.VISIBLE);
                        error = true;
                        Toast.makeText(getBaseActivity(), "El comentario no ha sido publicado.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return params[0];
        }

        @Override
        protected void onPostExecute(Comentarios comentarios) {
            super.onPostExecute(comentarios);
            if (!error) {
                mEditTextComentario.setText("");
                mEditTextComentario.clearFocus();
                mComentarios.add(comentarios);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

}
