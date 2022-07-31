package com.smartscheduler_admin.model;

import java.io.Serializable;

public class CourseModel implements Serializable {
    String ID;
    String COURSE_NAME;
    String CREDIT_HOUR;
    String DEPARTMENT;
    String SEMESTER;
    String FACULTY_ID;
    String FACULTY_NAME;

    public CourseModel() {
    }

    public CourseModel(String ID, String COURSE_NAME, String CREDIT_HOUR, String DEPARTMENT, String SEMESTER, String FACULTY_ID, String FACULTY_NAME) {
        this.ID = ID;
        this.COURSE_NAME = COURSE_NAME;
        this.CREDIT_HOUR = CREDIT_HOUR;
        this.DEPARTMENT = DEPARTMENT;
        this.SEMESTER = SEMESTER;
        this.FACULTY_ID = FACULTY_ID;
        this.FACULTY_NAME = FACULTY_NAME;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCOURSE_NAME() {
        return COURSE_NAME;
    }

    public void setCOURSE_NAME(String COURSE_NAME) {
        this.COURSE_NAME = COURSE_NAME;
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

    public String getSEMESTER() {
        return SEMESTER;
    }

    public void setSEMESTER(String SEMESTER) {
        this.SEMESTER = SEMESTER;
    }

    public String getFACULTY_ID() {
        return FACULTY_ID;
    }

    public void setFACULTY_ID(String FACULTY_ID) {
        this.FACULTY_ID = FACULTY_ID;
    }

    public String getFACULTY_NAME() {
        return FACULTY_NAME;
    }

    public void setFACULTY_NAME(String FACULTY_NAME) {
        this.FACULTY_NAME = FACULTY_NAME;
    }
}
