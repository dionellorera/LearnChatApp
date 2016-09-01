package com.example.dione.learnchatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{
    GoogleApiClient mGoogleApiClient;
    SignInButton signInButton;
    GoogleSignInOptions gso;
    GoogleSignInAccount acct;
    public static SharedPreferenceManager mSharedPreferenceManager;
    Button googleSignOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        buildGsignIn();
        init();
        verifyIfUserStillSignedIn();
    }

    @Override
    protected void onResume() {
        super.onResume();
        verifyIfUserStillSignedIn();
    }

    private void startChatActivity(){
        startActivity(new Intent(this, ChatActivity.class));
        finish();
    }
    private void init(){
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());
        googleSignOut = (Button) findViewById(R.id.googleSignOut);
        googleSignOut.setOnClickListener(this);
        mSharedPreferenceManager = new SharedPreferenceManager(getApplicationContext());
    }
    private void verifyIfUserStillSignedIn(){
        if (!mSharedPreferenceManager.getStringPreferences(Constants.USERNAME_TAG, "").isEmpty()){
            signInButton.setVisibility(View.GONE);
            startChatActivity();
        }else{
            signInButton.setVisibility(View.VISIBLE);
            googleSignOut.setVisibility(View.GONE);
        }
    }
    private void buildGsignIn(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage((AppCompatActivity) getApplicationContext());
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("on_connecting_failed", connectionResult.getErrorMessage());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.googleSignOut:
                signOut();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
                break;
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            acct = result.getSignInAccount();
            mSharedPreferenceManager.saveStringPreferences(Constants.USERNAME_TAG, acct.getEmail());
            mSharedPreferenceManager.saveStringPreferences(Constants.FIRSTNAME_TAG, acct.getGivenName());
            mSharedPreferenceManager.saveStringPreferences(Constants.LASTNAME_TAG, acct.getFamilyName());
            startChatActivity();
        }
    }
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
            new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    mGoogleApiClient.disconnect();
                    mSharedPreferenceManager.clearSharedPreference();
                    signInButton.setVisibility(View.VISIBLE);
                    googleSignOut.setVisibility(View.GONE);
                }
            });
    }

}
