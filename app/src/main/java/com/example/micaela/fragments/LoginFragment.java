package com.example.micaela.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.micaela.HuellasApplication;
import com.example.micaela.activities.BaseActivity;
import com.example.micaela.huellas.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends BaseFragment {

    CallbackManager mCallbackManager;
    private LoginButton mLoginButton;
    private Profile mProfile;

    public LoginFragment() {
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        mLoginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton.setReadPermissions(Arrays.asList("email", "user_birthday", "user_location", "public_profile"));
        // If using in a fragment
        mLoginButton.setFragment(this);
        // Other app specific specialization

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.textView_nombre_app).setVisibility(View.INVISIBLE);
            }
        });

        // Callback registration
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    private ProfileTracker mProfileTracker;

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        mLoginButton.setVisibility(View.INVISIBLE);
                        final AccessToken token = loginResult.getAccessToken();
                        if (token != null) {
                            HuellasApplication.getInstance().saveAccessTokenFacebook(token.getToken());
                            if (Profile.getCurrentProfile() == null) {
                                mProfileTracker = new ProfileTracker() {
                                    @Override
                                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                                        // profile2 is the new profile
                                        mProfile = profile2;
                                        mProfileTracker.stopTracking();
                                    }
                                };
                                mProfileTracker.startTracking();
                            } else {
                                mProfile = Profile.getCurrentProfile();
                            }

                            GraphRequest request = GraphRequest.newMeRequest(
                                    loginResult.getAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            getFacebookData(object, mProfile);
                                            getActivity().setResult(0);
                                            getActivity().finish();
                                            ((BaseActivity) getActivity()).hideOverlay();
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,email,gender,birthday,location");
                            request.setParameters(parameters);
                            request.executeAsync();
                        }
                    }


                    @Override
                    public void onCancel() {
                        getBaseActivity().setResult(-1);
                        getBaseActivity().logOut();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        View.OnClickListener listener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getBaseActivity().setResult(-1);
                                getBaseActivity().logOut();
                            }
                        };
                        ((BaseActivity) getActivity()).showMessageOverlay("Hubo un problema, intente nuevamente", listener);
                    }
                }

        );

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void getFacebookData(JSONObject object, Profile profile) {
        String email, birthday, location, gender;
        // Application code
        try {
            email = object.getString("email");
        } catch (JSONException e) {
            email = "";
        }
        try {
            birthday = object.getString("birthday");
        } catch (JSONException e) {
            birthday = "";
        }

        try {
            location = object.getJSONObject("location").getString("name");
        } catch (JSONException e) {
            location = "";
        }
        try {
            gender = object.getString("gender");
        } catch (JSONException e) {
            gender = "";
        }

        if (gender.equals("female")) {
            gender = "femenino";
        } else {
            if (gender.equals("male")) {
                gender = "masculino";
            }
        }
        HuellasApplication.getInstance().saveProfileFacebook(profile.getProfilePictureUri(400, 400), profile.getName(), email, location,
                gender, birthday);
    }

}
