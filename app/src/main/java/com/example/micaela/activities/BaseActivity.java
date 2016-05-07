package com.example.micaela.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.micaela.HuellasApplication;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.Constants;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {

    Toolbar mToolbar;
    TextView mToolbarTitle;
    ViewGroup mainContainer;
    ViewGroup containerLayout;
    CollapsingToolbarLayout mCollapsingToolbar;
    private boolean isOverlayOpen = false;
    FloatingActionButton mFloatingButton;
    ImageView mImageViewPicture;
    View mViewDialogCamera;

    private View mCardEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public abstract int getLayoutBase();

    @Override
    public void setContentView(int layoutResID) {
        mainContainer = (ViewGroup) getLayoutInflater().inflate(getLayoutBase(), null);
        switch (getLayoutBase()) {
            case R.layout.activity_base:
                mToolbarTitle = (TextView) mainContainer.findViewById(R.id.toolbar_title);

                break;
            case R.layout.activity_base_collapsing:
                mCollapsingToolbar = (CollapsingToolbarLayout) mainContainer.findViewById(R.id.collapsing_toolbar);
                mViewDialogCamera = mainContainer.findViewById(R.id.dialog_take_picture);
                mFloatingButton = (FloatingActionButton) mainContainer.findViewById(R.id.button_camera);
                mImageViewPicture = (ImageView) mainContainer.findViewById(R.id.imageView_foto);
                mCardEstado = mainContainer.findViewById(R.id.card_estado);
                break;
        }
        setupToolbar(mainContainer);
        containerLayout = (ViewGroup) mainContainer.findViewById(R.id.container_base);
        ViewGroup content = (ViewGroup) getLayoutInflater().inflate(layoutResID, containerLayout, false);
        containerLayout.addView(content);

        super.setContentView(mainContainer);
    }

    public View getCardEstado() {
        return mCardEstado;
    }

    public View getmViewDialogCamera() {
        return mViewDialogCamera;
    }

    public void setToolbarTitle(String title) {
        mToolbarTitle.setText(title);
        mToolbarTitle.setTextAppearance(this, R.style.condensed_normal_22);
    }

    public ImageView getmImageViewPicture() {
        return mImageViewPicture;
    }

    public FloatingActionButton getmFloatingButton() {
        return mFloatingButton;
    }


    public void setUpCollapsingToolbar(String title) {
        mCollapsingToolbar.setTitle(title);
        mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
    }

    public void setUpCollapsingToolbar(String title, Bitmap bitmapImage) {
        mCollapsingToolbar.setTitle(title);
        mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        if (bitmapImage != null) {
            ((ImageView) mCollapsingToolbar.findViewById(R.id.imageView_foto)).setImageBitmap(bitmapImage);
        }
    }

    public void setUpCollapsingToolbar(String title, String uriImage) {
        mCollapsingToolbar.setTitle(title);
        mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        if (uriImage != null) {
            Picasso.with(this).load(Uri.parse(uriImage)).placeholder(R.mipmap.placeholder).into(((ImageView) mCollapsingToolbar.findViewById(R.id.imageView_foto)));
        }
    }


    public Address getLocation(double latitud, double longitud) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        Address address = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(latitud, longitud, 1);
            try {
                address = addresses.get(0);
            } catch (NullPointerException e) {
            }
        } catch (IOException e) {
        }

        return address;
    }

    public LatLng convertAddress(String address) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        LatLng latLng = null;
        if (address != null && !address.isEmpty()) {
            try {
                List<Address> addressList = geocoder.getFromLocationName(address, 1);
                if (addressList != null && addressList.size() > 0) {
                    latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                }
            } catch (Exception e) {
                latLng = null;
            } // end catch
        } // end if
        return latLng;
    }

    private void setupToolbar(ViewGroup mainContainer) {
        mToolbar = (Toolbar) mainContainer.findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public String getToolbarTitle() {
        return mToolbarTitle.getText().toString();
    }

    public void showUpButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            if (upArrow != null) {
                upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
            }
            actionBar.setHomeAsUpIndicator(upArrow);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    public void hideOverlay() {
        mainContainer.findViewById(R.id.layout_base_overlay).setVisibility(View.GONE);
        isOverlayOpen = false;
    }


    public void showOverlay(String mensaje) {
        isOverlayOpen = true;
        mainContainer.findViewById(R.id.layout_base_overlay).setVisibility(View.VISIBLE);
        mainContainer.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        ((ProgressBar) mainContainer.findViewById(R.id.progress_bar)).getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.accent), android.graphics.PorterDuff.Mode.MULTIPLY);
        ((TextView) mainContainer.findViewById(R.id.textView_titulo_overlay)).setText(mensaje);
        mainContainer.findViewById(R.id.button_confirmar).setVisibility(View.GONE);
    }

    public void showMessageOverlay(String mensaje, View.OnClickListener listener) {
        isOverlayOpen = true;
        mainContainer.findViewById(R.id.layout_base_overlay).setVisibility(View.VISIBLE);
        mainContainer.findViewById(R.id.progress_bar).setVisibility(View.GONE);
        ((TextView) mainContainer.findViewById(R.id.textView_titulo_overlay)).setText(mensaje);
        mainContainer.findViewById(R.id.button_confirmar).setVisibility(View.VISIBLE);
        mainContainer.findViewById(R.id.button_confirmar).setOnClickListener(listener);
    }

    public void logOut() {
        LoginManager.getInstance().logOut();
        HuellasApplication.getInstance().clearProfileFacebook();
        Intent aIntent = new Intent(getBaseContext(), PrincipalActivity.class);
        aIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(aIntent);
    }


    public void loadMainScreen() {
        Intent aIntent = new Intent(getBaseContext(), PrincipalActivity.class);
        aIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(aIntent);
    }

    public String getFormattedDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.PUBLICATION_DETAILS_DATE_FORMAT, Locale.getDefault());
        return sdf.format(date) + " hs.";
    }

    public String getFormattedDate2(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.COMENTARIOS_DATE_FORMAT, Locale.getDefault());
        return sdf.format(date) + " hs.";
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
                        difference = "Hace un momento";
                    } else {
                        difference = String.format("Hace %s min", diffMinutes);
                    }
                } else {
                    difference = String.format("Hace %s hs", diffHours);
                }
            } else {
                int diffMonths = (int) (diffDays / 30);
                if (diffMonths <= 0) {
                    if (diffDays == 1) {
                        difference = "Hace 1 día";
                    } else {
                        difference = String.format("Hace %s días", diffDays);
                    }
                } else {
                    if (diffMonths == 1) {
                        difference = "Hace 1 mes";
                    } else {
                        difference = String.format("Hace %s meses", diffMonths);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return difference;
    }

    public Bitmap convertFromByteToBitmap(byte[] pic) {
        return BitmapFactory.decodeByteArray(pic, 0, pic.length);
    }

    public byte[] convertFromBitmapToByte(Bitmap pic) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onBackPressed() {
        if (!isOverlayOpen) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

        }
        return true;
    }
}