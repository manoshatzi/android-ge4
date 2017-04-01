package com.example.manoshatzi.ge4_sdy61;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.content.Intent;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String DEBUG_TAG = "Connectivity Manager";
    private static final String TAG = "Google Signin Result";
    private SignInButton SignIn;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    private TextView ResultMessage;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ResultMessage = (TextView)findViewById(R.id.loginResultText);
        SignIn = (SignInButton)findViewById(R.id.sign_in_button);
        SignIn.setOnClickListener(this);
        SignIn.setAlpha(0);
        nextButton = (Button)findViewById(R.id.buttonNext);
        nextButton.setVisibility(View.GONE);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.button_sound);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Button start = (Button)findViewById(R.id.buttonStart);
        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // check if the network exist or not
                boolean isConnected = isNetworkAvailable();
                Log.d(DEBUG_TAG, "" + isConnected);
                mp.start();

                // if not connected show alert else go ahead
                if(!isConnected) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setMessage(getString(R.string.no_internet))
                            .setCancelable(false)
                            .setPositiveButton("OK", null);
                    AlertDialog alert = dialog.create();
                    alert.show();
                }else{
                    v.animate().alpha(0).withLayer();
                    v.setVisibility(View.GONE);
                    SignIn.animate().setDuration(300).alpha(1.0f).withLayer();
                }
            }
        });

        Button next = (Button)findViewById(R.id.buttonNext);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mp.start();
                mp.release();
                Intent intent = new Intent(MainActivity.this, ShowImages.class);
                startActivity(intent);
            }
        });
    }

    // check if netwowrk is available
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void updateUI(boolean isLogin) {
        if(isLogin) {
            // hide sign in button and show the next button
            SignIn.setVisibility(View.GONE);
            ResultMessage.setText(getString(R.string.validation_message));
            nextButton.setVisibility(View.VISIBLE);
        } else {
            // Show a message if your registratin fails
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setMessage(getString(R.string.validation_error))
                    .setCancelable(false)
                    .setPositiveButton("OK", null);
            AlertDialog alert = dialog.create();
            alert.show();
            SignIn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage(getString(R.string.no_internet))
                .setCancelable(false)
                .setPositiveButton("OK", null);
        AlertDialog alert = dialog.create();
        alert.show();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
}
