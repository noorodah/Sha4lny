package com.example.sha4lny.classes;

import java.util.ArrayList;

public class User {
    String username,name,password,age,phone,location,job;
    ArrayList<String> Contacts;

    public ArrayList<String> getContacts() {
        return Contacts;
    }

    public void setContacts(ArrayList<String> contacts) {
        Contacts = contacts;
    }
    public  void addContact(String contact){
        Contacts.add(contact);
    }

    public User(String username, String name, String password, String age, String phone, String location, String job, ArrayList<String> contacts) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.age = age;
        this.phone = phone;
        this.location = location;
        this.job = job;
        Contacts = contacts;
    }



    public String getUsername() {
        return username;
    }

    public User() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
