package com.example.micaela.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.micaela.huellas.R;

public class CustomSpinnerHintAdapter extends ArrayAdapter<String> {


    Context mContext;

    public CustomSpinnerHintAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public CustomSpinnerHintAdapter(Context context) {
        super(context, android.R.layout.simple_spinner_item);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view;
        TextView textView;
        if (position == 0) {
            TextView hintTextView = new TextView(getContext());
            hintTextView.setHeight(0);
            hintTextView.setVisibility(View.GONE);
            view = hintTextView;
        } else {
            view = super.getView(position, null, parent);
            textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setPadding(50, 25, 0, 25);
            textView.setTextAppearance(getContext(), R.style.condensed_normal_17);
        }

        return view;
    }


}
