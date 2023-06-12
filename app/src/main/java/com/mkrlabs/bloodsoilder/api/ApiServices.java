package com.mkrlabs.bloodsoilder.api;

import com.mkrlabs.bloodsoilder.notification.NotificationItem;
import com.mkrlabs.bloodsoilder.notification.NotificationResponse;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiServices {
    @POST("send")
    Call<NotificationResponse> postNotification(@Body NotificationItem notificationItem, @Header("Authorization") String headerToken);
}
