package com.example.micaela.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;

import com.example.micaela.fragments.MisDatosFragment;
import com.example.micaela.huellas.R;

/**
 * Created by Micaela on 20/04/2016.
 */
public class MisDatosActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_datos);
        getmFloatingButton().setLayoutParams(new CoordinatorLayout.LayoutParams(0, 0));
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, new MisDatosFragment()).commit();
    }
}
