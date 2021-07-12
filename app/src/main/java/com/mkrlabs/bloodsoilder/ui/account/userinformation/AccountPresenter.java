package com.mkrlabs.bloodsoilder.ui.account.userinformation;

import com.mkrlabs.bloodsoilder.model.User;

public class AccountPresenter implements AccountContract.Presenter,AccountContract.Model.OnAccountCreateListener{

    private AccountContract.View view ;
    private AccountContract.Model accountModel;

    public AccountPresenter(AccountContract.View view) {
        this.view = view;
        accountModel = new AccountModel();
    }

    @Override
    public void createAccount(User user) {
        if (view!=null){
            view.showLoading();
            accountModel.OnAccountCreate(this,user);
        }
    }

    @Override
    public void onDestroy() {
        view = null;

    }

    @Override
    public void onSuccessListener() {
        if (view!=null){
            view.onAccountSuccess();
            view.hideLoading();
        }
    }

    @Override
    public void onFailureListener(String error) {
        if (view!=null){
            view.onAccountFailure(error);
            view.hideLoading();
        }
    }
}
