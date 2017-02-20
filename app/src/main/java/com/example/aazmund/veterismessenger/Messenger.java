package com.example.aazmund.veterismessenger;

import java.util.Date;

public class Messenger {
    private String messengerText;
    private String messengerUser;
    private long messegetime;

    public Messenger(String messengerText, String messengerUser) {
        this.messengerText = messengerText;
        this.messengerUser = messengerUser;

        messegetime = new Date().getTime();

    }

    public long getMessegetime() {
        return messegetime;
    }

    public void setMessegetime(long messegetime) {
        this.messegetime = messegetime;
    }

    public String getMessengerText() {
        return messengerText;
    }

    public void setMessengerText(String messengerText) {
        this.messengerText = messengerText;
    }

    public String getMessengerUser() {
        return messengerUser;
    }

    public void setMessengerUser(String messengerUser) {
        this.messengerUser = messengerUser;
    }

    public Messenger() {

    }

}
