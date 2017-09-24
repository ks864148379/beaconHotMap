package com.beacon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.internal.NotNull;

import javax.persistence.*;

/**
 * Created by linhanda on 2016/10/31.
 */


@Entity
@Table(name = "visitor")
public class Visitor {

    @Id
    @Column (name = "visitor_id",nullable = false)
    private String visitor_id;

    @Transient
    private String mac_id;

    @Column (name = "name",nullable = false)
    private String name;

    @JsonIgnore
    @Column(name = "password",nullable = false)
    private String password;

    @JsonIgnore
    @Column(name = "role")
    private String role;

    @Column (name = "corporation")
    private String corporation;

    @Column (name = "position")
    private String position;

    @Column (name = "province")
    private String province;

    @Column (name = "city")
    private String city;

    @Column (name = "gender",nullable = false)
    private Integer gender;

    @Column (name = "tel")
    private String tel;

    @Column (name = "email")
    private String email;

    @Column (name = "country")
    private String country;

    @JsonIgnore
    @Column(name = "type_id")
    private String type_id;

    @JsonIgnore
    @Column(name = "image")
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getVisitor_id() {
        return visitor_id;
    }

    public void setVisitor_id(String visitor_id) {
        this.visitor_id = visitor_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCorporation() {
        return corporation;
    }

    public void setCorporation(String corporation) {
        this.corporation = corporation;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMac_id() {
        return mac_id;
    }

    public void setMac_id(String mac_id) {
        this.mac_id = mac_id;
    }
}
