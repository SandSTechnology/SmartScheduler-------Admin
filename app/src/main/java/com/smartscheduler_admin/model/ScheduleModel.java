package com.smartscheduler_admin.model;

import java.io.Serializable;

public class ScheduleModel implements Serializable {
    String ID;
    String DAY;
    String TIMESLOT;
    String COURSE;
    String FACULTY;
    String ROOM;
    String SEMESTER;
    String CREDIT_HOUR;
    String DEPARTMENT;

    public ScheduleModel() {
    }

    public ScheduleModel(String ID, String DAY, String timeSLOT, String COURSE, String FACULTY, String ROOM, String SEMESTER, String CREDIT_HOUR, String DEPARTMENT) {
        this.ID = ID;
        this.DAY = DAY;
        TIMESLOT = timeSLOT;
        this.COURSE = COURSE;
        this.FACULTY = FACULTY;
        this.ROOM = ROOM;
        this.SEMESTER = SEMESTER;
        this.CREDIT_HOUR = CREDIT_HOUR;
        this.DEPARTMENT = DEPARTMENT;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDAY() {
        return DAY;
    }

    public void setDAY(String DAY) {
        this.DAY = DAY;
    }

    public String getTIMESLOT() {
        return TIMESLOT;
    }

    public void setTIMESLOT(String TIMESLOT) {
        this.TIMESLOT = TIMESLOT;
    }

    public String getCOURSE() {
        return COURSE;
    }

    public void setCOURSE(String COURSE) {
        this.COURSE = COURSE;
    }

    public String getFACULTY() {
        return FACULTY;
    }

    public void setFACULTY(String FACULTY) {
        this.FACULTY = FACULTY;
    }

    public String getROOM() {
        return ROOM;
    }

    public void setROOM(String ROOM) {
        this.ROOM = ROOM;
    }

    public String getSEMESTER() {
        return SEMESTER;
    }

    public void setSEMESTER(String SEMESTER) {
        this.SEMESTER = SEMESTER;
    }

    public String getCREDIT_HOUR() {
        return CREDIT_HOUR;
    }

    public void setCREDIT_HOUR(String CREDIT_HOUR) {
        this.CREDIT_HOUR = CREDIT_HOUR;
    }

    public String getDEPARTMENT() {
        return DEPARTMENT;
    }

    public void setDEPARTMENT(String DEPARTMENT) {
        this.DEPARTMENT = DEPARTMENT;
    }
}
