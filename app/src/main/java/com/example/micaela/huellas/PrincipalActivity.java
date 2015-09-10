package com.example.micaela.huellas;

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


public class PrincipalActivity extends BaseActivity {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mPager = (ViewPager) findViewById(R.id.pager);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
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

    private void setTabsTextStyle(TabLayout tabLayout) {

        try {
            ViewGroup slidingTabLayout = (ViewGroup) tabLayout.getChildAt(0);
            TextView txtTab1 = (TextView) ((ViewGroup) slidingTabLayout.getChildAt(0)).getChildAt(1);
            TextView txtTab2 = (TextView) ((ViewGroup) slidingTabLayout.getChildAt(1)).getChildAt(1);
            TextView txtTab3 = (TextView) ((ViewGroup) slidingTabLayout.getChildAt(2)).getChildAt(1);
            txtTab1.setTextAppearance(this, R.style.condensed_normal_small_15);
            txtTab2.setTextAppearance(this, R.style.condensed_normal_small_15);
            txtTab3.setTextAppearance(this, R.style.condensed_normal_small_15);
            tabLayout.invalidate();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
    }

}
