package com.teamblank.tgcdboard.model;

public class Syllabus {
    private String sID,sFaculty,sDepartment,sTitle,sDate,sTime,SPDFLink;

    public Syllabus() {
    }

    public Syllabus(String sID, String sFaculty, String sDepartment, String sTitle, String sDate, String sTime, String SPDFLink) {
        this.sID = sID;
        this.sFaculty = sFaculty;
        this.sDepartment = sDepartment;
        this.sTitle = sTitle;
        this.sDate = sDate;
        this.sTime = sTime;
        this.SPDFLink = SPDFLink;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getsFaculty() {
        return sFaculty;
    }

    public void setsFaculty(String sFaculty) {
        this.sFaculty = sFaculty;
    }

    public String getsDepartment() {
        return sDepartment;
    }

    public void setsDepartment(String sDepartment) {
        this.sDepartment = sDepartment;
    }

    public String getsTitle() {
        return sTitle;
    }

    public void setsTitle(String sTitle) {
        this.sTitle = sTitle;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String getSPDFLink() {
        return SPDFLink;
    }

    public void setSPDFLink(String SPDFLink) {
        this.SPDFLink = SPDFLink;
    }
}
