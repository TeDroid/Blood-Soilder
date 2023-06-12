package com.mkrlabs.bloodsoilder.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.Utils.DisplayUtils;
import com.mkrlabs.bloodsoilder.Utils.MySharedPref;
import com.mkrlabs.bloodsoilder.ui.activity.ActivityFragment;
import com.mkrlabs.bloodsoilder.ui.home.HomeFragment;
import com.mkrlabs.bloodsoilder.ui.homemap.HomeMapFragment;
import com.mkrlabs.bloodsoilder.ui.profile.ProfileFragment;
import com.mkrlabs.bloodsoilder.ui.request.RequestFragment;
import com.mkrlabs.customstatusbar.CustomStatusBar;

public class HomeActivity extends AppCompatActivity {

    private FrameLayout frameContainer;

    private BottomNavigationView homeBottomNavigation;

    private MySharedPref sharedPref;


    // backend branch upgrade
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomStatusBar.setCustomStatusColor(this,R.color.primaryColor,false);
        setContentView(R.layout.activity_home);
        sharedPref = new MySharedPref(this);
        init();

        if (!sharedPref.isAlreadyNotificationSubscribed()){
            subscribeToNotification();
            sharedPref.setNotificationSubscribed(true);
        }


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
                   /* case R.id.activityItem:
                        setUpFragment(new ActivityFragment());
                        return true;
                        */
                    case R.id.profileItem:
                        setUpFragment(new ProfileFragment());
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    private void subscribeToNotification() {
        FirebaseMessaging.getInstance().subscribeToTopic("blood_request")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe failed";
                        }
                        Log.d("Notification", msg);
                        Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
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