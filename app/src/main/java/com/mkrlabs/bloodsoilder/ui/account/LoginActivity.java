package com.mkrlabs.bloodsoilder.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.Utils.Display;
import com.mkrlabs.bloodsoilder.Utils.MySharedPref;
import com.mkrlabs.bloodsoilder.Utils.NodeName;
import com.mkrlabs.bloodsoilder.model.User;
import com.mkrlabs.bloodsoilder.ui.HomeActivity;
import com.mkrlabs.bloodsoilder.ui.account.userinformation.UserAccountInformation;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail,loginPasswordEdt;
    private TextView dontHaveAnAccountTV;
    private Button loginButton;
    private ProgressDialog dialog;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private MySharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        mAuth = FirebaseAuth.getInstance();
        sharedPref = new MySharedPref(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("verifying user");





        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString().trim();
                String password = loginPasswordEdt.getText().toString().trim();



                if (TextUtils.isEmpty(email)){
                    loginEmail.setError("required");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    loginPasswordEdt.setError("required");
                    return;
                }

                dialog.show();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            setUserInformationToSharedPref(task.getResult().getUser().getUid());

                        }else {
                            dialog.dismiss();
                            Display.errorToast(LoginActivity.this,"Invalid Credential");
                        }
                    }
                });



            }
        });



        dontHaveAnAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, UserAccountInformation.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setUserInformationToSharedPref(String uid) {

        firebaseFirestore.collection(NodeName.USER_NODE).document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    User user = task.getResult().toObject(User.class);

                    sharedPref.setUID(user.getUid());
                    sharedPref.setUSER_NAME(user.getName());
                    sharedPref.setUSER_IMAGE(user.getProfileImage());

                    Log.v("User","User Info ------> " + user.toString());
                    String userInfo ="";
                            userInfo= "Name : ==> "+ sharedPref.getUID()+"\n\n"+
                            "Name : ==> "+ sharedPref.getUSER_NAME()+"\n\n"+
                            "Image : ==> "+ sharedPref.getUSER_NAME()+"\n\n"+

                    Log.v("User",userInfo.isEmpty()?"Empty":userInfo);

                    Display.successToast(LoginActivity.this,"Logged in successfully");
                    dialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    Display.errorToast(LoginActivity.this,"Something went wrong");
                    mAuth.signOut();
                    dialog.dismiss();
                }

            }
        });



    }


    private void init(){

        loginEmail = findViewById(R.id.loginEmailEdt);
        loginPasswordEdt = findViewById(R.id.loginPasswordEdt);
        loginButton = findViewById(R.id.loginLoginButton);
        dontHaveAnAccountTV = findViewById(R.id.dontHaveAnAccountTV);

    }
}