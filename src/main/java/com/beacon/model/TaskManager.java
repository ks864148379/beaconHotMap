package com.beacon.model;

import net.sf.json.JSONArray;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by James on 2017/4/11.
 */
@Entity
@Table(name = "task_manager")
public class TaskManager {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private int id;
    @Column(name="stuff_id",nullable = false)
    private String stuff_id;
    @Column(name="customer_id",nullable = false)
    private String customer_id;
    @Column(name="task_title",nullable = false)
    private String task_title;
    @Column(name="task_content")
    private String task_content;
    @Column(name="create_time",nullable = false)
    private Timestamp create_time;
    @Column(name="accept_time")
    private Timestamp accept_time;
    @Column(name="finish_time")
    private Timestamp finish_time;
    @Column(name="image")
    private String image;
    @Column(name="audio")
    private String audio;
    @Column(name="state")
    private int state;
    @Column(name="task_description")
    private String task_description;
    @Column(name = "card")
    private String card;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStuffId() {
        return stuff_id;
    }

    public void setStuffId(String stuffId) {
        this.stuff_id = stuffId;
    }

    public String getCustomerId() {
        return customer_id;
    }

    public void setCustomerId(String customerId) {
        this.customer_id = customerId;
    }

    public String getTaskTitle() {
        return task_title;
    }

    public void setTaskTitle(String taskTitle) {
        this.task_title = taskTitle;
    }

    public String getTaskContent() {
        return task_content;
    }

    public void setTaskContent(String taskContent) {
        this.task_content = taskContent;
    }

    public Timestamp getCreateTime() {
        return create_time;
    }

    public void setCreateTime(Timestamp createTime) {
        this.create_time = createTime;
    }

    public Timestamp getAcceptTime() {
        return accept_time;
    }

    public void setAcceptTime(Timestamp acceptTime) {
        this.accept_time = acceptTime;
    }

    public Timestamp getFinishTime() {
        return finish_time;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finish_time = finishTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTask_description() {
        return task_description;
    }

    public void setTask_description(String task_description) {
        this.task_description = task_description;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getCard() {
        return card;
    }
}
