package com.Attendance.AttendanceApp.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserDetails  extends RealmObject {
    private String username;
    private String address;
    private String university;
    private String password;
    private Class_Names ClassNames;
    @PrimaryKey
    private String id;
    public UserDetails() {
    }

    public UserDetails(String username, String address, String university, String password, String id) {
        this.username = username;
        this.address = address;
        this.university = university;
        this.password = password;
        this.id = id;
    }

    public Class_Names getClassNames() {
        return ClassNames;
    }

    public void setClassNames(Class_Names classNames) {
        ClassNames = classNames;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}