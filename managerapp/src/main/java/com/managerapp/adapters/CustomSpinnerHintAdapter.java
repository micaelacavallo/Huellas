package com.managerapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomSpinnerHintAdapter extends ArrayAdapter<String> {

    Context mContext;
    int textSizeRes;

    public CustomSpinnerHintAdapter(Context context, int resource, int textViewResourceId, int textSizeRes) {
        super(context, resource, textViewResourceId);
        this.textSizeRes = textSizeRes;
    }

    public CustomSpinnerHintAdapter(Context context, int textSizeRes) {
        super(context, android.R.layout.simple_spinner_item);
        this.textSizeRes = textSizeRes;
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
            textView.setTextAppearance(getContext(),textSizeRes);
        }

        return view;
    }


}
