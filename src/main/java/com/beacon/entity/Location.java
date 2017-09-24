package com.beacon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;

/**
 * Created by LK on 2017/4/12.
 * 人员位置
 */
public class Location {
    @JsonIgnore
    String name;

    String customer_id;
    String mapId;
    String floor;
    Double x;
    Double y;

    @JsonIgnore
    Timestamp time;

    String timeString;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
}
