package com.managerapp.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.managerapp.HuellasApplication;
import com.managerapp.R;
import com.managerapp.db.Controladores.IEstadosImpl;
import com.managerapp.db.Controladores.IPerdidosImpl;
import com.managerapp.db.Controladores.IPersonasImpl;
import com.managerapp.db.Interfaces.IEstados;
import com.managerapp.db.Interfaces.IPerdidos;
import com.managerapp.db.Interfaces.IPersonas;
import com.managerapp.db.Modelo.Estados;
import com.managerapp.fragments.BaseFragment;
import com.managerapp.fragments.DonacionesFragment;
import com.managerapp.fragments.InformacionUtilFragment;
import com.managerapp.fragments.PerdidosFragment;
import com.managerapp.utils.Constants;
import com.managerapp.utils.ZoomOutPageTransformer;

import java.util.List;


public class PrincipalActivity extends BaseActivity {
    private ViewPager mPager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mIsDrawerOpen = false;

    private IPerdidos mIPerdidosImpl;
    private IEstados mIEstadosImpl;
    private IPersonas mIPersonasImpl;

    private View mDialogContainer;
    private TextView mTextViewDialogMsg;
    private TextView mTextViewConfirmar;
    private TextView mTextViewCancelar;

    private boolean thereWasError = false;
    private boolean isDialogOpen = false;

    private int mCountInfoLoaded;

    public void setCountInfoLoaded() {
        mCountInfoLoaded++;
        if (mCountInfoLoaded == 3) {
            hideOverlay();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        mIPerdidosImpl = new IPerdidosImpl(this);
        mIEstadosImpl = new IEstadosImpl(this);
        mIPersonasImpl = new IPersonasImpl(this);

        mCountInfoLoaded = 0;

        new AsyncTaskPerdidosInfo().execute();

        if (!HuellasApplication.getInstance().isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE_OK);
        } else {
            setUpPager();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer_nav_layout);
        navigationView.setItemIconTintList(null);
        setupDrawerContent(navigationView);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.app_name,  /* "open drawer" description */
                R.string.app_name  /* "close drawer" description */
        ) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                mIsDrawerOpen = false;
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mIsDrawerOpen = true;
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        View header = navigationView.inflateHeaderView(R.layout.drawer_header);

        mDialogContainer = findViewById(R.id.layout_dialog_container);
        mTextViewCancelar = (TextView) findViewById(R.id.textView_cancelar);
        mTextViewConfirmar = (TextView) findViewById(R.id.textView_confirmar);
        mTextViewDialogMsg = (TextView) findViewById(R.id.textView_confirmar_mensaje);
    }

    private void setUpPager() {
        mPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(false, new ZoomOutPageTransformer());
        mPager.setOffscreenPageLimit(3);

        setUpTabs();

    }


    public void showLoadDialog() {
        findViewById(R.id.dialog_content).setVisibility(View.GONE);
        findViewById(R.id.dialog_load).setVisibility(View.VISIBLE);
        ((ProgressBar) findViewById(R.id.progress_bar_dialog)).getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.accent), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public void hideLoadDialog() {
        findViewById(R.id.dialog_content).setVisibility(View.VISIBLE);
        findViewById(R.id.dialog_load).setVisibility(View.GONE);
    }


    public void showNormalDialog(String text, View.OnClickListener listener) {
        mDialogContainer.setVisibility(View.VISIBLE);
        isDialogOpen = true;
        mTextViewDialogMsg.setText(text);
        mTextViewConfirmar.setEnabled(true);
        findViewById(R.id.view_line).setVisibility(View.GONE);
        mTextViewCancelar.setVisibility(View.VISIBLE);
        mTextViewCancelar.setOnClickListener(listener);
        mTextViewConfirmar.setOnClickListener(listener);
    }


    public void closeDialog() {
        hideLoadDialog();
        isDialogOpen = false;
        mDialogContainer.setVisibility(View.GONE);
    }

    @Override
    public int getLayoutBase() {
        return R.layout.activity_base;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_OK) {
            switch (resultCode) {
                case 0:
                    hideOverlay();
                    setUpPager();
                    break;
                case -10:
                    logOut();
                    break;

            }
        }
    }

    @Override
    public void onBackPressed() {
        boolean backPressed = false;
        if (mIsDrawerOpen) {
            mDrawerLayout.closeDrawers();
        } else {
            if (isDialogOpen) {
                closeDialog();
            } else {
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    if (((BaseFragment) fragment).onBackPressed()) {
                        backPressed = true;
                    }
                }

                if (!backPressed) {
                    super.onBackPressed();
                }
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setUpTabs() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(mPager);

        LinearLayout tabsList = ((LinearLayout) tabLayout.getChildAt(0));
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPager.setCurrentItem(((ViewGroup) view.getParent()).indexOfChild(view), true);
            }
        };
        tabsList.getChildAt(0).setOnClickListener(listener);
        tabsList.getChildAt(1).setOnClickListener(listener);
        tabsList.getChildAt(2).setOnClickListener(listener);

        setTabsTextStyle(tabLayout);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mDrawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.nav_drawer_cerrar_sesion:
                                showOverlay("Cerrando sesión");
                                logOut();
                                hideOverlay();
                                break;
                            case R.id.nav_drawer_personas_reportadas:
                                Intent intent = new Intent(PrincipalActivity.this, PersonasDenunciadasActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_drawer_publicaciones_reportadas:
                                Intent intentPublicaciones = new Intent(PrincipalActivity.this, PublicacionesDenunciadasActivity.class);
                                startActivity(intentPublicaciones);
                                break;
                        }

                        return true;
                    }
                });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_search:
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = PerdidosFragment.getInstance();
                    break;
                case 1:
                    fragment = DonacionesFragment.getInstance();
                    break;
                default:
                    fragment = InformacionUtilFragment.getInstance();
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title;
            switch (position) {
                case 0:
                    title = getString(R.string.perdidos_titulo);
                    break;
                case 1:
                    title = getString(R.string.donaciones_titulo);
                    break;
                default:
                    title = getString(R.string.informacion_util_titulo);
                    break;
            }
            return title.toUpperCase();
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public void setCurrentPage(int page) {
        mPager.setCurrentItem(page);
    }

    public int getCurrentPage() {
        return mPager.getCurrentItem();
    }

    private void setTabsTextStyle(TabLayout tabLayout) {

        try {
            ViewGroup slidingTabLayout = (ViewGroup) tabLayout.getChildAt(0);
            TextView txtTab1 = (TextView) ((ViewGroup) slidingTabLayout.getChildAt(0)).getChildAt(1);
            TextView txtTab2 = (TextView) ((ViewGroup) slidingTabLayout.getChildAt(1)).getChildAt(1);
            TextView txtTab3 = (TextView) ((ViewGroup) slidingTabLayout.getChildAt(2)).getChildAt(1);
            txtTab1.setTextAppearance(this, R.style.condensed_normal_16);
            txtTab2.setTextAppearance(this, R.style.condensed_normal_16);
            txtTab3.setTextAppearance(this, R.style.condensed_normal_16);
            tabLayout.invalidate();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (thereWasError) {
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMainScreen();
                }
            };
            showMessageOverlay("Hubo un problema, por favor intente nuevamente", listener);
            thereWasError = false;
        }
    }


    private class AsyncTaskPerdidosInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                mIPerdidosImpl.cargarDBLocalCaracteristicasPerdidos(PrincipalActivity.this);
                HuellasApplication.getInstance().setmEspecies(mIPerdidosImpl.getEspecies());
                HuellasApplication.getInstance().setmRazas(mIPerdidosImpl.getRazas());
                HuellasApplication.getInstance().setmColores(mIPerdidosImpl.getColores());
                HuellasApplication.getInstance().setmEdades(mIPerdidosImpl.getEdades());
                HuellasApplication.getInstance().setmTamanios(mIPerdidosImpl.getTamaños());
                HuellasApplication.getInstance().setmSexos(mIPerdidosImpl.getSexos());

                HuellasApplication.getInstance().setmPersonas(mIPersonasImpl.getPersonas());


                List<Estados> estados = mIEstadosImpl.getEstados();
                for (int x = 0; x < estados.size(); x++) {
                    if (estados.get(x).getSituacion().equals("-")) {
                        estados.remove(x);
                    }
                }
                HuellasApplication.getInstance().setmEstados(estados);
            } catch (Exception e) {
                thereWasError = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        View.OnClickListener listener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadMainScreen();
                            }
                        };
                        showMessageOverlay("Hubo un problema, por favor intente nuevamente", listener);
                    }
                });
            }
            return null;
        }
    }
}
