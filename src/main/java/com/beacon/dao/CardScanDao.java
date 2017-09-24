package com.beacon.dao;

import com.beacon.model.CardScan;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * Created by James on 2017/6/30.
 */
@Repository
@Transactional
public class CardScanDao {
    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<CardScan> searchCardList(){
        String hsql="from CardScan c";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        return query.list();
    }

    public CardScan getCardDetail(String id){
        String hsql="from CardScan c where c.id = ?";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        query.setParameter(0,id);
        return (CardScan)query.uniqueResult();
    }
}
