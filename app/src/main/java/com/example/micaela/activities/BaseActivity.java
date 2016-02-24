package com.example.micaela.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.micaela.HuellasApplication;
import com.example.micaela.huellas.R;
import com.facebook.login.LoginManager;

import java.util.Date;

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


    public void showOverlay(String mensaje) {
        mainContainer.findViewById(R.id.layout_base_overlay).setVisibility(View.VISIBLE);
        mainContainer.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        ((TextView) mainContainer.findViewById(R.id.textView_titulo)).setText(mensaje);
        mainContainer.findViewById(R.id.button_confirmar).setVisibility(View.GONE);
    }

    public void showErrorOverlay(String mensaje) {
        mainContainer.findViewById(R.id.layout_base_overlay).setVisibility(View.VISIBLE);
        mainContainer.findViewById(R.id.progress_bar).setVisibility(View.GONE);
        ((TextView) mainContainer.findViewById(R.id.textView_titulo)).setText(mensaje);
        mainContainer.findViewById(R.id.button_confirmar).setVisibility(View.VISIBLE);
        mainContainer.findViewById(R.id.button_confirmar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(-1);
                finish();
            }
        });

    }

    public void logOut() {
        LoginManager.getInstance().logOut();
        HuellasApplication.getInstance().clearProfileFacebook();
        Intent aIntent = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(aIntent);
    }

    public String getPublicationTime(Date date) {
        Date currentDate = new Date();
        String difference = "";
        try {
            long diff = currentDate.getTime() - date.getTime();
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if (diffDays == 0) {
                if (diffHours == 0) {
                    if (diffMinutes < 1) {
                        difference = "Hace un momento.";
                    } else {
                        difference = String.format("Hace %s min.", diffMinutes);
                    }
                } else {
                    difference = String.format("Hace %s hs.", diffHours);
                }
            } else {
                int diffMonths = (int) (diffDays / 30);
                if (diffMonths <= 0) {
                    if (diffDays == 1) {
                        difference = "Hace 1 día.";
                    } else {
                        difference = String.format("Hace %s días.", diffDays);
                    }
                } else {
                    if (diffMonths == 1) {
                        difference = "Hace 1 mes.";
                    } else {
                        difference = String.format("Hace %s meses.", diffMonths);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return difference;
    }

    public Bitmap convertFromByteToBitmap (byte[] pic) {
        return BitmapFactory.decodeByteArray(pic, 0, pic.length);
    }

}