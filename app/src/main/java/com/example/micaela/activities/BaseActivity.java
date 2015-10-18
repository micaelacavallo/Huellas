package com.example.micaela.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.example.micaela.db.Controladores.IPerdidosImpl;
import com.example.micaela.huellas.R;
import com.parse.ParseException;

public class BaseActivity extends AppCompatActivity {

    Toolbar mToolbar;
    ViewGroup mainContainer;
    ViewGroup containerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        mainContainer = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_base, null);

        setupToolbar(mainContainer);
        containerLayout = (ViewGroup) mainContainer.findViewById(R.id.container_base);
        ViewGroup content = (ViewGroup) getLayoutInflater().inflate(layoutResID, containerLayout, false);
        containerLayout.addView(content);

        super.setContentView(mainContainer);
    }


    private void setupToolbar(ViewGroup mainContainer) {
        mToolbar = (Toolbar) mainContainer.findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        // If it is not Dashboard, set up button for header
        if (!(this instanceof PrincipalActivity)) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }
//
//    public void setToolbarTitle(String title) {
//        TextView loyalTV = (TextView) mToolbar.findViewById(R.id.txt_toolbar1);
//        TextView titleTV = (TextView) mToolbar.findViewById(R.id.toolbar_fragment_title);
//
//        if (TextUtils.isEmpty(title)) {
//            loyalTV.setVisibility(View.VISIBLE);
//            titleTV.setVisibility(View.GONE);
//        } else {
//            loyalTV.setVisibility(View.GONE);
//            titleTV.setVisibility(View.VISIBLE);
//            titleTV.setText(title);
//        }
//    }
}
