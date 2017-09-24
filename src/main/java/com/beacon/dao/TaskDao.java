package com.beacon.dao;

import com.beacon.model.TaskManager;
import com.beacon.model.Visitor;
import org.hibernate.*;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
/**
 * Created by James on 2017/4/11.
 */
@Repository
@Transactional
public class TaskDao {
    //最大记录数
    private static final int MAX_RESULTS = 50;

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Map> getStuffByName (String name){
        String hsql="from Visitor v where v.type_id = 1 and v.name like :param";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hsql);
        query.setString("param", "%" + name + "%");
        query.setMaxResults(MAX_RESULTS);
        return (List<Map>) query.list();
    }

    public List<Visitor> getAllStuff(){
        String hsql="from Visitor v where v.type_id = 1";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        return query.list();
    }

    public List<Map> getCustomerByName (String name){
        String hsql="from Visitor v where v.type_id = 2 and v.name like :param";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hsql);
        query.setString("param", "%" + name + "%");
        query.setMaxResults(MAX_RESULTS);
        return (List<Map>) query.list();
    }

    public List<Map> getStuffTaskState(String name){
        String sql="SELECT v.visitor_id, v.name, v.image, v.position, v.tel, t.state, t.create_time FROM visitor v" +
                " LEFT JOIN task_manager t ON (v.visitor_id = t.stuff_id AND t.create_time IN " +
                " (SELECT max(create_time) FROM task_manager GROUP BY stuff_id)) WHERE v.type_id = 1" +
                " and v.name like :param order by t.state,t.create_time desc;";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(sql).setString("param", "%" + name + "%");
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return (List<Map>) query.list();
    }

    public List<Map> getTaskByStuffId(String stuffId){
        String sql = "SELECT t.stuff_id, t.customer_id, t.task_title, t.task_content, t.audio, t.image AS t_image," +
                " t.task_description, c.name, c.image AS c_image, c.corporation, c.position," +
                " DATE_FORMAT(t.create_time,'%Y-%m-%d %h:%i %p') AS create_time" +
                " FROM task_manager t LEFT JOIN visitor c ON (t.customer_id = c.visitor_id) WHERE t.stuff_id = ?" +
                " order by t.create_time desc;";
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        query.setString(0,stuffId);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return (List<Map>) query.list();
    }

    public String getLastTime(){
        String hsql="SELECT DATE_FORMAT(MAX(create_time),'%Y-%m-%d %h:%i %p') FROM task_manager";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql);
        return (String)query.uniqueResult();
    }

    public void addTask(List<TaskManager> taskManagerList){

        Session session = null;
        Transaction tran = null;
        try {
            session = sessionFactory.openSession();
            //使用了事物方式提交
            tran = session.beginTransaction();
            for (TaskManager taskManager: taskManagerList){
                session.save(taskManager);
            }
            tran.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if(tran != null)
                tran.rollback();
        }finally {
            session.close();
        }
    }

}
