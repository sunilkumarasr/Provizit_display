package com.provizit.provizitscheduler.Utilities;

import java.io.Serializable;

public class Room implements Serializable {
    private String name;
    private String floor;
    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
