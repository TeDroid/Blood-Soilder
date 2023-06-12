package com.mkrlabs.bloodsoilder.ui.account.userinformation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.Utils.AccountCreation;
import com.mkrlabs.bloodsoilder.Utils.Display;
import com.mkrlabs.bloodsoilder.Utils.FirebaseUploader;
import com.mkrlabs.bloodsoilder.Utils.MySharedPref;
import com.mkrlabs.bloodsoilder.Utils.NodeName;
import com.mkrlabs.bloodsoilder.model.User;
import com.mkrlabs.bloodsoilder.ui.HomeActivity;
import com.mkrlabs.bloodsoilder.ui.account.VerifyAccountActivity;

import org.w3c.dom.Node;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class DonorInfoActivity extends AppCompatActivity implements AccountContract.View {
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

    private AccountPresenter presenter;

    private StorageReference storageReference;
    private ProgressDialog dialog;
    private FirebaseFirestore firebaseFirestore;
    private String frontImageUrl=null;
    private MySharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_info);
        init();

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        sharedPref = new MySharedPref(this);
        dialog = new ProgressDialog(this);
        presenter = new AccountPresenter(this);
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
        Log.v("Info","Email "+ AccountCreation.email);
        Log.v("Info","Password "+ AccountCreation.password);
        Log.v("Info","Profile Image "+ AccountCreation.profileImage.getPath().toString());
        Log.v("Info","User Type "+ User_Type);
        Log.v("Info","Blood Group "+ BLOOD_GROUP);

        StringBuilder user = new StringBuilder();
        user.append("Name "+ AccountCreation.name+"\n");
        user.append("Email "+ AccountCreation.email+"\n");
        user.append("Phone "+ AccountCreation.phone+"\n");
        user.append("Password "+ AccountCreation.phone+"\n");
        user.append("Profile Image "+ AccountCreation.profileImage.getPath()+"\n");
        user.append("User Type "+ User_Type+"\n");
        user.append("Blood Group "+ BLOOD_GROUP+"\n");

        Log.v("Info",user.toString());
        Display.infoToast(this,user.toString());

        createUserAccount();




    }

    private void createUserAccount() {
        mAuth.createUserWithEmailAndPassword(AccountCreation.email,AccountCreation.password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    String user_UID = task.getResult().getUser().getUid().toString();

                    uploadImage(AccountCreation.profileImage,user_UID);

                }else {
                    Display.errorToast(DonorInfoActivity.this, task.getException().getMessage());
                }
            }
        });
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


    @Override
    public void showLoading() {

        dialog.show();
    }

    @Override
    public void hideLoading() {

        dialog.dismiss();
    }

    @Override
    public void onAccountSuccess() {

        Intent intent = new Intent(DonorInfoActivity.this,HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAccountFailure(String error) {

        Display.errorToast(this,error);
    }


    private File bitmapToFile(Bitmap bitmap, String name) {
        File filesDir = getCacheDir();
        File imageFile = new File(filesDir, name + ".jpg");
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("Bitmap Error", "Error writing bitmap", e);
        }
        return imageFile;
    }
    private void uploadImage(Uri uri,String user_uid) {
        dialog.show();
        String pushKey = firebaseFirestore.collection(NodeName.USER_NODE).document().getId();
        Bitmap bitmap = uriToBitmap(uri);
        File file = bitmapToFile(bitmap,"photo");

        FirebaseUploader firebaseUploader = new FirebaseUploader(new FirebaseUploader.UploadListener() {
            @Override
            public void onUploadFail(String message) {

            }

            @Override
            public void onUploadSuccess(String downloadUrl) {
                //registration(downloadUrl);

                frontImageUrl = downloadUrl;
                long timestamp = Calendar.getInstance().getTime().getTime();
//    public User(String name, String phone, String email, String password, int user_type, boolean status, String uid, String blood_group, long account_created_at, Double lat, Double lon, boolean donation_status, String profileImage) {
                User user = new User(AccountCreation.name,AccountCreation.email,AccountCreation.phone,AccountCreation.password
                        ,User_Type,true,user_uid,BLOOD_GROUP,timestamp,0.0,0.0,true,frontImageUrl);

                sharedPref.setUSER_NAME(user.getName());
                sharedPref.setUSER_IMAGE(frontImageUrl);
                sharedPref.setUID(user.getUid());
                presenter.createAccount(user);


            }

            @Override
            public void onUploadProgress(int progress) {

            }

            @Override
            public void onUploadCancelled() {

                Display.errorToast(DonorInfoActivity.this,"Image Upload Failed");
            }
        },storageReference.child("user_image").child(pushKey));
        firebaseUploader.uploadImage(DonorInfoActivity.this,file);


    }

    private Bitmap uriToBitmap(Uri uri){

        try {
            if(  Uri.parse(uri.toString())!=null   ){
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() , Uri.parse(uri.toString()));

                return bitmap;
            }
        }
        catch (Exception e) {
            //handle exception
            Log.v("Tag" , "uri to bitmap error "+ e.getMessage());
        }
        return null;
    }
}