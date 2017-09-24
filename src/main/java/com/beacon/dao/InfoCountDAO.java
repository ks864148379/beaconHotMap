package com.beacon.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2017/7/18.
 */
public class InfoCountDAO {
    @Autowired
    private SessionFactory sessionFactory;
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Map getCountPerson(){
        Map<String, String> map = new HashMap();

        return map;
    }
}
