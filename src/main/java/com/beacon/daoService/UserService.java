package com.beacon.daoService;

import com.beacon.dao.UserDao;
import com.beacon.model.KeyCustomer;
import com.beacon.model.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LK on 2017/3/30.
 */
@Service("userService")
public class UserService {
    @Autowired
    UserDao userDao;
    public Visitor getVisitorById(String id,String password){
        return userDao.getVisitorById(id,password);
    }

    public List<Visitor> getAllVisitor(){
        return userDao.getAllVisitor();
    }

    public Visitor getVisitorById(String id){
        return userDao.getVisitorById(id);
    }

    public List<KeyCustomer> getAllKeyCustomer(){
        List<KeyCustomer> list = new ArrayList<>();
        List<Map> map = userDao.getAllKeyCustomers();
        for (int i=0;i<map.size();i++){
            KeyCustomer keyCustomer = new KeyCustomer();
            keyCustomer.setVisitor_id((String) map.get(i).get("visitor_id"));
            keyCustomer.setName((String) map.get(i).get("name"));
            list.add(keyCustomer);
        }
        return list;
    }
}
