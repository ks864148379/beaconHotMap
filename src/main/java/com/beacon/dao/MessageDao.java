package com.beacon.dao;

import com.beacon.model.Message;
import com.beacon.model.Visitor;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by James on 2017/4/13.
 */
@Repository
@Transactional
public class MessageDao {
    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Visitor> getAllVisitor(){
        String hsql="from Visitor v";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        return query.list();
    }

    public List<Message> getAllMessage(){
        String hsql="from Message m order by m.id desc";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        return query.list();
    }

    public void addMessage(Message message){
        Session session = sessionFactory.getCurrentSession();
        session.save(message);
    }
}
