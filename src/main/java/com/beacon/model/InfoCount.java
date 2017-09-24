package com.beacon.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by James on 2017/7/18.
 */
@Entity
@Table(name = "beaconinfo_count")
public class InfoCount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "device_id")
    private String device_id;

    @Column(name = "mac_id")
    private String mac_id;

    @Column(name = "collectTime")
    private Timestamp collectTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }
}
