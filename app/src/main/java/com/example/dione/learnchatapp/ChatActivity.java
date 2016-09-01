package com.example.dione.learnchatapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by dione on 01/09/2016.
 */
public class ChatActivity extends AppCompatActivity implements View.OnClickListener{
    GoogleApiClient mGoogleApiClient;
    EditText editTextMessage;
    AppCompatImageView imageViewSendMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(String.format("%s %s, %s", "Welcome",
                MainActivity.mSharedPreferenceManager.getStringPreferences(Constants.LASTNAME_TAG, ""),
                MainActivity.mSharedPreferenceManager.getStringPreferences(Constants.FIRSTNAME_TAG, "")));
        setContentView(R.layout.activity_chat);
        initViews();
        initDoneButtonListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void initViews(){
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        imageViewSendMessage = (AppCompatImageView) findViewById(R.id.imageViewSendMessage);
        imageViewSendMessage.setOnClickListener(this);
    }

    private void initDoneButtonListener(){
        editTextMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (!editTextMessage.getText().toString().isEmpty()){
                        editTextMessage.setText("");
                        editTextMessage.setError(null);
                    }else{
                        editTextMessage.setError("Required");
                    }

                    handled = true;
                }
                return handled;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        finish();
                        MainActivity.mSharedPreferenceManager.clearSharedPreference();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageViewSendMessage:
                if (!editTextMessage.getText().toString().isEmpty()){
                    editTextMessage.setText("");
                    editTextMessage.setError(null);
                }else{
                    editTextMessage.setError("Required");
                }
                break;
        }
    }
}
