package com.mkrlabs.bloodsoilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.mkrlabs.bloodsoilder.Utils.DisplayUtils;
import com.mkrlabs.bloodsoilder.ui.HomeActivity;
import com.mkrlabs.bloodsoilder.ui.account.CreateAccountActivity;
import com.mkrlabs.bloodsoilder.ui.account.userinformation.DonorInfoActivity;
import com.mkrlabs.bloodsoilder.ui.account.userinformation.PersonalInfoActivity;
import com.mkrlabs.bloodsoilder.ui.account.userinformation.UserAccountInformation;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayUtils.systembartransparent(this);
        //splash screen added
        setContentView(R.layout.activity_splash);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, UserAccountInformation.class));
                finish();
            }
        },2500);
    }
}