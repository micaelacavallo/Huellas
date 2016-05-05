package com.example.micaela.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micaela.HuellasApplication;
import com.example.micaela.ZoomOutPageTransformer;
import com.example.micaela.db.Controladores.IDenunciasImpl;
import com.example.micaela.db.Controladores.IEstadosImpl;
import com.example.micaela.db.Controladores.IGeneralImpl;
import com.example.micaela.db.Controladores.IPerdidosImpl;
import com.example.micaela.db.Interfaces.IDenuncias;
import com.example.micaela.db.Interfaces.IEstados;
import com.example.micaela.db.Interfaces.IGeneral;
import com.example.micaela.db.Interfaces.IPerdidos;
import com.example.micaela.db.Modelo.Estados;
import com.example.micaela.db.Modelo.MotivoDenuncia;
import com.example.micaela.fragments.BaseFragment;
import com.example.micaela.fragments.DonacionesFragment;
import com.example.micaela.fragments.InformacionUtilFragment;
import com.example.micaela.fragments.PerdidosFragment;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.CircleImageTransform;
import com.example.micaela.utils.Constants;
import com.software.shell.fab.ActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;


public class PrincipalActivity extends BaseActivity {
    private ViewPager mPager;
    private ActionButton mActionButton;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mIsDrawerOpen = false;
    private TextView mUserNameTextView;
    private ImageView mUserPhotoImageView;

    private IPerdidos mIPerdidosImpl;
    private IGeneral mIGeneralImpl;
    private IEstados mIEstadosImpl;
    private IDenuncias mIDenunciasImpl;

    private View mDialogContainer;
    private TextView mTextViewDialogMsg;
    private TextView mTextViewConfirmar;
    private TextView mTextViewCancelar;
    private RadioGroup mItemsDenunciaContainer;

    private boolean thereWasError = false;
    private boolean isDialogOpen = false;

    private String mDestinoDenuncia = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        mIPerdidosImpl = new IPerdidosImpl(this);
        mIGeneralImpl = new IGeneralImpl(this);
        mIEstadosImpl = new IEstadosImpl(this);
        mIDenunciasImpl = new IDenunciasImpl(this);

        showOverlay(getString(R.string.cargando_publicaciones_mensaje));

        mPager = (ViewPager) findViewById(R.id.pager);

        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(false, new ZoomOutPageTransformer());
        mPager.setOffscreenPageLimit(3);

        setUpTabs();
        setUpFAB();

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

        mUserNameTextView = (TextView) header.findViewById(R.id.nav_drawer_nombre_cuenta);
        mUserPhotoImageView = (ImageView) header.findViewById(R.id.nav_drawer_foto_perfil);
        new AsyncTaskPerdidosInfo().execute();
        if (HuellasApplication.getInstance().getAccessTokenFacebook().equals("")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE_OK);
        } else {
            updateFacebookData();
        }

        mDialogContainer = findViewById(R.id.layout_dialog_container);
        mTextViewCancelar = (TextView) findViewById(R.id.textView_cancelar);
        mTextViewConfirmar = (TextView) findViewById(R.id.textView_confirmar);
        mTextViewDialogMsg = (TextView) findViewById(R.id.textView_confirmar_mensaje);
        mItemsDenunciaContainer = (RadioGroup) findViewById(R.id.items_denuncia_container);
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
        mItemsDenunciaContainer.setVisibility(View.GONE);
        findViewById(R.id.view_line).setVisibility(View.GONE);
        mTextViewCancelar.setVisibility(View.VISIBLE);
        mTextViewCancelar.setOnClickListener(listener);
        mTextViewConfirmar.setOnClickListener(listener);
    }

    public void showDenunciasDialog(String destinoDenuncia, final String objectId) {
        mDestinoDenuncia = destinoDenuncia;
        showNormalDialog("¿Cuál es el problema?", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.textView_cancelar:
                        closeDialog();
                        break;
                    case R.id.textView_confirmar:
                        mItemsDenunciaContainer.clearCheck();
                        showLoadDialog();
                        new AsyncDenunciarPublicacion().execute(objectId);
                        break;
                }
            }
        });
        mTextViewConfirmar.setEnabled(false);
        mItemsDenunciaContainer.setVisibility(View.VISIBLE);
        mItemsDenunciaContainer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mTextViewConfirmar.setEnabled(true);
            }
        });
        findViewById(R.id.view_line).setVisibility(View.VISIBLE);
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

    private void updateFacebookData() {
        mUserNameTextView.setText(HuellasApplication.getInstance().getProfileNameFacebook());
        String facebookImagen = HuellasApplication.getInstance().getProfileImageFacebook();
        Picasso.with(getApplicationContext()).load(Uri.parse(facebookImagen)).transform(new CircleImageTransform()).into(mUserPhotoImageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_OK) {
            switch (resultCode) {
                case 0:
                    hideOverlay();
                    updateFacebookData();
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

    private void setUpFAB() {
        mActionButton = (ActionButton) findViewById(R.id.fab);
        mActionButton.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        mActionButton.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPage = getCurrentPage();
                Intent intent = new Intent(getBaseContext(), AltaAnimalesActivity.class);
                switch (currentPage) {
                    case 0:
                        intent.putExtra(Constants.FROM_FRAGMENT, Constants.PERDIDOS);
                        break;
                    case 1:
                        intent.putExtra(Constants.FROM_FRAGMENT, Constants.ADICIONALES_DONACIONES);
                        break;
                    case 2:

                        intent.putExtra(Constants.FROM_FRAGMENT, Constants.ADICIONALES_INFO);
                        break;
                }
                intent.putExtra(Constants.ACTION, Constants.ALTA);
                startActivity(intent);
            }
        });
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

                            case R.id.nav_drawer_perfil:
                                Intent intent = new Intent(PrincipalActivity.this, MisDatosActivity.class);
                                startActivity(intent);
                                break;
                        }

                        return true;
                    }
                });
    }


    public ActionButton getActionButton() {
        return mActionButton;
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
                HuellasApplication.getInstance().setmEspecies(mIPerdidosImpl.getEspecies());
                HuellasApplication.getInstance().setmRazas(mIPerdidosImpl.getRazas());
                HuellasApplication.getInstance().setmColores(mIPerdidosImpl.getColores());
                HuellasApplication.getInstance().setmEdades(mIPerdidosImpl.getEdades());
                HuellasApplication.getInstance().setmTamanios(mIPerdidosImpl.getTamaños());
                HuellasApplication.getInstance().setmSexos(mIPerdidosImpl.getSexos());
                HuellasApplication.getInstance().setmMotivosDenuncia(mIDenunciasImpl.getMotivoDenuncias());

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


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            for (MotivoDenuncia motivoDenuncia : HuellasApplication.getInstance().getmMotivosDenuncia()) {
                RadioButton radioButton = new RadioButton(PrincipalActivity.this);
                radioButton.setTextAppearance(getBaseContext(), R.style.condensed_normal_18);
                radioButton.setTextColor(getResources().getColor(R.color.primary_text));
                radioButton.setPadding(0, 5, 0, 5);
                radioButton.setText(motivoDenuncia.getmMotivo());
                radioButton.setTag(motivoDenuncia.getmObjectId());
                mItemsDenunciaContainer.addView(radioButton);
            }
        }
    }

    private class AsyncDenunciarPublicacion extends AsyncTask<String, Void, Void> {
        private String mMotivoDenuncia = "";
        private boolean error = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            int selectedId = mItemsDenunciaContainer.getCheckedRadioButtonId();
            RadioButton radioButtonSelected = (RadioButton) findViewById(selectedId);
            mMotivoDenuncia = radioButtonSelected.getText().toString();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                mIDenunciasImpl.denunciar(params[0], mMotivoDenuncia, mDestinoDenuncia);
            } catch (Exception e) {
                error = true;
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            closeDialog();
            if (!error) {
                if (mDestinoDenuncia.equals(Constants.TABLA_PERSONAS)) {
                    Toast.makeText(PrincipalActivity.this, "Usuario reportado con éxito!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PrincipalActivity.this, "Publicación reportada con éxito!", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (mDestinoDenuncia.equals(Constants.TABLA_PERSONAS)) {
                    Toast.makeText(PrincipalActivity.this, "No se pudo reportar al usuario. Intente de nuevo más tarde", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PrincipalActivity.this, "No se pudo reportar la publicación. Intente de nuevo más tarde", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}
