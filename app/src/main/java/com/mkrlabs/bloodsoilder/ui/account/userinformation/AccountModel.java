package com.mkrlabs.bloodsoilder.ui.account.userinformation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mkrlabs.bloodsoilder.Utils.AccountCreation;
import com.mkrlabs.bloodsoilder.Utils.NodeName;
import com.mkrlabs.bloodsoilder.model.User;

import androidx.annotation.NonNull;

public class AccountModel implements AccountContract.Model {

    private FirebaseFirestore firebaseFirestore ;
    private FirebaseAuth mAuth;

    public AccountModel() {
        firebaseFirestore= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void OnAccountCreate(OnAccountCreateListener onAccountCreateListener, User user) {
        firebaseFirestore.collection(NodeName.USER_NODE).document(user.getUid())
                .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    onAccountCreateListener.onSuccessListener();
                }else {
                    onAccountCreateListener.onFailureListener(task.getException().getMessage());
                }

            }
        });
    }
}
