package com.example.micaela.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.micaela.activities.BaseActivity;
import com.example.micaela.activities.ComentariosActivity;
import com.example.micaela.activities.MapActivity;
import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.db.Modelo.Perdidos;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.Constants;

import java.util.Date;

import static com.example.micaela.utils.SpannableUtils.bold;

public class DetallePublicacionFragment extends BaseFragment {

    TextView mTextViewEstado;
    TextView mTextViewDescripcion;
    TextView mTextViewFecha;
    TextView mTextViewDatos;
    TextView mTextViewPersona;
    TextView mTextViewDireccion;
    View mViewLocation;
    ImageView mImageViewLocation;

    Perdidos mPerdidos;
    Adicionales mAdicionales;
    private View mRootView;

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_detalle_publicacion, container, false);
        wireUpViews();

        if (Constants.PERDIDOS.equals(getBaseActivity().getIntent().getStringExtra(Constants.FROM_FRAGMENT))) {
            mPerdidos = getBaseActivity().getIntent().getParcelableExtra(Constants.OBJETO_PERDIDO);
            fillViews(mPerdidos.getPersona().getNombre(), mPerdidos.getPersona().getTelefono(),
                    mPerdidos.getEstado().getSituacion(), mPerdidos.getFoto(), mPerdidos.getTitulo(),
                    mPerdidos.getDescripcion(), mPerdidos.getRaza().getmRaza(), mPerdidos.getEspecie().getEspecie(),
                    mPerdidos.getColor().getColor(), mPerdidos.getTamaño().getTamaño(), mPerdidos.getEdad().getEdad(),
                    mPerdidos.getSexo().getSexo(), mPerdidos.getLatitud(), mPerdidos.getLongitud(), mPerdidos.getFecha());
        } else {
            mAdicionales = getBaseActivity().getIntent().getParcelableExtra(Constants.OBJETO_PERDIDO);
            mTextViewDatos.setVisibility(View.GONE);
            mViewLocation.setVisibility(View.GONE);
            fillViews(mAdicionales.getPersona().getNombre(), mAdicionales.getPersona().getTelefono(), "", mAdicionales.getFoto(),
                    mAdicionales.getTitulo(), mAdicionales.getDescripcion(), "", "", "", "", "", "", 0, 0, mAdicionales.getFecha());
        }

        setHasOptionsMenu(true);

        return mRootView;
    }

    private void fillViews(String nombre, final String telefono, String situacion, byte[] foto,
                           String titulo, String descripcion, String raza, String especie, String color,
                           String tamaño, String edad, String sexo, double latitud, double longitud, Date fecha) {

        if (!situacion.equals("")) {
            if (situacion.equals(getString(R.string.buscado_minus))) {
                mTextViewEstado.setText(getString(R.string.buscado_mayus));
                mTextViewEstado.setBackgroundResource(R.color.orange_light);
            } else {
                if (situacion.equals(getString(R.string.encontrado_minus))) {
                    mTextViewEstado.setText(getString(R.string.encontrado_mayus));
                    mTextViewEstado.setBackgroundResource(R.color.blue_light);
                } else {
                    if (situacion.equals(getString(R.string.adopcion_minus))) {
                        mTextViewEstado.setText(getString(R.string.adopcion_mayus));
                        mTextViewEstado.setBackgroundResource(R.color.green_light);
                    }
                }
            }
        }
        getBaseActivity().setUpCollapsingToolbar(titulo, getBaseActivity().convertFromByteToBitmap(foto));
        mTextViewDescripcion.setText(descripcion);

        if (raza.equals("Otra")) {
            raza = "";
        }

        mTextViewDatos.setText(especie + " " + raza + " de color " + color.toLowerCase() +
                ", " + sexo.toLowerCase() + ", " + edad.toLowerCase() + " y de tamaño " + tamaño.toLowerCase() + ".");

        mTextViewFecha.setText(TextUtils.concat(bold("Fecha de publicación: "), ((BaseActivity) getActivity()).getFormattedDate(fecha)));

        if (latitud != 0 & longitud != 0) {
            Address address = getBaseActivity().getLocation(latitud, longitud);

            try {
                String location = address.getThoroughfare() + " " + address.getSubThoroughfare();
                if (!location.equals("")) {
                    mTextViewDireccion.setText(TextUtils.concat(bold(getBaseActivity().getString(R.string.ubicacion)), location));
                } else {
                    mImageViewLocation.setVisibility(View.GONE);
                    mTextViewDireccion.setText(TextUtils.concat(bold(getBaseActivity().getString(R.string.ubicacion)), "No especificada"));

                }
            } catch (NullPointerException e) {
                mImageViewLocation.setVisibility(View.GONE);
                mTextViewDireccion.setText(TextUtils.concat(bold(getBaseActivity().getString(R.string.ubicacion)), "No especificada"));
            }
        }

        String infoContacto = "Contacto: " + nombre + " (tel: " + telefono + ")";

        SpannableString ss = new SpannableString(TextUtils.concat(bold("Contacto: "), nombre + " (tel: " + telefono + ")"));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + telefono));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        ss.setSpan(clickableSpan, infoContacto.lastIndexOf("tel: "), infoContacto.indexOf(")"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextViewPersona.setText(ss);
        mTextViewPersona.setMovementMethod(LinkMovementMethod.getInstance());
        mTextViewPersona.setHighlightColor(Color.TRANSPARENT);
    }


    private void wireUpViews() {
        getBaseActivity().getCardEstado().setVisibility(View.VISIBLE);
        mImageViewLocation = (ImageView) mRootView.findViewById(R.id.imageView_location);
        mViewLocation = mRootView.findViewById(R.id.layout_ubicacion_container);
        mViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageViewLocation.getVisibility() == View.VISIBLE) {
                    Intent intent = new Intent(getBaseActivity(), MapActivity.class);
                    intent.putExtra(Constants.LATITUD, mPerdidos.getLatitud());
                    intent.putExtra(Constants.LONGITUD, mPerdidos.getLongitud());
                    intent.putExtra(Constants.ADDRESS, mTextViewDireccion.getText().toString());
                    startActivity(intent);
                }
            }
        });
        mTextViewEstado = (TextView) ((CardView)getBaseActivity().getCardEstado()).getChildAt(0);
        mTextViewDescripcion = (TextView) mRootView.findViewById(R.id.textView_descripcion);
        mTextViewFecha = (TextView) mRootView.findViewById(R.id.textView_fecha);
        mTextViewDatos = (TextView) mRootView.findViewById(R.id.textView_datos);
        mTextViewPersona = (TextView) mRootView.findViewById(R.id.textView_persona);
        mTextViewDireccion = (TextView) mRootView.findViewById(R.id.textView_direccion);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detalle, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_comment:
                Intent intent = new Intent(getBaseActivity(), ComentariosActivity.class);
                if (Constants.PERDIDOS.equals(getBaseActivity().getIntent().getStringExtra(Constants.FROM_FRAGMENT))) {
                    intent.putParcelableArrayListExtra(Constants.COMENTARIOS_LIST, mPerdidos.getComentarios());
                }
                else {
                    intent.putParcelableArrayListExtra(Constants.COMENTARIOS_LIST, mAdicionales.getComentarios());
                }
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}