package com.beacon.model;

import javax.persistence.*;

/**
 * Created by LK on 2017/4/5.
 * 展区表(含类别)
 */
@Entity
@Table(name = "exhibition")
public class Exhibition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private int id;


    @Column(name = "device_id")
    private String device_id;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
