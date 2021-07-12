package com.mkrlabs.bloodsoilder.ui.account.userinformation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mkrlabs.bloodsoilder.R;

public class DonorInfoActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    private Button doneBtn;
    LinearLayout linearLayout;


    // blood radio button
    RadioGroup mFirstGroup;
    RadioGroup mSecondGroup;
    boolean isChecking = true;
    int mCheckedId = R.id.type1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_info);
        init();
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                //Toast.makeText(getApplicationContext(),radioButton.getText(),Toast.LENGTH_SHORT).show();
                showType();
                Intent intent = new Intent(DonorInfoActivity.this, AddProfilePictureActivity.class);
                startActivity(intent);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.donorRadioButton) {
                    linearLayout.setVisibility(View.VISIBLE);
                } else {
                    linearLayout.setVisibility(View.INVISIBLE);

                }
            }
        });


        mFirstGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    mSecondGroup.clearCheck();
                    mCheckedId = checkedId;
                }
                isChecking = true;
            }
        });

        mSecondGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    mFirstGroup.clearCheck();
                    mCheckedId = checkedId;
                }
                isChecking = true;
            }
        });

    }

    private void init() {
        doneBtn = findViewById(R.id.doneBtn);
        radioGroup = findViewById(R.id.radioGroup);
        linearLayout = findViewById(R.id.bloodGroupSelectorLayout);
        mFirstGroup = (RadioGroup) findViewById(R.id.first_group);
        mSecondGroup = (RadioGroup) findViewById(R.id.second_group);

    }

    public void showType() {
        if (mCheckedId == R.id.type1) {
            Toast.makeText(this, "A+", Toast.LENGTH_SHORT).show();
        } else if (mCheckedId == R.id.type2) {
            Toast.makeText(this, "A-", Toast.LENGTH_SHORT).show();
        } else if (mCheckedId == R.id.type3) {
            Toast.makeText(this, "B+", Toast.LENGTH_SHORT).show();
        } else if (mCheckedId == R.id.type4) {
            Toast.makeText(this, "B-", Toast.LENGTH_SHORT).show();
        } else if (mCheckedId == R.id.type5) {
            Toast.makeText(this, "AB+", Toast.LENGTH_SHORT).show();
        } else if (mCheckedId == R.id.type6) {
            Toast.makeText(this, "AB-", Toast.LENGTH_SHORT).show();
        } else if (mCheckedId == R.id.type7) {
            Toast.makeText(this, "O+", Toast.LENGTH_SHORT).show();
        } else if (mCheckedId == R.id.type8) {
            Toast.makeText(this, "O-", Toast.LENGTH_SHORT).show();
        }
    }
}