package com.example.micaela.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.micaela.activities.AltaAnimalesActivity;
import com.example.micaela.db.DAO.PerdidosDAO;
import com.example.micaela.db.Modelo.Colores;
import com.example.micaela.db.Modelo.Edades;
import com.example.micaela.db.Modelo.Especies;
import com.example.micaela.db.Modelo.Estados;
import com.example.micaela.db.Modelo.Razas;
import com.example.micaela.db.Modelo.Tamaños;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.Constants;

import java.io.File;
import java.util.List;

public class AltaAnimalesFragment extends BaseFragment {

    private TextView mTextViewTakePicture;
    private TextView mTextViewSelectPicture;
    private View mViewDialogCamera;
    private boolean mIsDialogVisible = false;

    private List<Razas> mRazas;
    private List<Especies> mEspecies;
    private List<Tamaños> mTamanios;
    private List<Edades> mEdades;
    private List<Colores> mColores;
    private List<Estados> mEstados;

    private ImageView mImageViewFoto;
    private Spinner mSpinnerEdades;
    private Spinner mSpinnerRazas;
    private Spinner mSpinnerEspecies;
    private Spinner mSpinnerTamanios;
    private Spinner mSDpinnerColores;
    private Spinner mSpinnerSexos;
    private Spinner mSpinnerEstado;
    private TextInputLayout mTextInputLayoutTitulo;
    private TextInputLayout mTextInputLayoutDescription;
    private TextInputLayout mTextInputLayoutUbicacion;
    private EditText mEditTextTitulo;
    private EditText mEditTextDescripcion;
    private EditText mEditTextUbicacion;

    private final int CAMERA_CAPTURE = 1;
    private final int SELECT_PICTURE = 2;
    private final int CROP_PIC = 3;

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alta_animales, container, false);

        getBaseActivity().setUpCollapsingToolbar("Crear publicación");

        mImageViewFoto = (ImageView) view.findViewById(R.id.imageView_foto);
        mSpinnerEdades = (Spinner) view.findViewById(R.id.spinner_edades);
        mSpinnerEspecies = (Spinner) view.findViewById(R.id.spinner_especies);
        mSpinnerEstado = (Spinner) view.findViewById(R.id.spinner_estado);
        mSDpinnerColores =  (Spinner) view.findViewById(R.id.spinner_colores);
        mSpinnerSexos = (Spinner) view.findViewById(R.id.spinner_sexos);
        mSpinnerTamanios = (Spinner) view.findViewById(R.id.spinner_tamanios);

        mTextInputLayoutDescription = (TextInputLayout) view.findViewById(R.id.textInputLayout_descripcion);
        mTextInputLayoutTitulo = (TextInputLayout) view.findViewById(R.id.textInputLayout_titulo);
        mTextInputLayoutUbicacion = (TextInputLayout) view.findViewById(R.id.textInputLayout_ubicacion);

        mEditTextDescripcion = (EditText) mTextInputLayoutDescription.findViewById(R.id.editText_descripcion);
        mEditTextTitulo = (EditText) mTextInputLayoutTitulo.findViewById(R.id.editText_titulo);
        mEditTextUbicacion = (EditText) mTextInputLayoutUbicacion.findViewById(R.id.editText_ubicacion);

        /*CustomSpinnerHintAdapter adapter = new CustomSpinnerHintAdapter(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll("Item 1");
        adapter.add("Hint to be displayed");

        spinner.setAdapter(adapter);
*/


/*
        PerdidosDAO perdidosDAO = new PerdidosDAO(getActivity());
        List<Especies> especiesList = perdidosDAO.getEspecies();
        ListperdidosDAO.getRazas();
        perdidosDAO.getColores();
        perdidosDAO.getEdades();
        perdidosDAO.
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);*/

        FloatingActionButton buttonCamera = (FloatingActionButton) view.findViewById(R.id.button_camera);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewDialogCamera.setVisibility(View.VISIBLE);
                mIsDialogVisible = true;
            }
        });

        mViewDialogCamera = view.findViewById(R.id.dialog_take_picture);
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
                mViewDialogCamera.setVisibility(View.GONE);
                mIsDialogVisible = false;
            }
        });


        return view;

    }


    private class AsyncTaskPerdidos extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                PerdidosDAO perdidosDAO = new PerdidosDAO(getActivity());
                mEspecies = perdidosDAO.getEspecies();
                mRazas = perdidosDAO.getRazas();
                mColores = perdidosDAO.getColores();
                mEdades = perdidosDAO.getEdades();
                mTamanios = perdidosDAO.getTamaños();
                mEstados = perdidosDAO.getEstados();
            }
            catch (Exception e) {
                getBaseActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        View.OnClickListener listener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getBaseActivity().finish();
                                Intent intent = new Intent(getBaseActivity(), AltaAnimalesActivity.class);
                                startActivity(intent);
                            }
                        };
                        getBaseActivity().showErrorOverlay("Hubo un problema, por favor intente nuevamente", listener);
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getBaseActivity().showOverlay("Cargando...");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getBaseActivity().hideOverlay();
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
        cropIntent.putExtra(Constants.EXTRA_OUTPUTX, mImageViewFoto.getWidth() + 100);
        cropIntent.putExtra(Constants.EXTRA_OUTPUTY, mImageViewFoto.getHeight() + 100);
        cropIntent.putExtra(Constants.EXTRA_RETURN_DATA, true);
        startActivityForResult(cropIntent, CROP_PIC);
    }

    public boolean onBackPressed() {
        if (mIsDialogVisible) {
            mViewDialogCamera.setVisibility(View.GONE);
            mIsDialogVisible = false;
            return true;
        } else {
            return false;
        }

    }
}
