package com.mkrlabs.bloodsoilder.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPref {

    Context context;
    private SharedPreferences default_prefence;
    private String USER_NAME = "USER_NAME";
    private String USER_UID = "USER_UID";
    private String USER_IMAGE = "USER_IMAGE";
    private String USER_FIRST_TIME = "USER_FIRST_TIME";
    private String NOTIFICATION_SUBSCRIBE="SUBSCRIPTION_TOPIC";

    public MySharedPref(Context context) {
        this.context = context;
        default_prefence = context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    public  void  setUID(String uid){
        default_prefence.edit().putString(USER_UID,uid).apply();
    }
    public String getUID(){
        return  default_prefence.getString(USER_UID,"");
    }

    public  void  setUSER_NAME(String name){
        default_prefence.edit().putString(USER_NAME,name).apply();
    }
    public String getUSER_NAME(){
        return  default_prefence.getString(USER_NAME,"");
    }

     public  void  setUSER_IMAGE(String image){
        default_prefence.edit().putString(USER_IMAGE,image).apply();
    }
    public String getUSER_IMAGE(){
        return  default_prefence.getString(USER_IMAGE,"");
    }


    public void setNotificationSubscribed(boolean status){
        default_prefence.edit().putBoolean(NOTIFICATION_SUBSCRIBE,status).apply();
    }

    public boolean isAlreadyNotificationSubscribed(){
        return default_prefence.getBoolean(NOTIFICATION_SUBSCRIBE,false);
    }



}
