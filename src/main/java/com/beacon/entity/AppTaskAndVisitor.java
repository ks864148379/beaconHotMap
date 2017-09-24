package com.beacon.entity;

import com.beacon.model.Visitor;

import java.util.List;

/**
 * Created by LK on 2017/4/19.
 */
public class AppTaskAndVisitor {
    List<AppTask> taskList;
    List<Visitor> visitorList;

    public List<AppTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<AppTask> taskList) {
        this.taskList = taskList;
    }

    public List<Visitor> getVisitorList() {
        return visitorList;
    }

    public void setVisitorList(List<Visitor> visitorList) {
        this.visitorList = visitorList;
    }
}
