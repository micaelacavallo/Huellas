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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.managerapp.HuellasApplication;
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
                if (mEditTextUser.getText().toString().equals("") || mEditTextPass.getText().toString().equals("")) {
                    showError("Todos los campos son requeridos.");
                } else {
                    if (mEditTextPass.length() < 4) {
                        showError("La contraseña debe tener mínimo 4 caracteres.");
                    }
                    else {
                        if (getBaseActivity().internet()) {
                            new AsyncTaskLoginAdmin().execute();
                        }
                        else {
                            showError("No hay conexión. Vuelva a intentarlo.");
                        }
                    }
                }
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
                mTextViewError.setVisibility(View.INVISIBLE);
            }
        };

        mEditTextPass.addTextChangedListener(textWatcher);
        mEditTextUser.addTextChangedListener(textWatcher);

        return rootView;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    private class AsyncTaskLoginAdmin extends AsyncTask<Void, Void, Void> {
        private int status;
        String mUser = "";
        String mPass = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getBaseActivity().showOverlay("Cargando...");
            mUser = mEditTextUser.getText().toString();
            mPass = mEditTextPass.getText().toString();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                status = mIAdminImpl.login(mUser, mPass);

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
            switch (status) {
                case 0:
                    HuellasApplication.getInstance().saveProfile(mUser, mPass);
                    getActivity().setResult(0);
                    getActivity().finish();
                    break;
                case -1:
                    showError("El usuario ingresado no existe.");
                    getBaseActivity().hideOverlay();
                    break;
                case -2:
                    showError("La contraseña ingresada es incorrecta.");
                    getBaseActivity().hideOverlay();
                    break;
            }
        }
    }

    private void showError(String text) {
        mTextViewError.setVisibility(View.VISIBLE);
        mTextViewError.setText(text);
        YoYo.with(Techniques.Tada)
                .duration(700)
                .playOn(mTextViewError);
    }

}
