package com.teamblank.tgcdboard.model;

public class Notice {
    private String nID,Title,Details,Date,Time,Image;

    public Notice() {
    }

    public Notice(String nID, String title, String details, String date, String time, String image) {
        this.nID = nID;
        Title = title;
        Details = details;
        Date = date;
        Time = time;
        Image = image;
    }

    public String getnID() {
        return nID;
    }

    public void setnID(String nID) {
        this.nID = nID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
