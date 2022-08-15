package com.teamblank.tgcdboard.model;

public class Admin {
    private String Email,Password;

    public Admin() {
    }

    public Admin(String email, String password) {
        Email = email;
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }
}
