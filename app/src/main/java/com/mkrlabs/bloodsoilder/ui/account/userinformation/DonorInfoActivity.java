package com.mkrlabs.bloodsoilder.ui.account.userinformation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mkrlabs.bloodsoilder.R;

public class DonorInfoActivity extends AppCompatActivity {

    private Button doneBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_info);
        init();
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonorInfoActivity.this, AddProfilePictureActivity.class);
                startActivity(intent);
            }
        });

    }
    private  void  init (){
        doneBtn= findViewById(R.id.doneBtn);
    }
}