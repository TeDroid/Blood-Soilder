package com.mkrlabs.bloodsoilder.ui.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.Utils.Display;
import com.mkrlabs.bloodsoilder.Utils.DisplayUtils;

public class CreateAccountActivity extends AppCompatActivity {

    private Button sendBtn;
    private EditText createAccountPhoneEdt;
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        init();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone = createAccountPhoneEdt.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    createAccountPhoneEdt.setError("required");
                    return;
                }

                Intent intent = new Intent(CreateAccountActivity.this,VerifyAccountActivity.class);
                intent.putExtra("PHONE",phone);
                startActivity(intent);
            }
        });
    }

    private void init(){
        sendBtn= findViewById(R.id.sendBtn);
        createAccountPhoneEdt= findViewById(R.id.createAccountPhoneEdt);
    }
}