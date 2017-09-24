package com.beacon.entity;

/**
 * Created by LK on 2017/3/20.
 */
public class DeviceIdRssi {
    String device_id;
    int rssi ;

    public String getDevice_id() {
        return device_id;
    }

    public int getRssi() {
        return rssi;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}
