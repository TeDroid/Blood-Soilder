package com.mkrlabs.bloodsoilder.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private  static String BASE_URL = "https://fcm.googleapis.com/fcm/";
    public static Retrofit retrofit;

    public static Retrofit getInstance(){
        if (retrofit==null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return  retrofit;
    }
}
