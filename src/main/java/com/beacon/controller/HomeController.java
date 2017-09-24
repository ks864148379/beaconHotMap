package com.beacon.controller;

import com.beacon.daoService.BeaconInfoService;
import com.beacon.daoService.DeviceInfoService;
import com.beacon.daoService.UserService;
import com.beacon.daoService.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by LK on 2017/03/26.
 */
@RequestMapping("/beaconHotMap")
@Controller
public class HomeController {

    @Resource(name = "beaconInfoService")
    private BeaconInfoService beaconInfoService;
    @Resource(name = "deviceInfoService")
    private DeviceInfoService deviceInfoService;
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "taskService")
    private TaskService taskService;


    @RequestMapping(value = "/index")
    public ModelAndView mainPage() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index_n");
        mav.addObject("floors",deviceInfoService.getFloorsByMapId("","",""));
        Integer allCount = beaconInfoService.getTotalVisitorCount();
        Integer currentCount = beaconInfoService.getCurrentVisitorCount();
        Integer todayCount = beaconInfoService.getTodayVisitorCount();
        Integer latestHourCount = beaconInfoService.getLatestHourVisitorCount();
        mav.addObject("allCount",allCount);
        mav.addObject("currentCount",currentCount);
        mav.addObject("todayCount",todayCount);
        mav.addObject("latestHourCount",latestHourCount);
        return mav;
    }


    @RequestMapping(value = "/shopQuery")
    public String shopPage() {return "shopQuery";}

    @RequestMapping(value = "/peoples")
    public String Peoples() {return  "peoples";}

    @RequestMapping(value = "/login")
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/hot_spot")
    public ModelAndView hot_spot() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("hot_spot");
        mav.addObject("floors", deviceInfoService.getFloorsByMapId("", "", ""));
        return mav;
    }

    @RequestMapping(value = "/customer_flow")
    public ModelAndView customer_flow() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("customer_flow");
        mav.addObject("visitors",userService.getAllVisitor());
        mav.addObject("floors",deviceInfoService.getFloorsByMapId("","",""));
        return mav;
    }

    @RequestMapping(value = "/key_personnel")
    public ModelAndView key_personnel() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("key_personnel");
        mav.addObject("floors",deviceInfoService.getFloorsByMapId("","",""));
        mav.addObject("visitors",userService.getAllKeyCustomer());
        return mav;
    }
    @RequestMapping(value = "/baseStation_monitoring")
    public ModelAndView baseStation_monitoring() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("baseStation_monitoring");
        mav.addObject("floors", deviceInfoService.getFloorsByMapId("", "", ""));
        return mav;
    }

    @RequestMapping(value = "/find_customer")
    public ModelAndView find_customer() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("find_customer");
        mav.addObject("floors",deviceInfoService.getFloorsByMapId("","",""));
        mav.addObject("visitors",userService.getAllVisitor());
        return mav;
    }

    @RequestMapping(value = "/assignTask")
    public String assignTask() {return  "assign_task";}

    @RequestMapping(value = "/taskList")
    public String taskList() {return  "task_list";}

    @RequestMapping(value = "/findStaff")
    public ModelAndView findStuff() throws Exception{
        ModelAndView mav = new ModelAndView();
        mav.setViewName("find_staff");
        mav.addObject("floors",deviceInfoService.getFloorsByMapId("","",""));
        mav.addObject("visitors",taskService.getAllStuff());
        return mav;
    }

    @RequestMapping(value = "/message")
    public String message() {return  "message";}

    @RequestMapping(value = "/msgList")
    public String messageList() {return  "msg_list";}

    @RequestMapping(value = "/cardScan")
    public String cardScan() {return  "card_scan";}

}
