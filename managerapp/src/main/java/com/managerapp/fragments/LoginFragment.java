package com.managerapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.managerapp.R;
import com.managerapp.db.Controladores.IAdminImpl;
import com.managerapp.db.Interfaces.IAdmin;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends BaseFragment {

    private EditText mEditTextUser;
    private EditText mEditTextPass;
    private TextView mTextViewError;
    private IAdmin mIAdminImpl;
    private Button mButtonLogIn;

    public LoginFragment() {
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        mIAdminImpl = new IAdminImpl(getBaseActivity());
        mEditTextUser = (EditText) rootView.findViewById(R.id.editText_usuario);
        mEditTextPass = (EditText) rootView.findViewById(R.id.editText_pass);
        mButtonLogIn = (Button) rootView.findViewById(R.id.button_entrar);
        mTextViewError = (TextView) rootView.findViewById(R.id.textView_error);
        mButtonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTaskLoginAdmin().execute();
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        mEditTextUser.addTextChangedListener(textWatcher);
        mEditTextPass.addTextChangedListener(textWatcher);
        return rootView;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    private class AsyncTaskLoginAdmin extends AsyncTask<Void, Void, Void> {
        private boolean exists = false;
        String mUser = "";
        String mPass = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mUser = mEditTextUser.getText().toString();
            mPass = mEditTextPass.getText().toString();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                exists = mIAdminImpl.login(mUser, mPass);

            } catch (Exception e) {
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBaseActivity().logOut();
                    }
                };
                getBaseActivity().showMessageOverlay("Hubo un problema, intente nuevamente", listener);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (exists) {
                getActivity().setResult(0);
                getActivity().finish();
            } else {
                mTextViewError.setVisibility(View.VISIBLE);
                mTextViewError.setText("El usuario ingresado no existe");
            }
        }
    }

}
