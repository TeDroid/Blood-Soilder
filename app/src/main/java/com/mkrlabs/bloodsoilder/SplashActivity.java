package com.mkrlabs.bloodsoilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.firebase.auth.FirebaseAuth;
import com.mkrlabs.bloodsoilder.Utils.AccountCreation;
import com.mkrlabs.bloodsoilder.Utils.DisplayUtils;
import com.mkrlabs.bloodsoilder.ui.HomeActivity;
import com.mkrlabs.bloodsoilder.ui.account.CreateAccountActivity;
import com.mkrlabs.bloodsoilder.ui.account.userinformation.DonorInfoActivity;
import com.mkrlabs.bloodsoilder.ui.account.userinformation.UserAccountInformation;

public class SplashActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayUtils.systembartransparent(this);
        //splash screen added
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser()!=null){
                    // Bug what if user won't fulfill all step for registration
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashActivity.this, UserAccountInformation.class));
                    finish();
                }



            }
        },2500);
    }
}