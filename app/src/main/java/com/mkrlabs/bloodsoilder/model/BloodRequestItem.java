package com.mkrlabs.bloodsoilder.model;

import com.google.firebase.Timestamp;

public class BloodRequestItem {

    private String hospitalName;
    private String hospitalAddress;
    private Timestamp timestamp;
    private String  bloodGroup;
    private boolean  status;
    private String  requestBy;
    private boolean transportationExpense;
    private String contactNumber;
    private String requestOwnerName;
    private String requestOwnerImage;
    private String pId;

    private  Timestamp time;


    public BloodRequestItem() {
    }

    public BloodRequestItem(String pid,  Timestamp time,String hospitalName, String hospitalAddress, Timestamp timestamp, String bloodGroup, boolean status, String requestBy, boolean transportationExpense, String contactNumber, String requestOwnerName, String requestOwnerImage) {
        this.pId = pid;
        this.time = time;
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
        this.timestamp = timestamp;
        this.bloodGroup = bloodGroup;
        this.status = status;
        this.requestBy = requestBy;
        this.transportationExpense = transportationExpense;
        this.contactNumber = contactNumber;
        this.requestOwnerName = requestOwnerName;
        this.requestOwnerImage = requestOwnerImage;
    }


    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }

    public boolean isTransportationExpense() {
        return transportationExpense;
    }

    public void setTransportationExpense(boolean transportationExpense) {
        this.transportationExpense = transportationExpense;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getRequestOwnerName() {
        return requestOwnerName;
    }

    public void setRequestOwnerName(String requestOwnerName) {
        this.requestOwnerName = requestOwnerName;
    }

    public String getRequestOwnerImage() {
        return requestOwnerImage;
    }

    public void setRequestOwnerImage(String requestOwnerImage) {
        this.requestOwnerImage = requestOwnerImage;
    }
}
