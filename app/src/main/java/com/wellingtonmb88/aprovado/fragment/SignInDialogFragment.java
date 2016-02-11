package com.wellingtonmb88.aprovado.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.wellingtonmb88.aprovado.AppApplication;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.activity.MainActivity;
import com.wellingtonmb88.aprovado.dagger.components.DaggerFragmentInjectorComponent;
import com.wellingtonmb88.aprovado.entity.User;
import com.wellingtonmb88.aprovado.preferences.DriveApiPreferences;
import com.wellingtonmb88.aprovado.preferences.UserPreferences;
import com.wellingtonmb88.aprovado.presenter.MainPresenterImpl;
import com.wellingtonmb88.aprovado.utils.AprovadoLogger;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInDialogFragment extends DialogFragment implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    @Bind(R.id.sign_in_button)
    SignInButton mSignInButton;
    @Bind(R.id.sign_out_button)
    Button mSignOutButton;
    @Bind(R.id.disconnect_button)
    Button mRevokeButton;
    @Inject
    MainPresenterImpl mMainPresenter;
    private ProgressDialog mProgressDialog;
    private GoogleApiClient mGoogleApiClient;

    public SignInDialogFragment() {
    }

    public static SignInDialogFragment newInstance() {
        return new SignInDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_sign_in, container, false);

        getDialog().setTitle("Google account!");
        ButterKnife.bind(this, view);

        DaggerFragmentInjectorComponent.builder().baseComponent(AppApplication.getBaseComponent())
                .build()
                .inject(this);

        mMainPresenter.registerView((MainActivity) getActivity());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(GoogleSignInResult googleSignInResult) {
                        hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onDestroy();
    }

    @OnClick(R.id.sign_in_button)
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.sign_out_button)
    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                        getDialog().dismiss();
                    }
                });
    }

    @OnClick(R.id.disconnect_button)
    public void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                        getDialog().dismiss();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            getDialog().dismiss();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            updateUserPreferences(result.getSignInAccount());
            updateUI(true);
        } else {
            updateUI(false);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        AprovadoLogger.i("GoogleApiClient connection failed: " + connectionResult.toString());
        if (!connectionResult.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), connectionResult.getErrorCode(), 0).show();
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            mSignInButton.setEnabled(false);
            mSignOutButton.setVisibility(View.VISIBLE);
        } else {
            mSignInButton.setEnabled(true);
            mSignOutButton.setVisibility(View.GONE);
            DriveApiPreferences.setDriveApiConnected(false);
            UserPreferences.setFirstFlow(true);
            mMainPresenter.onDisconnectFromDrive();
            mMainPresenter.onHideNavMenuItem();
            updateUserPreferences(null);
        }
    }

    private void updateUserPreferences(GoogleSignInAccount acct) {

        User user = null;

        if (acct != null) {
            Uri uri = Uri.parse("android.resource://" + getContext().getPackageName() + "/mipmap/ic_login_default");
            String photoUrl = acct.getPhotoUrl() != null ? acct.getPhotoUrl().toString() : uri.toString();
            user = new User(acct.getDisplayName(), acct.getEmail(), photoUrl);
        }

        UserPreferences.saveUserToPreferences(user);

        mMainPresenter.onUpdateDrawerLayout(user);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.dialog_progress_message));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
}
