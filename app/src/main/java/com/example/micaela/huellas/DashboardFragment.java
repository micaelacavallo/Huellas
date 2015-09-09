package com.example.micaela.huellas;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DashboardFragment extends BaseFragment {

    private ActionBar actionBar;

    public DashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View aView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        TabLayout tabLayout = (TabLayout) aView.findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Perdidos"));
        tabLayout.addTab(tabLayout.newTab().setText("Encontrados"));
        tabLayout.addTab(tabLayout.newTab().setText("Donaciones"));
        tabLayout.addTab(tabLayout.newTab().setText("Información útil"));


        ViewPager viewPager = (ViewPager) aView.findViewById(R.id.pager);
        TabsPagerAdapter mAdapter = new TabsPagerAdapter(getFragmentManager());

        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return aView;
    }

    public class TabsPagerAdapter extends FragmentPagerAdapter {

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {

            switch (index) {
                case 0:
                    return new PerdidosFragment();
                case 1:
                    return new EncontradosFragment();
                case 2:
                    return new DonacionesFragment();
                case 3:
                    return new InformacionUtilFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
