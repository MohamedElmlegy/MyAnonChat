package com.example.myanonchat.model;

import androidx.lifecycle.MutableLiveData;

public class UserAccount {

    private String email ;
    private String password;
    private String nickname;


    private MutableLiveData<Boolean> privileged;

    public UserAccount(String email, String password, String nickname,  boolean privileged) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.privileged.setValue(privileged);
    }

    public UserAccount() {
    }



    public MutableLiveData<Boolean> isPrivileged() {

        return privileged;
    }

    public void setPrivileged(boolean privileged) {
        this.privileged.setValue(privileged);
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
