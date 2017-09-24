package com.beacon.dao;

import com.beacon.model.KeyCustomer;
import com.beacon.model.Visitor;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/3/30.
 */
@Repository
@Transactional
public class UserDao {
    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Visitor getVisitorById (String id,String password){
        String hql = "from Visitor v where v.visitor_id=? and v.password=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setString(0, id).setString(1,password);
        return (Visitor) query.uniqueResult();
    }

    public List<Visitor> getAllVisitor(){
        String hsql="from Visitor v";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        return query.list();
    }

    public Visitor getVisitorById(String id){
        String hsql="from Visitor where visitor_id = ?";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        query.setString(0,id);
        return (Visitor) query.uniqueResult();
    }

    public List<Map> getAllKeyCustomers(){
        String hsql="select k.visitor_id,v.name from keyCustomer k left join visitor V on V.visitor_id=k.visitor_id";
        Query query = sessionFactory.getCurrentSession().createSQLQuery(hsql);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    public String getMacIdByVisitorId(String visitor_id){
        String hsql="SELECT B.mac_id FROM beaconvisitor B WHERE B.visitor_id=?";
        Query query = sessionFactory.getCurrentSession().createSQLQuery(hsql).setParameter(0,visitor_id);
        return (String) query.uniqueResult();
    }

    public String getVisitorIdByMacId(String mac_id){
        String hsql="SELECT B.visitor_id FROM beaconvisitor B WHERE B.mac_id=?";
        Query query = sessionFactory.getCurrentSession().createSQLQuery(hsql).setParameter(0,mac_id);
        return (String) query.uniqueResult();
    }
}
