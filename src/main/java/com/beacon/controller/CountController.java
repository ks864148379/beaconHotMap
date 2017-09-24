package com.beacon.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

/**
 * Created by James on 2017/7/19.
 */
@Controller
@RequestMapping("/api")
public class CountController {

    //用于测试数据
    private boolean TEST_SWITCH = true;
    private static JSONArray personList = null;
    private static JSONArray timesList = null;

    @ResponseBody
    @RequestMapping("/infocount/getDayCount")
    public JSONObject getDayCount(){
        JSONObject result = new JSONObject();
        if (TEST_SWITCH) {
            int total_person = (int)(Math.random() * 100000 + 200000);
            int total_times = (int)(total_person*(1+(Math.random()*0.5)));

            int person = (int)(Math.random() * 10000 + 30000);
            int times = (int)(person*(1+(Math.random()*0.5)));

            int vip_person = (int)(person*(Math.random()*0.1+0.1));

            result.put("total_person", total_person);
            result.put("total_times", total_times);
            result.put("today_person", person);
            result.put("today_times", times);
            result.put("today_normal", person - vip_person);
            result.put("today_vip", vip_person);
            result.put("count_from", "2017-07-18");
        }else{

        }

        result.put("success",true);
        return result;
    }

    @ResponseBody
    @RequestMapping("/infocount/getHourCount")
    public JSONObject getHourCount(){
        JSONObject result = new JSONObject();
        if(TEST_SWITCH) {
            if (personList == null){
                personList = new JSONArray();
                timesList = new JSONArray();
            }else if(personList.size() > 8){
                personList.clear();
                timesList.clear();
            }
            int person = 0;
            if(personList.size() == 3 || personList.size() == 4 || personList.size() == 8){
                person = (int)(Math.random() * 1000);
            }else{
                person = (int)(Math.random() * 500 + 1000);
            }

            int times = (int)(person*(Math.random()*0.2+1.1));
            personList.add(person);
            timesList.add(times);

            result.put("person_list", personList);
            result.put("times_list", timesList);
            result.put("hour_person", person);
            result.put("hour_times", times);

            int vip_person = (int)(person*(Math.random()*0.1+0.1));
            int vip_times = (int)(vip_person*(Math.random()*0.2+1.1));
            result.put("vip_person", vip_person);
            result.put("vip_times", vip_times);

            result.put("normal_person", person-vip_person);
            result.put("normal_times", times-vip_times);

            String fromTo = String.format("%d:00-%d:00", personList.size() + 7, personList.size() + 8);
            result.put("hour_from_to", fromTo);
            result.put("vip_from_to", fromTo);
            result.put("normal_from_to", fromTo);

            Date date=new Date();
            DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time= format.format(date);
            result.put("update_time",time);
        }else{

        }

        result.put("success",true);
        return result;
    }

    @ResponseBody
    @RequestMapping("/infocount/getDonutChartData1")
    public JSONObject getDonutChartData1(){
        JSONObject result = new JSONObject();
        if(TEST_SWITCH) {
            JSONArray list = new JSONArray();
            String[] label = {"普通观众", "VIP观众", "媒体观众"};
            for (int i = 0; i < label.length; i++) {
                JSONObject data = new JSONObject();
                data.put("label", label[i]);
                data.put("value", (int)(Math.random() * 100));
                list.add(data);
            }
            result.put("data", list);
        }else{

        }
        result.put("success",true);
        return result;
    }

    @ResponseBody
    @RequestMapping("/infocount/getDonutChartData2")
    public JSONObject getDonutChartData3(){
        JSONObject result = new JSONObject();
        if(TEST_SWITCH) {
            JSONArray list = new JSONArray();
            String[] label = {"展位区", "会议区", "活动区"};
            for (int i = 0; i < label.length; i++) {
                JSONObject data = new JSONObject();
                data.put("label", label[i]);
                data.put("value", (int)(Math.random() * 100));
                list.add(data);
            }
            result.put("data", list);
        }else{

        }
        result.put("success",true);
        return result;
    }

    @ResponseBody
    @RequestMapping("/infocount/getBarChartData")
    public JSONObject getBarChartData(){
        JSONObject result = new JSONObject();
        if(TEST_SWITCH) {
            JSONArray list = new JSONArray();
            String[] label = {"无人机", "航拍", "卫星", "导航", "室内地图", "LBS", "iBeacon"};
            for (int i = 0; i < label.length; i++) {
                JSONObject data = new JSONObject();
                data.put("label", label[i]);
                data.put("value", (int)(Math.random() * 100));
                list.add(data);
            }
            result.put("data", list);
        }else{

        }
        result.put("success",true);
        return result;
    }
}
