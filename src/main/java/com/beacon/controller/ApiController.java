package com.beacon.controller;

import com.beacon.dao.HeatMapDao;
import com.beacon.daoService.BeaconInfoService;
import com.beacon.daoService.DeviceInfoService;
import com.beacon.daoService.HeatMapService;
import com.beacon.entity.HeatPoint;
import com.beacon.entity.Location;
import com.beacon.entity.OriginData;
import com.beacon.entity.TrackPoint;
import com.beacon.model.*;
import com.beacon.utils.HttpClientUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by LK on 2017/03/26.
 */
@Controller
@RequestMapping("/api")
public class ApiController {


    private static Logger logger = Logger.getLogger(ApiController.class);
    public  static String requestHost = "http://10.103.240.246:8080";
    public  static String returnHost = "http://10.103.242.183:8080";



    public  static String jsonArrayAnalyzeKey = "data";

    @Autowired
    private BeaconInfoService beaconInfoService;

    @Autowired
    private DeviceInfoService deviceInfoService;

    @Autowired
    private HeatMapService heatMapService;

    @Autowired
    private HeatMapDao heatMapDao;

    //测试接口
    @RequestMapping("/test")
    @ResponseBody
    public JSONObject test(){


        System.out.println(heatMapDao);
        JSONObject json = new JSONObject();

        json.put("aaa",true);
        return json;
    }



    //基站上传获取beaconInfo信息也就是路径信息
    @ResponseBody
    @RequestMapping(value = "/beaconInfo/upload", method = RequestMethod.POST,produces = "application/json")
    public JSONObject beaconInfoUpload(HttpServletRequest request,@RequestBody String param) {


        System.out.println(param);


        JSONObject obj = JSONObject.fromObject(param);
        JSONArray arr = JSONArray.fromObject(obj.getJSONArray(jsonArrayAnalyzeKey));

        List<BeaconInfo> list = (List<BeaconInfo>)JSONArray.toCollection(arr, BeaconInfo.class);


        JSONObject result = new JSONObject();
        result.put("success",beaconInfoService.inputBeaconInfoList(list));
        return result;
    }

    //配置beacon的mac_id和minor值的绑定接口
    @ResponseBody
    @RequestMapping(value = "/beacon/bindingMacIdAndMinor",method = RequestMethod.POST,produces = "application/json")
    public JSONObject beaconUpload(HttpServletRequest request,@RequestBody String param) {

        System.out.println(param);
        JSONObject obj = JSONObject.fromObject(param);
        JSONArray arr = JSONArray.fromObject(obj.getJSONArray(jsonArrayAnalyzeKey));
        List<Beacon> list = (List<Beacon>)JSONArray.toCollection(arr,Beacon.class);
        JSONObject result = new JSONObject();
        result.put("success",deviceInfoService.inputBeaconList(list));
        return result;
    }

    //配置beacon和visitor_id绑定的接口
    @ResponseBody
    @RequestMapping(value = "/beacon/bindingBeaconAndVisitorId",method = RequestMethod.POST,produces = "application/json")
    public JSONObject beaconVisitorBinding(HttpServletRequest request, @RequestBody String param) {
        System.out.println(param);
        JSONObject obj = JSONObject.fromObject(param);
        JSONArray arr = JSONArray.fromObject(obj.getJSONArray(jsonArrayAnalyzeKey));
        List<BeaconVisitor> list = (List<BeaconVisitor>)JSONArray.toCollection(arr,BeaconVisitor.class);
        JSONObject result = new JSONObject();
        result.put("success",deviceInfoService.inputBeaconVisitorList(list));
        return result;
    }





    //根据时间间隔获取info数据
    @ResponseBody
    @RequestMapping(value = "/beaconInfo/getInfoByTimeRange",method = RequestMethod.POST)
    public JSONObject beaconInfoListByTimeRange(HttpServletRequest request, Timestamp startTime, Timestamp endTime) {
        JSONObject  json = new JSONObject();
        List<BeaconInfo> beaconList = beaconInfoService.getBeaconInfoBetweenTimeRange(startTime, endTime);
        json.put("list",beaconList);
        return json;
    }


    //根据起始时间、时间、个数来获取info数据
    @ResponseBody
    @RequestMapping(value = "/beaconInfo/getInfoListForLineChart",method = RequestMethod.POST)
    public JSONObject beaconInfoListForLineChart(HttpServletRequest request, Timestamp startTime, int count, int intevalSec, String device_id) {
        JSONObject json = new JSONObject();
//        int count = 10;
//        int intevalSec = 60;
        List[] allData = beaconInfoService.getBeanInfoForChartView(startTime, count, intevalSec, device_id);
        json.put("list",allData);
        return json;
    }


    //根据当日的时间和device_id来获取时间
    @ResponseBody
    @RequestMapping(value = "/beaconInfo/getVisitorForDevice", method = RequestMethod.POST)
    public JSONObject getVisitorCountByDeviceIdAndDayTime(String device_id, Timestamp dayTime) {
        JSONObject response = new JSONObject();
        Timestamp dayStartTime = new Timestamp(dayTime.getTime()/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset());
        long dayStart = dayStartTime.getTime();
        dayStart += 3600*8*1000;
        Timestamp startTime = new Timestamp(dayStart);
        List<Map> list = beaconInfoService.getVisitorCountByDeviceIdAndDayTime(device_id, startTime);
        response.put("list",list);
        return response;
    }

    //根据当日的时间和device_id来获取时间
    @ResponseBody
    @RequestMapping(value = "/beaconInfo/getVisitorDetailForDevice", method = RequestMethod.POST)
    public JSONObject getVisitorCountByDeviceIdAndDayTime(String device_id) {
        JSONObject response = new JSONObject();
        List<Map> list = beaconInfoService.getVisitorDetailForDevice(device_id);
        response.put("list",list);
        return response;
    }

    //根据user_id来获取beaconInfo的信息
    @ResponseBody
    @RequestMapping(value = "/beaconInfo/getVisitorRouteInfo", method = RequestMethod.POST)
    public JSONObject visitorRouteInfoList(HttpServletRequest request, String visitor_id, Timestamp dayTime) {
        JSONObject response = new JSONObject();
        Timestamp dayStartTime = new Timestamp(dayTime.getTime()/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset());
        long dayStart = dayStartTime.getTime();
        dayStart += 3600*8*1000;
        Timestamp startTime = new Timestamp(dayStart);
        List<Map>  allData = beaconInfoService.getBeaconInfoForVisitor(startTime, visitor_id);
        response.put("list",allData);
        return response;
    }

    //获取visitor根据name
    @ResponseBody
    @RequestMapping(value = "/visitor/getByName",method = RequestMethod.POST)
    public JSONObject getVisitorsByName (String name) {

        JSONObject result = new JSONObject();
        result.put("result",deviceInfoService.getVisitorByName(name));
        return result;
    }

    //获取visitor根据name 或者 visitor_id
    @ResponseBody
    @RequestMapping(value = "/visitor/getByNameOrVisitorId",method = RequestMethod.POST)
    public JSONObject getVisitorsByNameOrVisitorId(String name){
        JSONObject result= new JSONObject();
        result.put("result",deviceInfoService.getVisitorsByNameOrVisitorId(name));
        return result;
    }

    //获取本日的实时人数，15分钟一组，早上八点开始，到当前时间
    @ResponseBody
    @RequestMapping(value = "/visitor/getTodayVisitorCountList",method = RequestMethod.POST)
    public JSONObject getTodayVisitorCountList() {
        JSONObject result = new JSONObject();
        result.put("list",beaconInfoService.getTodayVisitorCountList());
        return result;
    }


    //设置基站位置信息
    @ResponseBody
    @RequestMapping(value = "/beaconInfo/setDeviceLoaction",method = RequestMethod.POST)
    public JSONObject deviceUpload(HttpServletRequest request,@RequestBody String param) {
        System.out.println(param);
        JSONObject obj = JSONObject.fromObject(param);

        DeviceInfo info = (DeviceInfo)JSONObject.toBean(obj,DeviceInfo.class);

        JSONObject result = new JSONObject();
        result.put("success",deviceInfoService.inputDeviceInfo(info));
        return result;
    }

    //获取基站位置信息列表
    @ResponseBody
    @RequestMapping(value = "/beaconInfo/getDeviceInfoList",method = RequestMethod.POST)
    public  JSONObject getDeviceInfoList(HttpServletRequest request, String building, String floor) {
        JSONObject json = new JSONObject();
        List<DeviceInfo> deviceList = deviceInfoService.getDeviceInfoByBuildingAndFloor(building,floor);
        json.put("list",deviceList);
        return json;
    }

    //配置visitor信息的接口
    @ResponseBody
    @RequestMapping(value = "/visitor/upload", method = RequestMethod.POST, produces = "application/json")
    public JSONObject visitorUpload(HttpServletRequest request,@RequestBody String param)  {
        System.out.println(param);
//        try {
//            param =  new String(param.getBytes("ISO-8859-1"),"UTF-8");
//        } catch (Exception e) {
//
//        }
        JSONObject obj = JSONObject.fromObject(param);
        JSONArray arr = JSONArray.fromObject(obj.getJSONArray(jsonArrayAnalyzeKey));
        List<Visitor> visitors = (List<Visitor>)JSONArray.toCollection(arr,Visitor.class);
        JSONObject result = new JSONObject();
        result.put("success",deviceInfoService.inputVisitorList(visitors));
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/beaconVisitor/inputOriginData",method = RequestMethod.POST,produces = "application/json")
    public  JSONObject sepOriginDataIntoTwoTable(HttpServletRequest request, @RequestBody String param) {
        System.out.println(param);

        JSONObject obj = JSONObject.fromObject(param);

        JSONArray arr = JSONArray.fromObject(obj.getJSONArray("result"));

        List<OriginData> originDataList = (List<OriginData>)JSONArray.toCollection(arr,OriginData.class);


        JSONObject result = new JSONObject();
        List<String> access =new ArrayList<>();
        try {
            access = deviceInfoService.inputOriginDataList(originDataList);
        }
        catch (Exception e){
            result.put("success",false);
            return result;
        }
        result.put("success",true);
        result.put("access", access);
        return result;
    }


    //获取排行榜信息
    @ResponseBody
    @RequestMapping(value = "/device/getRankMapList",method = RequestMethod.POST)
    public JSONObject getDeviceRank() {
        JSONObject result = new JSONObject();
        result.put("list",beaconInfoService.getDeviceRank());
        return result;
    }

    //获取会展类别
    @ResponseBody
    @RequestMapping(value = "/getExhibitionCategory")
    public JSONObject getCategoryAndVisitorCount(){
        JSONObject result= new JSONObject();
        result.put("list",beaconInfoService.getCategoryAndVisitorCount1());
        return result;
    }

    //热力回顾接口
    @ResponseBody
    @RequestMapping(value = "/heatPoint/getHeatPointsByTimeRange/",method = RequestMethod.POST)
    public JSONObject getHeatPointsByTimeRange(Timestamp startTime, Timestamp endTime, String floor) {
        JSONObject result = new JSONObject();
        //Timestamp startTime = Timestamp.valueOf("2016-11-09 15:15:00");
        //Timestamp endTime = Timestamp.valueOf("2016-11-09 15:17:00");
        //List<HeatPoint> heatPoints = heatMapService.getBeaconInfoBetweenTimeRange(startTime, endTime);
        System.out.println(heatMapService);
        List<HeatPoint> heatPoints = heatMapService.getBeaconInfoBetweenTimeRangeHasCompute(startTime, endTime, floor);
        result.put("list",heatPoints);
        return result;
    }

    //热力统揽接口
    @ResponseBody
    @RequestMapping(value = "/heatPoint/getHeatPointsByTimeRangeLongTime/",method = RequestMethod.POST)
    public JSONObject getHeatPointsByTimeRangeLongTime(Timestamp startTime, Timestamp endTime, String floor) {
        JSONObject result = new JSONObject();
        //Timestamp startTime = Timestamp.valueOf("2016-11-09 15:15:00");
        //Timestamp endTime = Timestamp.valueOf("2016-11-09 15:17:00");
        //List<HeatPoint> heatPoints = heatMapService.getBeaconInfoBetweenTimeRange(startTime, endTime);
        System.out.println(heatMapService);
        List<HeatPoint> heatPoints = heatMapService.getBeaconInfoBetweenTimeRangeHasComputeLongTime(startTime, endTime, floor);
        result.put("list",heatPoints);
        return result;
    }

    //轨迹接口
    @ResponseBody
    @RequestMapping(value = "/track/getTrackByVisitorId",method = RequestMethod.POST)
    public JSONObject getTrackByVisitorId(String visitorId,Timestamp startTime,Timestamp endTime, String floor){
        JSONObject result = new JSONObject();
        //visitorId="240000102";
        //startTime = Timestamp.valueOf("2016-11-09 12:15:00");
        //endTime = Timestamp.valueOf("2016-11-09 15:17:00");
        List<TrackPoint> trackPoints = heatMapService.getTrackPointsByVisitorIdBetweenTimeRange2(visitorId, startTime, endTime, floor);
        result.put("list",trackPoints);
        return result;
    }
    //搜索普通人员最近出现的位置
    @ResponseBody
    @RequestMapping(value = "/getLocations",method = RequestMethod.POST)
    public JSONObject getLatestLocation(String visitor_id){
        JSONObject result = new JSONObject();
        String[] ids=visitor_id.split(",");
        List<Location> list = heatMapService.getLocationByVisitorId(ids);
        result.put("list",list);
        return result;
    }
}
