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
    String BLOCK_NUM;
    String FLOOR_NUM;

    public ScheduleModel() {
    }

    public ScheduleModel(String ID, String DAY, String TIMESLOT, String COURSE, String FACULTY, String ROOM, String SEMESTER, String CREDIT_HOUR, String DEPARTMENT, String BLOCK_NUM, String FLOOR_NUM) {
        this.ID = ID;
        this.DAY = DAY;
        this.TIMESLOT = TIMESLOT;
        this.COURSE = COURSE;
        this.FACULTY = FACULTY;
        this.ROOM = ROOM;
        this.SEMESTER = SEMESTER;
        this.CREDIT_HOUR = CREDIT_HOUR;
        this.DEPARTMENT = DEPARTMENT;
        this.BLOCK_NUM = BLOCK_NUM;
        this.FLOOR_NUM = FLOOR_NUM;
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

    public String getBLOCK_NUM() {
        return BLOCK_NUM;
    }

    public void setBLOCK_NUM(String BLOCK_NUM) {
        this.BLOCK_NUM = BLOCK_NUM;
    }

    public String getFLOOR_NUM() {
        return FLOOR_NUM;
    }

    public void setFLOOR_NUM(String FLOOR_NUM) {
        this.FLOOR_NUM = FLOOR_NUM;
    }
}
