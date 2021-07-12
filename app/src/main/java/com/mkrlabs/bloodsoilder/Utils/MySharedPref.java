package com.mkrlabs.bloodsoilder.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPref {

    Context context;
    private SharedPreferences default_prefence;
    private String USER_NAME = "USER_NAME";
    private String USER_UID = "USER_UID";
    private String USER_FIRST_TIME = "USER_FIRST_TIME";
    private String USER_REWARD_COIN = "USER_REWARD_COIN";

    public MySharedPref(Context context) {
        this.context = context;
        default_prefence = context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }


}
