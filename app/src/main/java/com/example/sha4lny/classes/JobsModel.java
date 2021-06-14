package com.example.sha4lny.classes;

public class JobsModel {
    private int jobImg;
    private String jobTitle,jobType,offeredJob;

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getOfferedJob() {
        return offeredJob;
    }

    public void setOfferedJob(String offeredJob) {
        this.offeredJob = offeredJob;
    }

    public int getJobImg() {
        return jobImg;
    }

    public void setJobImg(int jobImg) {
        this.jobImg = jobImg;
    }

    public JobsModel(int jobImg, String jobTitle, String jobType, String offeredJob) {
        this.jobImg = jobImg;
        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.offeredJob = offeredJob;
    }
}
