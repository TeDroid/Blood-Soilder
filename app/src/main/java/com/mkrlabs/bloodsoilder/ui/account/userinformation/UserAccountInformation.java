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
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.Utils.AccountCreation;
import com.mkrlabs.bloodsoilder.Utils.Display;

public class UserAccountInformation extends AppCompatActivity {
    private static final int FRONT_IMAGE = 101;
    private static final int BACK_IMAGE = 102;

    private ImageView nidFrontImage;
    private ImageView nidBackImage;
    private EditText nameEdt,pinEdt,confrmPinEdt,nidNumEdt;
    private String name ;
    private int pin , nid_no;
    Button accountInfoNextButton;

    Uri frontImage = null;
    Uri backImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_information);
        init();

        nidFrontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(UserAccountInformation.this)
                        .crop(16f, 9f)    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(FRONT_IMAGE);
            }
        });
        nidBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(UserAccountInformation.this)
                        .crop(16f, 9f)	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(BACK_IMAGE);
            }
        });

        accountInfoNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInfo();
            }
        });

    }

    private void validateInfo() {

        name = nameEdt.getText().toString();
        String pinText = pinEdt.getText().toString();
        String confirmText = confrmPinEdt.getText().toString();
        String nidText = nidNumEdt.getText().toString();

        if (TextUtils.isEmpty(name)){
            nameEdt.setError("required");
            return;
        }
        if (TextUtils.isEmpty(pinText)){
            pinEdt.setError("required");
            return;
        }
        if (TextUtils.isEmpty(confirmText)){
            confrmPinEdt.setError("required");
            return;
        }
        if (TextUtils.isEmpty(nidText)){
            nidNumEdt.setError("required");
            return;
        }

        if (!pinText.equals(confirmText)){
            Display.errorToast(this,"pin mismatch");
            return;
        }

        if (frontImage ==null){
            Display.infoToast(this,"select nid front image");
            return;
        }
        if (backImage==null){
            Display.infoToast(this,"select nid back image");
            return;
        }
        try {
            pin = Integer.parseInt(confirmText);
            nid_no = Integer.parseInt(nidText);


            AccountCreation.name=name;
            AccountCreation.pin=pin;
            AccountCreation.frontImage=frontImage;
            AccountCreation.backImage=backImage;


            Intent intent = new Intent(UserAccountInformation.this,DonorInfoActivity.class);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
        Display.errorToast(this,"Something went wrong");



    }


    private void  init(){
        nidFrontImage=findViewById(R.id.nidFrontImage);
        nidBackImage=findViewById(R.id.nidBackImage);
        nameEdt=findViewById(R.id.nameEdt);
        pinEdt=findViewById(R.id.pinEdt);
        confrmPinEdt=findViewById(R.id.confrmPinEdt);
        nidNumEdt=findViewById(R.id.nidNumEdt);
        accountInfoNextButton=findViewById(R.id.accountInfoNextButton);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==FRONT_IMAGE){
            Uri uri = data.getData();
            frontImage = uri;
            nidFrontImage.setImageURI(uri);
        }else if (requestCode == BACK_IMAGE){
            Uri uri = data.getData();
            backImage = uri;
            nidBackImage.setImageURI(uri);
        }

        else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}