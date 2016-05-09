package com.example.micaela.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micaela.HuellasApplication;
import com.example.micaela.activities.AltaAnimalesActivity;
import com.example.micaela.adapters.AdicionalesAdapter;
import com.example.micaela.adapters.AnimalesAdapter;
import com.example.micaela.db.Controladores.IAdicionalesImpl;
import com.example.micaela.db.Controladores.IEstadosImpl;
import com.example.micaela.db.Controladores.IPerdidosImpl;
import com.example.micaela.db.Interfaces.IPerdidos;
import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.db.Modelo.Perdidos;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.Constants;
import com.example.micaela.utils.CustomDialog;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Micaela on 05/05/2016.
 */
public class HistorialPublicacionesFragment extends BaseFragment implements AdicionalesAdapter.PopupMenuCallback, AnimalesAdapter.PopupMenuCallback {
    private View mRootView;
    private Spinner mSpinnerTipoPublicacion;
    private RecyclerView mRecyclerViewHistorial;
    private List<Perdidos> mPerdidos = new ArrayList<>();
    private List<Adicionales> mDonaciones = new ArrayList<>();
    private List<Adicionales> mInfoUtil = new ArrayList<>();

    private View mViewTipoPubli;

    private View mDialogContainer;
    private TextView mTextViewDialogMsg;
    private TextView mTextViewConfirmar;
    private TextView mTextViewCancelar;
    private boolean isDialogOpen = false;

    private TextView mViewEmptyPublicaciones;

    private boolean isSolucionadosOpen = false;
    private AnimalesAdapter mAnimalesAdapter;
    private AdicionalesAdapter mDonacionesAdapter;
    private AdicionalesAdapter mInfoUtilAdapter;

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_historial_publicaciones, container, false);
        wireUpViews();

        setHasOptionsMenu(true);
        mViewEmptyPublicaciones.setVisibility(View.GONE);
        isSolucionadosOpen = false;
        getBaseActivity().setToolbarTitle(getBaseActivity().getString(R.string.mis_publicaciones_title));


        return mRootView;
    }

    private void wireUpViews() {
        mRecyclerViewHistorial = (RecyclerView) mRootView.findViewById(R.id.recycler_view_historial);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(getContext());
        mViewEmptyPublicaciones = (TextView) mRootView.findViewById(R.id.empty_publicaciones);
        mRecyclerViewHistorial.setLayoutManager(LinearLayoutManager);
        mSpinnerTipoPublicacion = (Spinner) mRootView.findViewById(R.id.spinner_tipo_publi);
        mSpinnerTipoPublicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppCompatTextView textViewItem = ((AppCompatTextView) view);
                textViewItem.setTextAppearance(getBaseActivity(), R.style.condensed_normal_19);
                setUpAdapters(textViewItem.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mViewTipoPubli = mRootView.findViewById(R.id.card_view_tipo_publi);
        mDialogContainer = mRootView.findViewById(R.id.layout_dialog_container);
        mTextViewCancelar = (TextView) mRootView.findViewById(R.id.textView_cancelar);
        mTextViewConfirmar = (TextView) mRootView.findViewById(R.id.textView_confirmar);
        mTextViewDialogMsg = (TextView) mRootView.findViewById(R.id.textView_confirmar_mensaje);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getBaseActivity().getToolbarTitle().equals(getBaseActivity().getString(R.string.solucionados_title))) {
            setUpAdapters(Constants.SOLUCIONADOS);
        } else {
            getMisPublicaciones();
            setUpAdapters(mSpinnerTipoPublicacion.getSelectedItem().toString());
        }

    }

    private void setUpAdapters(String selectedItem) {

        switch (selectedItem) {
            case Constants.TABLA_PERDIDOS:
                mViewEmptyPublicaciones.setVisibility(View.GONE);
                mAnimalesAdapter = new AnimalesAdapter(mPerdidos, getBaseActivity(), HistorialPublicacionesFragment.this);
                mRecyclerViewHistorial.setAdapter(mAnimalesAdapter);
                if (mAnimalesAdapter.getItemCount() == 0) {
                    mViewEmptyPublicaciones.setText(R.string.empty_publicaciones_msg);
                    mViewEmptyPublicaciones.setVisibility(View.VISIBLE);
                } else {
                    mViewEmptyPublicaciones.setVisibility(View.GONE);
                }
                break;
            case Constants.ADICIONALES_DONACIONES:
                mDonacionesAdapter = new AdicionalesAdapter(mDonaciones, getBaseActivity(), HistorialPublicacionesFragment.this, Constants.ADICIONALES_DONACIONES);
                mRecyclerViewHistorial.setAdapter(mDonacionesAdapter);
                if (mDonacionesAdapter.getItemCount() == 0) {
                    mViewEmptyPublicaciones.setText(R.string.empty_publicaciones_msg);
                    mViewEmptyPublicaciones.setVisibility(View.VISIBLE);
                } else {
                    mViewEmptyPublicaciones.setVisibility(View.GONE);
                }
                break;
            case Constants.ADICIONALES_INFO:
                mInfoUtilAdapter = new AdicionalesAdapter(mInfoUtil, getBaseActivity(), HistorialPublicacionesFragment.this, Constants.ADICIONALES_INFO);
                mRecyclerViewHistorial.setAdapter(mInfoUtilAdapter);
                if (mInfoUtilAdapter.getItemCount() == 0) {
                    mViewEmptyPublicaciones.setText(R.string.empty_publicaciones_msg);
                    mViewEmptyPublicaciones.setVisibility(View.VISIBLE);
                } else {
                    mViewEmptyPublicaciones.setVisibility(View.GONE);
                }
                break;

            case Constants.SOLUCIONADOS:
                mAnimalesAdapter = new AnimalesAdapter(HuellasApplication.getInstance().getmMisSolucionados(), getBaseActivity(), PerdidosFragment.getInstance());
                mRecyclerViewHistorial.setAdapter(mAnimalesAdapter);
                if (mAnimalesAdapter.getItemCount() == 0) {
                    mViewEmptyPublicaciones.setText(R.string.empty_solucionados_msg);
                    mViewEmptyPublicaciones.setVisibility(View.VISIBLE);
                } else {
                    mViewEmptyPublicaciones.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void getMisPerdidos() {
        mPerdidos.clear();
        for (Perdidos perdido : HuellasApplication.getInstance().getmPerdidos()) {
            if (perdido.getPersona().getEmail().equals(HuellasApplication.getInstance().getProfileEmailFacebook())) {
                mPerdidos.add(perdido);
            }
        }
    }

    private void getMisDonaciones() {
        mDonaciones.clear();
        for (Adicionales donacion : HuellasApplication.getInstance().getDonaciones()) {
            if (donacion.getPersona().getEmail().equals(HuellasApplication.getInstance().getProfileEmailFacebook())) {
                mDonaciones.add(donacion);
            }
        }
    }

    private void getMisInfoUtil() {
        mInfoUtil.clear();
        for (Adicionales infoUtil : HuellasApplication.getInstance().getInfoUtil()) {
            if (infoUtil.getPersona().getEmail().equals(HuellasApplication.getInstance().getProfileEmailFacebook())) {
                mInfoUtil.add(infoUtil);
            }
        }
    }

    private void getMisPublicaciones() {
        getMisPerdidos();
        getMisDonaciones();
        getMisInfoUtil();
    }

    @Override
    public boolean onBackPressed() {
        if (isSolucionadosOpen) {
            showLayoutHistorial();
            isSolucionadosOpen = false;
            return true;
        } else {
            if (isDialogOpen) {
                closeDialog();
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_historial, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_solucionadas:
                if (getBaseActivity().internet()) {
                    if (isSolucionadosOpen) {
                        showLayoutHistorial();
                    } else {
                        showLayoutSolucionados();
                    }
                } else {
                    CustomDialog.showDialog(getBaseActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClickItem(int idItem, final Adicionales adicional) {
        switch (idItem) {
            case R.id.item_editar:
                Intent intent = new Intent(getBaseActivity(), AltaAnimalesActivity.class);
                intent.putExtra(Constants.FROM_FRAGMENT, mSpinnerTipoPublicacion.getSelectedItem().toString());
                intent.putExtra(Constants.ACTION, Constants.EDITAR);
                intent.putExtra(Constants.OBJETO_PERDIDO, adicional);
                getBaseActivity().startActivity(intent);
                break;
            case R.id.item_eliminar:
                View.OnClickListener onClickEliminarListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.textView_cancelar:
                                closeDialog();
                                break;
                            case R.id.textView_confirmar:
                                showLoadDialog();
                                if (mSpinnerTipoPublicacion.getSelectedItem().toString().equals(Constants.ADICIONALES_DONACIONES)) {
                                    new AsyncTaskDeleteDonacion().execute(adicional);
                                } else {
                                    new AsyncTaskDeleteInfoUtil().execute(adicional);
                                }
                                break;
                        }
                    }
                };
                showNormalDialog(getBaseActivity().getString(R.string.dialog_eliminar_descripcion), onClickEliminarListener);

                break;
        }
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
            case R.id.item_solucionado:
                showNormalDialog(getBaseActivity().getString(R.string.dialog_solucionado_descripcion), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.textView_cancelar:
                                closeDialog();
                                break;
                            case R.id.textView_confirmar:
                                showLoadDialog();
                                new AsyncPublicacionSolucionada().execute(perdido.getObjectId());
                                break;
                        }
                    }
                });

                break;
            case R.id.item_eliminar:
                showNormalDialog(getBaseActivity().getString(R.string.dialog_eliminar_descripcion), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.textView_cancelar:
                                closeDialog();
                                break;
                            case R.id.textView_confirmar:
                                showLoadDialog();
                                new AsyncTaskDeletePerdido().execute(perdido);
                                break;
                        }
                    }
                });
                break;
        }
    }

    public void showLoadDialog() {
        mRootView.findViewById(R.id.dialog_content).setVisibility(View.GONE);
        mRootView.findViewById(R.id.dialog_load).setVisibility(View.VISIBLE);
        ((ProgressBar) mRootView.findViewById(R.id.progress_bar_dialog)).getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.accent), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public void hideLoadDialog() {
        mRootView.findViewById(R.id.dialog_content).setVisibility(View.VISIBLE);
        mRootView.findViewById(R.id.dialog_load).setVisibility(View.GONE);
    }

    public void showNormalDialog(String text, View.OnClickListener listener) {
        mDialogContainer.setVisibility(View.VISIBLE);
        isDialogOpen = true;
        mTextViewDialogMsg.setText(text);
        mRootView.findViewById(R.id.view_line).setVisibility(View.GONE);
        mTextViewCancelar.setVisibility(View.VISIBLE);
        mTextViewCancelar.setOnClickListener(listener);
        mTextViewConfirmar.setOnClickListener(listener);
    }

    public void closeDialog() {
        hideLoadDialog();
        isDialogOpen = false;
        mDialogContainer.setVisibility(View.GONE);
    }

    private void showLayoutSolucionados() {
        setHasOptionsMenu(false);
        isSolucionadosOpen = true;
        mViewTipoPubli.setVisibility(View.GONE);
        mViewEmptyPublicaciones.setVisibility(View.GONE);
        getBaseActivity().setToolbarTitle(getBaseActivity().getString(R.string.solucionados_title));
        if (HuellasApplication.getInstance().getmMisSolucionados() == null) {
            new AsynstaskGetSolucionados().execute();
        } else {
            setUpAdapters(Constants.SOLUCIONADOS);
        }
    }

    private void showLayoutHistorial() {
        setHasOptionsMenu(true);
        getMisPublicaciones();
        setUpAdapters(mSpinnerTipoPublicacion.getSelectedItem().toString());
        mViewEmptyPublicaciones.setVisibility(View.GONE);
        isSolucionadosOpen = false;
        mViewTipoPubli.setVisibility(View.VISIBLE);
        getBaseActivity().setToolbarTitle(getBaseActivity().getString(R.string.mis_publicaciones_title));
    }


    private class AsynstaskGetSolucionados extends AsyncTask<Void, Void, Void> {
        private boolean error = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getBaseActivity().showOverlay(getBaseActivity().getString(R.string.cargando_publicaciones_mensaje));
        }

        @Override
        protected Void doInBackground(Void... params) {
            IPerdidos iPerdidos = new IPerdidosImpl(getBaseActivity());
            try {
                HuellasApplication.getInstance().setmMisSolucionados(iPerdidos.getPublicacionPerdidosByEmail(HuellasApplication.getInstance().getProfileEmailFacebook()));
            } catch (ParseException e) {
                error = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (error) {
                showLayoutHistorial();
                HuellasApplication.getInstance().setmMisSolucionados(null);
                Toast.makeText(getBaseActivity(), "No se pudieron cargar las publicaciones solucionadas.", Toast.LENGTH_LONG).show();
            } else {
                setUpAdapters(Constants.SOLUCIONADOS);
            }
            getBaseActivity().hideOverlay();
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
            closeDialog();
            if (!error) {
                List<Perdidos> perdidos = HuellasApplication.getInstance().getmPerdidos();
                for (int x = 0; x < perdidos.size(); x++) {
                    if (perdidoId.equals(perdidos.get(x).getObjectId())) {
                        perdidos.get(x).setSolucionado(true);
                        HuellasApplication.getInstance().getmMisSolucionados().add(0, perdidos.get(x));
                        perdidos.remove(x);
                    }
                }
                getMisPerdidos();
                setUpAdapters(Constants.TABLA_PERDIDOS);
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
            closeDialog();
            if (!error) {
                List<Perdidos> perdidos = HuellasApplication.getInstance().getmPerdidos();
                for (int x = 0; x < perdidos.size(); x++) {
                    if (perdido.getObjectId().equals(perdidos.get(x).getObjectId())) {
                        perdidos.remove(x);
                    }
                }
                getMisPerdidos();
                setUpAdapters(Constants.TABLA_PERDIDOS);
                Toast.makeText(getBaseActivity(), "Publicación eliminada con éxito!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseActivity(), "No se pudo eliminar la publicación. Intente de nuevo más tarde", Toast.LENGTH_LONG).show();
            }

        }

    }

    private class AsyncTaskDeleteDonacion extends AsyncTask<Adicionales, Void, Void> {
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
            closeDialog();
            if (!error) {
                List<Adicionales> adicionales = HuellasApplication.getInstance().getDonaciones();
                for (int x = 0; x < adicionales.size(); x++) {
                    if (adicional.getObjectId().equals(adicionales.get(x).getObjectId())) {
                        adicionales.remove(x);
                    }
                }
                getMisDonaciones();
                setUpAdapters(Constants.ADICIONALES_DONACIONES);
                Toast.makeText(getBaseActivity(), "Publicación eliminada con éxito!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseActivity(), "No se pudo eliminar la publicación. Intente de nuevo más tarde", Toast.LENGTH_LONG).show();
            }

        }
    }

    private class AsyncTaskDeleteInfoUtil extends AsyncTask<Adicionales, Void, Void> {
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
            closeDialog();
            if (!error) {
                List<Adicionales> adicionales = HuellasApplication.getInstance().getInfoUtil();
                for (int x = 0; x < adicionales.size(); x++) {
                    if (adicional.getObjectId().equals(adicionales.get(x).getObjectId())) {
                        adicionales.remove(x);
                    }
                }
                getMisInfoUtil();
                setUpAdapters(Constants.ADICIONALES_INFO);
                Toast.makeText(getBaseActivity(), "Publicación eliminada con éxito!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseActivity(), "No se pudo eliminar la publicación. Intente de nuevo más tarde", Toast.LENGTH_LONG).show();
            }

        }
    }

}
