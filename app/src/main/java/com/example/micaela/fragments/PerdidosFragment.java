package com.example.micaela.fragments;

import android.content.Intent;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micaela.HuellasApplication;
import com.example.micaela.activities.AltaAnimalesActivity;
import com.example.micaela.activities.PrincipalActivity;
import com.example.micaela.adapters.AnimalesAdapter;
import com.example.micaela.adapters.CustomSpinnerHintAdapter;
import com.example.micaela.db.Controladores.IEstadosImpl;
import com.example.micaela.db.Controladores.IPerdidosImpl;
import com.example.micaela.db.Interfaces.IPerdidos;
import com.example.micaela.db.Modelo.Colores;
import com.example.micaela.db.Modelo.Comentarios;
import com.example.micaela.db.Modelo.Especies;
import com.example.micaela.db.Modelo.Estados;
import com.example.micaela.db.Modelo.Perdidos;
import com.example.micaela.db.Modelo.Razas;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.Constants;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;


public class PerdidosFragment extends BaseFragment implements AltaAnimalesFragment.AdapterCallback, ComentariosFragment.AdapterCallback, AnimalesAdapter.PopupMenuCallback {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
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

    AnimalesAdapter mAdapterAnimales;

    private boolean mFromSwipeRefresh = false;
    private static PerdidosFragment mInstancePerdidos;

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

        inicializarSwipeRefresh();
        inicializarRecycler();
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

        setUpSpinners();

        new AsyncTaskPerdidos().execute();
        return mRootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard, menu);
        super.onCreateOptionsMenu(menu, inflater);
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

    private void inicializarRecycler() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list_recycler_view);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mSwipeRefreshLayout.isRefreshing();
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
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

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
        if (mAdapterAnimales == null) {
            mAdapterAnimales = new AnimalesAdapter(HuellasApplication.getInstance().getmPerdidos(), getBaseActivity(), PerdidosFragment.this);
            mRecyclerView.setAdapter(mAdapterAnimales);
        } else {
            mAdapterAnimales.notifyDataSetChanged();
        }
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
    public void addElementAdapterPublicaciones(Object objeto) {
        List<Perdidos> perdidos = HuellasApplication.getInstance().getmPerdidos();
        perdidos.add(0, (Perdidos) objeto);
        notifyAdapter(perdidos);

        Toast.makeText(getBaseActivity(), "Publicación realizada con éxito!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateElementAdapterPublicacion(Object object) {
        List<Perdidos> perdidos = HuellasApplication.getInstance().getmPerdidos();
        for (Perdidos perdido : perdidos) {
            if (perdido.getObjectId().equals(((Perdidos) object).getObjectId())) {
                perdido.setFoto(((Perdidos) object).getFoto());
                perdido.setDescripcion(((Perdidos) object).getDescripcion());
                perdido.setTitulo(((Perdidos) object).getTitulo());
                perdido.setColor(((Perdidos) object).getColor());
                perdido.setEstado(((Perdidos) object).getEstado());
                perdido.setEdad(((Perdidos) object).getEdad());
                perdido.setTamaño(((Perdidos) object).getTamaño());
                perdido.setUbicacion(((Perdidos) object).getUbicacion());
                perdido.setEspecie(((Perdidos) object).getEspecie());
                perdido.setRaza(((Perdidos) object).getRaza());
                perdido.setSexo(((Perdidos) object).getSexo());
                Toast.makeText(getBaseActivity(), "Publicación editada con éxito!", Toast.LENGTH_SHORT).show();
            }
        }
        notifyAdapter(perdidos);
    }

    private void notifyAdapter(List<Perdidos> perdidos) {
        if (mAdapterAnimales == null) {
            mAdapterAnimales = new AnimalesAdapter(perdidos, getBaseActivity(), PerdidosFragment.this);

        } else {
            mAdapterAnimales.notifyDataSetChanged();
        }
    }


    @Override
    public void updateDataSetAdapterComentarios(Comentarios comentario, Object object) {
        List<Perdidos> perdidos = HuellasApplication.getInstance().getmPerdidos();
        for (Perdidos perdido : perdidos) {
            if (perdido.getObjectId().equals(((Perdidos) object).getObjectId())) {
                perdido.getComentarios().add(comentario);
            }
        }
        notifyAdapter(perdidos);
    }

    @Override
    public void onClickItem(int idItem, final Perdidos perdido) {
        switch (idItem) {
            case R.id.item_editar:
                Intent intent = new Intent(getBaseActivity(), AltaAnimalesActivity.class);
                intent.putExtra(Constants.FROM_FRAGMENT, Constants.PERDIDOS);
                intent.putExtra(Constants.ACTION, Constants.EDITAR);
                intent.putExtra(Constants.OBJETO_PERDIDO, perdido);
                getBaseActivity().startActivity(intent);
                break;
            case R.id.item_reportar_publicacion:
                ((PrincipalActivity) getActivity()).showDenunciasDialog(Constants.TABLA_PERDIDOS, perdido.getObjectId());
                break;
            case R.id.item_reportar_usuario:
                ((PrincipalActivity) getActivity()).showDenunciasDialog(Constants.TABLA_PERSONAS, perdido.getPersona().getObjectId());
                break;
            case R.id.item_solucionado:
                ((PrincipalActivity) getBaseActivity()).showNormalDialog(getBaseActivity().getString(R.string.dialog_solucionado_descripcion), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.textView_cancelar:
                                ((PrincipalActivity) getBaseActivity()).closeDialog();
                                break;
                            case R.id.textView_confirmar:
                                ((PrincipalActivity) getBaseActivity()).showLoadDialog();
                                new AsyncPublicacionSolucionada().execute(perdido.getObjectId());
                                break;
                        }
                    }
                });

                break;
            case R.id.item_eliminar:
                ((PrincipalActivity) getBaseActivity()).showNormalDialog(getBaseActivity().getString(R.string.dialog_eliminar_descripcion), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.textView_cancelar:
                                ((PrincipalActivity) getBaseActivity()).closeDialog();
                                break;
                            case R.id.textView_confirmar:
                                ((PrincipalActivity) getBaseActivity()).showLoadDialog();
                                new AsyncTaskDeletePerdido().execute(perdido);
                                break;
                        }
                    }
                });
                break;
        }

    }


    private class AsyncPublicacionSolucionada extends AsyncTask<String, Void, Void> {
        private boolean error = false;
        private String perdidoId = "";

        @Override
        protected Void doInBackground(String... params) {
            perdidoId = params[0];
            IEstadosImpl iEstados = new IEstadosImpl(getBaseActivity());
            try {
                iEstados.cambiarEstado(perdidoId, true);
            } catch (Exception e) {
                error = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ((PrincipalActivity) getBaseActivity()).closeDialog();
            if (!error) {
                List<Perdidos> perdidos = HuellasApplication.getInstance().getmPerdidos();
                for (int x = 0; x < perdidos.size(); x++) {
                    if (perdidoId.equals(perdidos.get(x).getObjectId())) {
                        perdidos.get(x).setSolucionado(true);
                    List<Perdidos> solucionados = HuellasApplication.getInstance().getmMisSolucionados();
                        if (solucionados == null) {
                            solucionados = new ArrayList<>();
                        }
                        solucionados.add(0, perdidos.get(x));
                        perdidos.remove(x);
                    }
                }
                mAdapterAnimales.notifyDataSetChanged();
                Toast.makeText(getBaseActivity(), "Estado modificado con éxito!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseActivity(), "No se pudo modificar el estado de la publicación. Intente de nuevo más tarde", Toast.LENGTH_LONG).show();
            }

        }

    }

    private class AsyncTaskDeletePerdido extends AsyncTask<Perdidos, Void, Void> {
        private boolean error = false;
        private Perdidos perdido = null;

        @Override
        protected Void doInBackground(Perdidos... params) {
            perdido = params[0];
            IPerdidosImpl iPerdidos = new IPerdidosImpl(getBaseActivity());
            try {
                iPerdidos.deletePerdido(perdido.getObjectId());
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
                List<Perdidos> perdidos = HuellasApplication.getInstance().getmPerdidos();
                for (int x = 0; x < perdidos.size(); x++) {
                    if (perdido.getObjectId().equals(perdidos.get(x).getObjectId())) {
                        perdidos.remove(x);
                    }
                }
                mAdapterAnimales.notifyDataSetChanged();
                Toast.makeText(getBaseActivity(), "Publicación eliminada con éxito!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseActivity(), "No se pudo eliminar la publicación. Intente de nuevo más tarde", Toast.LENGTH_LONG).show();
            }

        }

    }

    private class AsyncTaskPerdidos extends AsyncTask<Void, Void, List<Perdidos>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!mFromSwipeRefresh) {
                getBaseActivity().showOverlay(getString(R.string.cargando_publicaciones_mensaje));
            }
        }

        @Override
        protected void onPostExecute(final List<Perdidos> perdidosList) {
            super.onPostExecute(perdidosList);
            HuellasApplication.getInstance().setmPerdidos(perdidosList);
            if (mFromSwipeRefresh) {
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapterAnimales.notifyDataSetChanged();
                mFromSwipeRefresh = false;

            } else {
                mAdapterAnimales = new AnimalesAdapter(HuellasApplication.getInstance().getmPerdidos(), getBaseActivity(), PerdidosFragment.this);
                mRecyclerView.setAdapter(mAdapterAnimales);
                ((PrincipalActivity) getBaseActivity()).setCountInfoLoaded();
            }

        }

        @Override
        protected List<Perdidos> doInBackground(Void... voids) {
            try {
                mIperdidosImpl.cargarDBLocalListaPerdidos(getBaseActivity());
                return mIperdidosImpl.getPerdidos();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;

        }
    }
}
