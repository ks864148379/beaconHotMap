package com.beacon.dao;

import com.beacon.model.BeaconPointInfo;
import com.beacon.model.TestDataInfo;
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
 * Created by ks on 2017/7/25.
 */

@Repository
@Transactional
public class TestDataDao {

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addTestData(TestDataInfo testDataInfo) {
        Session session = sessionFactory.getCurrentSession();
        session.save(testDataInfo);
    }

    public List<Map> getBeaconInfoData() {
        String hsql = "SELECT * FROM beaconinfo WHERE collectTime >= '2017-07-20 16:44:00' AND collectTime <= '2017-07-20 17:10:00' ORDER BY collectTime";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return (List<Map>) query.list();
    }

}
