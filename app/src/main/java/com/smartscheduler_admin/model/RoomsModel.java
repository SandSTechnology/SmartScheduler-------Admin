package com.smartscheduler_admin.model;

import java.io.Serializable;

public class RoomsModel implements Serializable {
    String ID;
    String ROOM;
    String ROOM_TYPE;
    String BLOCK_NUM;
    String FLOOR_NUM;

    public RoomsModel() {
    }

    public RoomsModel(String ID, String ROOM, String ROOM_TYPE, String BLOCK_NUM, String FLOOR_NUM) {
        this.ID = ID;
        this.ROOM = ROOM;
        this.ROOM_TYPE = ROOM_TYPE;
        this.BLOCK_NUM = BLOCK_NUM;
        this.FLOOR_NUM = FLOOR_NUM;
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
