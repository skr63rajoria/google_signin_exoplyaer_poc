package com.rajouriya.shubham.exoplayerpoc.auth.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.rajouriya.shubham.exoplayerpoc.R;

public class SignInSignUpActivity extends AppCompatActivity {
    SignInButton googleSign;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_sign_up);
        googleSign = (SignInButton)findViewById(R.id.google_sign_in);
        mContext = this;
        setGooglePlusButtonText(googleSign,"Google");

        /*initialise google client*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        /*check if user already logged in*/
        if (account != null) {
             Toast.makeText(mContext, "account information "+account.getDisplayName()+"\ngiven name : "+account.getGivenName()+"\nemail "+account.getEmail(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext, "no user present", Toast.LENGTH_SHORT).show();
        }

        googleSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

    }


    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    private void signInWithGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Toast.makeText(mContext, "account information "+account.getDisplayName()+"\ngiven name : "+account.getGivenName()+"\nemail "+account.getEmail()+"\nphoto "+account.
                    getPhotoUrl(), Toast.LENGTH_SHORT).show();
            String url = "";
            if(account.getPhotoUrl() != null){
                url = account.getPhotoUrl().toString();;
            }
            finish();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("go", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(mContext, "no user found "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
