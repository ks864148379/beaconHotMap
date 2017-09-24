package com.beacon.controller;

import com.beacon.daoService.TaskService;
import com.beacon.model.TaskManager;
import com.beacon.model.Visitor;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2017/4/11.
 */
@Controller
@RequestMapping("/api")
public class TaskManagerController {

    @Autowired
    private TaskService taskService;
    public static String jsonArrayAnalyzeKey = "data";

    //测试接口
    @RequestMapping("/tt")
    @ResponseBody
    public JSONObject test(){
        JSONObject json = new JSONObject();

        json.put("aaa",true);
        return json;
    }

    @ResponseBody
    @RequestMapping(value="/task/searchStuff", method = RequestMethod.POST,produces = "application/json")
    public JSONObject searchStuff(String param){
        //System.out.println(param);
        JSONObject result = new JSONObject();
        List<Map> visitorList = taskService.getStuffByName(param);
        //System.out.println("vistors:"+visitorList);
        result.put("list",visitorList);
        result.put("success",true);
        result.put("length", visitorList.size());
        return result;
    }

    @ResponseBody
    @RequestMapping(value="/task/searchCustomer", method = RequestMethod.POST,produces = "application/json")
    public JSONObject searchCustomer(String param){
        //System.out.println(param);
        JSONObject result = new JSONObject();
        List<Map> visitorList = taskService.getCustomerByName(param);
        //System.out.println("vistors:"+visitorList);
        result.put("list",visitorList);
        result.put("success",true);
        result.put("length", visitorList.size());
        return result;
    }

    @ResponseBody
    @RequestMapping(value="/task/searchStuffTaskState", method = RequestMethod.POST,produces = "application/json")
    public JSONObject searchStuffTaskState(String param){
        JSONObject result = new JSONObject();
        List<Map> visitorList = taskService.getStuffTaskState(param);
        for (int i = 0; i < visitorList.size(); i++) {
            Map visitor = visitorList.get(i);
            if(visitor.get("state") == null){
                visitor.put("state","");
            }
            if(visitor.get("create_time") == null){
                visitor.put("create_time","");
            }
        }
//        System.out.println(visitorList);
        result.put("list",visitorList);
        result.put("success",true);
        result.put("length", visitorList.size());
        return result;
    }

    @RequestMapping("/task/getTaskLastTime")
    @ResponseBody
    public JSONObject getTaskLastTime(){
        JSONObject json = new JSONObject();
        String ts = taskService.getTaskLastTime();
        if(ts != null){
            json.put("success",true);
            json.put("time",ts);
        }
        return json;
    }

    @ResponseBody
    @RequestMapping("/task/submitTask")
    public JSONObject sendMessage(String stuff_id, String customer_id, String task_title, String task_content){
        JSONObject result = new JSONObject();
        result.put("success", taskService.addTask(stuff_id,customer_id,task_title,task_content));
        return result;
    }

    @ResponseBody
    @RequestMapping("/task/getTaskByStuffId")
    public JSONObject getTaskByStuffId(String stuff_id){
        JSONObject result = new JSONObject();
        List<Map> taskList = taskService.getTaskByStuffId(stuff_id);
        System.out.println(taskList.toString());
        for (int i = 0; i < taskList.size(); i++) {
            Map task = taskList.get(i);
            if(task.get("t_image") == null){
                task.put("t_image","");
            }
            if(task.get("audio") == null){
                task.put("audio","");
            }
            if(task.get("task_description") == null){
                task.put("task_description","");
            }
        }
        result.put("list",taskList);
        result.put("success",true);
        result.put("length",taskList.size());
        return result;
    }

}
