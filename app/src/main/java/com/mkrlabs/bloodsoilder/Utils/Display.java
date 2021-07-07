package com.mkrlabs.bloodsoilder.Utils;

import android.content.Context;

import es.dmoral.toasty.Toasty;

public class Display {

    private Display(){}

    public  static void successToast(Context context , String msg){
        Toasty.success(context,msg,Toasty.LENGTH_SHORT,true).show();
    }
    public  static void errorToast(Context context , String error){
        Toasty.success(context,error,Toasty.LENGTH_SHORT,true).show();
    }
    public  static void infoToast(Context context , String info){
        Toasty.success(context, info,Toasty.LENGTH_SHORT,true).show();
    }



}
