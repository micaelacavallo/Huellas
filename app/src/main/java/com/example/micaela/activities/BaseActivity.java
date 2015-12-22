package com.example.micaela.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.micaela.huellas.R;

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
        if (!(this instanceof PrincipalActivity) && !(this instanceof LoginActivity)) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    public void hideOverlay() {
        mainContainer.findViewById(R.id.layout_base_overlay).setVisibility(View.GONE);
    }

    public void showOverlay() {
        mainContainer.findViewById(R.id.layout_base_overlay).setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_image);
        mainContainer.findViewById(R.id.imageView).startAnimation(animation);
    }

    public void showOverlay(String mensaje) {
        mainContainer.findViewById(R.id.layout_base_overlay).setVisibility(View.VISIBLE);
        mainContainer.findViewById(R.id.imageView).setVisibility(View.GONE);
        ((TextView) mainContainer.findViewById(R.id.textView_titulo)).setText(mensaje);
    }
}