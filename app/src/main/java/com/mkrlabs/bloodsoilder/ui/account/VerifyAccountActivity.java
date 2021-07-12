package com.mkrlabs.bloodsoilder.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.Utils.AccountCreation;
import com.mkrlabs.bloodsoilder.Utils.Display;
import com.mkrlabs.bloodsoilder.ui.account.userinformation.UserAccountInformation;

import java.util.concurrent.TimeUnit;

public class VerifyAccountActivity extends AppCompatActivity {

    private Button verifyBtn;
    private String phone;
    private TextView verifyAccountOTPTV;
    private String otpText;
    private EditText verifyAccountOTPEdt;

    private String TAG ="OTP";
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);
        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("en");
        init();
        if (getIntent()!=null){
            phone = getIntent().getExtras().getString("PHONE","").trim();

            otpText = "We have send an OTP on "+phone+" will apply to the field";
            verifyAccountOTPTV.setText(otpText);


        }



        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+88"+phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = verifyAccountOTPEdt.getText().toString().trim();

                if (TextUtils.isEmpty(otp)){
                    verifyAccountOTPEdt.setError("required");
                    Display.infoToast(VerifyAccountActivity.this,"otp required");
                }

                verifyOTPCode(otp);
            }
        });

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String s = phoneAuthCredential.getSmsCode();
            verifyAccountOTPEdt.setText(s);
            verifyOTPCode(s);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.d(TAG, "error:" +e.getMessage());
                Display.errorToast(VerifyAccountActivity.this,"Invalid Request");
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Log.d(TAG, "error:" +e.getMessage());
                // The SMS quota for the project has been exceeded
                Display.errorToast(VerifyAccountActivity.this,"Too many request");
            }
        }

        @Override
        public void onCodeSent(@NonNull String VerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(VerificationId, forceResendingToken);
            Log.d(TAG, "onCodeSent:" +VerificationId);
            // Save verification ID and resending token so we can use them later
            mVerificationId = VerificationId;
            Display.infoToast(VerifyAccountActivity.this,"Code Sent");
        }
    };
    private void verifyOTPCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        AccountCreation.phone = phone;
        signInWithPhoneAuthCredential(credential);

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI

                            AccountCreation.uid = user.getUid();
                            Display.successToast(VerifyAccountActivity.this,"Success Valid Number");
                            Intent intent = new Intent(VerifyAccountActivity.this, UserAccountInformation.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);


                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Display.errorToast(VerifyAccountActivity.this,"Invalids code");
                            }
                        }
                    }
                });
    }


    private  void  init (){
        verifyBtn= findViewById(R.id.verifyBtn);
        verifyAccountOTPTV= findViewById(R.id.verifyAccountOTPTV);
        verifyAccountOTPEdt= findViewById(R.id.verifyAccountOTPEdt);
    }
}