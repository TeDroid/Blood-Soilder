package com.mkrlabs.bloodsoilder.ui.account.userinformation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.Utils.AccountCreation;
import com.mkrlabs.bloodsoilder.Utils.Display;
import com.mkrlabs.bloodsoilder.model.User;
import com.mkrlabs.bloodsoilder.ui.HomeActivity;
import com.mkrlabs.bloodsoilder.ui.account.VerifyAccountActivity;

public class DonorInfoActivity extends AppCompatActivity {
    RadioGroup userTypeRadioGroup;
    RadioButton radioButton;
    private Button doneBtn;
    LinearLayout linearLayout;

    private FirebaseAuth mAuth;
    // blood radio button
    RadioGroup mFirstGroup;
    RadioGroup mSecondGroup;
    boolean isChecking = true;
    int mCheckedId =-1;
    private int User_Type ;
    private String BLOOD_GROUP = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_info);
        init();
        mAuth = FirebaseAuth.getInstance();
        mFirstGroup.clearCheck();
        mSecondGroup.clearCheck();
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // int radioId = userTypeRadioGroup.getCheckedRadioButtonId();
               // radioButton = findViewById(radioId);
                //Toast.makeText(getApplicationContext(),radioButton.getText(),Toast.LENGTH_SHORT).show();
                validateUserData();
                
            }
        });

        userTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.donorRadioButton) {
                    User_Type = 1;
                    linearLayout.setVisibility(View.VISIBLE);
                } else {
                    User_Type = 0;
                    BLOOD_GROUP = null;
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
                    userBloodGroupSelection();
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
                    userBloodGroupSelection();
                }
                isChecking = true;
            }
        });

    }

    private void validateUserData() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append((User_Type==0)?"Seeker":"Donar");

        Log.v("Tag", "Check Id " + mCheckedId);

        if (User_Type ==1 && mCheckedId<=0){
            Display.errorToast(this, "select blood group");
            return;
        }

        if (BLOOD_GROUP!=null){
            stringBuilder.append(BLOOD_GROUP);
        }


        Display.successToast(this,"All Okay : " +stringBuilder);
        displayUserAllInfo();
    }

    private void displayUserAllInfo() {


        Log.v("Info","Name "+ AccountCreation.name);
        Log.v("Info","Pin "+ AccountCreation.pin);
        Log.v("Info","Phone "+ AccountCreation.phone);
        Log.v("Info","Front Image "+ AccountCreation.frontImage.toString());
        Log.v("Info","Back Image "+ AccountCreation.backImage.toString());
        Log.v("Info","User Type "+ User_Type);
        Log.v("Info","Blood Group "+ BLOOD_GROUP);

        StringBuilder user = new StringBuilder();
        user.append("Name "+ AccountCreation.name+"\n");
        user.append("Pin "+ AccountCreation.pin+"\n");
        user.append("Phone "+ AccountCreation.phone+"\n");
        user.append("Front Image "+ AccountCreation.frontImage.getPath()+"\n");
        user.append("back Image "+ AccountCreation.backImage.getPath()+"\n");
        user.append("User Type "+ User_Type+"\n");
        user.append("Blood Group "+ BLOOD_GROUP+"\n");

        Log.v("Info",user.toString());
        Display.infoToast(this,user.toString());

        createUserAccount();




    }

    private void createUserAccount() {
//    public User(String name, String phone, int nid, int pin, int user_type, boolean status, String uid, String blood_group, long account_created_at) {
        User user = new User();
    }

    private void init() {
        doneBtn = findViewById(R.id.doneBtn);
        userTypeRadioGroup = findViewById(R.id.userTypeRadioGroup);
        linearLayout = findViewById(R.id.bloodGroupSelectorLayout);
        mFirstGroup = (RadioGroup) findViewById(R.id.first_group);
        mSecondGroup = (RadioGroup) findViewById(R.id.second_group);

    }

    public void userBloodGroupSelection() {
        if (mCheckedId == R.id.type1) {
            BLOOD_GROUP = "A+";
        } else if (mCheckedId == R.id.type2) {
            BLOOD_GROUP = "A-";
        } else if (mCheckedId == R.id.type3) {
            BLOOD_GROUP = "B+";
        } else if (mCheckedId == R.id.type4) {
            BLOOD_GROUP = "B-";
        } else if (mCheckedId == R.id.type5) {
            BLOOD_GROUP = "AB+";
        } else if (mCheckedId == R.id.type6) {
            BLOOD_GROUP = "AB-";
        } else if (mCheckedId == R.id.type7) {
            BLOOD_GROUP = "O+";
        } else if (mCheckedId == R.id.type8) {
            BLOOD_GROUP = "O-";
        }else {
            BLOOD_GROUP =null;
        }
    }


}