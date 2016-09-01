package com.example.dione.learnchatapp;

/**
 * Created by dione on 01/09/2016.
 */
public class Chat {
    private String user;
    private String message;

    public Chat(String user, String message){
        this.user = user;
        this.message = message;
    }
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
