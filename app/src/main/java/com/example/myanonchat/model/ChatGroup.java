package com.example.myanonchat.model;

public class ChatGroup {

    private String name ;
    private String password ;
    private boolean is_locked ;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIs_locked() {
        return is_locked;
    }

    public void setIs_locked(boolean is_locked) {
        this.is_locked = is_locked;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ChatGroup(String name, String password) {
        this.name = name;
        this.password = password;
        this.is_locked = !password.isEmpty();
    }

    /*
    public ChatGroup(String name) {

        this.name = name;
    }

     */
}
