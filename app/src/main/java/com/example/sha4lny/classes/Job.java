package com.example.sha4lny.classes;

public class Job {
    private String postTitle;
    private String jobType;
    private String freeSpot;
    private String workHoursFrom;
    private String workHourseTo;
    private String workTime;
    private String paymentTime;
    private String paymentAmount;
    private String postComment;
    private String jobLoc;
    private String JobID;
    private String jobOwner;


    public String getJobOwnerName() {
        return jobOwnerName;
    }

    public void setJobOwnerName(String jobOwnerName) {
        this.jobOwnerName = jobOwnerName;
    }

    public Job(String postTitle, String jobType, String freeSpot, String workHoursFrom, String workHourseTo, String workTime, String paymentTime, String paymentAmount, String postComment, String jobLoc, String jobID, String jobOwner, String jobOwnerName, boolean accepted) {
        this.postTitle = postTitle;
        this.jobType = jobType;
        this.freeSpot = freeSpot;
        this.workHoursFrom = workHoursFrom;
        this.workHourseTo = workHourseTo;
        this.workTime = workTime;
        this.paymentTime = paymentTime;
        this.paymentAmount = paymentAmount;
        this.postComment = postComment;
        this.jobLoc = jobLoc;
        JobID = jobID;
        this.jobOwner = jobOwner;
        this.jobOwnerName = jobOwnerName;
        this.accepted = accepted;
    }

    private String jobOwnerName;
    boolean accepted;

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }



    public String getJobOwner() {
        return jobOwner;
    }

    public void setJobOwner(String jobOwner) {
        this.jobOwner = jobOwner;
    }



    public String getJobID() {
        return JobID;
    }

    public void setJobID(String jobID) {
        JobID = jobID;
    }

    public String getJobLoc() {
        return jobLoc;
    }

    public void setJobLoc(String jobLoc) {
        this.jobLoc = jobLoc;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getFreeSpot() {
        return freeSpot;
    }

    public void setFreeSpot(String freeSpot) {
        this.freeSpot = freeSpot;
    }

    public String getWorkHoursFrom() {
        return workHoursFrom;
    }

    public void setWorkHoursFrom(String workHoursFrom) {
        this.workHoursFrom = workHoursFrom;
    }

    public String getWorkHourseTo() {
        return workHourseTo;
    }

    public void setWorkHourseTo(String workHourseTo) {
        this.workHourseTo = workHourseTo;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPostComment() {
        return postComment;
    }

    public void setPostComment(String postComment) {
        this.postComment = postComment;
    }

    public Job() {
    }


}
