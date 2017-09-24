package com.beacon.dao;

import com.beacon.model.DyBeaconInfo;
import com.beacon.model.Message;
import com.beacon.model.TaskManager;
import com.beacon.model.Visitor;
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
 * Created by LK on 2017/4/19.
 */
@Repository
@Transactional
public class AppDao {

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {return sessionFactory;}
    public void setSessionFactory(SessionFactory sessionFactory) {this.sessionFactory=sessionFactory;}


    public List<Map> getTasksByVisitorId(String visitor_id,Timestamp time){
        String hsql="SELECT T.customer_id,T.task_title,T.task_content,T.create_time from task_manager T WHERE T.stuff_id=? AND T.create_time>?";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0, visitor_id).setParameter(1, time);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    public List<Visitor> getCustomersByVisitorId(String visitor_id,Timestamp time){
        String hsql="SELECT V.* FROM visitor V LEFT JOIN task_manager T ON T.customer_id=V.visitor_id WHERE T.stuff_id=? AND T.create_time>?";
        Query query =sessionFactory.getCurrentSession().createSQLQuery(hsql).addEntity(Visitor.class).setParameter(0,visitor_id).setParameter(1,time);
        return query.list();
    }

    public List<Map> getCustomersByVisitorId1(String visitor_id,Timestamp time){
        String hsql="SELECT V.*,B.mac_id FROM visitor V LEFT JOIN task_manager T ON T.customer_id=V.visitor_id LEFT JOIN beaconvisitor B ON B.visitor_id = V.visitor_id  WHERE T.stuff_id=? AND T.create_time>?";
        Query query =sessionFactory.getCurrentSession().createSQLQuery(hsql).setParameter(0,visitor_id).setParameter(1,time);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    public List<Map> getMessageByVisitorId(String visitor_id,Timestamp time){
        String hsql="SELECT T.content,T.create_time FROM message T WHERE T.create_time>? AND T.stuff_id LIKE ? ";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0,time).setParameter(1,"%"+'"' + visitor_id + '"'+"%");
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    public TaskManager getTaskManage(String stuff_id,String customer_id){
        String hsql="SELECT T.* FROM task_manager T WHERE T.stuff_id=? AND T.customer_id=?";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).addEntity(TaskManager.class).setParameter(0,stuff_id).setParameter(1,customer_id);
        return (TaskManager) query.uniqueResult();
    }

    public void updateTaskManage(TaskManager taskManager){
        Session session = sessionFactory.getCurrentSession();
        session.update(taskManager);
    }

    public void addDyBeaconInfo(DyBeaconInfo dyBeaconInfo){
        Session session =sessionFactory.getCurrentSession();
        session.save(dyBeaconInfo);
    }

    public List<Map> getFloorByMacId(String macId) {
        String hsql="SELECT D.floor FROM deviceinfo D, ( SELECT T.device_id FROM beaconinfo T WHERE T.mac_id = ? ORDER BY T.collectTime DESC LIMIT 0,1) A WHERE A.device_id = D.device_id";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0, macId);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }
}
