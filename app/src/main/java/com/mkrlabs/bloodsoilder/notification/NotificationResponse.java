package com.mkrlabs.bloodsoilder.notification;

import java.util.List;

public class NotificationResponse {
    int canonical_ids;
    int failure;
    Long multicast_id;
    List<Result> results;
    int success;
}
 class Result {
     String message_id;
 }