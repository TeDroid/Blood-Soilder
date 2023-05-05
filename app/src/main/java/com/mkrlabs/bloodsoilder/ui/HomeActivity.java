package com.mkrlabs.bloodsoilder.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.Utils.DisplayUtils;
import com.mkrlabs.bloodsoilder.ui.activity.ActivityFragment;
import com.mkrlabs.bloodsoilder.ui.home.HomeFragment;
import com.mkrlabs.bloodsoilder.ui.homemap.HomeMapFragment;
import com.mkrlabs.bloodsoilder.ui.profile.ProfileFragment;
import com.mkrlabs.bloodsoilder.ui.request.RequestFragment;
import com.mkrlabs.customstatusbar.CustomStatusBar;

public class HomeActivity extends AppCompatActivity {

    private FrameLayout frameContainer;

    private BottomNavigationView homeBottomNavigation;

    // backend branch upgrade
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomStatusBar.setCustomStatusColor(this,R.color.primaryColor,false);
        setContentView(R.layout.activity_home);
        init();

        homeBottomNavigation.setSelectedItemId(R.id.homeItem);
        setUpFragment(new HomeFragment());
        homeBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeItem:
                        setUpFragment(new HomeFragment());
                        return true;
                    case R.id.requestItem:
                        setUpFragment(new RequestFragment());
                        return true;
                    case R.id.activityItem:
                        setUpFragment(new ActivityFragment());
                        return true;
                    case R.id.profileItem:
                        setUpFragment(new ProfileFragment());
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    private void setUpFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragment)
                    .commit();
        }

    }

    private void init() {
        homeBottomNavigation = findViewById(R.id.homeBottomNavigation);
        frameContainer = findViewById(R.id.frameContainer);
    }

    @Override
    public void onBackPressed() {
        if (homeBottomNavigation.getSelectedItemId() != R.id.homeItem) {
            homeBottomNavigation.setSelectedItemId(R.id.homeItem);
        } else {
            super.onBackPressed();
        }
    }
}