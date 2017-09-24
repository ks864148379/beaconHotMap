package com.beacon.controller;

import com.beacon.daoService.AppService;
import com.beacon.daoService.BeaconInfoService;
import com.beacon.daoService.HeatMapService;
import com.beacon.daoService.UserService;
import com.beacon.entity.AppResult;
import com.beacon.entity.AppTaskAndVisitor;
import com.beacon.entity.Location;
import com.beacon.model.BeaconInfo;
import com.beacon.model.DyBeaconInfo;
import com.beacon.model.TaskManager;
import com.beacon.model.Visitor;
import com.beacon.utils.Constant;
import com.beacon.utils.JsonTools;
import com.beacon.utils.StringTools;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by LK on 2017/4/14.
 */
@RequestMapping(value = "app")
@Controller
public class AppController {

    public static String IP = "http://123.57.46.160:8080/bupt3/Downloads/";
    //public static  String IP="http://10.103.240.141:8080/Downloads/";
    public static  String MapUrl=IP+ Constant.MAPID+".db";

    @Autowired
    BeaconInfoService beaconInfoService;
    @Autowired
    UserService userService;
    @Autowired
    AppService appService;
    @Autowired
    HeatMapService heatMapService;
    Logger logger = Logger.getLogger(AppController.class);

    @ResponseBody
    @RequestMapping(value = "uploadBeaconInfo",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String UploadBeaconInfo(@RequestBody String param){
        JSONObject obj = JSONObject.fromObject(param);
        JSONArray arr = JSONArray.fromObject(obj.getJSONArray("data"));
        AppResult appResult = new AppResult();
        try {
            List<DyBeaconInfo> list = (List<DyBeaconInfo>)JSONArray.toCollection(arr, DyBeaconInfo.class);
            appResult.setSuccess(appService.addDyBeaconInfoList(list));
        }catch (Exception e){
            appResult.setSuccess(false);
            appResult.setError("内部错误");
        }
        return JsonTools.fromObject(appResult);
    }

    @ResponseBody
    @RequestMapping(value = "login",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public String AppLogin(String visitor_id){
        AppResult appResult = new AppResult();
        if (StringTools.isEmpty(visitor_id)){
            appResult.setError("visitor_id不能为空");
            appResult.setSuccess(false);
            return JsonTools.fromObject(appResult);
        }
        try {
            Visitor visitor =userService.getVisitorById(visitor_id);
            if (visitor==null){
                appResult.setSuccess(false);
                appResult.setError("登录失败");
            } else {
                appResult.setSuccess(true);
                JSONObject object = new JSONObject();
                object.put("mapId",Constant.MAPID+".db");
                object.put("name",visitor.getName());
                object.put("mapUrl",MapUrl);
                appResult.setResult(object);
            }
        }catch (Exception e){
            logger.error("error",e);
            appResult.setSuccess(false);
            appResult.setError("内部错误");
        }
        return JsonTools.fromObject(appResult);
    }

    @ResponseBody
    @RequestMapping(value = "login1",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String AppLogin1(@RequestBody String param){
        JSONObject json = JSONObject.fromObject(param);
        String visitor_id= (String) json.get("visitor_id");
        AppResult appResult = new AppResult();
        if (StringTools.isEmpty(visitor_id)){
            appResult.setError("visitor_id不能为空");
            appResult.setSuccess(false);
            return JsonTools.fromObject(appResult);
        }
        try {
            Visitor visitor =userService.getVisitorById(visitor_id);
            if (visitor==null){
                appResult.setSuccess(false);
                appResult.setError("登录失败");
            }else {
                appResult.setSuccess(true);
                JSONObject object = new JSONObject();
                object.put("mapId",Constant.MAPID+".db");
                object.put("mapUrl", MapUrl);
                appResult.setResult(object);
            }
        }catch (Exception e){
            logger.error("error",e);
            appResult.setSuccess(false);
            appResult.setError("内部错误");
        }
        return JsonTools.fromObject(appResult);
    }
    //获取任务，传参数visitorId+time
    @ResponseBody
    @RequestMapping(value = "getTasksAndCustomers",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String GetTaskByVisitorId(@RequestBody String param ){
        JSONObject json = JSONObject.fromObject(param);
        String visitor_id= (String) json.get("visitor_id");
        String time= (String) json.get("time");
        AppResult appResult = new AppResult();
        if (StringTools.isEmpty(visitor_id) || StringTools.isEmpty(time)){
            appResult.setError("参数不能为空");
            appResult.setSuccess(false);
            return JsonTools.fromObject(appResult);
        }
        try {
            Timestamp timestamp = Timestamp.valueOf(time);
            AppTaskAndVisitor appTaskAndVisitor = new AppTaskAndVisitor();
            appTaskAndVisitor.setTaskList(appService.getTasksByVisitorId(visitor_id,timestamp));
            appTaskAndVisitor.setVisitorList(appService.getCustomersByVisitorId1(visitor_id, timestamp));
            appResult.setResult(appTaskAndVisitor);
            appResult.setSuccess(true);
        }catch (Exception e){
            logger.error("error",e);
            appResult.setSuccess(false);
            appResult.setError("内部错误");
        }
        return JsonTools.fromObject(appResult);
    }

    //获取坐标，传参数visitor_id
    @ResponseBody
    @RequestMapping(value = "getLocation",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String GetLocationByVisitorId(@RequestBody String param ){
        JSONObject json = JSONObject.fromObject(param);
        String visitor_id= (String) json.get("visitor_id");
        String floor = appService.getVisitorFloorByVisitorId(visitor_id);
        AppResult appResult = new AppResult();
        if (StringTools.isEmpty(visitor_id)){
            appResult.setError("参数不能为空");
            appResult.setSuccess(false);
            return JsonTools.fromObject(appResult);
        }
        try {
            JSONObject jsonObject = new JSONObject();
            appResult.setSuccess(true);
            Location location = heatMapService.getLocationByVisitorId(visitor_id);
            if (location!=null){
                location.setMapId(Constant.MAPID);
                location.setFloor(floor);
            }
            jsonObject.put("location", location);
            jsonObject.put("floor", floor);
            appResult.setResult(jsonObject);
        }catch (Exception e){
            logger.error("error",e);
            appResult.setSuccess(false);
            appResult.setError("内部错误");
        }
        return JsonTools.fromObject(appResult);
    }

    //获取周边坐标(APP传的mac_ids)
    @ResponseBody
    @RequestMapping(value = "getLocationList",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String GetLocationList(@RequestBody String param ){
        JSONObject json = JSONObject.fromObject(param);
        JSONArray mac_ids= json.getJSONArray("mac_id");
        String visitorId = json.getString("visitor_id");
        String floor = appService.getVisitorFloorByVisitorId(visitorId);
        List<Location> locationList = new ArrayList<>();
        AppResult appResult = new AppResult();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if (mac_ids.size()<=0){
            appResult.setError("参数不能为空");
            appResult.setSuccess(false);
            return JsonTools.fromObject(appResult);
        }
        try {
            for (int i=0;i<mac_ids.size();i++){
                String mac_id= (String) mac_ids.get(i);
                String curFloor = appService.getVisitorFloorByMacId(mac_id);
                if (!curFloor.equals(floor)) {
                    continue;
                }
                String visitor_id = appService.getVisitorIdByMacId(mac_id);
                Location location = heatMapService.getLocationByVisitorId(visitor_id);
                if (location!=null){
                    location.setMapId(Constant.MAPID);
                    location.setFloor(floor);
                    location.setCustomer_id(visitor_id);
                }
                locationList.add(location);
                jsonArray.add(location);
            }
            appResult.setSuccess(true);
            jsonObject.put("location", jsonArray);
            jsonObject.put("floor", floor);
            appResult.setResult(jsonObject);
        }catch (Exception e){
            logger.error("error",e);
            appResult.setSuccess(false);
            appResult.setError("内部错误");
        }
        return JsonTools.fromObject(appResult);
    }

    //获取message,传参数visitor_id,time
    @ResponseBody
    @RequestMapping(value = "getMessage",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String GetMessageListByVisitorId(@RequestBody String param ){
        JSONObject json = JSONObject.fromObject(param);
        String visitor_id= (String) json.get("visitor_id");
        String time= (String) json.get("time");
        AppResult appResult = new AppResult();
        if (StringTools.isEmpty(visitor_id) || StringTools.isEmpty(time)){
            appResult.setSuccess(false);
            appResult.setError("参数不能为空");
            return JsonTools.fromObject(appResult);
        }
        try {
            Timestamp timestamp = Timestamp.valueOf(time);
            appResult.setSuccess(true);
            //JSONObject object = new JSONObject();
            //object.put("messageList",appService.getMessageByVisitorId(visitor_id,timestamp));
            appResult.setResult(appService.getMessageByVisitorId(visitor_id, timestamp));
        }catch (Exception e){
            logger.error("error", e);
            appResult.setSuccess(false);
            appResult.setError("内部错误");
        }
        return JsonTools.fromObject(appResult);
    }

    //上传task信息(text,audio,image)
    @Transactional
    @ResponseBody
    @RequestMapping(value = "uploadTaskInfo",method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    public String uploadTaskInfo(MultipartHttpServletRequest request){
        AppResult appResult=new AppResult();
        try {
            List<MultipartFile> imageList = request.getFiles("image");
            List<MultipartFile> audioList=request.getFiles("audio");
            List<MultipartFile> cardList=request.getFiles("card");//名片
            String customer_id=request.getParameter("visitor_id");
            String staff_id = request.getParameter("staff_id");
            String task_text=request.getParameter("text");
            TaskManager taskManager=appService.getTaskManagerByStuffIdAndCustomerId(staff_id, customer_id);
            if (taskManager!=null){
                JSONArray imageArray=appService.writeImageToDisk(imageList);
                JSONArray audioArray=appService.writeAudioToDisk(audioList);
                JSONArray cardArray=appService.writeCardToDisk(cardList);
                if (taskManager.getTask_description()==null || taskManager.getTask_description().equals("")){
                    taskManager.setTask_description(task_text);
                }else {
                    taskManager.setTask_description(joinJSONArray(JSONArray.fromObject(taskManager.getTask_description()),JSONArray.fromObject(task_text)));
                }
                if (taskManager.getImage()==null || taskManager.getImage().equals("")){
                    taskManager.setImage(imageArray.toString());
                }else {
                    taskManager.setImage(joinJSONArray(JSONArray.fromObject(taskManager.getImage()), imageArray));
                }
                if (taskManager.getCard()==null || taskManager.getCard().equals("")){
                    taskManager.setCard(cardArray.toString());
                }else {
                    taskManager.setCard(joinJSONArray(JSONArray.fromObject(taskManager.getCard()), cardArray));
                }
                if (taskManager.getAudio()==null || taskManager.getAudio().equals("")){
                    taskManager.setAudio(audioArray.toString());
                }else {
                    taskManager.setAudio(joinJSONArray(JSONArray.fromObject(taskManager.getAudio()), audioArray));
                }
                appService.updateTaskManage(taskManager);
            }
            appResult.setSuccess(true);
            System.out.println(getTime() + "上传任务：" + customer_id);
        }catch (Exception e){
            logger.error("error",e);
            appResult.setSuccess(false);
            appResult.setError("内部错误");
        }
        return JsonTools.fromObject(appResult);
    }

    @ResponseBody
    @RequestMapping(value = "test",method = RequestMethod.GET)
    public String Test(String stuff_id,String customer_id){
        AppResult appResult = new AppResult();
        TaskManager taskManager=appService.getTaskManagerByStuffIdAndCustomerId(stuff_id, customer_id);
        appResult.setSuccess(true);
        appResult.setResult(taskManager);
        return JsonTools.fromObject(appResult);
    }

    //蓝牙网关上传beacon信息接口
    @ResponseBody
    @RequestMapping(value="/{gatewayMac}/status",method = RequestMethod.POST)
    public String uploadBeaconInfo(@PathVariable String gatewayMac,@RequestBody String param){
        AppResult appResult=new AppResult();
        System.out.println(gatewayMac + " " + param);
        if (StringTools.isEmpty(gatewayMac)){
            appResult.setSuccess(false);
            appResult.setError("参数不能为空");
            return JsonTools.fromObject(appResult);
        }
        try {
            JSONArray jsonArray = JSONArray.fromObject(param);
            List<BeaconInfo> beaconInfoList=new ArrayList<>();
            for (int i=0;i<jsonArray.size();i++){
                JSONObject json=jsonArray.getJSONObject(i);
                if (json.containsKey("age")){
                    continue;
                }else {
                    BeaconInfo beaconInfo=new BeaconInfo();
                    beaconInfo.setDevice_id(gatewayMac);
                    beaconInfo.setMac_id((String) json.get("mac"));
                    beaconInfo.setUuid((String) json.get("ibeaconUuid"));
                    beaconInfo.setMajor((Integer) json.get("ibeaconMajor"));
                    beaconInfo.setMinor((Integer) json.get("ibeaconMinor"));
                    //过滤掉major和minor号不为0的
                    if ((Integer) json.get("ibeaconMajor") != 0 || (Integer) json.get("ibeaconMinor") != 0) {
                        continue;
                    }
                    beaconInfo.setRssi((Integer) json.get("rssi"));
                    beaconInfo.setCollectTime(Timestamp.valueOf(transTime1((String) json.get("timestamp"))));
                    beaconInfoList.add(beaconInfo);
                }
            }
            appResult.setSuccess(beaconInfoService.inputBeaconInfoList(beaconInfoList));
        }catch (Exception e){
            logger.error("error",e);
            appResult.setSuccess(false);
            appResult.setError("内部错误");
        }
        return JsonTools.fromObject(appResult);
    }

    //讯通BLE网关上传Beacon
    @ResponseBody
    @RequestMapping(value="/ble/postdata",method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    public String uploadBLEBeaconInfo(HttpServletRequest request){
        AppResult appResult=new AppResult();
        try {
            String param = request.getParameter("beacons");
            //System.out.println(param);
            JSONArray jsonArray = JSONArray.fromObject(param);
            List<BeaconInfo> beaconInfoList=new ArrayList<>();
            for (int i=0;i<jsonArray.size();i++){
                JSONObject json=jsonArray.getJSONObject(i);
                BeaconInfo beaconInfo=new BeaconInfo();
                beaconInfo.setDevice_id((String) json.get("gatewaymac"));
                beaconInfo.setMac_id((String) json.get("devicemac"));
                beaconInfo.setRssi((Integer) json.get("rssi"));
                beaconInfo.setCollectTime(Timestamp.valueOf(getTime()));
                beaconInfoList.add(beaconInfo);
            }
            appResult.setSuccess(beaconInfoService.inputBeaconInfoList(beaconInfoList));
        }catch (Exception e){
            logger.error("error", e);
            appResult.setSuccess(false);
            appResult.setError("内部错误");
        }
        return JsonTools.fromObject(appResult);
    }


    public static String joinJSONArray(JSONArray mData, JSONArray array) {
        for (int i=0;i<array.size();i++){
            mData.add(array.get(i));
        }
        return mData.toString();
    }

    public String getTime(){
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  sdf.format(new Date());
    }

    //蓝牙网关上报时间+8时
    public String transTime(String time){
        Instant instant=Instant.parse(time);
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        String date = ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return date;
    }

    public  String transTime1(String time){
        String s1=time.substring(0, time.indexOf("T"));
        String s2=time.substring(time.indexOf("T") + 1, time.indexOf("Z"));
        Date date = string2Date(s1 + " " + s2, "yyyy-MM-dd HH:mm:ss");
        int inteval=8*60*60*1000;
        Long time1=date.getTime()+inteval;
        return getStringOfTime(time1);
    }

    public  String getStringOfTime(long time){
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(time));
    }

    public  Date string2Date(String strDate,String patten) {
        DateFormat format = new SimpleDateFormat(patten);
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}
