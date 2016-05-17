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

    private String mUbicacion;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        showUpButton();
        setToolbarTitle("Ubicación");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mUbicacion = getIntent().getStringExtra(Constants.DIRECCION);

        latLng = convertAddress(mUbicacion);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (latLng != null) {
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(mUbicacion));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(17).build();
            //Zoom in and animate the camera.
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public int getLayoutBase() {
        return R.layout.activity_base;
    }

}
