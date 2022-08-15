package com.teamblank.tgcdboard.model;

public class Teacher {
    private String tID,tName,tDesignation,tSearch,tEmail,tPhone,tImage;

    public Teacher() {
    }

    public Teacher(String tID, String tName, String tDesignation, String tSearch, String tEmail, String tPhone, String tImage) {
        this.tID = tID;
        this.tName = tName;
        this.tDesignation = tDesignation;
        this.tSearch = tSearch;
        this.tEmail = tEmail;
        this.tPhone = tPhone;
        this.tImage = tImage;
    }

    public String gettID() {
        return tID;
    }

    public void settID(String tID) {
        this.tID = tID;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public String gettDesignation() {
        return tDesignation;
    }

    public void settDesignation(String tDesignation) {
        this.tDesignation = tDesignation;
    }

    public String gettSearch() {
        return tSearch;
    }

    public void settSearch(String tSearch) {
        this.tSearch = tSearch;
    }

    public String gettEmail() {
        return tEmail;
    }

    public void settEmail(String tEmail) {
        this.tEmail = tEmail;
    }

    public String gettPhone() {
        return tPhone;
    }

    public void settPhone(String tPhone) {
        this.tPhone = tPhone;
    }

    public String gettImage() {
        return tImage;
    }

    public void settImage(String tImage) {
        this.tImage = tImage;
    }
}
