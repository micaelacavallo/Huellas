package com.example.micaela.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.micaela.fragments.DonacionesFragment;
import com.example.micaela.fragments.InformacionUtilFragment;
import com.example.micaela.fragments.PerdidosFragment;
import com.example.micaela.ZoomOutPageTransformer;
import com.example.micaela.huellas.R;
import com.software.shell.fab.ActionButton;


public class PrincipalActivity extends BaseActivity {
    private ViewPager mPager;
    private ActionButton mActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mPager = (ViewPager) findViewById(R.id.pager);

        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(false, new ZoomOutPageTransformer());
        mPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
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
        inicializarFAB();
    }

    private void inicializarFAB() {
        mActionButton = (ActionButton)findViewById(R.id.fab);
        mActionButton.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        mActionButton.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           /*     Intent intent = new Intent(getActivity(), EventsManagerPagerActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.bottom_in, R.anim.top_out); */
            }
        });
    }

    public ActionButton getActionButton() {
        return mActionButton;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment;

            Bundle args = new Bundle();
            // Our object is just an integer :-P
            // args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);


            switch (position) {
                case 0:
                    fragment = new PerdidosFragment();
                    break;
                case 1:
                    fragment = new DonacionesFragment();
                    break;
                default:
                    fragment = new InformacionUtilFragment();
            }
            fragment.setArguments(args);
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
                    title= getString(R.string.informacion_util_titulo);
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

    public int getCurrentPage () {
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

}
