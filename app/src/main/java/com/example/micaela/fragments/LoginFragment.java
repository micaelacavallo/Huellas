package com.example.micaela.fragments;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.micaela.HuellasApplication;
import com.example.micaela.activities.BaseActivity;
import com.example.micaela.activities.PrincipalActivity;
import com.example.micaela.huellas.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {

    CallbackManager mCallbackManager;
    private LoginButton mLoginButton;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        mLoginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton.setReadPermissions("user_friends");
        // If using in a fragment
        mLoginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken token = loginResult.getAccessToken();
                if (token != null) {
                    getActivity().finish();
                    ((BaseActivity)getActivity()).hideOverlay();
                    HuellasApplication.getInstance().saveAccessTokenFacebook(token.getToken());
                    Profile profile = Profile.getCurrentProfile();
                    HuellasApplication.getInstance().saveProfileFacebook(profile.getProfilePictureUri(150, 150), profile.getName());
                }
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                ((BaseActivity)getActivity()).showOverlay("Hubo un problema, intente nuevamente");
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
