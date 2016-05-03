package com.managerapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.managerapp.R;
import com.managerapp.activities.BaseActivity;
import com.managerapp.db.Controladores.IPersonasImpl;
import com.managerapp.db.Modelo.Personas;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends BaseFragment {

    private IPersonasImpl mIPersonasImpl;

    public LoginFragment() {
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);


        return rootView;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    private class AsyncTaskRegistrarUsuario extends AsyncTask<Personas, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Personas... params) {
            try {
                mIPersonasImpl.registar(params[0]);
            } catch (Exception e) {
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBaseActivity().logOut();
                    }
                };
                ((BaseActivity) getActivity()).showMessageOverlay("Hubo un problema, intente nuevamente", listener);
            }
            return null;
        }

    }



}
