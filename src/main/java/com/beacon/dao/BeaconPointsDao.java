package com.beacon.dao;

import com.beacon.model.BeaconInfo;
import com.beacon.model.BeaconPointInfo;
import com.beacon.model.Message;
import com.beacon.model.Visitor;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.quartz.impl.matchers.StringMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ks on 2017/7/3.
 */
@Repository
@Transactional
public class BeaconPointsDao {
    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addBeaconPoints(BeaconPointInfo beaconPointInfo) {
        Session session = sessionFactory.getCurrentSession();
        session.save(beaconPointInfo);
    }

    public List<Map> getBeaconInfoBetweenTimeRange(Timestamp start, Timestamp end){
        int rssi =-200;
        //String hsql="SELECT B.*, D.floor, D.position_x, D.position_y FROM beaconinfo B, ( SELECT MAX(T.collectTime) AS a, T.device_id, T.mac_id FROM beaconinfo T WHERE T.rssi <>- 200 AND T.collectTime >= ? AND T.collectTime < ? GROUP BY T.device_id, T.mac_id) A LEFT JOIN deviceinfo D ON D.device_id = A.device_id  WHERE B.rssi <>- 200 AND B.mac_id = A.mac_id  AND B.collectTime = A.a  ORDER BY  B.mac_id";
        String hsql = "SELECT B.*, D.floor, D.position_x, D.position_y FROM beaconinfo B, ( SELECT MAX(T.collectTime) AS a, T.device_id, T.mac_id FROM beaconinfo T WHERE T.rssi <>- 200 AND T.collectTime >= ? AND T.collectTime < ? GROUP BY T.device_id, T.mac_id) A LEFT JOIN deviceinfo D ON D.device_id = A.device_id  WHERE B.rssi >= -91 AND B.mac_id = A.mac_id  AND B.collectTime = A.a  ORDER BY  B.mac_id";
        //String hsql = "SELECT B.*, D.floor, D.position_x, D.position_y FROM test_data B, ( SELECT MAX(T.collectTime) AS a, T.device_id, T.mac_id FROM test_data T WHERE T.rssi <>- 200 AND T.collectTime >= ? AND T.collectTime < ? GROUP BY T.device_id, T.mac_id) A LEFT JOIN deviceinfo D ON D.device_id = A.device_id  WHERE B.rssi >= -91 AND B.mac_id = A.mac_id  AND B.collectTime = A.a  ORDER BY  B.mac_id";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0,start).setParameter(1, end);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    public List<Map> getSimpleBeaconInfoBetweenTimeRange(String mac_id,String device_id,Timestamp startTime,Timestamp endTime,String floor){
        int rssi= -200;
//        String hsql="SELECT B.*,D.floor,D.position_x,D.position_y FROM beaconinfo B, " +
//                "(SELECT MAX(T.collectTime) AS a , T.device_id,T.mac_id FROM beaconinfo T WHERE T.rssi<>-200 AND T.device_id<>? AND T.mac_id=? AND T.collectTime>? AND T.collectTime<? GROUP BY T.device_id ,T.mac_id  " +
//                ") A " +
//                "LEFT JOIN deviceinfo D on D.device_id=A.device_id " +
//                "WHERE B.mac_id=A.mac_id AND B.collectTime= A.a AND D.floor=? ORDER BY B.mac_id";

        String hsql="SELECT B.*,D.floor,D.position_x,D.position_y FROM beaconinfo B, " +
                "(SELECT MAX(T.collectTime) AS a , T.device_id,T.mac_id FROM beaconinfo T WHERE T.rssi >= -91 AND T.device_id<>? AND T.mac_id=? AND T.collectTime>? AND T.collectTime<? GROUP BY T.device_id ,T.mac_id  " +
                ") A " +
                "LEFT JOIN deviceinfo D on D.device_id=A.device_id " +
                "WHERE B.mac_id=A.mac_id AND B.collectTime= A.a AND D.floor=? ORDER BY B.mac_id";

//        String hsql="SELECT B.*,D.floor,D.position_x,D.position_y FROM test_data B, " +
//                "(SELECT MAX(T.collectTime) AS a , T.device_id,T.mac_id FROM test_data T WHERE T.rssi >= -91 AND T.device_id<>? AND T.mac_id=? AND T.collectTime>? AND T.collectTime<? GROUP BY T.device_id ,T.mac_id  " +
//                ") A " +
//                "LEFT JOIN deviceinfo D on D.device_id=A.device_id " +
//                "WHERE B.mac_id=A.mac_id AND B.collectTime= A.a AND D.floor=? ORDER BY B.mac_id";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0,device_id).setParameter(1,mac_id).setParameter(2, startTime).setParameter(3, endTime).setParameter(4, floor);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    public List<Map> getHasComputeBetweenTimeRange(Timestamp startTime, Timestamp endTime, String floor) {
        String hsql = "SELECT * from beaconpoint T where T.time>=? AND T.time<? AND T.floor=? ORDER BY T.time ";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0, startTime).setParameter(1,endTime).setParameter(2, floor);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    public List<Map> getHasComputeBetweenTimeRangeOrderByTimeDESC(Timestamp startTime, Timestamp endTime, String floor) {
        String hsql = "SELECT * from beaconpoint T where T.time>=? AND T.time<? AND T.floor=? ORDER BY T.time DESC ";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0, startTime).setParameter(1,endTime).setParameter(2, floor);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    public List<Map> getHasComputeBetweenTimeRangeWithVisitor(Timestamp startTime, Timestamp endTime, String floor, String visitor) {
        String hsql = "SELECT * from beaconpoint T where T.time>=? AND T.time<? AND T.floor=? AND T.mac_id=? ORDER BY T.time";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0, startTime).setParameter(1,endTime).setParameter(2, floor).setParameter(3, visitor);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }


//    public static void main(String[] args) {
//        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath:spring-hibernate.xml"});
//        BeaconPointsDao beaconPointsDao = context.getBean("beaconPointsDao",BeaconPointsDao.class);
//        Date start = string2Date("2017-06-02 16:25:20");
//        Date endTime = string2Date("2017-06-02 16:27:20");
//        //beaconPointsDao.getBeaconInfoBetweenTimeRange("2017-06-01 11:25:20","2017-06-28 16:40:20");
//    }

}
