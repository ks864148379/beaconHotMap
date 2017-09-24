package com.beacon.daoService;

import com.beacon.dao.BeaconInfoDao;
import com.beacon.model.BeaconInfo;

import com.beacon.model.DeviceInfo;
import com.beacon.entity.VisitorCount;
import com.beacon.model.Exhibition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by LK on 2016/11/01.
 */

@Service("beaconInfoService")
public class BeaconInfoService {
    @Autowired
    private BeaconInfoDao beaconInfoDao;

    //保存beaconinfo
    public void inputBeaconInfo (BeaconInfo beaconInfo) {
        beaconInfoDao.inputBeaconInfo(beaconInfo);
    }


    //保存beaconInfoList
    //@Transactional
    public boolean inputBeaconInfoList (List<BeaconInfo> beaconList) {
        try {
            for (BeaconInfo b:beaconList) {
                /*System.out.println("");
                System.out.println("BeaconInfo_device_id:"+b.getDevice_id());
                System.out.println("BeaconInfo_mac_id:"+b.getMac_id());
                System.out.println("BeaconInfo_uuid:"+b.getUuid());
                System.out.println("BeaconInfo_major:"+b.getMajor());
                System.out.println("BeaconInfo_minor:"+b.getMinor());
                System.out.println("BeaconInfo_collect_time:"+b.getCollectTime());
                System.out.println("BeaconInfo_rssi:"+b.getRssi());
                System.out.println("BeaconInfo_distance:"+b.getDistance());
                System.out.println("BeaconInfo_flag:"+b.getFlag());*/
                beaconInfoDao.inputBeaconInfo(b);
            }
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    //获取时间间隔内的beaconInfo
    public List<BeaconInfo> getBeaconInfoBetweenTimeRange (Timestamp startTime, Timestamp endTime) {
        return beaconInfoDao.getBeaconInfoBetweenTimeRange(startTime, endTime);
    }

    //为曲线图获取beaconInfo
    public List[] getBeanInfoForChartView (Timestamp startTime, int count, int intevalSec, String device_id) {
        return beaconInfoDao.getBeaconInfoBetweenTimeForChartView(startTime, count, intevalSec, device_id);
    }
    //获取Visitor某一天12个小时的所在
    public List<Map>  getBeaconInfoForVisitor(Timestamp startTime, String visitor_id) {
        return beaconInfoDao.getAllBeaconInfoByVisitorId(visitor_id, startTime);
//        return beaconInfoDao.getBeaconInfoByVisitorId(visitor_id,startTime);/**/

    }

    //获取展位排行榜
    public List<VisitorCount> getDeviceRank(){
        List<VisitorCount> list = new ArrayList<>();
        Comparator<VisitorCount> comparator = new Comparator<VisitorCount>() {
            @Override
            public int compare(VisitorCount o1, VisitorCount o2) {
                if (o1!=null && o2!=null){
                    int count1 = o1.getVisitorCount();
                    int count2 = o2.getVisitorCount();
                    if (count1 > count2){
                        return -1;//从高到底排序
                    }else if (count1 < count2){
                        return 1;
                    }
                }
                return 0;
            }
        };
        List<DeviceInfo> deviceInfos = beaconInfoDao.getTotalDevice();
        for (DeviceInfo deviceInfo :deviceInfos){
            Integer count = beaconInfoDao.getDeviceVisitorCount(deviceInfo.getDevice_id());
            if (count!=0){
                VisitorCount visitorCount  = new VisitorCount();
                visitorCount.setDevice_id(deviceInfo.getDevice_id());
                visitorCount.setSpotName(deviceInfo.getSpotName());
                visitorCount.setVisitorCount(count);
                list.add(visitorCount);
            }
        }
        Collections.sort(list,comparator);
        return list;
    }

    //获取展区类别对应人数
    public List<Map> getCategoryAndVisitorCount(){
        List<DeviceInfo> deviceInfos = beaconInfoDao.getTotalDevice();
        List<Map> list = new ArrayList<>();
        Map<String,Integer> map = new HashMap<>();
        for (DeviceInfo deviceInfo :deviceInfos){
            Integer count = beaconInfoDao.getDeviceVisitorCount(deviceInfo.getDevice_id());
            String category = beaconInfoDao.getCategoryByDeviceId(deviceInfo.getDevice_id());
            if (count!=0){
                if (!map.containsKey(category)){
                    map.put(category,count);
                }else {
                    map.put(category,map.get(category)+count);
                }
            }
        }
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            Map<String,String> map1 = new HashMap<>();
            String key = iterator.next();
            map1.put("category",key);
            map1.put("count",String.valueOf(map.get(key)));
            list.add(map1);
        }
        return list;
    }

    //获取展区类别对应人数
    public List<Map> getCategoryAndVisitorCount1(){
        List<Map> list = new ArrayList<>();
        List<Map> categorys = beaconInfoDao.getTotalCategory();
        List<Exhibition> exhibitions=beaconInfoDao.getTotalExhibition();
        Map<String,Integer> map =new HashMap<>();//存放各个基站对应的人数
        for (int i=0;i<categorys.size();i++){
            Map<String,String> result =new HashMap<>();
            String category = (String) categorys.get(i).get("category");
            Integer count=0;
            for (int j=0;j<exhibitions.size();j++){
                Exhibition exhibition = exhibitions.get(j);
                if (exhibition.getCategory().equals(category)){
                    String device_id=exhibition.getDevice_id();
                    if (map.containsKey(device_id)){
                        count+=map.get(device_id);
                    }else {
                        Integer value=beaconInfoDao.getDeviceVisitorCount(device_id);
                        map.put(device_id,value);
                        count+=value;
                    }
                }
            }
            result.put("category",category);
            result.put("count",String.valueOf(count));
            list.add(result);
        }
        return list;
    }

    public Integer getCurrentVisitorCount() {
        return beaconInfoDao.getCurrentVisitorCount();
    }

    public  Integer getTotalVisitorCount() {
        return beaconInfoDao.getTotalVisitorCount();
    }

    public Integer getTodayVisitorCount(){
        return beaconInfoDao.getTodayVisitorCount();
    }

    public Integer getLatestHourVisitorCount(){
        return beaconInfoDao.getLatestHourVisitorCount();
    }

    public List<Map> getTodayVisitorCountList() {
        return beaconInfoDao.getTodayVisitorCountList();
    }

    public List<Map> getVisitorCountByDeviceIdAndDayTime(String device_id,Timestamp startTime) {
        return beaconInfoDao.getVisitorCountByDeviceIdAndDayTime(device_id, startTime);
    }

    public List<Map> getVisitorDetailForDevice(String device_id) {
        return beaconInfoDao.getVisitorDetailForDevice(device_id);
    }
}
