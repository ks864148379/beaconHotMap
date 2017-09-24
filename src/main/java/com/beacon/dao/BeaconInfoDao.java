package com.beacon.dao;

import com.beacon.model.BeaconInfo;
import com.beacon.model.BeaconVisitor;
import com.beacon.model.DeviceInfo;
import com.beacon.model.Exhibition;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by LK on 2017/03/26.
 */
@Repository
@Transactional
public class BeaconInfoDao {
    @Autowired
    private SessionFactory sessionFactory;
    public SessionFactory getSessionFactory() {return sessionFactory;}
    public void setSessionFactory(SessionFactory sessionFactory) {this.sessionFactory=sessionFactory;}

    public void inputBeaconInfo(BeaconInfo beaconInfo) {
        Session session = sessionFactory.getCurrentSession();
        session.save(beaconInfo);
    }

    public List<BeaconInfo> getBeaconInfoBetweenTimeRange(Timestamp startTime, Timestamp endTime) {
        String hsql = "from BeaconInfo where collectTime >= ? and collectTime < ?";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hsql).setParameter(0, startTime).setParameter(1,endTime);
        return query.list();
    }

    //获取该device的在该时间段所有的数据分为count组
    public List[] getBeaconInfoBetweenTimeForChartView(Timestamp startTime, int count, int intevalSec,String device_id) {
        List[] allData = new List[count];
        for (int i=0;i<count;i++) {
            long start = startTime.getTime()+i*intevalSec*1000;
            long end = start+intevalSec*1000;
            System.out.println(start);
            System.out.println(end);
            Timestamp endTime = new Timestamp(end);
            Timestamp startTimeNew = new Timestamp(start);
            String hsql = "from BeaconInfo where collectTime >= ? and collectTime < ? and device_id = ?";
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery(hsql).setParameter(0,startTimeNew).setParameter(1,endTime).setParameter(2,device_id);
            allData[i] = query.list();
            System.out.println(query.list().size());
        }
        return allData;
    }

    //获取某天的曲线图，按小时显示
    public List<Map> getVisitorCountByDeviceIdAndDayTime(String device_id,Timestamp startTime) {
        int intevalSec = 3600*1000;
        List<Map> list = new ArrayList<>();
        int count = 12;
        for (int i=0;i<count;i++) {
            long start = startTime.getTime()+i*intevalSec;
            long end = start+intevalSec;
            Timestamp startTimeNew = new Timestamp(start);
            Timestamp endTime = new Timestamp(end);
            String hsql = "select COUNT(DISTINCT(T.mac_id)) count from beaconinfo T where T.collectTime >= ? and collectTime < ? and device_id = ?";
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createSQLQuery(hsql).setParameter(0,startTimeNew).setParameter(1,endTime).setParameter(2, device_id);
            Map map = new HashMap();
            map.put("count",((BigInteger)query.uniqueResult()).intValue());
            map.put("time",startTimeNew);
            list.add(map);
        }
        return list;
    }

    //获取某个基站的参观人及参观时间
    public List<Map> getVisitorDetailForDevice(String device_id) {
        /*String hsql = "select c.name,min(collectTime) enterTime from beaconinfo a left join beaconvisitor b on a.mac_id=b.mac_id left join visitor c on b.visitor_id=c.visitor_id GROUP BY c.name,a.device_id " +
                " having a.device_id=?  and c.name IS NOT NULL" +
                " order by enterTime asc";*/
        String hsql ="SELECT c.name,T1.collectTime enterTime  FROM (select DISTINCT T.mac_id ,T.collectTime from beaconinfo T where T.device_id=? GROUP BY T.mac_id ) T1 left join beaconvisitor b on T1.mac_id=b.mac_id left join visitor c on b.visitor_id=c.visitor_id GROUP BY c.name having c.name IS NOT NULL  ORDER BY T1.collectTime ASC";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0,device_id);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        List<Map> data = query.list();
        return data;
    }

    //获取某个基站的采集的mac_id及参观时间
    public List<Map> getSimpleBeaconInfoByDeviceId(String device_id){
        String hsql="select DISTINCT T.mac_id ,T.c from beaconinfo T where T.device_id=? ";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0, device_id);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    //通过visitor_id获取mac_id
    public BeaconVisitor getMacIdFromVisitorId(String visitor_id) {
        String hsql = "from BeaconVisitor where visitor_id = ?";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hsql).setParameter(0, visitor_id);
        return (BeaconVisitor) query.uniqueResult();
    }

    //获取该Visitor当天的12组数据
    public List<Map> getBeaconInfoByVisitorId(String visitor_id, Timestamp startTime) {
        List<Map> allData = new ArrayList<>();
        int intevalSec = 3600; //3600秒表示一小时
        BeaconVisitor bv = getMacIdFromVisitorId(visitor_id);
        for (int i=0;i<12;i++) {
            long start = startTime.getTime()+i*intevalSec*1000;
            long end = start+intevalSec*1000;
            Timestamp startTimeNew = new Timestamp(start);
            Timestamp endTime = new Timestamp(end);
            String hsql = "select a.*,b.spotName from BeaconInfo a left join DeviceInfo b on a.device_id= b.device_id  where a.collectTime >= ? and a.collectTime < ? and a.mac_id = ? ORDER BY a.collectTime ASC ";
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createSQLQuery(hsql).setParameter(0,startTimeNew).setParameter(1,endTime).setParameter(2,bv.getMac_id());
            query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
            List<Map> data = query.list();
            List<String> spots = new ArrayList<>();
            Map<String,Integer> counts = new HashMap<>();
            Map<String,List<Map>> times = new HashMap<>();
            int max=0;
            String spotName ="";
            for(int k=0;k<data.size();k++){
                String sname = data.get(k).get("spotName").toString();
                if(!spots.contains(sname)){
                    spots.add(sname);
                    counts.put(sname,1);
                    times.put(sname,new ArrayList<Map>());
                }else {
                    counts.put(sname,counts.get(sname)+1);
                }
                times.get(sname).add(data.get(k));
                if(counts.get(sname)>max){
                    spotName=sname;
                }
            }
            Long duration =null;
            List<Map> toptimes = times.get(spotName);
            if(toptimes!=null){
                int size = toptimes.size();
                long lastTime = ((Timestamp)toptimes.get(size-1).get("collectTime")).getTime();
                if((int)toptimes.get(toptimes.size()-1).get("flag") == 0){
                    lastTime = endTime.getTime();
                }
                duration =  lastTime - ((Timestamp)toptimes.get(0).get("collectTime")).getTime();
                duration /=(1000*60);
            }
            if(duration<=1) continue;
            Map map = new HashMap();
            map.put("position",spotName);
            if(duration!=null) {
                map.put("duration", duration);
            }
            map.put("time",startTimeNew);
            allData.add(map);
        }
        return allData;
    }

    //获取该Visitor当天的全部数据
    public List<Map> getAllBeaconInfoByVisitorId(String visitor_id, Timestamp startTime) {
        List<Map> allData = new ArrayList<>();

        BeaconVisitor bv = getMacIdFromVisitorId(visitor_id);

        long end = startTime.getTime()+12*60*60*1000;
        Timestamp endTime = new Timestamp(end);
        String hsql = "select a.*,b.spotName from BeaconInfo a left join DeviceInfo b on a.device_id= b.device_id  where a.collectTime >= ? and a.collectTime < ? and a.mac_id = ? ORDER BY a.collectTime ASC ";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0,startTime).setParameter(1,endTime).setParameter(2,bv.getMac_id());
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        List<Map> data = query.list();

        String spotName ="";

        List<Map> toptimes = null;

        for(int k=0;k<data.size();k++){
            String sname = data.get(k).get("spotName").toString();
            if(sname.equals(spotName)){
                toptimes.add(data.get(k));
            }else {
                setAllData(allData,toptimes,spotName);
                spotName=sname;
                toptimes = new ArrayList<>();
                toptimes.add(data.get(k));
            }
            if(k==data.size()-1){
                setAllData(allData,toptimes,spotName);
            }
        }
        return allData;
    }

    private  void setAllData(List<Map> allData,List<Map> toptimes,String spotName){
        Long duration;
        if(toptimes!=null && toptimes.size()>0) {
            long lastTime = ((Timestamp) toptimes.get(toptimes.size() - 1).get("collectTime")).getTime();
//            if ((int) toptimes.get(toptimes.size() - 1).get("flag") == 0) {
//                Timestamp last = new Timestamp( ((Timestamp) toptimes.get(toptimes.size() - 1).get("collectTime")).getTime());
//                last.setMinutes(0);
//                lastTime =last.getTime()+3600*1000;
//            }
            duration = lastTime - ((Timestamp) toptimes.get(0).get("collectTime")).getTime();
            duration /= (1000 * 60);
            Map map = new HashMap();
            if(duration<3) return;
            map.put("position", spotName);
            map.put("duration", duration);
            map.put("time", toptimes.get(0).get("collectTime"));
            allData.add(map);
        }
    }

    //获取当天8点开始到目前为止，每十五分钟一组数据
    public List<Map> getTodayVisitorCountList() {
        //Timestamp nowStamp = new Timestamp(System.currentTimeMillis());
        Timestamp nowStamp = Timestamp.valueOf("2016-11-09 10:24:17");
        Timestamp dayStartTime = new Timestamp(nowStamp.getTime()/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset());
        long dayStart = dayStartTime.getTime();
        dayStart += 3600*8*1000;
        long nowTime = nowStamp.getTime();
        int count = (int) ((nowTime-dayStart)/(15*60*1000))+1;
        int intevalSec = 15*60*1000;   // 间隔为15分钟
        List<Map> list = new ArrayList<>();
        for (int i=0;i<count;i++) {
            long start = dayStart+i*intevalSec;
            long end = start+intevalSec;
            Timestamp startTimeNew = new Timestamp(start);
            Timestamp endTime = new Timestamp(end);
            String hsql = "select COUNT(DISTINCT(T.mac_id)) count from beaconinfo T where T.collectTime >= ? and collectTime < ?";
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createSQLQuery(hsql).setParameter(0,startTimeNew).setParameter(1,endTime);
            Map map = new HashMap();
            map.put("count",((BigInteger)query.uniqueResult()).intValue());
            map.put("time",startTimeNew);
            list.add(map);
        }
        return list;
    }

    public List<Map> getTopDeviceInfoByOrder()
    {
        //String hsql = "select COUNT(DISTINCT(T.mac_id)) count,T.device_id,T1.spotName from beaconinfo T left join deviceinfo T1 on T1.device_id=T.device_id GROUP BY T.device_id,T1.spotName HAVING T1.spotName is not null order by count desc ";//运行25s,GROUP BY T1.spotName 运行2s,结果不变
        String hsql="SELECT T1.visitorCount,T1.device_id,T2.spotName FROM(SELECT COUNT(DISTINCT(T.mac_id)) visitorCount,T.device_id from beaconinfo T GROUP BY T.device_id) T1 LEFT JOIN deviceinfo T2 on T2.device_id=T1.device_id  HAVING T2.spotName is not null ORDER BY T1.visitorCount DESC";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    public Integer getCurrentVisitorCount() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis()-1*60*1000);
        String hsql = "select COUNT(DISTINCT(T.mac_id)) count from beaconinfo T where T.collectTime >= ?";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0, timestamp);
        return ((BigInteger)query.uniqueResult()).intValue();
    }


    public Integer getTotalVisitorCount() {
        String hsql = "select COUNT(DISTINCT(T.mac_id)) count from beaconinfo T";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql);
        return ((BigInteger)query.uniqueResult()).intValue();
    }

    public Integer getTodayVisitorCount(){
        Timestamp nowStamp = new Timestamp(System.currentTimeMillis());
        Timestamp dayStartTime = new Timestamp(nowStamp.getTime()/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset());
        String hsql = "select COUNT(DISTINCT(T.mac_id)) count from beaconinfo T where T.collectTime>= ?";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0, dayStartTime);
        return ((BigInteger)query.uniqueResult()).intValue();
    }

    public Integer getLatestHourVisitorCount(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis()-60*60*1000);
        String hsql="select COUNT(DISTINCT(T.mac_id)) count from beaconinfo T WHERE T.collectTime>= ?";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0, timestamp);
        return ((BigInteger) query.uniqueResult()).intValue();
    }
    /**
     * 以下方法为新增
     */
    public List<DeviceInfo> getTotalDevice(){
        String hql = "from DeviceInfo ";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        return query.list();
    }

    /**
     * GetDeviceVisitorCountByDevice_id
     */
    public Integer getDeviceVisitorCount(String device_id){
        String hsql = "select COUNT(DISTINCT(T.mac_id)) count from beaconinfo T where T.device_id=?";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0, device_id);
        return ((BigInteger)query.uniqueResult()).intValue();
    }

    public String getCategoryByDeviceId(String device_id){
        String hsql = "SELECT T.category from exhibition T LEFT JOIN deviceinfo T1 on T1.spotId = T.id WHERE T1.device_id=?";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hsql).setParameter(0, device_id);
        return (String)query.uniqueResult();
    }

    //新加
    public List<Map> getTotalCategory(){
        String hql="SELECT DISTINCT(T.category) FROM exhibition T ";
        Session session = sessionFactory.getCurrentSession();
        Query query =session.createSQLQuery(hql);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    public List<Exhibition> getTotalExhibition(){
        String hql = "SELECT T.* FROM exhibition T ";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hql).addEntity(Exhibition.class);
        return query.list();
    }
}
