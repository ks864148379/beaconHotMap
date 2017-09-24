package com.beacon.controller;

import com.beacon.daoService.UserService;
import com.beacon.model.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

/**
 * Created by LK on 2017/3/30.
 */
@Controller
@RequestMapping(value = "user")
public class UserController {
    @Autowired
    UserService userService;
//    @Autowired
    HttpSession session = new HttpSession() {
    @Override
    public long getCreationTime() {
        return 0;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public long getLastAccessedTime() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(int i) {

    }

    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Object getValue(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String[] getValueNames() {
        return new String[0];
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void putValue(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public void removeValue(String s) {

    }

    @Override
    public void invalidate() {

    }

    @Override
    public boolean isNew() {
        return false;
    }
};

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String Login(@RequestParam("visitor_id") String visitor_id,
                        @RequestParam("password") String password){

        Visitor visitor = userService.getVisitorById(visitor_id,password);
        if(visitor!=null){
            session.setAttribute("currentUser",visitor);
            return "redirect:/beaconHotMap/index";
        }else {
            return "redirect:/beaconHotMap/login";
        }
    }

}
