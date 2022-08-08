package com.example.test.Models;

public class Message {
    String name;
    String text;
    String sentByUserUid;
    String time;


    public Message() {
    }

    public Message(String name, String uid, String text, String s) {
        this.name = name;
        this.sentByUserUid = uid;
        this.text = text;
        this.time = s;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return time;
    }

    public String getSentByUserUid() {
        return sentByUserUid;
    }

}
