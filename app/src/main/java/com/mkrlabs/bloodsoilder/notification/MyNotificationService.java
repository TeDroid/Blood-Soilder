package com.mkrlabs.bloodsoilder.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mkrlabs.bloodsoilder.R;
import com.mkrlabs.bloodsoilder.ui.HomeActivity;

public class MyNotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification()!=null){
            Log.v("Channel","Different Topic");
                simpleNotification(remoteMessage);
        }

    }


    private void displayNotification(RemoteMessage remoteMessage){
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL= "High Channel";

        String title=remoteMessage.getData().get("title");
        String body=remoteMessage.getData().get("body");
        RemoteMessage.Notification notification = remoteMessage.getNotification();

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL,"Notification", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Blood Request");
            channel.enableLights(true);
            manager.createNotificationChannel(channel);
        }


        //int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL);
        notificationBuilder
                .setSmallIcon(R.drawable.ic_outline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent);
        /*int i=0;
        if(j>0){
            i=j;
        }*/
        manager.notify(0,notificationBuilder.build());

    }

    private void simpleNotification(RemoteMessage remoteMessage){
        String notification_channel_name = "Urgent Blood";
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL = "High Channel";

        String title=remoteMessage.getNotification().getTitle();
        String body=remoteMessage.getNotification().getBody();

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(notification_channel_name,"Blood Request", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Urgent blood needed");
            channel.enableLights(true);
            manager.createNotificationChannel(channel);
        }

        //int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,101,intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,notification_channel_name);
        notificationBuilder
                .setSmallIcon(R.drawable.ic_outline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent);
        manager.notify(101,notificationBuilder.build());

    }
}