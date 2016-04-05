package com.example.micaela.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.micaela.activities.BaseActivity;
import com.example.micaela.db.Modelo.Perdidos;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.Constants;

import static com.example.micaela.utils.SpannableUtils.bold;

public class DetallePublicacionFragment extends BaseFragment {

    TextView mTextViewEstado;
    TextView mTextViewDescripcion;
    TextView mTextViewFecha;
    TextView mTextViewDatos;
    TextView mTextViewPersona;
    TextView mTextViewDireccion;

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_publicacion, container, false);

        final Perdidos perdido = getActivity().getIntent().getParcelableExtra(Constants.OBJETO_PERDIDO);
        wireUpViews(view, perdido);

        if (perdido.getEstado().getSituacion().equals(getString(R.string.buscado_minus))) {
            mTextViewEstado.setText(getString(R.string.buscado_mayus));
            mTextViewEstado.setBackgroundResource(R.color.orange_light);
        } else {
            if (perdido.getEstado().getSituacion().equals(getString(R.string.encontrado_minus))) {
                mTextViewEstado.setText(getString(R.string.encontrado_mayus));
                mTextViewEstado.setBackgroundResource(R.color.blue_light);
            } else {
                if (perdido.getEstado().getSituacion().equals(getString(R.string.adopcion_minus))) {
                    mTextViewEstado.setText(getString(R.string.adopcion_mayus));
                    mTextViewEstado.setBackgroundResource(R.color.green_light);
                }
            }
        }
        byte[] foto = perdido.getFoto();
        getBaseActivity().setUpCollapsingToolbar(perdido.getTitulo(), getBaseActivity().convertFromByteToBitmap(foto));

        mTextViewDescripcion.setText(perdido.getDescripcion());
        mTextViewDatos.setText(perdido.getEspecie().getEspecie() + " " + perdido.getRaza().getRaza() + " de color " + perdido.getColor().getColor().toLowerCase() +
                ", " + perdido.getSexo().getSexo().toLowerCase() + ", " + perdido.getEdad().getEdad().toLowerCase() + " y de tamaño " + perdido.getTamaño().getTamaño().toLowerCase() + ".");

        mTextViewFecha.setText(TextUtils.concat(bold("Fecha de publicación: "), ((BaseActivity) getActivity()).getFormattedDate(perdido.getFecha())));
        mTextViewDireccion.setText(TextUtils.concat(bold("Ubicación: "), "PROXIMAMENTE"));// TODO falta direccion que hay que relacionarlo con google maps y falta poner tel para que pueda llamar directo de la publicacion


        String infoContacto = "Contacto: " + perdido.getPersona().getNombre() + " " + perdido.getPersona().getApellido()
                + " (tel: " + perdido.getPersona().getTelefono() + ")";

        SpannableString ss = new SpannableString(TextUtils.concat(bold("Contacto: "), perdido.getPersona().getNombre() + " " + perdido.getPersona().getApellido()
                + " (tel: " + perdido.getPersona().getTelefono() + ")"));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + perdido.getPersona().getTelefono()));
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


        return view;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void wireUpViews(View view, Perdidos perdido) {

        TextView textViewEstado = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                130, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        textViewEstado.setLayoutParams(layoutParams);
        textViewEstado.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textViewEstado.setPadding(15, 15, 15, 15);
        textViewEstado.setTextAppearance(getContext(),R.style.condensed_bold_17);
        textViewEstado.setTextColor(getResources().getColor(R.color.primary_text));

        CardView cardViewEstado = new CardView(getContext());
        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.TOP | Gravity.END;
        cardViewEstado.setElevation(2);
        cardViewEstado.setLayoutParams(layoutParams);

        cardViewEstado.addView(textViewEstado);

        mTextViewEstado = textViewEstado;
        mTextViewDescripcion = (TextView) view.findViewById(R.id.textView_descripcion);
        mTextViewFecha = (TextView) view.findViewById(R.id.textView_fecha);
        mTextViewDatos = (TextView) view.findViewById(R.id.textView_datos);
        mTextViewPersona = (TextView) view.findViewById(R.id.textView_persona);
        mTextViewDireccion = (TextView) view.findViewById(R.id.textView_direccion);
    }
}