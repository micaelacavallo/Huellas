package com.managerapp.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;

import com.managerapp.fragments.MisDatosFragment;


/**
 * Created by Micaela on 20/04/2016.
 */
public class MisDatosActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_datos);
        showUpButton();
        getmFloatingButton().setLayoutParams(new CoordinatorLayout.LayoutParams(0, 0));
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, new MisDatosFragment()).commit();
    }

    @Override
    public int getLayoutBase() {
        return R.layout.activity_base_collapsing;
    }
}
