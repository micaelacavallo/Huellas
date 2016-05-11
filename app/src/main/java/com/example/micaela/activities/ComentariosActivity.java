package com.example.micaela.activities;

import android.os.Bundle;

import com.example.micaela.huellas.R;
import com.example.micaela.utils.Constants;

public class ComentariosActivity extends BaseActivity {

    private boolean mFromPush;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);
        getSupportActionBar().hide();
        mFromPush = getIntent().getBooleanExtra(Constants.PUSH_OPEN, false);

    }

    @Override
    public int getLayoutBase() {
        return R.layout.activity_base;
    }


    @Override
    public void onBackPressed() {
        if (mFromPush) {
            goToMainActivity();
        }
        else {
            super.onBackPressed();
        }
    }
}
