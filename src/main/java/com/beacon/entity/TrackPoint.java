package com.beacon.entity;

/**
 * Created by LK on 2017/3/16.
 * 轨迹点
 */
public class TrackPoint {
    Double x;
    Double y;
    long time;//后来算时间加上的字段

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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
