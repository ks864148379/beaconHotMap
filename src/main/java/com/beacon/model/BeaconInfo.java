package com.beacon.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by linhanda on 2016/10/12.
 */

@Entity
@Table(name = "beaconInfo")
public class BeaconInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;


    @Column(name = "device_id")
    private String device_id;

    @Column(name = "mac_id")
    private String mac_id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "major")
    private int major;

    @Column(name = "minor")
    private int minor;

    @Column(name = "rssi")
    private int rssi;

    @Column(name = "distance")
    private float distance;

    @Column(name = "collectTime")
    private Timestamp collectTime;
    @Column(name = "collectTimeString")
    private String collectTimeString;


    @Column(name = "create_time")
    private Timestamp create_time;

    @Column(name = "flag")
    private int flag;

    @Transient
    public String getCollectTimeString() {
        return collectTimeString;
    }

    public void setCollectTimeString(String collectTimeString) {
        this.collectTimeString = collectTimeString;
        this.collectTime=Timestamp.valueOf(collectTimeString);
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getMac_id() {
        return mac_id;
    }

    public void setMac_id(String mac_id) {
        this.mac_id = mac_id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }
}
