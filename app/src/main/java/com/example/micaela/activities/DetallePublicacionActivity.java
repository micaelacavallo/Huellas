package com.example.micaela.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.MenuItem;

import com.example.micaela.fragments.DetallePublicacionFragment;
import com.example.micaela.huellas.R;

public class DetallePublicacionActivity  extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_publicacion);
        showUpButton();

        getmFloatingButton().setLayoutParams(new CoordinatorLayout.LayoutParams(0, 0));
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, new DetallePublicacionFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_comment:
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public int getLayoutBase() {
        return R.layout.activity_base_collapsing;
    }
}