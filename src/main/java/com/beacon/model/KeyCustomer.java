package com.beacon.model;

import javax.persistence.*;

/**
 * Created by LK on 2017/4/18.
 */
@Entity
@Table(name = "keyCustomer")
public class KeyCustomer {

    @Id
    @Column(name = "visitor_id",nullable = false)
    private String visitor_id;

    @Transient
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVisitor_id() {
        return visitor_id;
    }

    public void setVisitor_id(String visitor_id) {
        this.visitor_id = visitor_id;
    }
}
