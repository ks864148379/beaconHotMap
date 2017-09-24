package com.beacon.model;

import javax.persistence.*;

/**
 * Created by linhanda on 2016/10/26.
 */


@Entity
@Table(name = "deviceInfo")
public class DeviceInfo {

    @Id
    @Column(name = "device_id",nullable = false)
    private String device_id;

    @Column(name = "position_x",nullable = false)
    private double position_x;

    @Column(name = "position_y",nullable = false)
    private double position_y;

    @Column(name = "building",nullable = false)
    private String building;

    @Column(name = "floor",nullable = false)
    private  String floor;

    @Column(name = "spotName")
    private  String spotName;

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

    @Column(name = "spotId")

    private String spotId;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public double getPosition_x() {
        return position_x;
    }

    public void setPosition_x(double position_x) {
        this.position_x = position_x;
    }

    public double getPosition_y() {
        return position_y;
    }

    public void setPosition_y(double position_y) {
        this.position_y = position_y;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }
}
