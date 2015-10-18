package com.example.micaela.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.micaela.activities.BaseActivity;
import com.example.micaela.huellas.R;


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
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
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
