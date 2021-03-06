package com.example.micaela.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.micaela.HuellasApplication;
import com.example.micaela.activities.BaseActivity;
import com.example.micaela.db.Controladores.IPersonasImpl;
import com.example.micaela.db.Modelo.Personas;
import com.example.micaela.huellas.R;
import com.example.micaela.utils.CustomDialog;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
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
    private IPersonasImpl mIPersonasImpl;

    public LoginFragment() {
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        mLoginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        mIPersonasImpl = new IPersonasImpl(getBaseActivity());
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
                        getActivity().setResult(-10);
                        getActivity().finish();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        View.OnClickListener listener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getBaseActivity().setResult(-10);
                                getActivity().finish();
                            }
                        };
                        ((BaseActivity) getActivity()).showMessageOverlay("Hubo un problema, intente nuevamente", listener);
                    }
                }

        );

        return rootView;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    private class AsyncTaskRegistrarUsuario extends AsyncTask<Personas, Void, Void> {
        private boolean blocked = false;
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (blocked) {
                CustomDialog.showDialog("Usuario bloqueado", "Lo sentimos pero tu cuenta fue bloqueada.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LoginManager.getInstance().logOut();
                        HuellasApplication.getInstance().clearProfileFacebook();
                        getBaseActivity().finishAffinity();
                    }
                }, getBaseActivity());
            }
            else {
                getActivity().setResult(0);
                getActivity().finish();
            }
        }

        @Override
        protected Void doInBackground(Personas... params) {
            try {
              blocked = mIPersonasImpl.registar(params[0]);

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

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void getFacebookData(JSONObject object, Profile profile) {
        String email, location, gender;
        // Application code
        try {
            email = object.getString("email");
        } catch (JSONException e) {
            email = "";
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
        try {
            HuellasApplication.getInstance().saveProfileFacebook(profile.getProfilePictureUri(400, 400), profile.getName(), email, location,
                    gender);
            Personas persona = new Personas("", HuellasApplication.getInstance().getProfileEmailFacebook(), HuellasApplication.getInstance().getProfileNameFacebook(),
                    "", false, false, "", HuellasApplication.getInstance().getProfileImageFacebook());
            new AsyncTaskRegistrarUsuario().execute(persona);
        }
        catch (NullPointerException e) {
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getBaseActivity().logOut();
                }
            };
            ((BaseActivity) getActivity()).showMessageOverlay("Hubo un problema, intente nuevamente", listener);
        }
    }

}
