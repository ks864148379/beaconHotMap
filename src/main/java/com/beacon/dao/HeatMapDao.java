package com.beacon.dao;

import com.beacon.model.BeaconInfo;
import com.beacon.model.DeviceInfo;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by LK on 2017/3/2.
 */
@Repository
@Transactional
public class HeatMapDao {
    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Map> getBeaconInfoBetweenTimeRange(Timestamp startTime,Timestamp endTime){
        int rssi= -200;
        String hsql="select T.device_id,T.mac_id,MAX(T.rssi) rssi from beaconinfo T where T.collectTime >= ? and T.collectTime < ? and T.rssi <> ? GROUP BY T.mac_id,T.device_id order BY T.mac_id";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0, startTime).setParameter(1,endTime).setParameter(2,rssi);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }


    //单个beacon
    public List<Map> getSimpleBeaconInfoBetweenTimeRange(String mac_id,String device_id,Timestamp startTime,Timestamp endTime){
        int rssi= -200;
        String hsql="select T.device_id,T.mac_id,MAX(T.rssi) rssi from beaconinfo T where T.mac_id = ? AND T.device_id <> ? AND T.collectTime >= ? and T.collectTime < ? and T.rssi <> ? GROUP BY T.mac_id,T.device_id order BY T.rssi DESC";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0,mac_id).setParameter(1,device_id).setParameter(2, startTime).setParameter(3, endTime).setParameter(4,rssi);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    /**
     * getBeaconInfoByVisitorIdBetweenTimeRange
     */
    public List<Map> getBeaconInfoByMacIdBetweenTimeRange(String mac_id,Timestamp startTime,Timestamp endTime){
        int rssi =-200;
        String hsql="select T.rssi,T.device_id,T.collectTime from beaconinfo T where T.mac_id = ? and T.collectTime >= ? and T.collectTime <= ? and T.rssi <> ? order by T.collectTime asc ";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0, mac_id).setParameter(1,startTime).setParameter(2,endTime).setParameter(3,rssi);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }


    /**
     * getMacIds
     */
    public List<Map>  getMacIdsBetweenTimeRange(Timestamp startTime,Timestamp endTime){
        int rssi= -200;
        String hsql = "select DISTINCT(T.mac_id) mac_id from beaconinfo T where T.collectTime >= ? and T.collectTime < ? and T.rssi <> ?  order BY T.mac_id";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0, startTime).setParameter(1,endTime).setParameter(2,rssi);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    /**
     * 历史一段时间rssi最大一条beaconinfo数据
     */
    public Object getBeaconInfoMaxRssi(Timestamp startTime, Timestamp endTime, String mac_id, String device_id){
        int rssi=-200;
        String hsql="SELECT T.device_id,MAX(T.rssi) rssi FROM beaconinfo T" +
                " WHERE T.collectTime >= ? and T.collectTime < ? AND T.mac_id = ? AND T.device_id <> ?";
        Session session = sessionFactory.getCurrentSession();
        Query query =session.createSQLQuery(hsql).setParameter(0, startTime).setParameter(1,endTime)
                .setParameter(2, mac_id).setParameter(3,device_id);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return query.uniqueResult();
    }

    public DeviceInfo getDeviceInfoByDeviceId(String device_id){
        String hsql="from DeviceInfo T where T.device_id = ?";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hsql).setParameter(0,device_id);
        return (DeviceInfo)query.uniqueResult();
    }

    /**
     * 根据visitor_id找到visitor最近被基站采集到的时间
     */
    public Timestamp getLatestTimeByVisitorId(String visitor_id){
        String hsql="SELECT MAX(T.collectTime) collectTime FROM beaconinfo T LEFT JOIN beaconvisitor T1 on T.mac_id=T1.mac_id LEFT JOIN visitor T2 ON T2.visitor_id=T1.visitor_id WHERE T2.visitor_id= ? AND T.rssi<> -200";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0,visitor_id);
        return (Timestamp)query.uniqueResult();
    }

    /**
     * 根据mac_id得到每个基站采集到的最大rssi对应数据(降序)
     */
    public List<Map> getMaxRssiGroupByDeviceByMacId(String mac_id,Timestamp startTime,Timestamp endTime){
        String hsql="select T.device_id,MAX(T.rssi) rssi from beaconinfo T where T.mac_id=? and T.collectTime > ? and T.collectTime <= ? and T.rssi <> -200 GROUP BY T.device_id ORDER BY T.rssi DESC ";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0,mac_id).setParameter(1,startTime).setParameter(2,endTime);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

}
