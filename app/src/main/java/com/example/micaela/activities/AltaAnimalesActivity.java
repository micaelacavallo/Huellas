package com.example.micaela.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.micaela.fragments.AltaAnimalesFragment;
import com.example.micaela.huellas.R;

import java.util.List;

public class AltaAnimalesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_animales);
    }


    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (!((AltaAnimalesFragment) fragment).onBackPressed()) {
                    super.onBackPressed();
                }
            }
        }
    }

}
