package com.example.micaela.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.micaela.HuellasApplication;
import com.example.micaela.activities.PrincipalActivity;
import com.example.micaela.adapters.AnimalesAdapter;
import com.example.micaela.adapters.CustomSpinnerHintAdapter;
import com.example.micaela.db.Controladores.IPerdidosImpl;
import com.example.micaela.db.Interfaces.IPerdidos;
import com.example.micaela.db.Modelo.Colores;
import com.example.micaela.db.Modelo.Especies;
import com.example.micaela.db.Modelo.Estados;
import com.example.micaela.db.Modelo.Perdidos;
import com.example.micaela.db.Modelo.Razas;
import com.example.micaela.huellas.R;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;


public class PerdidosFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private IPerdidos mIperdidosImpl;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Spinner mSpinnerRazas;
    private Spinner mSpinnerEspecies;
    private Spinner mSpinnerColores;
    private Spinner mSpinnerEstado;

    CustomSpinnerHintAdapter mAdapterEstados;
    CustomSpinnerHintAdapter mAdapterEspecies;
    CustomSpinnerHintAdapter mAdapterRazas;
    CustomSpinnerHintAdapter mAdapterColores;

    private View mViewRazasContainer;
    private View mRootView;
    private View mViewCardFilter;
    private View mViewNoFilterContainer;
    private View mViewFilerContainer;
    private Button mButtonFilter;
    private boolean isFilterApplied = false;
    private boolean isItemSelected = false;
    private boolean isFilterCardVisible = false;
    private TextView mTextViewEmpty;

    private ImageView mImageViewClear;

    List<Razas> mRazas;
    List<Especies> mEspecies;
    List<Colores> mColores;
    List<Estados> mEstados;

    private boolean mFromSwipeRefresh = false;

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_perdidos, container, false);
        mIperdidosImpl = new IPerdidosImpl(getActivity().getApplicationContext());
        inicializarSwipeRefresh(mRootView);
        inicializarRecycler(mRootView);
        mTextViewEmpty = (TextView) mRootView.findViewById(R.id.empty_publicaciones);
        mViewCardFilter = mRootView.findViewById(R.id.cardView_filter);
        mViewFilerContainer = mRootView.findViewById(R.id.layout_filter_container);
        mViewNoFilterContainer = mRootView.findViewById(R.id.layout_no_filter_container);
        mImageViewClear = (ImageView) mRootView.findViewById(R.id.imageView_clear);
        mImageViewClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFilterApplied = false;
                mTextViewEmpty.setVisibility(View.GONE);
                mViewCardFilter.setVisibility(View.GONE);
                mViewNoFilterContainer.setVisibility(View.GONE);
                AnimalesAdapter mAdapter = new AnimalesAdapter(HuellasApplication.getInstance().getmPerdidos(), getBaseActivity());
                mRecyclerView.setAdapter(mAdapter);
            }
        });
        mButtonFilter = (Button) mRootView.findViewById(R.id.button_filter);
        mButtonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Perdidos> perdidosFilter = new ArrayList<>();
                perdidosFilter.addAll(HuellasApplication.getInstance().getmPerdidos());
                String especie = mSpinnerEspecies.getSelectedItem().toString();
                String raza = mSpinnerRazas.getSelectedItem().toString();
                String color = mSpinnerColores.getSelectedItem().toString();
                String tipoPubli = mSpinnerEstado.getSelectedItem().toString();
                if (!tipoPubli.equals(getString(R.string.tipo_publicacion))) {
                    for (int x = 0; x < perdidosFilter.size(); x++) {
                        if (!perdidosFilter.get(x).getEstado().getSituacion().equals(tipoPubli)) {
                            perdidosFilter.remove(x);
                            x--;
                        }
                    }
                    isItemSelected = true;
                }
                if (!especie.equals(getString(R.string.especie))) {
                    for (int x = 0; x < perdidosFilter.size(); x++) {
                        if (!perdidosFilter.get(x).getEspecie().getEspecie().equals(especie)) {
                            perdidosFilter.remove(x);
                            x--;
                        }
                    }
                    isItemSelected = true;
                }
                if (!raza.equals(getString(R.string.raza))) {
                    for (int x = 0; x < perdidosFilter.size(); x++) {
                        if (!perdidosFilter.get(x).getEstado().getSituacion().equals(tipoPubli)) {
                            perdidosFilter.remove(x);
                            x--;
                        }
                    }
                    isItemSelected = true;
                }
                if (!color.equals(getString(R.string.color_predominante))) {
                    for (int x = 0; x < perdidosFilter.size(); x++) {
                        if (!perdidosFilter.get(x).getEstado().getSituacion().equals(tipoPubli)) {
                            perdidosFilter.remove(x);
                            x--;
                        }
                    }
                    isItemSelected = true;
                }

                if (isItemSelected) {
                    mViewNoFilterContainer.setVisibility(View.VISIBLE);
                    mViewFilerContainer.setVisibility(View.GONE);
                    AnimalesAdapter mAdapter = new AnimalesAdapter(perdidosFilter, getBaseActivity());
                    mRecyclerView.setAdapter(mAdapter);
                    if (perdidosFilter.size() == 0) {
                        mTextViewEmpty.setVisibility(View.VISIBLE);
                    }
                    isFilterApplied = true;
                    isItemSelected = false;
                }
            }
        });
        setHasOptionsMenu(true);

        setUpSpinners();

        new AsyncTaskPerdidos().execute();
        return mRootView;
    }

    private void inicializarSwipeRefresh(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFromSwipeRefresh = true;
                isFilterApplied = false;
                mTextViewEmpty.setVisibility(View.GONE);
                mViewCardFilter.setVisibility(View.GONE);
                mViewNoFilterContainer.setVisibility(View.GONE);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setUpSpinners() {
        mViewRazasContainer = mRootView.findViewById(R.id.layout_razas_container);

        mSpinnerRazas = (Spinner) mRootView.findViewById(R.id.spinner_razas);
        mSpinnerEspecies = (Spinner) mRootView.findViewById(R.id.spinner_especies);
        mSpinnerEstado = (Spinner) mRootView.findViewById(R.id.spinner_estado);
        mSpinnerColores = (Spinner) mRootView.findViewById(R.id.spinner_colores);

        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                if (view != null) {
                    String textItemSelected = ((AppCompatTextView) view).getText().toString();
                    switch (parent.getId()) {
                        case R.id.spinner_especies:
                            if (!textItemSelected.equals(getString(R.string.especie))) {
                                String selectedEspecie = mSpinnerEspecies.getSelectedItem().toString();
                                mAdapterRazas.clear();
                                mAdapterRazas.add(getString(R.string.raza));
                                for (int x = 0; x < mRazas.size(); x++) {
                                    if (selectedEspecie.equals(mRazas.get(x).getmEspecie().getEspecie()) ||
                                            mRazas.get(x).getmEspecie().getEspecie() == null)
                                        mAdapterRazas.add(mRazas.get(x).getRaza());
                                }
                                mSpinnerRazas.setAdapter(mAdapterRazas);
                                mViewRazasContainer.setVisibility(View.VISIBLE);
                            }
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        mSpinnerEspecies.setOnItemSelectedListener(onItemSelectedListener);
        mSpinnerEstado.setOnItemSelectedListener(onItemSelectedListener);
        mSpinnerColores.setOnItemSelectedListener(onItemSelectedListener);
    }

    private void fillSpinners() {
        mRazas = HuellasApplication.getInstance().getmRazas();
        mEspecies = HuellasApplication.getInstance().getmEspecies();
        mColores = HuellasApplication.getInstance().getmColores();
        mEstados = HuellasApplication.getInstance().getmEstados();

        mViewRazasContainer.setVisibility(View.GONE);

        mAdapterEstados = new CustomSpinnerHintAdapter(getBaseActivity(), R.style.condensed_normal_small_15);
        mAdapterEstados.add(getString(R.string.tipo_publicacion));
        for (int x = 0; x < mEstados.size(); x++) {
            mAdapterEstados.add(mEstados.get(x).getSituacion());
        }
        mSpinnerEstado.setAdapter(mAdapterEstados);


        mAdapterEspecies = new CustomSpinnerHintAdapter(getBaseActivity(), R.style.condensed_normal_small_15);
        mAdapterEspecies.add(getString(R.string.especie));
        for (int x = 0; x < mEspecies.size(); x++) {
            mAdapterEspecies.add(mEspecies.get(x).getEspecie());
        }
        mSpinnerEspecies.setAdapter(mAdapterEspecies);

        mAdapterRazas = new CustomSpinnerHintAdapter(getBaseActivity(), R.style.condensed_normal_small_15);
        mAdapterRazas.add(getString(R.string.raza));
        mSpinnerRazas.setAdapter(mAdapterRazas);

        mAdapterColores = new CustomSpinnerHintAdapter(getBaseActivity(), R.style.condensed_normal_small_15);
        mAdapterColores.add(getString(R.string.color_predominante));
        for (int x = 0; x < mColores.size(); x++) {
            mAdapterColores.add(mColores.get(x).getColor());
        }
        mSpinnerColores.setAdapter(mAdapterColores);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                if (isFilterCardVisible) {
                    mViewCardFilter.setVisibility(View.GONE);
                    isFilterCardVisible = false;
                } else {
                    if (isFilterApplied) {
                        mViewNoFilterContainer.setVisibility(View.VISIBLE);
                        mViewFilerContainer.setVisibility(View.GONE);
                    } else {
                        fillSpinners();
                        mViewNoFilterContainer.setVisibility(View.GONE);
                        mViewFilerContainer.setVisibility(View.VISIBLE);
                    }
                    mViewCardFilter.setVisibility(View.VISIBLE);
                    isFilterCardVisible = true;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class AsyncTaskPerdidos extends AsyncTask<Void, Void, List<Perdidos>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getBaseActivity().showOverlay(getString(R.string.cargando_publicaciones_mensaje));
        }

        @Override
        protected void onPostExecute(final List<Perdidos> perdidosList) {
            super.onPostExecute(perdidosList);
            HuellasApplication.getInstance().setmPerdidos(perdidosList);
            AnimalesAdapter mAdapter = new AnimalesAdapter(perdidosList, getBaseActivity());
            mRecyclerView.setAdapter(mAdapter);

            if (mFromSwipeRefresh) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        getBaseActivity().hideOverlay();
                        mFromSwipeRefresh = false;
                    }
                });
            }
        }

        @Override
        protected List<Perdidos> doInBackground(Void... voids) {
            try {
                mIperdidosImpl.cargarDBLocal(getBaseActivity());
                return mIperdidosImpl.getPerdidos();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;

        }
    }
}
