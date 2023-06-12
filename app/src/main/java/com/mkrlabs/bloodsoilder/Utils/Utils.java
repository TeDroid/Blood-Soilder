package com.mkrlabs.bloodsoilder.Utils;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {


    public static String geTime(Timestamp timestamp){
        Date date = timestamp.toDate();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String time = timeFormat.format(date);
        return  time;
    }
    public static String getMonth(Timestamp timestamp){
        Date date = timestamp.toDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM", Locale.getDefault());
        String monthName = dateFormat.format(date);
        return monthName;
    }

    public static String getDate(Timestamp timestamp){
        Date date = timestamp.toDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    public static String BASE_URL = "https://fcm.googleapis.com/fcm/";
    public static String BLOOD_REQUEST_TOPIC = "/topics/blood_request";
    public static String headerToken = "key=AAAAhgMgDBI:APA91bF-6DyNNo42Z-fx5giYjky_Rc9LNtDzq2dPCVLtteP47mRMy4ZRgIi1pUb9i723pouinWL6BVtSRAUD_UFJBoc2YidBUHIiwN38XZf4GAU_drU5TS3UAsNuUuYZb_kzv9aonOey";




    public static String getTimeAgo(long time ) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }


}