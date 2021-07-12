package com.mkrlabs.bloodsoilder.ui.account.userinformation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.mkrlabs.bloodsoilder.R;

public class PersonalInfoActivity extends AppCompatActivity {


    private static final int FRONT_IMAGE = 101;
    private static final int BACK_IMAGE = 102;
    private Button nextBtn;
    private ImageView nidImage;
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
        nextBtn= findViewById(R.id.nidBackImage);
        nidImage= findViewById(R.id.nidImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==FRONT_IMAGE){
            Uri uri = data.getData();
            nidImage.setImageURI(uri);
        }else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

}