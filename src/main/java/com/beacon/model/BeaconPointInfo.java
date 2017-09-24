package com.beacon.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by ks on 2017/7/3.
 */

@Entity
@Table(name = "beaconpoint")
public class BeaconPointInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "mac_id")
    private String mac_id;

    @Column(name = "x")
    private double x;

    @Column(name = "y")
    private double y;

    @Column(name = "floor")
    private String floor;

    @Column(name = "time")
    private Timestamp time;

    @Transient
    public String getMac_id() {
        return mac_id;
    }

    public void setMac_id(String mac_id) {
        this.mac_id = mac_id;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp collectTime) {
        this.time = collectTime;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
