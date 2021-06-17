package com.example.sha4lny.classes;

public class AcceptedJobs {
    String jobID,ownerUsername,ownerFullName,applicantUserName,applicatnFullName;

    public AcceptedJobs() {
    }

    public AcceptedJobs(String jobID) {
        this.jobID = jobID;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public String getApplicantUserName() {
        return applicantUserName;
    }

    public void setApplicantUserName(String applicantUserName) {
        this.applicantUserName = applicantUserName;
    }

    public String getApplicatnFullName() {
        return applicatnFullName;
    }

    public void setApplicatnFullName(String applicatnFullName) {
        this.applicatnFullName = applicatnFullName;
    }

    public AcceptedJobs(String jobID, String ownerUsername, String ownerFullName, String applicantUserName, String applicatnFullName) {
        this.jobID = jobID;
        this.ownerUsername = ownerUsername;
        this.ownerFullName = ownerFullName;
        this.applicantUserName = applicantUserName;
        this.applicatnFullName = applicatnFullName;
    }
}
