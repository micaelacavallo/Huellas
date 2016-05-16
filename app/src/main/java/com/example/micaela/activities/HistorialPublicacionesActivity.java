package com.example.micaela.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.micaela.fragments.HistorialPublicacionesFragment;
import com.example.micaela.huellas.R;

/**
 * Created by Micaela on 05/05/2016.
 */
public class HistorialPublicacionesActivity extends BaseActivity {
    private HistorialPublicacionesFragment mFragment;
    @Override
    public int getLayoutBase() {
        return R.layout.activity_base;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_publicaciones);
        showUpButton();
        mFragment = new HistorialPublicacionesFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, mFragment).commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_solucionadas:
                return false;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        boolean backPressed = false;
            if (mFragment.onBackPressed()) {
                backPressed = true;
            }

        if (!backPressed) {
            super.onBackPressed();
        }
    }


}
