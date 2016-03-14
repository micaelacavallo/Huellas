package com.example.micaela.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.micaela.huellas.R;

public class AltaAnimalesFragment extends BaseFragment {

    private TextView mTextViewTakePicture;
    private TextView mTextViewSelectPicture;
    private View mViewDialogCamera;
    private boolean mIsDialogVisible = false;

    private static final int SELECT_PICTURE = 1;

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alta_animales, container, false);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        mViewDialogCamera = view.findViewById(R.id.dialog_take_picture);
        mTextViewSelectPicture = (TextView) mViewDialogCamera.findViewById(R.id.textView_escoger_foto);
        mTextViewSelectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mTextViewTakePicture = (TextView) mViewDialogCamera.findViewById(R.id.textView_tomar_foto);
        mTextViewTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });

        mViewDialogCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewDialogCamera.setVisibility(View.GONE);
                mIsDialogVisible = false;
            }
        });

        FloatingActionButton buttonCamera = (FloatingActionButton) view.findViewById(R.id.button_camera);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewDialogCamera.setVisibility(View.VISIBLE);
                mIsDialogVisible = true;
            }
        });




        return view;

    }

    public boolean onBackPressed() {
        if (mIsDialogVisible) {
            mViewDialogCamera.setVisibility(View.GONE);
            mIsDialogVisible = false;
            return true;
        } else {
            return false;
        }

    }


}
