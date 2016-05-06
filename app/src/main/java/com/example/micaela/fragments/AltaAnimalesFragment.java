package com.example.micaela.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.micaela.HuellasApplication;
import com.example.micaela.activities.AltaAnimalesActivity;
import com.example.micaela.adapters.CustomSpinnerHintAdapter;
import com.example.micaela.db.Controladores.IAdicionalesImpl;
import com.example.micaela.db.Controladores.IPerdidosImpl;
import com.example.micaela.db.Controladores.IPersonasImpl;
import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.db.Modelo.Colores;
import com.example.micaela.db.Modelo.Edades;
import com.example.micaela.db.Modelo.Especies;
import com.example.micaela.db.Modelo.Estados;
import com.example.micaela.db.Modelo.Perdidos;
import com.example.micaela.db.Modelo.Personas;
import com.example.micaela.db.Modelo.Razas;
import com.example.micaela.db.Modelo.Sexos;
import com.example.micaela.db.Modelo.Tamaños;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.Constants;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.Date;
import java.util.List;

public class AltaAnimalesFragment extends BaseFragment {

    private List<Razas> mRazas;
    private List<Especies> mEspecies;
    private List<Tamaños> mTamanios;
    private List<Edades> mEdades;
    private List<Colores> mColores;
    private List<Estados> mEstados;
    private List<Sexos> mSexos;

    private TextView mTextViewTakePicture;
    private TextView mTextViewSelectPicture;
    private View mViewDialogCamera;
    private boolean mIsDialogVisible = false;

    private ImageView mImageViewFoto;
    private Spinner mSpinnerEdades;
    private Spinner mSpinnerRazas;
    private Spinner mSpinnerEspecies;
    private Spinner mSpinnerTamanios;
    private Spinner mSpinnerColores;
    private Spinner mSpinnerSexos;
    private Spinner mSpinnerEstado;
    private TextInputLayout mTextInputLayoutTitulo;
    private TextInputLayout mTextInputLayoutDescription;
    private TextInputLayout mTextInputLayoutUbicacion;
    private TextInputLayout mTextInputLayoutTelefono;
    private EditText mEditTextTitulo;
    private EditText mEditTextDescripcion;
    private EditText mEditTextDireccion;
    private EditText mEditTextNombre;
    private EditText mEditTextTelefono;
    private EditText mEditTextMail;
    private Button mButtonPublicar;
    private ImageView mImageViewMap;
    private View mViewRazasContainer;
    private ProgressBar mProgressBar;
    private TextView mTextViewError;
    private final int CAMERA_CAPTURE = 1;
    private final int SELECT_PICTURE = 2;
    private final int CROP_PIC = 3;
    private View mRootView;
    private Location mLastLocation;

    private String mFromFragment;
    private String mAction;
    private boolean mIsEverythingOK = true;

    private boolean mIsFromResource = true;
    private LatLng latLng;

    private IPerdidosImpl mIPerdidosImpl;
    private IAdicionalesImpl mIAdicionalesImpl;
    private IPersonasImpl mIPersonasImpl;

    private Adicionales mAdicionales;
    private Perdidos mPerdidos;

    private Adicionales mAdicionalEdit;
    private Perdidos mPerdidoEdit;

    private AdapterCallback mAdapterCallback;

    public interface AdapterCallback {
        void addElementAdapterPublicaciones(Object object);

        void updateElementAdapterPublicacion(Object object);
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_alta_animales, container, false);
        mIPerdidosImpl = new IPerdidosImpl(getBaseActivity());
        mIAdicionalesImpl = new IAdicionalesImpl(getBaseActivity());
        mIPersonasImpl = new IPersonasImpl(getBaseActivity());
        wireUpViews();

        mFromFragment = getBaseActivity().getIntent().getStringExtra(Constants.FROM_FRAGMENT);
        mAction = getBaseActivity().getIntent().getStringExtra(Constants.ACTION);
        if (mFromFragment.equals(Constants.PERDIDOS)) {
            setUpSpinners();
            mAdapterCallback = PerdidosFragment.getInstance();
        } else {
            mRootView.findViewById(R.id.layout_detalle_mascota_container).setVisibility(View.GONE);
            mRootView.findViewById(R.id.layout_estado_container).setVisibility(View.GONE);
            mRootView.findViewById(R.id.layout_ubicacion_container).setVisibility(View.GONE);
            if (mFromFragment.equals(Constants.ADICIONALES_DONACIONES)) {

                mAdapterCallback = DonacionesFragment.getInstance();
            } else {
                mAdapterCallback = InformacionUtilFragment.getInstance();
            }
        }

        if (mAction.equals(Constants.EDITAR)) {
            getBaseActivity().showOverlay("Cargando...");
            getBaseActivity().setUpCollapsingToolbar(getBaseActivity().getString(R.string.editar_publicacion));
            populateViews();
        } else {
            getBaseActivity().setUpCollapsingToolbar(getBaseActivity().getString(R.string.crear_publicacion));
        }
        return mRootView;

    }

    private void populateViews() {
        mButtonPublicar.setText("Editar");
        mImageViewFoto.setTag(false);
        mEditTextTelefono.setEnabled(true);
        if (mFromFragment.equals(Constants.PERDIDOS)) {
            mPerdidoEdit = getBaseActivity().getIntent().getParcelableExtra(Constants.OBJETO_PERDIDO);
            mImageViewFoto.setImageBitmap(getBaseActivity().convertFromByteToBitmap(mPerdidoEdit.getFoto()));
            mEditTextDescripcion.setText(mPerdidoEdit.getDescripcion());
            mEditTextTitulo.setText(mPerdidoEdit.getTitulo());
            mEditTextTelefono.setText(mPerdidoEdit.getPersona().getTelefono());
            mSpinnerEspecies.setSelection(Especies.returnPositionElement(mEspecies, mPerdidoEdit.getEspecie().getEspecie()) + 1);
            mSpinnerEdades.setSelection(Edades.returnPositionElement(mEdades, mPerdidoEdit.getEdad().getEdad()) + 1);
            mSpinnerTamanios.setSelection(Tamaños.returnPositionElement(mTamanios, mPerdidoEdit.getTamaño().getTamaño()) + 1);
            mSpinnerColores.setSelection(Colores.returnPositionElement(mColores, mPerdidoEdit.getColor().getColor()) + 1);
            mSpinnerSexos.setSelection(Sexos.returnPositionElement(mSexos, mPerdidoEdit.getSexo().getSexo()) + 1);
            mSpinnerEstado.setSelection(Estados.returnPositionElement(mEstados, mPerdidoEdit.getEstado().getSituacion()) + 1);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSpinnerRazas.setSelection(Razas.returnPositionElement(mRazas, mPerdidoEdit.getRaza().getmRaza(), mPerdidoEdit.getEspecie().getEspecie()) + 1);
                }
            }, 600);
            mEditTextDireccion.setText(mPerdidoEdit.getUbicacion());
        } else {
            mAdicionalEdit = getBaseActivity().getIntent().getParcelableExtra(Constants.OBJETO_PERDIDO);
            mImageViewFoto.setImageBitmap(getBaseActivity().convertFromByteToBitmap(mAdicionalEdit.getFoto()));
            mEditTextDescripcion.setText(mAdicionalEdit.getDescripcion());
            mEditTextTitulo.setText(mAdicionalEdit.getTitulo());
            mEditTextTelefono.setText(mAdicionalEdit.getPersona().getTelefono());
        }
        getBaseActivity().hideOverlay();
    }

    private void setUpSpinners() {
        mRazas = HuellasApplication.getInstance().getmRazas();
        mEspecies = HuellasApplication.getInstance().getmEspecies();
        mTamanios = HuellasApplication.getInstance().getmTamanios();
        mEdades = HuellasApplication.getInstance().getmEdades();
        mColores = HuellasApplication.getInstance().getmColores();
        mEstados = HuellasApplication.getInstance().getmEstados();
        mSexos = HuellasApplication.getInstance().getmSexos();

        mSpinnerRazas = (Spinner) mRootView.findViewById(R.id.spinner_razas);
        mSpinnerEdades = (Spinner) mRootView.findViewById(R.id.spinner_edades);
        mSpinnerEspecies = (Spinner) mRootView.findViewById(R.id.spinner_especies);
        mSpinnerEstado = (Spinner) mRootView.findViewById(R.id.spinner_estado);
        mSpinnerColores = (Spinner) mRootView.findViewById(R.id.spinner_colores);
        mSpinnerSexos = (Spinner) mRootView.findViewById(R.id.spinner_sexos);
        mSpinnerTamanios = (Spinner) mRootView.findViewById(R.id.spinner_tamanios);

        CustomSpinnerHintAdapter adapterEstados = new CustomSpinnerHintAdapter(getBaseActivity(), R.style.condensed_normal_17);
        adapterEstados.add(getString(R.string.tipo_publicacion));
        for (int x = 0; x < mEstados.size(); x++) {
            adapterEstados.add(mEstados.get(x).getSituacion());
        }
        mSpinnerEstado.setAdapter(adapterEstados);


        CustomSpinnerHintAdapter adapterEspecies = new CustomSpinnerHintAdapter(getBaseActivity(), R.style.condensed_normal_17);
        adapterEspecies.add(getString(R.string.especie));
        for (int x = 0; x < mEspecies.size(); x++) {
            adapterEspecies.add(mEspecies.get(x).getEspecie());
        }
        mSpinnerEspecies.setAdapter(adapterEspecies);

        final CustomSpinnerHintAdapter adapterRazas = new CustomSpinnerHintAdapter(getBaseActivity(), R.style.condensed_normal_17);
        adapterRazas.add(getString(R.string.raza));
        mSpinnerRazas.setAdapter(adapterRazas);

        CustomSpinnerHintAdapter adapterTamaños = new CustomSpinnerHintAdapter(getBaseActivity(), R.style.condensed_normal_17);
        adapterTamaños.add(getString(R.string.tamaño));
        for (int x = 0; x < mTamanios.size(); x++) {
            adapterTamaños.add(mTamanios.get(x).getTamaño());
        }
        mSpinnerTamanios.setAdapter(adapterTamaños);

        CustomSpinnerHintAdapter adapterEdades = new CustomSpinnerHintAdapter(getBaseActivity(), R.style.condensed_normal_17);
        adapterEdades.add(getString(R.string.edad_aproximada));
        for (int x = 0; x < mEdades.size(); x++) {
            adapterEdades.add(mEdades.get(x).getEdad());
        }
        mSpinnerEdades.setAdapter(adapterEdades);

        CustomSpinnerHintAdapter adapterColores = new CustomSpinnerHintAdapter(getBaseActivity(), R.style.condensed_normal_17);
        adapterColores.add(getString(R.string.color_predominante));
        for (int x = 0; x < mColores.size(); x++) {
            adapterColores.add(mColores.get(x).getColor());
        }
        mSpinnerColores.setAdapter(adapterColores);


        CustomSpinnerHintAdapter adapterSexos = new CustomSpinnerHintAdapter(getBaseActivity(), R.style.condensed_normal_17);
        adapterSexos.add(getString(R.string.sexo));
        for (int x = 0; x < mSexos.size(); x++) {
            adapterSexos.add(mSexos.get(x).getSexo());
        }
        mSpinnerSexos.setAdapter(adapterSexos);


        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                if (view != null) {
                    String textItemSelected = ((AppCompatTextView) view).getText().toString();
                    switch (parent.getId()) {
                        case R.id.spinner_colores:
                            if (!textItemSelected.equals(getString(R.string.color_predominante))) {
                                mRootView.findViewById(R.id.textView_colores).setVisibility(View.VISIBLE);
                            }
                            break;
                        case R.id.spinner_edades:
                            if (!textItemSelected.equals(getString(R.string.edad_aproximada))) {
                                mRootView.findViewById(R.id.textView_edad).setVisibility(View.VISIBLE);
                            }
                            break;
                        case R.id.spinner_especies:
                            if (!textItemSelected.equals(getString(R.string.especie))) {
                                mRootView.findViewById(R.id.textView_especie).setVisibility(View.VISIBLE);
                                String selectedEspecie = mSpinnerEspecies.getSelectedItem().toString();
                                mSpinnerRazas.invalidate();
                                adapterRazas.clear();
                                adapterRazas.add(getString(R.string.raza));
                                for (int x = 0; x < mRazas.size(); x++) {
                                    if (mRazas.get(x).getmEspecie() == null ||
                                            selectedEspecie.equals(mRazas.get(x).getmEspecie().getEspecie()))
                                        adapterRazas.add(mRazas.get(x).getRaza());
                                }
                                adapterRazas.notifyDataSetChanged();
                                mSpinnerRazas.setAdapter(adapterRazas);
                                mViewRazasContainer.setVisibility(View.VISIBLE);
                            }
                            break;
                        case R.id.spinner_razas:
                            if (!textItemSelected.equals(getString(R.string.raza))) {
                                mRootView.findViewById(R.id.textView_raza).setVisibility(View.VISIBLE);
                            }
                            break;
                        case R.id.spinner_estado:
                            if (!textItemSelected.equals(getString(R.string.tipo_publicacion))) {
                                mRootView.findViewById(R.id.textView_tipo_publicacion).setVisibility(View.VISIBLE);
                            }
                            break;
                        case R.id.spinner_sexos:
                            if (!textItemSelected.equals(getString(R.string.sexo))) {
                                mRootView.findViewById(R.id.textView_sexo).setVisibility(View.VISIBLE);
                            }
                            break;
                        case R.id.spinner_tamanios:
                            if (!textItemSelected.equals(getString(R.string.tamaño))) {
                                mRootView.findViewById(R.id.textView_tamanios).setVisibility(View.VISIBLE);
                            }
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        mSpinnerEdades.setOnItemSelectedListener(onItemSelectedListener);
        mSpinnerEspecies.setOnItemSelectedListener(onItemSelectedListener);
        mSpinnerEstado.setOnItemSelectedListener(onItemSelectedListener);
        mSpinnerColores.setOnItemSelectedListener(onItemSelectedListener);
        mSpinnerSexos.setOnItemSelectedListener(onItemSelectedListener);
        mSpinnerRazas.setOnItemSelectedListener(onItemSelectedListener);
        mSpinnerTamanios.setOnItemSelectedListener(onItemSelectedListener);
    }

    private void wireUpViews() {
        mImageViewFoto = getBaseActivity().getmImageViewPicture();
        mImageViewFoto.setTag(mIsFromResource);
        mViewRazasContainer = mRootView.findViewById(R.id.layout_razas_container);
        mImageViewMap = (ImageView) mRootView.findViewById(R.id.imageView_location);
        mImageViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditTextDireccion.setEnabled(false);
                mImageViewMap.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                if (!((AltaAnimalesActivity) getBaseActivity()).isConnected()) {
                    Toast.makeText(getBaseActivity(), "No es posible encontrar tu ubicación", Toast.LENGTH_SHORT).show();
                    mEditTextDireccion.setEnabled(true);
                } else {
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                            ((AltaAnimalesActivity) getBaseActivity()).getmGoogleApiClient());
                    if (mLastLocation != null) {
                        Address address = getBaseActivity().getLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        try {
                            mEditTextDireccion.setText(address.getThoroughfare() + " " + address.getSubThoroughfare());
                        } catch (NullPointerException e) {
                            mEditTextDireccion.setText("");
                        }
                    } else {
                        Toast.makeText(getBaseActivity(), "No es posible encontrar tu ubicación", Toast.LENGTH_SHORT).show();

                    }
                    mEditTextDireccion.setEnabled(true);
                    mProgressBar.setVisibility(View.GONE);
                    mImageViewMap.setVisibility(View.VISIBLE);
                }
            }
        });

        mTextViewError = (TextView) mRootView.findViewById(R.id.textView_error);
        mTextInputLayoutDescription = (TextInputLayout) mRootView.findViewById(R.id.textInputLayout_descripcion);
        mTextInputLayoutTitulo = (TextInputLayout) mRootView.findViewById(R.id.textInputLayout_titulo);
        mTextInputLayoutUbicacion = (TextInputLayout) mRootView.findViewById(R.id.textInputLayout_direccion);
        mTextInputLayoutTelefono = (TextInputLayout) mRootView.findViewById(R.id.textInputLayout_telefono);
        mEditTextDescripcion = (EditText) mTextInputLayoutDescription.findViewById(R.id.editText_descripcion);
        mEditTextTitulo = (EditText) mTextInputLayoutTitulo.findViewById(R.id.editText_titulo);
        mEditTextDireccion = (EditText) mTextInputLayoutUbicacion.findViewById(R.id.editText_direccion);
        mEditTextNombre = (EditText) mRootView.findViewById(R.id.editText_nombre);
        mEditTextNombre.setText(HuellasApplication.getInstance().getProfileNameFacebook());
        mEditTextMail = (EditText) mRootView.findViewById(R.id.editText_mail);
        mEditTextMail.setText(HuellasApplication.getInstance().getProfileEmailFacebook());
        mEditTextTelefono = (EditText) mRootView.findViewById(R.id.editText_telefono);
        String telefono = HuellasApplication.getInstance().getProfileTelefono();
        if (!telefono.equals("")) {
            mEditTextTelefono.setEnabled(false);
            mEditTextTelefono.setText(telefono);
        }
        mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progress_bar_map);

        getBaseActivity().getmFloatingButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewDialogCamera.setVisibility(View.VISIBLE);
                mIsDialogVisible = true;
            }
        });
        mViewDialogCamera = getBaseActivity().getmViewDialogCamera();
        mTextViewSelectPicture = (TextView) mViewDialogCamera.findViewById(R.id.textView_escoger_foto);
        mTextViewSelectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, SELECT_PICTURE);
            }
        });

        mTextViewTakePicture = (TextView) mViewDialogCamera.findViewById(R.id.textView_tomar_foto);
        mTextViewTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Constants.IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.SHARED_PREF_IMG);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(cameraIntent, CAMERA_CAPTURE);
            }
        });

        mViewDialogCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDialogCamera();
            }
        });


        mButtonPublicar = (Button) mRootView.findViewById(R.id.button_publicar);
        mButtonPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().hideKeyboard();
                mTextViewError.setVisibility(View.GONE);
                if (mFromFragment.equals(Constants.PERDIDOS)) {
                    if (TextUtils.isEmpty(mEditTextTitulo.getText()) ||
                            TextUtils.isEmpty(mEditTextDescripcion.getText()) ||
                            TextUtils.isEmpty(mEditTextDireccion.getText()) ||
                            TextUtils.isEmpty(mEditTextTelefono.getText()) ||
                            mSpinnerEstado.getSelectedItem().equals(getString(R.string.tipo_publicacion)) ||
                            mSpinnerRazas.getSelectedItem().equals(getString(R.string.raza)) ||
                            mSpinnerTamanios.getSelectedItem().equals(getString(R.string.tamaño)) ||
                            mSpinnerEspecies.getSelectedItem().equals(getString(R.string.especie)) ||
                            mSpinnerSexos.getSelectedItem().equals(getString(R.string.sexo)) ||
                            mSpinnerColores.getSelectedItem().equals(getString(R.string.color_predominante)) ||
                            mSpinnerEdades.getSelectedItem().equals(getString(R.string.edad_aproximada))) {
                        mIsEverythingOK = false;
                    }

                    if (mIsEverythingOK) {
                        if (((boolean) mImageViewFoto.getTag())) {
                            mTextViewError.setVisibility(View.VISIBLE);
                            mTextViewError.setText("Antes de publicar, suba una imagen.");
                            YoYo.with(Techniques.Tada)
                                    .duration(700)
                                    .playOn(mTextViewError);
                        } else {
                            if (mAction.equals(Constants.ALTA)) {
                                (getBaseActivity()).showOverlay(getString(R.string.publicando));
                            } else {
                                (getBaseActivity()).showOverlay(getString(R.string.editando));
                            }
                            HuellasApplication.getInstance().saveProfileTelefono(mEditTextTelefono.getText().toString());
                            new AsyncTaskModificarTelUsuario().execute(mEditTextTelefono.getText().toString());
                        }
                    } else {
                        mIsEverythingOK = true;
                            mTextViewError.setVisibility(View.VISIBLE);
                            mTextViewError.setText("Todos los campos son requeridos.");
                            YoYo.with(Techniques.Tada)
                                    .duration(700)
                                    .playOn(mTextViewError);
                    }
                } else {
                    if (TextUtils.isEmpty(mEditTextTitulo.getText()) ||
                            TextUtils.isEmpty(mEditTextDescripcion.getText()) ||
                            TextUtils.isEmpty(mEditTextTelefono.getText())) {
                        mIsEverythingOK = false;
                    }

                    if (mIsEverythingOK) {
                        if (((boolean) mImageViewFoto.getTag())) {
                            mTextViewError.setVisibility(View.VISIBLE);
                            mTextViewError.setText("Antes de publicar, suba una imagen.");
                            YoYo.with(Techniques.Tada)
                                    .duration(700)
                                    .playOn(mTextViewError);
                        } else {
                            if (mAction.equals(Constants.ALTA)) {
                                (getBaseActivity()).showOverlay(getString(R.string.publicando));
                            } else {
                                (getBaseActivity()).showOverlay(getString(R.string.editando));
                            }
                            HuellasApplication.getInstance().saveProfileTelefono(mEditTextTelefono.getText().toString());
                            new AsyncTaskModificarTelUsuario().execute(mEditTextTelefono.getText().toString());
                        }
                    } else {
                        mIsEverythingOK = true;
                        mTextViewError.setVisibility(View.VISIBLE);
                        mTextViewError.setText("Todos los campos son requeridos.");
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(mTextViewError);
                    }
                }
            }
        });
    }

    private class AsyncTaskModificarTelUsuario extends AsyncTask<String, Void, Void> {
        private boolean error = false;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!error) {
                if (mFromFragment.equals(Constants.PERDIDOS)) {
                    mPerdidos = new Perdidos();
                    mPerdidos.setFoto(getBaseActivity().convertFromBitmapToByte(((BitmapDrawable) mImageViewFoto.getDrawable()).getBitmap()));
                    mPerdidos.setColor(new Colores(mSpinnerColores.getSelectedItem().toString()));
                    mPerdidos.setDescripcion(mEditTextDescripcion.getText().toString());
                    mPerdidos.setTitulo(mEditTextTitulo.getText().toString());
                    mPerdidos.setEstado(new Estados(mSpinnerEstado.getSelectedItem().toString()));
                    mPerdidos.setEdad(new Edades(mSpinnerEdades.getSelectedItem().toString()));
                    mPerdidos.setTamaño(new Tamaños(mSpinnerTamanios.getSelectedItem().toString()));
                    mPerdidos.setUbicacion(mEditTextDireccion.getText().toString());
                    mPerdidos.setEspecie(new Especies(mSpinnerEspecies.getSelectedItem().toString()));
                    mPerdidos.setRaza(new Razas(mSpinnerRazas.getSelectedItem().toString()));
                    mPerdidos.setSexo(new Sexos(mSpinnerSexos.getSelectedItem().toString()));
                    if (mAction.equals(Constants.ALTA)) {
                        mPerdidos.setFecha(new Date());
                        Personas personas = new Personas("", HuellasApplication.getInstance().getProfileEmailFacebook(), HuellasApplication.getInstance().getProfileNameFacebook(),
                                HuellasApplication.getInstance().getProfileTelefono(), false, false, "", "");
                        mPerdidos.setPersona(personas);
                    } else {
                        mPerdidos.setObjectId(mPerdidoEdit.getObjectId());
                        mPerdidos.setFecha(mPerdidoEdit.getFecha());
                        mPerdidos.setPersona(mPerdidoEdit.getPersona());
                        mPerdidos.setComentarios(mPerdidoEdit.getComentarios());
                    }
                    new AsyncTaskSavePublicacionPerdido().execute(mPerdidos);
                } else {
                    mAdicionales = new Adicionales();
                    mAdicionales.setFoto(getBaseActivity().convertFromBitmapToByte(((BitmapDrawable) mImageViewFoto.getDrawable()).getBitmap()));
                    mAdicionales.setDescripcion(mEditTextDescripcion.getText().toString());
                    mAdicionales.setTitulo(mEditTextTitulo.getText().toString());
                    if (mAction.equals(Constants.ALTA)) {
                        if (mFromFragment.equals(Constants.ADICIONALES_DONACIONES)) {
                            mAdicionales.setDonacion(true);
                        } else {
                            mAdicionales.setDonacion(false);
                        }
                        mAdicionales.setFecha(new Date());
                        Personas personas = new Personas(HuellasApplication.getInstance().getProfileEmailFacebook());
                        mAdicionales.setPersona(personas);
                    } else {
                        mAdicionales.setObjectId(mAdicionalEdit.getObjectId());
                        mAdicionales.setDonacion(mAdicionalEdit.isDonacion());
                        mAdicionales.setFecha(mAdicionalEdit.getFecha());
                        mAdicionales.setPersona(mAdicionalEdit.getPersona());
                        mAdicionales.setComentarios(mAdicionalEdit.getComentarios());
                    }
                    new AsyncTaskSavePublicacionAdicional().execute(mAdicionales);
                }
            } else {
                Toast.makeText(getBaseActivity(), "No se pudo dar de alta la publicación.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                mIPersonasImpl.editTelefono(HuellasApplication.getInstance().getProfileEmailFacebook(), params[0]);
            } catch (Exception e) {
                error = true;
            }
            return null;
        }
    }

    private class AsyncTaskSavePublicacionAdicional extends AsyncTask<Adicionales, Void, Adicionales> {
        private boolean error = false;

        @Override
        protected Adicionales doInBackground(Adicionales... params) {
            try {
                if (mAction.equals(Constants.ALTA)) {

                    mAdicionales.setObjectId(mIAdicionalesImpl.saveAdicional(params[0]));
                    return mAdicionales;
                } else {
                    mIAdicionalesImpl.editAdicional(params[0]);
                    return mAdicionales;
                }
            } catch (Exception e) {
                error = true;
                return null;
            }
        }

        @Override
        protected void onPostExecute(Adicionales adicional) {
            super.onPostExecute(adicional);
            if (!error) {
                if (mAction.equals(Constants.ALTA)) {
                    mAdapterCallback.addElementAdapterPublicaciones(adicional);
                    getBaseActivity().finish();
                    getBaseActivity().setResult(2);
                } else {
                    mAdapterCallback.updateElementAdapterPublicacion(adicional);
                    getBaseActivity().finish();
                    getBaseActivity().setResult(3);
                }
            } else {
                getBaseActivity().hideOverlay();
                if (mAction.equals(Constants.ALTA)) {
                    Toast.makeText(getBaseActivity(), "No se pudo dar de alta la publicación.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseActivity(), "No se pudo editar la publicación.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class AsyncTaskSavePublicacionPerdido extends AsyncTask<Perdidos, Void, Perdidos> {
        private boolean error = false;

        @Override
        protected Perdidos doInBackground(Perdidos... params) {
            try {
                if (mAction.equals(Constants.ALTA)) {
                    mPerdidos.setObjectId(mIPerdidosImpl.savePerdido(params[0]));
                    return mPerdidos;

                } else {
                    mIPerdidosImpl.editPerdido(params[0]);
                    return mPerdidos;
                }
            } catch (Exception e) {
                error = true;
                return null;
            }
        }

        @Override
        protected void onPostExecute(Perdidos perdidos) {
            super.onPostExecute(perdidos);
            if (!error) {
                if (mAction.equals(Constants.ALTA)) {
                    mAdapterCallback.addElementAdapterPublicaciones(perdidos);
                    getBaseActivity().finish();
                    getBaseActivity().setResult(2);
                } else {
                    mAdapterCallback.updateElementAdapterPublicacion(perdidos);
                    getBaseActivity().finish();
                    getBaseActivity().setResult(3);
                }
            } else {
                getBaseActivity().hideOverlay();
                if (mAction.equals(Constants.ALTA)) {
                    Toast.makeText(getBaseActivity(), "No se pudo dar de alta la publicación.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseActivity(), "No se pudo editar la publicación.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case SELECT_PICTURE:
                    performCrop(data.getData());
                    break;
                case CAMERA_CAPTURE:
                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.SHARED_PREF_IMG);
                    performCrop(Uri.fromFile(file));
                    break;
                case CROP_PIC:
                    mViewDialogCamera.setVisibility(View.GONE);
                    Bitmap mPhoto = data.getExtras().getParcelable(Constants.DATA);
                    mImageViewFoto.setImageBitmap(mPhoto);
                    mIsFromResource = false;
                    mImageViewFoto.setTag(mIsFromResource);
                    break;
            }
        }
    }

    private void performCrop(Uri picUri) {
        Intent cropIntent = new Intent(Constants.IMAGE_CROP);
        cropIntent.setDataAndType(picUri, Constants.URI_NAME);
        cropIntent.putExtra(Constants.EXTRA_CROP, "true");
        cropIntent.putExtra(Constants.EXTRA_ASPECTX, 1);
        cropIntent.putExtra(Constants.EXTRA_ASPECTY, 1);
        cropIntent.putExtra(Constants.EXTRA_OUTPUTX, 400);
        cropIntent.putExtra(Constants.EXTRA_OUTPUTY, 400);
        cropIntent.putExtra(Constants.EXTRA_RETURN_DATA, true);
        startActivityForResult(cropIntent, CROP_PIC);
    }

    public boolean onBackPressed() {
        if (mIsDialogVisible) {
            hideDialogCamera();
            return true;
        } else {
            return false;
        }

    }

    private void hideDialogCamera() {
        mViewDialogCamera.setVisibility(View.GONE);
        mIsDialogVisible = false;
    }
}
