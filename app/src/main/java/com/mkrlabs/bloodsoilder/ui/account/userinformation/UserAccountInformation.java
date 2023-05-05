package com.mkrlabs.bloodsoilder.ui.account.userinformation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.Utils.AccountCreation;
import com.mkrlabs.bloodsoilder.Utils.Display;
import com.mkrlabs.bloodsoilder.model.User;
import com.mkrlabs.bloodsoilder.ui.account.LoginActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAccountInformation extends AppCompatActivity {
    private static final int PROFILE_IMAGE = 101;

    private ImageView imageAddPlusButton;
    private CircleImageView circleProfileImage;
    private EditText nameEdt, emailEdt, passwordEdt, confirmPasswordEdt;
    private String name ;
    private int pin , nid_no;
    Button accountInfoNextButton;

    Uri profileImage = null;
    private TextView registrationAlreadyHaveAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_information);
        init();

        imageAddPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(UserAccountInformation.this)
                        .crop(9f, 9f)    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(PROFILE_IMAGE);
            }
        });

        accountInfoNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInfo();
            }
        });
        registrationAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserAccountInformation.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void validateInfo() {

        name = nameEdt.getText().toString();
        String email = emailEdt.getText().toString();
        String password = passwordEdt.getText().toString();
        String confirmPassword = confirmPasswordEdt.getText().toString();

        if (TextUtils.isEmpty(name)){
            nameEdt.setError("required");
            return;
        }
        if (TextUtils.isEmpty(email)){
            emailEdt.setError("required");
            return;
        }
        if (TextUtils.isEmpty(password)){
            passwordEdt.setError("required");
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)){
            confirmPasswordEdt.setError("required");
            return;
        }

        if (!password.equals(confirmPassword)){
            Display.errorToast(this,"password doesn't mismatch");
            return;
        }

        if (profileImage ==null){
            Display.infoToast(this,"select profile image");
            return;
        }

        try {


            AccountCreation.name=name;
            AccountCreation.email=email;
            AccountCreation.password=password;
            AccountCreation.profileImage= profileImage;

            Intent intent = new Intent(UserAccountInformation.this,DonorInfoActivity.class);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
        Display.errorToast(this,"Something went wrong");



    }


    private void  init(){
        imageAddPlusButton=findViewById(R.id.imageAddPlusButton);

        nameEdt=findViewById(R.id.nameEdt);
        emailEdt =findViewById(R.id.emailEdt);
        passwordEdt =findViewById(R.id.passwordEdt);
        confirmPasswordEdt =findViewById(R.id.confirmPasswordEdt);
        accountInfoNextButton=findViewById(R.id.accountInfoNextButton);
        circleProfileImage=findViewById(R.id.circleProfileImage);
        registrationAlreadyHaveAccount=findViewById(R.id.registrationAlreadyHaveAccount);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== PROFILE_IMAGE){
            Uri uri = data.getData();
            profileImage = uri;
            circleProfileImage.setImageURI(uri);
        }

        else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}