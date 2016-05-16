package com.managerapp.activities;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.managerapp.R;
import com.managerapp.utils.Constants;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {

    private double mLatitud;
    private double mLongitud;
    private String mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        showUpButton();
                setToolbarTitle("Ubicación");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mLatitud = getIntent().getDoubleExtra(Constants.LATITUD, 0);
        mLongitud = getIntent().getDoubleExtra(Constants.LONGITUD, 0);
        mAddress = getIntent().getStringExtra(Constants.ADDRESS);

        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(mLatitud, mLongitud))
                .title(mAddress));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mLatitud, mLongitud))
                .zoom(17).build();
        //Zoom in and animate the camera.
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public int getLayoutBase() {
        return R.layout.activity_base;
    }

}
