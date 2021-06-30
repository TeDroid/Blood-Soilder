package com.mkrlabs.bloodsoilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.mkrlabs.bloodsoilder.Utils.DisplayUtils;
import com.mkrlabs.bloodsoilder.ui.HomeActivity;

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
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            }
        },2500);
    }
}