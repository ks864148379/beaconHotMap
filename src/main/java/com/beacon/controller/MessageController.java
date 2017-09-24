package com.beacon.controller;

import com.beacon.daoService.MessageService;
import com.beacon.daoService.TaskService;
import com.beacon.model.Message;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by James on 2017/4/18.
 */
@Controller
@RequestMapping("/api")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @ResponseBody
    @RequestMapping("/message/sendMessage")
    public JSONObject sendMessage(String msg, String stuffList){
        JSONObject result = new JSONObject();
//        System.out.println(stuffList);
        result.put("success", messageService.sendMessage(msg, stuffList));
        return result;
    }

    @ResponseBody
    @RequestMapping(value="/message/msgList",method = RequestMethod.GET,produces = "application/json")
    public JSONObject msgList(){
        JSONObject result = new JSONObject();
        result.put("success", messageService.getMsgList());
        List<Message> msgrList = messageService.getMsgList();
        result.put("list",msgrList);
        result.put("success",true);
        result.put("length", msgrList.size());
        return result;
    }
}
