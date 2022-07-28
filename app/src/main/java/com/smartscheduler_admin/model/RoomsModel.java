package com.smartscheduler_admin.model;

import java.io.Serializable;

public class RoomsModel implements Serializable {
    String ID;
    String ROOM;
    String ROOM_TYPE;

    public RoomsModel() {
    }

    public RoomsModel(String ID, String ROOM, String ROOM_TYPE) {
        this.ID = ID;
        this.ROOM = ROOM;
        this.ROOM_TYPE = ROOM_TYPE;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getROOM() {
        return ROOM;
    }

    public void setROOM(String ROOM) {
        this.ROOM = ROOM;
    }

    public String getROOM_TYPE() {
        return ROOM_TYPE;
    }

    public void setROOM_TYPE(String ROOM_TYPE) {
        this.ROOM_TYPE = ROOM_TYPE;
    }
}
