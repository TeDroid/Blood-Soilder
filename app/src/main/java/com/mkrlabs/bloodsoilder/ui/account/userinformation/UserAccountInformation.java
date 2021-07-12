package com.mkrlabs.bloodsoilder.ui.account.userinformation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.mkrlabs.bloodsoilder.R;

public class UserAccountInformation extends AppCompatActivity {
    private static final int FRONT_IMAGE = 101;
    private static final int BACK_IMAGE = 102;

    ImageView nidFrontImage;
    ImageView nidBackImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_information);
        init();

        nidFrontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(UserAccountInformation.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(FRONT_IMAGE);
            }
        });
        nidBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(UserAccountInformation.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(BACK_IMAGE);
            }
        });

    }


    private void  init(){
        nidFrontImage=findViewById(R.id.nidFrontImage);
        nidBackImage=findViewById(R.id.nidBackImage);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==FRONT_IMAGE){
            Uri uri = data.getData();
            nidFrontImage.setImageURI(uri);
        }else if (requestCode == BACK_IMAGE){
            Uri uri = data.getData();
            nidBackImage.setImageURI(uri);
        }

        else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}