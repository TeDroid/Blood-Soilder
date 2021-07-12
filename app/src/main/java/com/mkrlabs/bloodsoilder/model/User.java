package com.mkrlabs.bloodsoilder.model;

public class User {

    private String name;
    private String phone;
    private int  nid;
    private int  pin;
    private int  user_type;
    private boolean  status;
    private String  uid;
    private String  blood_group;
    private long  account_created_at;
    private Double lat;
    private Double lon;
    private boolean donation_status;
    private String front_image;


    public User() {
    }

    public User(String name, String phone, int nid, int pin, int user_type, boolean status, String uid, String blood_group, long account_created_at,String front_image) {
        this.name = name;
        this.phone = phone;
        this.nid = nid;
        this.pin = pin;
        this.user_type = user_type;
        this.status = status;
        this.uid = uid;
        this.blood_group = blood_group;
        this.account_created_at = account_created_at;
        this.front_image = front_image;
    }

    public String getFront_image() {
        return front_image;
    }

    public void setFront_image(String front_image) {
        this.front_image = front_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public long getAccount_created_at() {
        return account_created_at;
    }

    public void setAccount_created_at(long account_created_at) {
        this.account_created_at = account_created_at;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public boolean isDonation_status() {
        return donation_status;
    }

    public void setDonation_status(boolean donation_status) {
        this.donation_status = donation_status;
    }
}
