package com.mkrlabs.bloodsoilder.ui.account.userinformation;

import com.mkrlabs.bloodsoilder.model.User;

public class AccountContract {
    public interface View{

        void showLoading();
        void hideLoading();
        void onAccountSuccess();
        void onAccountFailure(String error);
    }


    public  interface Model{
        interface OnAccountCreateListener{
            void onSuccessListener();
            void onFailureListener(String error);
        }
        void OnAccountCreate(OnAccountCreateListener onAccountCreateListener,User user);
    }

    public interface Presenter{
        void createAccount(User user);
        void onDestroy();
    }
}
