package com.beacon.model;

import javax.persistence.*;

/**
 * Created by James on 2017/6/30.
 */
@Entity
@Table(name = "card_scan")
public class CardScan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private int id;
    @Column (name = "name")
    private String name;
    @Column (name = "title")
    private String title;
    @Column (name = "mobile")
    private String mobile;
    @Column (name = "email")
    private String email;
    @Column (name = "organization")
    private String organization;
    @Column (name = "address")
    private String address;
    @Column (name = "url")
    private String url;
    @Column (name = "tel")
    private String tel;
    @Column (name = "fax")
    private String fax;
    @Column (name = "json_info",nullable = false)
    private String json_info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getJson_info() {
        return json_info;
    }

    public void setJson_info(String json_info) {
        this.json_info = json_info;
    }
}
