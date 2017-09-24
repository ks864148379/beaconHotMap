package com.beacon.daoService;

import com.beacon.dao.TaskDao;
import com.beacon.model.TaskManager;
import com.beacon.model.Visitor;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Created by James on 2017/4/11.
 */
@Service("taskService")
public class TaskService {
    @Autowired
    TaskDao taskDao;

    public TaskDao getTaskDao() {
        return taskDao;
    }

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public List<Map> getStuffByName(String name){
        return taskDao.getStuffByName(name);
    }

    public List<Map> getCustomerByName(String name) {
        return taskDao.getCustomerByName(name);
    }

    public List<Map> getStuffTaskState(String name){
        return taskDao.getStuffTaskState(name);
    }

    public String getTaskLastTime(){
        return taskDao.getLastTime();
    }

    public boolean addTask(String stuffIds, String customerId, String taskTitle, String taskContent){
        List<TaskManager> taskManagerList = new ArrayList();
        JSONArray stuffList = JSONArray.fromObject(stuffIds);
        for(Object id : stuffList) {
            TaskManager taskManager = new TaskManager();
            taskManager.setStuffId((String)id);
            taskManager.setCustomerId(customerId);
            taskManager.setTaskTitle(taskTitle);
            taskManager.setTaskContent(taskContent);
            taskManager.setCreateTime(new Timestamp(System.currentTimeMillis()));
            taskManagerList.add(taskManager);
        }
//        System.out.println(stuffId+"-"+customerId+"-"+taskTitle+"-"+taskContent);
        try {
            taskDao.addTask(taskManagerList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Map> getTaskByStuffId(String stuffId){
        return taskDao.getTaskByStuffId(stuffId);
    }

    public List<Visitor> getAllStuff(){
        return taskDao.getAllStuff();
    }

}
