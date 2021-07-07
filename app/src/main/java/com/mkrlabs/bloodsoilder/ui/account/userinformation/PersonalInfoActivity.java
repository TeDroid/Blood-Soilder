package com.mkrlabs.bloodsoilder.ui.account.userinformation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.ui.account.VerifyAccountActivity;

public class PersonalInfoActivity extends AppCompatActivity {

    private Button nextBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        init();
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalInfoActivity.this, DonorInfoActivity.class);
                startActivity(intent);
            }
        });

    }
    private  void  init (){
        nextBtn= findViewById(R.id.profileNextBtn);
    }
}