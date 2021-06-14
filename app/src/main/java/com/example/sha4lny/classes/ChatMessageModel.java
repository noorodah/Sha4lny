package com.example.sha4lny.classes;

import android.app.Activity;
import android.content.Context;

public class ChatMessageModel {
    String sender;
    String message;
    String interestedJobTitle;
    Context currentAct;

    public Context getCurrentAct() {
        return currentAct;
    }

    public void setCurrentAct(Context currentAct) {
        this.currentAct = currentAct;
    }

    public ChatMessageModel(String interestedJobTitle, String interestedJobID, int viewType, String sender, Context currentAct) {
        this.currentAct=currentAct;
        this.interestedJobTitle = interestedJobTitle;
        this.interestedJobID = interestedJobID;
        this.sender=sender;
        this.viewType = viewType;
    }

    String interestedJobID ;
    public static final int LayoutOne = 0;
    public static final int LayoutTwo = 1;
    public static final int LayoutThree = 2;
    // This variable ViewType specifies
    // which out of the two layouts
    // is expected in the given item
    private int viewType;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getInterestedJobTitle() {
        return interestedJobTitle;
    }

    public void setInterestedJobTitle(String interestedJobTitle) {
        this.interestedJobTitle = interestedJobTitle;
    }

    public String getInterestedJobID() {
        return interestedJobID;
    }

    public void setInterestedJobID(String interestedJobID) {
        this.interestedJobID = interestedJobID;
    }

    public static int getLayoutOne() {
        return LayoutOne;
    }

    public static int getLayoutTwo() {
        return LayoutTwo;
    }

    public static int getLayoutThree() {
        return LayoutThree;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public ChatMessageModel(int viewType, String sender, String message) {
        this.sender = sender;
        this.viewType=viewType;
        this.message = message;

    }
}
