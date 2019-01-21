package com.rajouriya.shubham.exoplayerpoc.auth.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class GoogleSignUpProvider {
    private static GoogleSignUpProvider mGoogleSignUpProvider = null;
    private Context mContext;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    private int RC_SIGN_IN = 1;
    private LoginService loginService;


    private GoogleSignUpProvider(Context mContext,  LoginService loginService) {
        this.mContext = mContext;
        this.loginService = loginService;
        /*initialise google client*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso);
        account = GoogleSignIn.getLastSignedInAccount(mContext);
        if(account != null)
        this.loginService.ifAlreadyActiveUser(account.getDisplayName());
    }

    public static GoogleSignUpProvider getInstance(Context mContext,  LoginService loginService){
        if(mGoogleSignUpProvider == null){
            mGoogleSignUpProvider = new GoogleSignUpProvider(mContext,loginService);
        }
        return mGoogleSignUpProvider;
    }
    public void startGoogleLogin(){
        if (account != null) {
            Toast.makeText(mContext, "account information "+account.getDisplayName()+"\ngiven name : "+account.getGivenName()+"\nemail "+account.getEmail(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext, "no user present", Toast.LENGTH_SHORT).show();
        }
    }

    public void signInWithGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        ((Activity)mContext).startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
           loginService.onLoginSuccess(account.getDisplayName());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            loginService.onLoginSuccess(e.getMessage());
        }
    }

}
