package com.mkrlabs.bloodsoilder.ui.account.userinformation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.ui.HomeActivity;

public class AddProfilePictureActivity extends AppCompatActivity {

    private Button setBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile_picture);

        init();
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddProfilePictureActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }
    private  void  init (){
        setBtn= findViewById(R.id.setBtn);
    }
}