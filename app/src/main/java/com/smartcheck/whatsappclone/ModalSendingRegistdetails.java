package com.smartcheck.whatsappclone;

public class ModalSendingRegistdetails {
    String name;
    String last_message = "No message available";

    String last_time;
    String iv;
    String phonenumber;

    public ModalSendingRegistdetails(String name, String iv, String phonenumber) {
        this.name = name;
        this.iv = iv;
        this.phonenumber = phonenumber;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
