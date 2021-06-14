package com.example.sha4lny.classes;

public class MessageOFIntereset {
    String sender;
    String jobID;
    String jobTitle;
    String reciver;
    String senderFullName;

    public String getSenderFullName() {
        return senderFullName;
    }

    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    public MessageOFIntereset(String sender, String jobID, String jobTitle, String reciver, String senderFullName, String recieverFullName, boolean accepted) {
        this.sender = sender;
        this.jobID = jobID;
        this.jobTitle = jobTitle;
        this.reciver = reciver;
        this.senderFullName = senderFullName;
        this.recieverFullName = recieverFullName;
        this.accepted = accepted;
    }

    public String getRecieverFullName() {
        return recieverFullName;
    }

    public void setRecieverFullName(String recieverFullName) {
        this.recieverFullName = recieverFullName;
    }



    String recieverFullName;
    boolean accepted;

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }




    public MessageOFIntereset() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getJobID() {
        return jobID;
    }

    @Override
    public String toString() {
        return "MessageOFIntereset{" +
                "sender='" + sender + '\'' +
                ", jobID='" + jobID + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", reciver='" + reciver + '\'' +
                '}';
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }
}
