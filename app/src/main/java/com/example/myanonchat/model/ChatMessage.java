package com.example.myanonchat.model;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class ChatMessage {
    private String sender_nickname;
    private String sender_uid;
    private String text;
    private String group_name;
    private long time;

    public ChatMessage() {
    }

    public ChatMessage(String sender_nickname, String sender_uid, String text, String group_name, long time) {
        this.sender_nickname = sender_nickname;
        this.sender_uid = sender_uid;
        this.text = text;
        this.group_name = group_name;
        this.time = time;

    }

    public String convertTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date = new Date(getTime());
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }

    public String getSender_nickname() {
        return sender_nickname;
    }

    public void setSender_nickname(String sender_nickname) {
        this.sender_nickname = sender_nickname;
    }

    public String getSender_uid() {
        return sender_uid;
    }

    public void setSender_uid(String sender_uid) {
        this.sender_uid = sender_uid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isMine() {
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals( sender_uid) ){
            return true;

        }

        return false;
    }

}
