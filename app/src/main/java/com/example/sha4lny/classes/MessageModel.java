package com.example.sha4lny.classes;

public class MessageModel {
    private String userName;
    private String fullName;
    private String lastMessage;

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    private String reciver;
    boolean approved;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public MessageModel() {
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public MessageModel(String userName, String fullName, String lastMessage) {
        this.userName = userName;
        this.fullName = fullName;
        this.lastMessage = lastMessage;
        this.approved = false;
    }
}
