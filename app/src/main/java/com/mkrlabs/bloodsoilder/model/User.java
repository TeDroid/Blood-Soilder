package com.mkrlabs.bloodsoilder.model;

import androidx.annotation.NonNull;

public class User {

    private String name;
    private  String email;
    private  String password;
    private int  user_type;
    private boolean  status;
    private String  uid;
    private String  blood_group;
    private long  account_created_at;
    private Double lat;
    private Double lon;
    private boolean donation_status;
    private String profileImage;
    private String phone;


    public User() {
    }

    public User(String name, String email,String phone, String password, int user_type, boolean status, String uid, String blood_group, long account_created_at, Double lat, Double lon, boolean donation_status, String profileImage) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.user_type = user_type;
        this.status = status;
        this.uid = uid;
        this.blood_group = blood_group;
        this.account_created_at = account_created_at;
        this.lat = lat;
        this.lon = lon;
        this.donation_status = donation_status;
        this.profileImage = profileImage;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", user_type=" + user_type +
                ", status=" + status +
                ", uid='" + uid + '\'' +
                ", blood_group='" + blood_group + '\'' +
                ", account_created_at=" + account_created_at +
                ", lat=" + lat +
                ", lon=" + lon +
                ", donation_status=" + donation_status +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
}
