package com.beacon.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by linhanda on 2016/10/28.
 */





@Entity
@Table(name = "beacon")
public class Beacon {


    @Id
    @Column(name = "mac_id")
    private String mac_id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "major")
    private int major;

    @Column(name = "minor")
    private int minor;


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
}
