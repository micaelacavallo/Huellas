package com.managerapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.managerapp.R;

/**
 * Created by Micaela on 20/05/2016.
 */
public class PersonasDenunciadasActivity  extends BaseActivity  {

    private View mDialogContainer;
    private TextView mTextViewDialogMsg;
    private TextView mTextViewConfirmar;
    private TextView mTextViewCancelar;
    private boolean isDialogOpen = false;

    @Override
    public int getLayoutBase() {
        return R.layout.activity_base;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personas_reportadas);
        showUpButton();
        setToolbarTitle("Personas Reportadas");

        mDialogContainer = findViewById(R.id.layout_dialog_container);
        mTextViewCancelar = (TextView) findViewById(R.id.textView_cancelar);
        mTextViewConfirmar = (TextView) findViewById(R.id.textView_confirmar);
        mTextViewDialogMsg = (TextView) findViewById(R.id.textView_confirmar_mensaje);
    }


    public void showLoadDialog() {
        findViewById(R.id.dialog_content).setVisibility(View.GONE);
        findViewById(R.id.dialog_load).setVisibility(View.VISIBLE);
        ((ProgressBar) findViewById(R.id.progress_bar_dialog)).getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.accent), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public void hideLoadDialog() {
        findViewById(R.id.dialog_content).setVisibility(View.VISIBLE);
        findViewById(R.id.dialog_load).setVisibility(View.GONE);
    }


    public void showNormalDialog(String text, View.OnClickListener listener) {
        mDialogContainer.setVisibility(View.VISIBLE);
        isDialogOpen = true;
        mTextViewDialogMsg.setText(text);
        mTextViewConfirmar.setEnabled(true);
        findViewById(R.id.view_line).setVisibility(View.GONE);
        mTextViewCancelar.setVisibility(View.VISIBLE);
        mTextViewCancelar.setOnClickListener(listener);
        mTextViewConfirmar.setOnClickListener(listener);
    }


    public void closeDialog() {
        hideLoadDialog();
        isDialogOpen = false;
        mDialogContainer.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (isDialogOpen) {
            closeDialog();
        } else {
            super.onBackPressed();
        }
    }
}
