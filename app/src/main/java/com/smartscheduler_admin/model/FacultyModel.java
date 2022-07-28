package com.smartscheduler_admin.model;

import java.io.Serializable;

public class FacultyModel implements Serializable {
    String ID;
    String NAME= "";
    String SUBJECT= "";
    String DEPARTMENT= "";
    String EMAIL= "";
    String MOBILE_NUMBER= "";
    String SEMESTER= "";
    String SHORT_NAME= "";

    public FacultyModel() {
    }

    public FacultyModel(String ID, String NAME, String SUBJECT, String DEPARTMENT, String EMAIL, String MOBILE_NUMBER, String SEMESTER, String SHORT_NAME) {
        this.ID = ID;
        this.NAME = NAME;
        this.SUBJECT = SUBJECT;
        this.DEPARTMENT = DEPARTMENT;
        this.EMAIL = EMAIL;
        this.MOBILE_NUMBER = MOBILE_NUMBER;
        this.SEMESTER = SEMESTER;
        this.SHORT_NAME = SHORT_NAME;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getSUBJECT() {
        return SUBJECT;
    }

    public void setSUBJECT(String SUBJECT) {
        this.SUBJECT = SUBJECT;
    }

    public String getDEPARTMENT() {
        return DEPARTMENT;
    }

    public void setDEPARTMENT(String DEPARTMENT) {
        this.DEPARTMENT = DEPARTMENT;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getMOBILE_NUMBER() {
        return MOBILE_NUMBER;
    }

    public void setMOBILE_NUMBER(String MOBILE_NUMBER) {
        this.MOBILE_NUMBER = MOBILE_NUMBER;
    }

    public String getSEMESTER() {
        return SEMESTER;
    }

    public void setSEMESTER(String SEMESTER) {
        this.SEMESTER = SEMESTER;
    }

    public String getSHORT_NAME() {
        return SHORT_NAME;
    }

    public void setSHORT_NAME(String SHORT_NAME) {
        this.SHORT_NAME = SHORT_NAME;
    }
}
