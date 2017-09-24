package com.beacon.entity;

/**
 * Created by LK on 2017/3/2.
 * 热力点,将2m*2m的网格覆盖在地图上
 */
public class HeatPoint {
    double x;
    double y;
    int count;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
