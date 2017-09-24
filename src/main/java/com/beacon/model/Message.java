package com.beacon.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by LK on 2017/4/5.
 * 通知消息表
 */
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private int id;

    @Column(name="stuff_id",nullable = false)
    private String stuffId;

    @Column(name = "content",nullable = false)
    private String content;

    @Column(name = "create_time",nullable = false)
    private Timestamp createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getStuffId() {
        return stuffId;
    }

    public void setStuffId(String stuffId) {
        this.stuffId = stuffId;
    }
}
