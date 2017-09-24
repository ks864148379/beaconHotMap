package com.beacon.dao;

import com.beacon.entity.OriginData;
import com.beacon.model.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by linhanda on 2016/10/26.
 */


@Repository
@Transactional
public class DeviceInfoDao {

    @Autowired
    private SessionFactory sessionFactory;
    public SessionFactory getSessionFactory() {return sessionFactory;}
    public void setSessionFactory(SessionFactory sessionFactory) {this.sessionFactory=sessionFactory;}


    //保存基站设备基本信息
    public void inputDeviceInfo(DeviceInfo info) {

        System.out.println(info);
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(info);

    }

    //获取beacon设备的基本信息  mac_id或minor
    public Beacon getBeaconByMacId(String mac_id) {
        String hsql = "from Beacon where mac_id = ?";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hsql).setParameter(0, mac_id);
        return (Beacon) query.uniqueResult();
    }

    public Beacon getBeaconByMinor(int minor) {
        String hsql = "from Beacon where minor = ?";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hsql).setParameter(0,minor);
        return (Beacon)query.uniqueResult();
    }

    //保存beacon设备基本信息
    public void inputBeacon(Beacon beacon) {
        System.out.println(beacon);
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(beacon);
    }

    //保存visitor基本信息
    public void inputVisitor(Visitor visitor) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(visitor);
    }

    public List<Visitor> getVisitorListByName(String name) {
        String hsql = "from Visitor where name like '%"+name+"%'";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hsql);
        return query.list();
    }

    public List<Visitor> getVisitorsByNameOrVisitorId(String parameter){
        String hsql = "from Visitor where name = ? or visitor_id= ?";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hsql).setParameter(0,parameter).setParameter(1,parameter);
        return query.list();
    }

    //绑定beacon和visitor信息的接口
    public void inputBeaconVisitor(BeaconVisitor beaconVisitor) {
        if (beaconVisitor.getMac_id()==null || "".equals(beaconVisitor.getMac_id())) {
            System.out.println("mac_id为空，需要补齐mac_id");
            Beacon beacon = getBeaconByMinor(beaconVisitor.getMinor());
            beaconVisitor.setMac_id(beacon.getMac_id());
        }else if (beaconVisitor.getMinor()==null) {
            System.out.println("minor为空，需要补齐minor");
            Beacon beacon = getBeaconByMacId(beaconVisitor.getMac_id());
            beaconVisitor.setMinor(beacon.getMinor());
        }
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(beaconVisitor);
    }


    //获取本建筑物及本层的基站信息
    public List<DeviceInfo> getDeviceInfoListByBuidingAndFloor(String building, String floor) {
        String hsql = "from DeviceInfo where building = ? and floor = ?";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hsql).setParameter(0,building).setParameter(1,floor);
        return query.list();
    }

    //保存外来的data
    public void inputOriginDataIntoBeaconAndVistor(OriginData originData) {
        String blueId = originData.getBluetoothID();

        BeaconVisitor beaconVisitor = new BeaconVisitor();
        System.out.println("bluetooth_id:"+blueId);
        if (blueId.length()==12) {
            //说明是mac_id
            System.out.println("mac_id:"+blueId);
            beaconVisitor.setMac_id(blueId);
        }else if (blueId.length()==0) {
            Integer minor = 0;
            System.out.println("minor:"+minor);
            beaconVisitor.setMinor(minor);
        }
        else{
            //说明是minor
            Integer minor = Integer.valueOf(blueId);
            System.out.println("minor:"+minor);
            beaconVisitor.setMinor(minor);
        }
        beaconVisitor.setVisitor_id(originData.getBarcode());
        //这里做的是吧beaconvisitor存储到数据库里
        inputBeaconVisitor(beaconVisitor);

        //这里是把原始数据转换成visitor类存储到数据库中
        Visitor visitor = new Visitor();
        visitor.setVisitor_id(originData.getBarcode().toString());
        visitor.setCity(originData.getCity());
        visitor.setCorporation(originData.getCompany());
        visitor.setCountry(originData.getCountry());
        visitor.setPosition(originData.getJobTitle());
        visitor.setName(originData.getFullName());
        visitor.setProvince(originData.getProvince());
        System.out.println(originData.getSex());
        if (originData.getSex()==null)
        {
            visitor.setGender(1);
        }else  {
            if (originData.getSex().equals("先生")) {
                visitor.setGender(1);
            }else  {
                visitor.setGender(0);
            }
        }


        inputVisitor(visitor);
    }




}
