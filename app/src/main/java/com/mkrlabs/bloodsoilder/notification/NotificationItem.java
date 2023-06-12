package com.mkrlabs.bloodsoilder.notification;

public class NotificationItem {
    public NotificationItem(){}
    public String to;
    public NotificationBody notification ;
    public MetaData data ;


    public class MetaData{
        public String  project_id ;
       public MetaData(){}
    }
    public static class NotificationBody{
        public String title ;
        public String body ;

       public NotificationBody(){}
    }
}
