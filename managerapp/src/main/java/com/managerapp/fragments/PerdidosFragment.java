package com.managerapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
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
import android.widget.Toast;

import com.managerapp.HuellasApplication;
import com.managerapp.R;
import com.managerapp.adapters.AnimalesAdapter;
import com.managerapp.adapters.CustomSpinnerHintAdapter;
import com.managerapp.db.Controladores.IPerdidosImpl;
import com.managerapp.db.Interfaces.IPerdidos;
import com.managerapp.db.Modelo.Colores;
import com.managerapp.db.Modelo.Comentarios;
import com.managerapp.db.Modelo.Especies;
import com.managerapp.db.Modelo.Estados;
import com.managerapp.db.Modelo.Perdidos;
import com.managerapp.db.Modelo.Razas;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;


public class PerdidosFragment extends BaseFragment implements AnimalesAdapter.PopupMenuCallback {

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
    private boolean isDialogOpen = false;
    private TextView mTextViewEmpty;

    private ImageView mImageViewClear;

    List<Razas> mRazas;
    List<Especies> mEspecies;
    List<Colores> mColores;
    List<Estados> mEstados;

    AnimalesAdapter mAdapterAnimales;

    private boolean mFromSwipeRefresh = false;
    private static PerdidosFragment mInstancePerdidos;

    private View mDialogContainer;
    private TextView mTextViewDialogMsg;
    private TextView mTextViewConfirmar;
    private TextView mTextViewCancelar;

    public static PerdidosFragment getInstance() {
        if (mInstancePerdidos == null) {
            mInstancePerdidos = new PerdidosFragment();
        }
        return mInstancePerdidos;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mInstancePerdidos = null;
    }

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
                AnimalesAdapter mAdapter = new AnimalesAdapter(HuellasApplication.getInstance().getmPerdidos(), getBaseActivity(), PerdidosFragment.this);
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
                        if (!perdidosFilter.get(x).getRaza().getRaza().equals(raza)) {
                            perdidosFilter.remove(x);
                            x--;
                        }
                    }
                    isItemSelected = true;
                }
                if (!color.equals(getString(R.string.color_predominante))) {
                    for (int x = 0; x < perdidosFilter.size(); x++) {
                        if (!perdidosFilter.get(x).getColor().getColor().equals(color)) {
                            perdidosFilter.remove(x);
                            x--;
                        }
                    }
                    isItemSelected = true;
                }

                if (isItemSelected) {
                    mViewNoFilterContainer.setVisibility(View.VISIBLE);
                    mViewFilerContainer.setVisibility(View.GONE);
                    AnimalesAdapter mAdapter = new AnimalesAdapter(perdidosFilter, getBaseActivity(), PerdidosFragment.this);
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

        mDialogContainer = mRootView.findViewById(R.id.layout_dialog_container);
        mTextViewCancelar = (TextView) mRootView.findViewById(R.id.textView_cancelar);
        mTextViewConfirmar = (TextView) mRootView.findViewById(R.id.textView_confirmar);
        mTextViewDialogMsg = (TextView) mRootView.findViewById(R.id.textView_confirmar_mensaje);

        new AsyncTaskPerdidos().execute();
        return mRootView;
    }

    @Override
    public boolean onBackPressed() {
        if (isDialogOpen) {
            mDialogContainer.setVisibility(View.GONE);
            isDialogOpen = false;
            return true;
        } else {
            return false;
        }
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
                                    if (mRazas.get(x).getmEspecie() == null ||
                                            selectedEspecie.equals(mRazas.get(x).getmEspecie().getEspecie()))
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

    @Override
    public void onClickItem(int idItem, final Perdidos perdido) {
        switch (idItem) {
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
                        new AsyncTaskDeletePerdido().execute(perdido);


                        break;
                }
            }
        };
        mTextViewCancelar.setOnClickListener(onClickListener);
        mTextViewConfirmar.setOnClickListener(onClickListener);

    }

    private class AsyncTaskDeletePerdido extends AsyncTask<Perdidos, Perdidos, Perdidos> {
        private boolean error = false;

        @Override
        protected Perdidos doInBackground(Perdidos... params) {
            IPerdidosImpl iPerdidos = new IPerdidosImpl(getBaseActivity());
            try {
                iPerdidos.deletePerdido(params[0].getObjectId());
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
        protected void onPostExecute(Perdidos perdido) {
            super.onPostExecute(perdido);
            if (!error) {
                mDialogContainer.setVisibility(View.GONE);
                isDialogOpen = false;
                List<Perdidos> perdidos = HuellasApplication.getInstance().getmPerdidos();
                for (int x = 0; x < perdidos.size(); x++) {
                    if (perdido.getObjectId().equals(perdidos.get(x).getObjectId())) {
                        perdidos.remove(x);
                    }
                }
                mAdapterAnimales.notifyDataSetChanged();
                Toast.makeText(getBaseActivity(), "Publicación eliminada con éxito!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseActivity(), "No se pudo eliminar la publicación", Toast.LENGTH_LONG).show();
            }

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
            mAdapterAnimales = new AnimalesAdapter(HuellasApplication.getInstance().getmPerdidos(), getBaseActivity(), PerdidosFragment.this);
            mRecyclerView.setAdapter(mAdapterAnimales);

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
