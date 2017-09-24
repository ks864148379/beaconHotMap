package com.beacon.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by linhanda on 2016/10/28.
 */



@Entity
@Table(name = "beaconVisitor")
public class BeaconVisitor {


    @Id
    @Column(name = "mac_id")
    private String mac_id;

    @Column(name = "minor")
    private Integer minor;

    @Column(name = "visitor_id",nullable = false)
    private String visitor_id;

    public String getMac_id() {
        return mac_id;
    }

    public void setMac_id(String mac_id) {
        this.mac_id = mac_id;
    }

    public Integer getMinor() {
        return minor;
    }

    public void setMinor(Integer minor) {
        this.minor = minor;
    }

    public String getVisitor_id() {
        return visitor_id;
    }

    public void setVisitor_id(String visitor_id) {
        this.visitor_id = visitor_id;
    }
}
