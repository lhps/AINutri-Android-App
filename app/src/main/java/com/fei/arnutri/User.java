package com.fei.arnutri;

import java.util.Date;

public class User {

    private String name;

    private String gender;

    private String email;

    private String birth_date;

    private String password;

    private String medicalRegister;

    private String userType;

    public User(String name, String gender, String email, String birth_date, String password, String medicalRegister, String userType) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.birth_date = birth_date;
        this.password = password;
        this.medicalRegister = medicalRegister;
        this.userType = userType;
    }

    public User(String userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public String getPassword() {
        return password;
    }

    public String getMedicalRegister() {
        return medicalRegister;
    }

    public String getUserType() {
        return userType;
    }
}
