package com.example.micaela.huellas;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public abstract class BaseFragment extends Fragment {
    private FrameLayout mContentLayout;
    View rootView;
    BaseActivity mActivity;

    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_base, container, false);
        wireUpLayouts(rootView);
        View content = this.onCreateEventView(inflater, null, savedInstanceState);
        mContentLayout.addView(content);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
    }


    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    abstract protected View onCreateEventView(LayoutInflater inflater, ViewGroup container,
                                              Bundle savedInstanceState);

    private void wireUpLayouts(View rootView) {
        mContentLayout = (FrameLayout) rootView.findViewById(R.id.contentPanel);
    }

}
