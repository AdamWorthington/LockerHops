package jumpit.lockereats.Controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import jumpit.lockereats.R;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Observable;
import java.util.Observer;

public class Welcome extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{
    private TextView statusText;
    private static final int RC_SIGN_IN = 9001;
    private ProgressDialog loginProgressDialog;
    private GoogleApiClient mGoogleApiClient;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        statusText = (TextView) findViewById(R.id.status_text);

        Intent intent = getIntent();
        boolean loggedOut = false;

        if(intent.hasExtra("LogoutSuccess"))
            loggedOut = intent.getBooleanExtra("LogoutSuccess", false);

        if(loggedOut)
        {
            statusText.setText("Logged out Successfully!");
            statusText.setVisibility(View.VISIBLE);
        }
        else
        {
            statusText.setText("");
            statusText.setVisibility(View.INVISIBLE);
        }


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signIn = (SignInButton) findViewById(R.id.sign_in_button);
        signIn.setSize(SignInButton.SIZE_STANDARD);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginGoogle(v);
            }
        });
    }

    /*Google API calls failed when logging in*/
    public void onConnectionFailed(ConnectionResult r)
    {
        statusText.setText("Login Attempt Failed. Connection Error.");
        statusText.setVisibility(View.VISIBLE);
        hideProgressDialog();
    }

    private void onLoginFail()
    {
        statusText.setText("Login Attempt Failed.");
        statusText.setVisibility(View.VISIBLE);
        hideProgressDialog();
    }

    public void onLoginGoogle(View v)
    {
        showProgressDialog();
        statusText.setVisibility(View.INVISIBLE);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void showProgressDialog() {
        if (loginProgressDialog == null) {
            loginProgressDialog = new ProgressDialog(this);
            loginProgressDialog.setMessage("Logging in...");
            loginProgressDialog.setIndeterminate(true);
        }

        loginProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (loginProgressDialog != null && loginProgressDialog.isShowing()) {
            loginProgressDialog.hide();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess())
                handleLoginSuccess(result);
            else
                onLoginFail();
        }
    }

    //@Override
    //public void onStart() {
    //    super.onStart();
//
    //    OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
    //    if (opr.isDone()) {
    //        // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
    //        // and the GoogleSignInResult will be available instantly.
    //        GoogleSignInResult result = opr.get();
    //        handleSignInResult(result);
    //    } else {
    //        // If the user has not previously signed in on this device or the sign-in has expired,
    //        // this asynchronous branch will attempt to sign in the user silently.  Cross-device
    //        // single sign-on will occur in this branch.
    //        showProgressDialog();
    //        opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
    //            @Override
    //            public void onResult(GoogleSignInResult googleSignInResult) {
    //                hideProgressDialog();
    //                handleSignInResult(googleSignInResult);
    //            }
    //        });
    //    }
    //}


    private void handleLoginSuccess(GoogleSignInResult result)
    {
        GoogleSignInAccount acct = result.getSignInAccount();
        Intent homeIntent = new Intent(this, Home.class);
        startActivity(homeIntent);
        finish();
    }
}

