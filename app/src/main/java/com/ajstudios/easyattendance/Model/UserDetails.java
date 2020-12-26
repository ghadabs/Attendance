package com.ajstudios.easyattendance.Model;

public class UserDetails {
    private String username;
    private String address;
    private String university;

    public UserDetails() {
    }

    public UserDetails(String username, String addess, String university) {
        this.username = username;
        this.address = address;
        this.university = university;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }
}