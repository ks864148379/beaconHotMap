package com.beacon.entity;

/**
 * Created by LK on 2017/4/6.
 * 调地图接口
 */
public class FloorInfo {
    String unit_name;//place名称
    String floor_id;//楼层名称
    int floor_num;//楼层号
    String floor_chn;//楼层中文名称
    String floor_brief;//楼层简称
    String floor_alias;//楼层别名
    String description;//楼层描述

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(String floor_id) {
        this.floor_id = floor_id;
    }

    public int getFloor_num() {
        return floor_num;
    }

    public void setFloor_num(int floor_num) {
        this.floor_num = floor_num;
    }

    public String getFloor_chn() {
        return floor_chn;
    }

    public void setFloor_chn(String floor_chn) {
        this.floor_chn = floor_chn;
    }

    public String getFloor_brief() {
        return floor_brief;
    }

    public void setFloor_brief(String floor_brief) {
        this.floor_brief = floor_brief;
    }

    public String getFloor_alias() {
        return floor_alias;
    }

    public void setFloor_alias(String floor_alias) {
        this.floor_alias = floor_alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
