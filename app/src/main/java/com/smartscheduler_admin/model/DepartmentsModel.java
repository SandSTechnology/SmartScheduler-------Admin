package com.smartscheduler_admin.model;

import java.io.Serializable;

public class DepartmentsModel implements Serializable {
    String ID;
    String DEPARTMENT;

    public DepartmentsModel() {
    }

    public DepartmentsModel(String ID, String DEPARTMENT) {
        this.ID = ID;

        this.DEPARTMENT = DEPARTMENT;

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDEPARTMENT() {
        return DEPARTMENT;
    }

    public void setDEPARTMENT(String DEPARTMENT) {
        this.DEPARTMENT = DEPARTMENT;
    }
}
