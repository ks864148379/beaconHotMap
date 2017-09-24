package com.beacon.daoService;

import com.beacon.dao.*;
import com.beacon.entity.*;
import com.beacon.model.*;
import com.sun.xml.internal.fastinfoset.algorithm.IntEncodingAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.lang.Math;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by LK on 2017/3/2.
 */
@Service("heatMapService")
public class HeatMapService {

    @Autowired
    HeatMapDao heatMapDao;
    @Autowired
    BeaconInfoDao beaconInfoDao;
    @Autowired
    UserDao userDao;
    @Autowired
    BeaconPointsDao beaconPointsDao;

    private static final int INTEVALSEC = 1*30*1000;//轨迹动线时间间隔
    private static final int INTEVALSEC_TEST = 1*60*1000;//轨迹动线时间间隔
    /**
     * 得到热力点集
     */
    /*
    public List<HeatPoint> getBeaconInfoBetweenTimeRange(Timestamp startTime,Timestamp endTime){
        Map<Map<Integer,Integer>,Integer> result = new HashMap<>();
        List<Map> list= heatMapDao.getBeaconInfoBetweenTimeRange(startTime, endTime);
        Timestamp history_startTime = new Timestamp(startTime.getTime()-1*60*1000);
        List<Map> before_list = heatMapDao.getBeaconInfoBetweenTimeRange(history_startTime, startTime);
        List<DeviceInfo> deviceInfos = beaconInfoDao.getTotalDevice();
        int count=0;//扫描到的基站数目
        int flag =0;//for循环标志
        List<HeatPoint> heatPoints = new ArrayList<>();
        DeviceInfo deviceInfo = new DeviceInfo();
        //单条数据处理
        if (list.size()==1){
            HeatPoint heatPoint = new HeatPoint();
            heatPoint.setCount(1);
            String mac_id= (String) list.get(0).get("mac_id");
            String device_id = (String) list.get(0).get("device_id");
            int rssi= (int) list.get(0).get("rssi");
            for (DeviceInfo device :deviceInfos){
                if (device.getDevice_id().equals(device_id)){
                    deviceInfo = device;
                    break;
                }
            }
            List<Map> history_infos=heatMapDao.getSimpleBeaconInfoBetweenTimeRange(mac_id,device_id,history_startTime,startTime);
            if (history_infos.size()==0){
                heatPoint.setX(getXorY(deviceInfo.getPosition_x()));
                heatPoint.setY(getXorY(deviceInfo.getPosition_y()));
            }else if (history_infos.size()==1){
                String device_id1 = (String) history_infos.get(0).get("device_id");
                int rssi1= (int) history_infos.get(0).get("rssi");
                DeviceInfo deviceInfo1=new DeviceInfo();
                for (DeviceInfo device :deviceInfos){
                    if (device.getDevice_id().equals(device_id1)){
                        deviceInfo1 = device;
                        break;
                    }
                }
                heatPoint.setX(getXorY(getValueByTwoPointsWeighting(deviceInfo.getPosition_x(),
                        deviceInfo1.getPosition_x(),rssi,rssi1)));
                heatPoint.setY(getXorY(getValueByTwoPointsWeighting(deviceInfo.getPosition_y(),
                        deviceInfo1.getPosition_y(),rssi,rssi1)));
            }else if (history_infos.size()>=2){
                String device_id1= (String) history_infos.get(0).get("device_id");
                int rssi1= (int) history_infos.get(0).get("rssi");
                String device_id2 = (String) history_infos.get(1).get("device_id");
                int rssi2= (int) history_infos.get(1).get("rssi");
                DeviceInfo deviceInfo1=new DeviceInfo();
                DeviceInfo deviceInfo2=new DeviceInfo();
                for (DeviceInfo device :deviceInfos){
                    if (device.getDevice_id().equals(device_id1)) deviceInfo1= device;
                    if (device.getDevice_id().equals(device_id2)) deviceInfo2= device;
                }
                heatPoint.setX(getXorY(getValueByThreePointWeighting(deviceInfo.getPosition_x(),
                        deviceInfo1.getPosition_x(), deviceInfo2.getPosition_x(), getDistance(rssi),getDistance(rssi1),getDistance(rssi2))));
                heatPoint.setY(getXorY(getValueByThreePointWeighting(deviceInfo.getPosition_x(),
                        deviceInfo1.getPosition_y(),deviceInfo2.getPosition_y(),getDistance(rssi),getDistance(rssi1),getDistance(rssi2))));

            }
            heatPoints.add(heatPoint);
        }
        //多条数据处理
        for (int i=1;i<list.size();i++){
            String mac_id= (String) list.get(i-1).get("mac_id");
            String device_id = (String) list.get(i-1).get("device_id");
            int rssi = (int) list.get(i-1).get("rssi");
            for (DeviceInfo device :deviceInfos){
                if (device.getDevice_id().equals(device_id)){
                    deviceInfo = device;
                }
            }
            String mac_id1= (String) list.get(i).get("mac_id");
            if (!mac_id.equals(mac_id1)){
                count=count+1;
            }else {
                count=count+2;
                if (i<list.size()-1){
                    i=i+1;
                }
                while (i<list.size() && mac_id.equals((String) list.get(i).get("mac_id"))){
                    i=i+1;
                    count =count+1;
                }
            }

            if (count==1){
                    //一个基站采集到
                    List<Integer> arry = new ArrayList<>();
                    for (int j=0;j<before_list.size();j++){
                        String mac_id_before = (String) before_list.get(j).get("mac_id");
                        if (mac_id.equals(mac_id_before)){
                            String device_id_before= (String) before_list.get(j).get("device_id");
                            if (!device_id_before.equals(device_id)){
                                arry.add(j);
                            }
                            flag=flag+1;
                        }else {
                            if (flag>0){
                                j=before_list.size();
                            }
                            flag=0;
                        }
                    }

                    if (arry.size()==0){
                        //历史没有(除去此次基站),点打在基站位置
                        if (!deviceInfo.getDevice_id().equals(null)){
                            int x = getXorY(deviceInfo.getPosition_x());
                            int y = getXorY(deviceInfo.getPosition_y());
                            Map<Integer,Integer> spot = new HashMap<>();
                            spot.put(x,y);
                            if (result.containsKey(spot)){
                                int people=result.get(spot);
                                result.put(spot,people+1);
                            }else {
                                result.put(spot,1);
                            }
                        }
                    }else if (arry.size()==1){
                        //历史有1个基站采集到(除去此次基站)，两点加权求坐标
                        String  device_id1= (String) before_list.get(arry.get(0)).get("device_id");
                        int rssi1 = (int) before_list.get(0).get("rssi");
                        DeviceInfo deviceInfo1 = new DeviceInfo() ;
                        for (DeviceInfo device :deviceInfos){
                            if (device.getDevice_id().equals(device_id1)){
                                deviceInfo1 = device;
                            }
                        }
                        if (!deviceInfo.getDevice_id().equals(null) && !deviceInfo1.getDevice_id().equals(null)){
                            int x = getXorY(getValueByTwoPointsWeighting(deviceInfo.getPosition_x(),deviceInfo1.getPosition_x(),getDistance(rssi),getDistance(rssi1)));
                            int y = getXorY(getValueByTwoPointsWeighting(deviceInfo.getPosition_y(),deviceInfo1.getPosition_y(),getDistance(rssi),getDistance(rssi1)));
                            //下面几行代码多处使用，最好封装起来
                            Map<Integer,Integer> spot = new HashMap<>();
                            spot.put(x,y);
                            if (result.containsKey(spot)){
                                int people=result.get(spot);
                                result.put(spot,people+1);
                            }else {
                                result.put(spot,1);
                            }
                        }
                    }else if (arry.size()>1){
                        DeviceInfo deviceInfo1 = new DeviceInfo() ;
                        DeviceInfo deviceInfo2 = new DeviceInfo();
                        //历史3个及以上基站采集到(除去此次基站)，取前两个，三点加权求坐标
                        String device_id1= (String) before_list.get(arry.get(0)).get("device_id");
                        int rssi1 = (int) before_list.get(arry.get(0)).get("rssi");
                        String device_id2= (String) before_list.get(arry.get(1)).get("device_id");
                        int rssi2 = (int) before_list.get(arry.get(1)).get("rssi");
                        for (int m=0;m<deviceInfos.size();m++){
                            DeviceInfo device = deviceInfos.get(m);
                            if (device.getDevice_id().equals(device_id1)){
                                deviceInfo1= device;
                            }else if (device.getDevice_id().equals(device_id2)){
                                deviceInfo2= device;
                            }
                        }
                        if (!deviceInfo.getDevice_id().equals(null) && !deviceInfo1.getDevice_id().equals(null)
                                && !deviceInfo2.getDevice_id().equals(null)){
                            int x = getXorY(getValueByThreePointWeighting(deviceInfo.getPosition_x(),deviceInfo1.getPosition_x(),
                                    deviceInfo2.getPosition_x(),getDistance(rssi),getDistance(rssi1),getDistance(rssi2)));
                            int y = getXorY(getValueByThreePointWeighting(deviceInfo.getPosition_y(),deviceInfo1.getPosition_y(),
                                    deviceInfo2.getPosition_y(),getDistance(rssi),getDistance(rssi1),getDistance(rssi2)));
                            Map<Integer,Integer> spot = new HashMap<>();
                            spot.put(x,y);
                            if (result.containsKey(spot)){
                                int people=result.get(spot);
                                result.put(spot,people+1);
                            }else {
                                result.put(spot,1);
                            }
                        }
                    }

            }else if (count==2){
                //两个基站采集到,两点加权求坐标
                String device_id1= (String) list.get(i-count+1).get("device_id");
                int rssi1 = (int) list.get(i-count+1).get("rssi");
                DeviceInfo deviceInfo1 = new DeviceInfo() ;
                for (DeviceInfo device :deviceInfos){
                    if (device.getDevice_id().equals(device_id1)){
                        deviceInfo1 = device;
                    }
                }
                if (!deviceInfo.getDevice_id().equals(null) && !deviceInfo1.getDevice_id().equals(null)){
                    int x = getXorY(getValueByTwoPointsWeighting(deviceInfo.getPosition_x(),deviceInfo1.getPosition_x(),getDistance(rssi),getDistance(rssi1)));
                    int y = getXorY(getValueByTwoPointsWeighting(deviceInfo.getPosition_y(),deviceInfo1.getPosition_y(),getDistance(rssi),getDistance(rssi1)));
                    Map<Integer,Integer> spot = new HashMap<>();
                    spot.put(x,y);
                    if (result.containsKey(spot)){
                        int people=result.get(spot);
                        result.put(spot,people+1);
                    }else {
                        result.put(spot,1);
                    }
                }
            }else if (count>=3){
                //三个基站采集到，三点加权求坐标
                DeviceInfo deviceInfo1 = new DeviceInfo() ;
                DeviceInfo deviceInfo2 = new DeviceInfo();
                String device_id1= (String) list.get(i-count+1).get("device_id");
                int rssi1 = (int) list.get(i-count+1).get("rssi");
                String device_id2= (String) list.get(i-count+2).get("device_id");
                int rssi2 = (int) list.get(i-count+2).get("rssi");
                for (int m=0;m<deviceInfos.size();m++){
                    DeviceInfo device = deviceInfos.get(m);
                    if (device.getDevice_id().equals(device_id1)){
                        deviceInfo1= device;
                    }else if (device.getDevice_id().equals(device_id2)){
                        deviceInfo2= device;
                    }
                }
                if (!deviceInfo.getDevice_id().equals(null) && !deviceInfo1.getDevice_id().equals(null)
                        && !deviceInfo2.getDevice_id().equals(null)){
                    int x = getXorY(getValueByThreePointWeighting(deviceInfo.getPosition_x(),deviceInfo1.getPosition_x(),
                            deviceInfo2.getPosition_x(),getDistance(rssi),getDistance(rssi1),getDistance(rssi2)));
                    int y = getXorY(getValueByThreePointWeighting(deviceInfo.getPosition_y(),deviceInfo1.getPosition_y(),
                            deviceInfo2.getPosition_y(),getDistance(rssi),getDistance(rssi1),getDistance(rssi2)));
                    Map<Integer,Integer> spot = new HashMap<>();
                    spot.put(x,y);
                    if (result.containsKey(spot)){
                        int people=result.get(spot);
                        result.put(spot,people+1);
                    }else {
                        result.put(spot,1);
                    }
                }
            }
            count=0;
        }


        Iterator<Map<Integer,Integer>> iter = result.keySet().iterator();
        while (iter.hasNext()){
            HeatPoint heatPoint = new HeatPoint();
            Map<Integer,Integer> map = iter.next();
            heatPoint.setCount(result.get(map));
            Iterator<Integer> iterator = map.keySet().iterator();
            while (iterator.hasNext()){
                Integer key1=iterator.next();
                Integer value1= map.get(key1);
                heatPoint.setX(key1);
                heatPoint.setY(value1);
            }
            heatPoints.add(heatPoint);
        }
        return heatPoints;
    }
    */

    ////////////////得到每一分钟的数据写到库里///////////////
    public List<BeaconPointInfo> getBeaconInfoBetweenTimeRangeNew(Timestamp startTime,Timestamp endTime){

        List<Map> list = beaconPointsDao.getBeaconInfoBetweenTimeRange(startTime, endTime);
        Timestamp history_startTime = new Timestamp(startTime.getTime() - 1 * 60 * 1000);
        List<Map> before_list = beaconPointsDao.getBeaconInfoBetweenTimeRange(history_startTime, startTime);
        List<DeviceInfo> deviceInfos = beaconInfoDao.getTotalDevice();
        int count=0;//扫描到的基站数目
        int flag =0;//for循环标志
        List<BeaconPointInfo> beaconPoints = new ArrayList<>();
        DeviceInfo deviceInfo = new DeviceInfo();
        //单条数据处理
        if (list.size()==1){
            BeaconPointInfo beaconPoint = new BeaconPointInfo();
            String mac_id = (String) list.get(0).get("mac_id");
            String device_id = (String) list.get(0).get("device_id");
            double x = (double)list.get(0).get("position_x");
            double y = (double)list.get(0).get("position_y");
            Timestamp nowTime = new Timestamp(((Timestamp)list.get(0).get("collectTime")).getTime());
            String floor = (String)list.get(0).get("floor");
            int rssi0 = (int)list.get(0).get("rssi");
            List<Map> history_infos=beaconPointsDao.getSimpleBeaconInfoBetweenTimeRange(mac_id, device_id, history_startTime, startTime, floor);
            if (history_infos.size()==0){
                beaconPoint.setX(x);
                beaconPoint.setY(y);
                beaconPoint.setMac_id(mac_id);
                beaconPoint.setTime(nowTime);
                beaconPoint.setFloor(floor);
            }else if (history_infos.size()==1){
                double historyx = (double)history_infos.get(0).get("position_x");
                double historyy = (double)history_infos.get(0).get("position_y");
                int rssiFirst = (int)history_infos.get(0).get("rssi");
                beaconPoint.setX(getValueByTwoPointsWeighting((double) x, (double) historyx, getDistance(rssi0), getDistance(rssiFirst)));
                beaconPoint.setY(getValueByTwoPointsWeighting((double) y, (double) historyy, getDistance(rssi0), getDistance(rssiFirst)));
                beaconPoint.setMac_id(mac_id);
                beaconPoint.setTime(nowTime);
                beaconPoint.setFloor(floor);
            } else if (history_infos.size() >= 2) {
                double historyx1 = (double)history_infos.get(0).get("position_x");
                double historyy1 = (double)history_infos.get(0).get("position_y");
                int rssiFirst = (int)history_infos.get(0).get("rssi");

                double historyx2 = (double)history_infos.get(1).get("position_x");
                double historyy2 = (double)history_infos.get(1).get("position_y");
                int rssiSecond = (int)history_infos.get(1).get("rssi");

                beaconPoint.setX(getValueByThreePointWeighting((double) x,
                        (double) historyx1, (double) historyx2, getDistance(rssi0), getDistance(rssiFirst), getDistance(rssiSecond)));
                beaconPoint.setY(getValueByThreePointWeighting((double) y,
                        (double) historyy1, (double) historyy2, getDistance(rssi0), getDistance(rssiFirst), getDistance(rssiSecond)));
                beaconPoint.setMac_id(mac_id);
                beaconPoint.setTime(nowTime);
                beaconPoint.setFloor(floor);
            }
            beaconPoints.add(beaconPoint);
        }
        //多条数据处理
        for (int i = 1; i < list.size(); i++){
            BeaconPointInfo beaconPoint = new BeaconPointInfo();
            String mac_id = (String) list.get(i-1).get("mac_id");
            String device_id = (String) list.get(i-1).get("device_id");
            int rssi = (int) list.get(i-1).get("rssi");
            double x = (double)list.get(i-1).get("position_x");
            double y = (double)list.get(i-1).get("position_y");
            String floor = (String)list.get(i-1).get("floor");
            Timestamp nowTime = new Timestamp(((Timestamp)list.get(i-1).get("collectTime")).getTime());
            String mac_id1 = (String) list.get(i).get("mac_id");
            if (!mac_id.equals(mac_id1)) {
                count = count + 1;
            } else {
                count = count + 2;
                if (i < list.size()-1) {
                    i = i + 1;
                }
                while (i < list.size() && mac_id.equals((String) list.get(i).get("mac_id"))) {
                    i = i + 1;
                    count = count+1;
                }
            }

            List<Integer> arry = new ArrayList<>();
            for (int j = 0; j < before_list.size(); j++){
                String mac_id_before = (String) before_list.get(j).get("mac_id");
                if (mac_id.equals(mac_id_before)){
                    String device_id_before= (String) before_list.get(j).get("device_id");
                    String floor1 = (String) before_list.get(j).get("floor");
                    if (!device_id_before.equals(device_id) && floor.equals(floor1)){
                        arry.add(j);
                    }
                    flag = flag + 1;
                }else {
                    if (flag > 0) {
                        j = before_list.size();
                    }
                    flag = 0;
                }
            }

            if (count == 1) {
                //一个基站采集到

                if (arry.size()==0){
                    //历史没有(除去此次基站),点打在基站位置
                    //if (!deviceInfo.getDevice_id().equals(null)) {
                        beaconPoint.setX(x);
                        beaconPoint.setY(y);
                        beaconPoint.setMac_id(mac_id);
                        beaconPoint.setTime(nowTime);
                        beaconPoint.setFloor(floor);
                   // }
                }else if (arry.size()==1){
                    //历史有1个基站采集到(除去此次基站)，两点加权求坐标
                    String  device_id1 = (String) before_list.get(arry.get(0)).get("device_id");
                    int rssi1 = (int) before_list.get(0).get("rssi");
                    double x1 = (double)before_list.get(arry.get(0)).get("position_x");
                    double y1 = (double)before_list.get(arry.get(0)).get("position_y");
                    if (!device_id1.equals(null)){
                        double finalx = getValueByTwoPointsWeighting((double)x, (double)x1, getDistance(rssi), getDistance(rssi1));
                        double finaly = getValueByTwoPointsWeighting((double) y, (double) y1, getDistance(rssi), getDistance(rssi1));

                        beaconPoint.setX(finalx);
                        beaconPoint.setY(finaly);
                        beaconPoint.setMac_id(mac_id);
                        beaconPoint.setTime(nowTime);
                        beaconPoint.setFloor(floor);


                    }
                }else if (arry.size()>1){
                    //DeviceInfo deviceInfo1 = new DeviceInfo() ;
                    //DeviceInfo deviceInfo2 = new DeviceInfo();
                    //历史3个及以上基站采集到(除去此次基站)，取前两个，三点加权求坐标
                    String device_id1= (String) before_list.get(arry.get(0)).get("device_id");
                    int rssi1 = (int) before_list.get(arry.get(0)).get("rssi");
                    String device_id2= (String) before_list.get(arry.get(1)).get("device_id");
                    int rssi2 = (int) before_list.get(arry.get(1)).get("rssi");
                    double x1 = (double)before_list.get(arry.get(0)).get("position_x");
                    double y1 = (double)before_list.get(arry.get(0)).get("position_y");
                    double x2 = (double)before_list.get(arry.get(1)).get("position_x");
                    double y2 = (double)before_list.get(arry.get(1)).get("position_y");
                    if (!device_id1.equals(null)
                            && !device_id2.equals(null)){
                        double finalx = getValueByThreePointWeighting((double) x, (double) x1,
                                (double) x2, getDistance(rssi), getDistance(rssi1), getDistance(rssi2));
                        double finaly = getValueByThreePointWeighting((double) y, (double) y1,
                                (double) y2, getDistance(rssi), getDistance(rssi1), getDistance(rssi2));

                        beaconPoint.setX(finalx);
                        beaconPoint.setY(finaly);
                        beaconPoint.setMac_id(mac_id);
                        beaconPoint.setTime(nowTime);
                        beaconPoint.setFloor(floor);
                    }
                }

            }else if (count==2){
                //两个基站采集到,两点加权求坐标
                if (arry.size() > 0) {
                    //历史有3个基站采集到(除去此次基站)，三点加权求坐标
                    String  device_id1 = (String) before_list.get(arry.get(0)).get("device_id");

                    int rssi1 = (int) before_list.get(0).get("rssi");
                    double x1 = (double) list.get(i-count+1).get("position_x");
                    double y1 = (double) list.get(i-count+1).get("position_y");

                    int rssi2 = (int) before_list.get(0).get("rssi");
                    double x2 = (double)before_list.get(arry.get(0)).get("position_x");
                    double y2 = (double)before_list.get(arry.get(0)).get("position_y");

                    if (!device_id1.equals(null)){
                        double finalx = getValueByThreePointWeighting((double) x, (double) x1, (double) x2, getDistance(rssi), getDistance(rssi1), getDistance(-95));
                        double finaly = getValueByThreePointWeighting((double) y, (double) y1, (double) y2, getDistance(rssi), getDistance(rssi1), getDistance(-95));

                        beaconPoint.setX(finalx);
                        beaconPoint.setY(finaly);
                        beaconPoint.setMac_id(mac_id);
                        beaconPoint.setTime(nowTime);
                        beaconPoint.setFloor(floor);
                    }
                } else {
                    String device_id1= (String) list.get(i-count+1).get("device_id");
                    int rssi1 = (int) list.get(i-count+1).get("rssi");
                    double x1 = (double) list.get(i-count+1).get("position_x");
                    double y1 = (double) list.get(i-count+1).get("position_y");
                    if (!device_id1.equals(null)){
                        double finalx = getValueByTwoPointsWeighting((double) x, (double) x1, getDistance(rssi), getDistance(rssi1));
                        double finaly = getValueByTwoPointsWeighting((double) y, (double) y1, getDistance(rssi), getDistance(rssi1));

                        beaconPoint.setX(finalx);
                        beaconPoint.setY(finaly);
                        beaconPoint.setMac_id(mac_id);
                        beaconPoint.setTime(nowTime);
                        beaconPoint.setFloor(floor);
                    }
                }
            }else if (count>=3){
                //三个基站采集到，三点加权求坐标
                DeviceInfo deviceInfo1 = new DeviceInfo() ;
                DeviceInfo deviceInfo2 = new DeviceInfo();
                String device_id1= (String) list.get(i-count+1).get("device_id");
                int rssi1 = (int) list.get(i-count+1).get("rssi");
                String device_id2= (String) list.get(i-count+2).get("device_id");
                int rssi2 = (int) list.get(i-count+2).get("rssi");
                double x1 = (double) list.get(i-count+1).get("position_x");
                double y1 = (double) list.get(i-count+1).get("position_y");
                double x2 = (double) list.get(i-count+2).get("position_x");
                double y2 = (double) list.get(i-count+2).get("position_y");

              //  if (!deviceInfo.getDevice_id().equals(null)){
                    double finalx = getValueByThreePointWeighting((double) x, (double) x1,
                            (double) x2, getDistance(rssi), getDistance(rssi1), getDistance(rssi2));
                    double finaly = getValueByThreePointWeighting((double) y, (double) y1,
                            (double) y2, getDistance(rssi), getDistance(rssi1), getDistance(rssi2));

                    beaconPoint.setX(finalx);
                    beaconPoint.setY(finaly);
                    beaconPoint.setMac_id(mac_id);
                    beaconPoint.setTime(nowTime);
                    beaconPoint.setFloor(floor);

              //  }
            }
            count=0;
            beaconPoints.add(beaconPoint);
        }
        return beaconPoints;
    }

    /**
     * 得到热力点(热力回顾)
     */
    public List<HeatPoint> getBeaconInfoBetweenTimeRangeHasCompute(Timestamp starttime, Timestamp endtime, String floor) {
        System.out.println(new Date(starttime.getTime()));
        List<HeatPoint> result = new ArrayList<>();
        System.out.println(beaconPointsDao);
        List<Map> allList = beaconPointsDao.getHasComputeBetweenTimeRangeOrderByTimeDESC(starttime, endtime, floor);
        Map<Double, Map<Double, Integer>> cur = new HashMap<>();
        Map<Double, Map<Double, Integer>> location = new HashMap<>();
        Map<String, Integer> haveVisit = new HashMap<>();
        for (int i = 0; i < allList.size(); i++) {
            if (haveVisit.get((String)allList.get(i).get("mac_id")) != null) {
                continue;
            }
            haveVisit.put((String)allList.get(i).get("mac_id"), 1);
            double x = getXorY((double)allList.get(i).get("x"));
            double y = getXorY((double)allList.get(i).get("y"));
            if (cur.get(x) != null && cur.get(x).get(y) != null) {
                result.get(location.get(x).get(y)).setCount(result.get(location.get(x).get(y)).getCount() + 1);
            } else {
                HeatPoint heatPoint = new HeatPoint();
                heatPoint.setX(x);
                heatPoint.setY(y);
                heatPoint.setCount(1);
                result.add(heatPoint);

                Map<Double, Integer> temp = new HashMap<>();
                temp.put(y, result.size() - 1);
                location.put(x, temp);

                Map<Double, Integer> temp1 = new HashMap<>();
                temp1.put(y, 1);
                cur.put(x, temp1);
            }
        }
        System.out.println("size::" + result.size());
        for (int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i).getX() + " " + result.get(i).getY() + " " + result.get(i).getCount());
        }
        return result;
    }

    /**
     * 得到热力点(热力统揽)
     */
    public List<HeatPoint> getBeaconInfoBetweenTimeRangeHasComputeLongTime(Timestamp starttime, Timestamp endtime, String floor) {
        List<HeatPoint> result = new ArrayList<>();
        Map<Double, Map<Double, Integer>> cur = new HashMap<>();
        Map<Double, Map<Double, Integer>> location = new HashMap<>();
        long time = starttime.getTime();
        time = time / (1000 * 60) * 1000 * 60;
        List<Map> allList = beaconPointsDao.getHasComputeBetweenTimeRange(new Timestamp(time), endtime, floor);
        int curLocation = 0;
        while (time <= endtime.getTime()) {
            Map<String, Integer> hasVisit = new HashMap<>();
            List<Map> curList = new ArrayList<>();
            long curTime = time + 1000 * 60;
            while (curLocation < allList.size() && ((Timestamp)allList.get(curLocation).get("time")).getTime() <= curTime) {
                if (hasVisit.get((String)allList.get(curLocation).get("mac_id")) == null) {
                    curList.add(allList.get(curLocation));
                    hasVisit.put((String)allList.get(curLocation).get("mac_id"), 1);
                }
                curLocation += 1;
            }
            for (int i = 0; i < curList.size(); i++) {

                double x = getXorY((double)curList.get(i).get("x"));
                double y = getXorY((double)curList.get(i).get("y"));

                if (cur.get(x) != null && cur.get(x).get(y) != null) {
                    result.get(location.get(x).get(y)).setCount(result.get(location.get(x).get(y)).getCount() + 1);
                } else {
                    HeatPoint heatPoint = new HeatPoint();
                    heatPoint.setX(x);
                    heatPoint.setY(y);
                    heatPoint.setCount(1);
                    result.add(heatPoint);

                    Map<Double, Integer> temp = new HashMap<>();
                    temp.put(y, result.size() - 1);
                    location.put(x, temp);

                    Map<Double, Integer> temp1 = new HashMap<>();
                    temp1.put(y, 1);
                    cur.put(x, temp1);
                }
            }
            time = time + 1000 * 60 * 10;
        }
        System.out.println("size::" + result.size());
        return result;
    }

    /**
     * 得到轨迹点集
     */
    public List<TrackPoint> getTrackPointsByVisitorIdBetweenTimeRange(String visitorId,Timestamp startTime,Timestamp endTime ){
        Comparator<DeviceIdRssi> comparator = new Comparator<DeviceIdRssi>() {
            @Override
            public int compare(DeviceIdRssi o1, DeviceIdRssi o2) {
                if (o1!=null && o2!=null){
                    int rssi1 = o1.getRssi();
                    int rssi2 = o2.getRssi();
                    if (rssi1 > rssi2){
                        return -1;//从高到底排序
                    }else if (rssi1 < rssi2){
                        return 1;
                    }
                }
                return 0;
            }
        };
        List<DeviceInfo> deviceInfos = beaconInfoDao.getTotalDevice();
        BeaconVisitor bv = beaconInfoDao.getMacIdFromVisitorId(visitorId);
        List<Map> beconInfos = new ArrayList<>();
        if (bv!=null){
            beconInfos = heatMapDao.getBeaconInfoByMacIdBetweenTimeRange(bv.getMac_id(), startTime, endTime);
        }
        List<TrackPoint> list = new ArrayList<>();
        long start = startTime.getTime()/(1*60*1000)*(1*60*1000)+INTEVALSEC;
        Map<String ,Integer> map = new HashMap<>();//1分钟内的数据
        TrackPoint before_trackPoint = new TrackPoint();//上个历史点
        long before_time =0;//上个历史点时间标志
        for (int i=0;i<beconInfos.size();i++){
                long collectTime = ((Timestamp) beconInfos.get(i).get("collectTime")).getTime();
                String device_id = (String) beconInfos.get(i).get("device_id");
                int rssi = (int) beconInfos.get(i).get("rssi");
                if(collectTime<=start && i<beconInfos.size()-1){
                    if(map.containsKey(device_id)){
                        if (map.get(device_id)<rssi) map.put(device_id,rssi);
                    }else {
                        map.put(device_id,rssi);
                    }
                }else {
                    if(i==beconInfos.size()-1){
                        if(map.containsKey(device_id)){
                            if (map.get(device_id)<rssi) map.put(device_id,rssi);
                        }else {
                            map.put(device_id,rssi);
                        }
                        i++;
                    }
                //处理这1分钟的数据
                 if (map.size()==0) {
                      map.clear();
                      i=i-1;
                      start+=INTEVALSEC;
                      continue;
                 }

                List<DeviceIdRssi> list1 = new ArrayList<>();
                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()){
                    DeviceIdRssi deviceIdRssi = new DeviceIdRssi();
                    String key = iterator.next();
                    Integer value = map.get(key);
                    deviceIdRssi.setDevice_id(key);
                    deviceIdRssi.setRssi(value);
                    list1.add(deviceIdRssi);
                }
                if (list1.size()>1) Collections.sort(list1,comparator);//按rssi从高到底排序
                //求坐标
                if (list1.size()==1){
                    //一个基站采集到，考虑前一周期
                    DeviceIdRssi deviceIdRssi1 = list1.get(0);
                    int rssi1 = deviceIdRssi1.getRssi();
                    String device_id1=deviceIdRssi1.getDevice_id();
                    DeviceInfo deviceInfo1= new DeviceInfo();
                    for (DeviceInfo device :deviceInfos){
                        if (device.getDevice_id().equals(device_id1)) deviceInfo1=device;
                    }
                    if (start - before_time>INTEVALSEC){
                        double x=deviceInfo1.getPosition_x();
                        double y=deviceInfo1.getPosition_y();
                        before_time=start;
                        before_trackPoint.setX(x);
                        before_trackPoint.setY(y);
                        TrackPoint trackPoint = new TrackPoint();
                        trackPoint.setX(x);
                        trackPoint.setY(y);
                        trackPoint.setTime(start);
                        list.add(trackPoint);
                    }else {
                        if (!before_trackPoint.equals(null)){
                            double x = getValueByTwoPointsWeighting(deviceInfo1.getPosition_x(), before_trackPoint.getX(), getDistance(rssi1), getDistance(-95));
                            double y = getValueByTwoPointsWeighting(deviceInfo1.getPosition_y(),before_trackPoint.getY(),getDistance(rssi1),getDistance(-95));
                            before_time=start;
                            before_trackPoint.setX(x);
                            before_trackPoint.setY(y);
                            TrackPoint trackPoint = new TrackPoint();
                            trackPoint.setX(x);
                            trackPoint.setY(y);
                            trackPoint.setTime(start);
                            list.add(trackPoint);
                        }

                    }
                }else if(list1.size()==2){
                    //两个基站采集到
                    DeviceIdRssi deviceIdRssi1 =list1.get(0);
                    DeviceIdRssi deviceIdRssi2 =list1.get(1);
                    int rssi1 = deviceIdRssi1.getRssi();
                    int rssi2 = deviceIdRssi2.getRssi();
                    String device_id1 = deviceIdRssi1.getDevice_id();
                    String device_id2 = deviceIdRssi2.getDevice_id();
                    DeviceInfo deviceInfo1= new DeviceInfo();
                    DeviceInfo deviceInfo2= new DeviceInfo();
                    for (DeviceInfo device :deviceInfos){
                        if (device.getDevice_id().equals(device_id1)) deviceInfo1=device;
                        if (device.getDevice_id().equals(device_id2)) deviceInfo2=device;
                    }
                    double x=0.0,y=0.0;
                    if (start - before_time>INTEVALSEC){
                         x= getValueByTwoPointsWeighting(deviceInfo1.getPosition_x(),deviceInfo2.getPosition_x(),getDistance(rssi1),getDistance(rssi2));
                         y= getValueByTwoPointsWeighting(deviceInfo1.getPosition_y(),deviceInfo2.getPosition_y(),getDistance(rssi1),getDistance(rssi2));
                    }else {
                        if (!before_trackPoint.equals(null)){
                             x = getValueByThreePointWeighting(deviceInfo1.getPosition_x(),deviceInfo2.getPosition_x(),
                                     before_trackPoint.getX(),getDistance(rssi1),getDistance(rssi2),getDistance(-95));
                             y = getValueByThreePointWeighting(deviceInfo1.getPosition_y(),deviceInfo2.getPosition_y(),
                                     before_trackPoint.getY(),getDistance(rssi1),getDistance(rssi2),getDistance(-95));
                        }
                    }
                    //double x = getValueByTwoPointsWeighting(deviceInfo1.getPosition_x(),deviceInfo2.getPosition_x(),getDistance(rssi1),getDistance(rssi2));
                    //double y = getValueByTwoPointsWeighting(deviceInfo1.getPosition_y(),deviceInfo2.getPosition_y(),getDistance(rssi1),getDistance(rssi2));
                    before_time=start;
                    before_trackPoint.setX(x);
                    before_trackPoint.setY(y);
                    TrackPoint trackPoint = new TrackPoint();
                    trackPoint.setX(x);
                    trackPoint.setY(y);
                    trackPoint.setTime(start);
                    list.add(trackPoint);
                }else if(list1.size()>=3){
                    //三个基站及以上采集到
                    DeviceIdRssi deviceIdRssi1 =list1.get(0);
                    DeviceIdRssi deviceIdRssi2 =list1.get(1);
                    DeviceIdRssi deviceIdRssi3 =list1.get(2);
                    int rssi1 = deviceIdRssi1.getRssi();
                    int rssi2 = deviceIdRssi2.getRssi();
                    int rssi3 = deviceIdRssi3.getRssi();
                    String device_id1 = deviceIdRssi1.getDevice_id();
                    String device_id2 = deviceIdRssi2.getDevice_id();
                    String device_id3 = deviceIdRssi3.getDevice_id();
                    DeviceInfo deviceInfo1= new DeviceInfo();
                    DeviceInfo deviceInfo2= new DeviceInfo();
                    DeviceInfo deviceInfo3= new DeviceInfo();
                    for (DeviceInfo device :deviceInfos){
                        if (device.getDevice_id().equals(device_id1)) deviceInfo1=device;
                        if (device.getDevice_id().equals(device_id2)) deviceInfo2=device;
                        if (device.getDevice_id().equals(device_id3)) deviceInfo3=device;
                    }
                    double x= getValueByThreePointWeighting(deviceInfo1.getPosition_x(),deviceInfo2.getPosition_x(),
                            deviceInfo3.getPosition_x(),getDistance(rssi1),getDistance(rssi2),getDistance(rssi3));
                    double y= getValueByThreePointWeighting(deviceInfo1.getPosition_y(),deviceInfo2.getPosition_y(),
                            deviceInfo3.getPosition_y(),getDistance(rssi1),getDistance(rssi2),getDistance(rssi3));
                    before_time=start;
                    before_trackPoint.setX(x);
                    before_trackPoint.setY(y);
                    TrackPoint trackPoint = new TrackPoint();
                    trackPoint.setX(x);
                    trackPoint.setY(y);
                    trackPoint.setTime(start);
                    list.add(trackPoint);
                }
                    map.clear();
                    i=i-1;
                    start+=INTEVALSEC;

            }

        }
        return list;
    }


    //动线轨迹(取最近时间)
    public List<TrackPoint> getTrackPointsByVisitorIdBetweenTimeRange1(String visitorId,Timestamp startTime,Timestamp endTime){
        Comparator<DeviceIdRssi> comparator = new Comparator<DeviceIdRssi>() {
            @Override
            public int compare(DeviceIdRssi o1, DeviceIdRssi o2) {
                if (o1!=null && o2!=null){
                    int rssi1 = o1.getRssi();
                    int rssi2 = o2.getRssi();
                    if (rssi1 > rssi2){
                        return -1;//从高到底排序
                    }else if (rssi1 < rssi2){
                        return 1;
                    }
                }
                return 0;
            }
        };
        List<DeviceInfo> deviceInfos = beaconInfoDao.getTotalDevice();
        BeaconVisitor bv = beaconInfoDao.getMacIdFromVisitorId(visitorId);
        List<Map> beconInfos = new ArrayList<>();
        if (bv!=null){
            beconInfos = heatMapDao.getBeaconInfoByMacIdBetweenTimeRange(bv.getMac_id(), startTime, endTime);
        }
        List<TrackPoint> list = new ArrayList<>();
        long start = startTime.getTime()/(1*60*1000)*(1*60*1000)+INTEVALSEC_TEST;
        Map<String ,Integer> map = new HashMap<>();//1分钟内的数据
        TrackPoint before_trackPoint = new TrackPoint();//上个历史点
        long before_time =0;//上个历史点时间标志
        for (int i=0;i<beconInfos.size();i++){
            long collectTime = ((Timestamp) beconInfos.get(i).get("collectTime")).getTime();
            String device_id = (String) beconInfos.get(i).get("device_id");
            int rssi = (int) beconInfos.get(i).get("rssi");
            if(collectTime<=start && i<beconInfos.size()-1){
                if(map.containsKey(device_id)){
                    map.put(device_id,rssi);
                }else {
                    map.put(device_id,rssi);
                }
            }else {
                if(i==beconInfos.size()-1){
                    if(map.containsKey(device_id)){
                        map.put(device_id,rssi);
                    }else {
                        map.put(device_id,rssi);
                    }
                    i++;
                }
                //处理这1分钟的数据
                if (map.size()==0) {
                    map.clear();
                    i=i-1;
                    start+=INTEVALSEC_TEST;
                    continue;
                }

                List<DeviceIdRssi> list1 = new ArrayList<>();
                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()){
                    DeviceIdRssi deviceIdRssi = new DeviceIdRssi();
                    String key = iterator.next();
                    Integer value = map.get(key);
                    deviceIdRssi.setDevice_id(key);
                    deviceIdRssi.setRssi(value);
                    list1.add(deviceIdRssi);
                }
                if (list1.size()>1) Collections.sort(list1,comparator);//按rssi从高到底排序
                //求坐标
                if (list1.size()==1){
                    //一个基站采集到，考虑前一周期
                    DeviceIdRssi deviceIdRssi1 = list1.get(0);
                    int rssi1 = deviceIdRssi1.getRssi();
                    String device_id1=deviceIdRssi1.getDevice_id();
                    DeviceInfo deviceInfo1= new DeviceInfo();
                    for (DeviceInfo device :deviceInfos){
                        if (device.getDevice_id().equals(device_id1)) deviceInfo1=device;
                    }
                    if (start - before_time>INTEVALSEC_TEST){
                        double x=deviceInfo1.getPosition_x();
                        double y=deviceInfo1.getPosition_y();
                        before_time=start;
                        before_trackPoint.setX(x);
                        before_trackPoint.setY(y);
                        TrackPoint trackPoint = new TrackPoint();
                        trackPoint.setX(x);
                        trackPoint.setY(y);
                        trackPoint.setTime(start);
                        list.add(trackPoint);
                    }else {
                        if (!before_trackPoint.equals(null)){
                            double x = getValueByTwoPointsWeighting(deviceInfo1.getPosition_x(), before_trackPoint.getX(), getDistance(rssi1), getDistance(-95));
                            double y = getValueByTwoPointsWeighting(deviceInfo1.getPosition_y(),before_trackPoint.getY(),getDistance(rssi1),getDistance(-95));
                            before_time=start;
                            before_trackPoint.setX(x);
                            before_trackPoint.setY(y);
                            TrackPoint trackPoint = new TrackPoint();
                            trackPoint.setX(x);
                            trackPoint.setY(y);
                            trackPoint.setTime(start);
                            list.add(trackPoint);
                        }

                    }
                }else if(list1.size()==2){
                    //两个基站采集到
                    DeviceIdRssi deviceIdRssi1 =list1.get(0);
                    DeviceIdRssi deviceIdRssi2 =list1.get(1);
                    int rssi1 = deviceIdRssi1.getRssi();
                    int rssi2 = deviceIdRssi2.getRssi();
                    String device_id1 = deviceIdRssi1.getDevice_id();
                    String device_id2 = deviceIdRssi2.getDevice_id();
                    DeviceInfo deviceInfo1= new DeviceInfo();
                    DeviceInfo deviceInfo2= new DeviceInfo();
                    for (DeviceInfo device :deviceInfos){
                        if (device.getDevice_id().equals(device_id1)) deviceInfo1=device;
                        if (device.getDevice_id().equals(device_id2)) deviceInfo2=device;
                    }
                    double x=0.0,y=0.0;
                    if (start - before_time>INTEVALSEC_TEST){
                        x= getValueByTwoPointsWeighting(deviceInfo1.getPosition_x(),deviceInfo2.getPosition_x(),getDistance(rssi1),getDistance(rssi2));
                        y= getValueByTwoPointsWeighting(deviceInfo1.getPosition_y(),deviceInfo2.getPosition_y(),getDistance(rssi1),getDistance(rssi2));
                    }else {
                        if (!before_trackPoint.equals(null)){
                            x = getValueByThreePointWeighting(deviceInfo1.getPosition_x(),deviceInfo2.getPosition_x(),
                                    before_trackPoint.getX(),getDistance(rssi1),getDistance(rssi2),getDistance(-95));
                            y = getValueByThreePointWeighting(deviceInfo1.getPosition_y(),deviceInfo2.getPosition_y(),
                                    before_trackPoint.getY(),getDistance(rssi1),getDistance(rssi2),getDistance(-95));
                        }
                    }
                    //double x = getValueByTwoPointsWeighting(deviceInfo1.getPosition_x(),deviceInfo2.getPosition_x(),getDistance(rssi1),getDistance(rssi2));
                    //double y = getValueByTwoPointsWeighting(deviceInfo1.getPosition_y(),deviceInfo2.getPosition_y(),getDistance(rssi1),getDistance(rssi2));
                    before_time=start;
                    before_trackPoint.setX(x);
                    before_trackPoint.setY(y);
                    TrackPoint trackPoint = new TrackPoint();
                    trackPoint.setX(x);
                    trackPoint.setY(y);
                    trackPoint.setTime(start);
                    list.add(trackPoint);
                }else if(list1.size()>=3){
                    //三个基站及以上采集到
                    DeviceIdRssi deviceIdRssi1 =list1.get(0);
                    DeviceIdRssi deviceIdRssi2 =list1.get(1);
                    DeviceIdRssi deviceIdRssi3 =list1.get(2);
                    int rssi1 = deviceIdRssi1.getRssi();
                    int rssi2 = deviceIdRssi2.getRssi();
                    int rssi3 = deviceIdRssi3.getRssi();
                    String device_id1 = deviceIdRssi1.getDevice_id();
                    String device_id2 = deviceIdRssi2.getDevice_id();
                    String device_id3 = deviceIdRssi3.getDevice_id();
                    DeviceInfo deviceInfo1= new DeviceInfo();
                    DeviceInfo deviceInfo2= new DeviceInfo();
                    DeviceInfo deviceInfo3= new DeviceInfo();
                    for (DeviceInfo device :deviceInfos){
                        if (device.getDevice_id().equals(device_id1)) deviceInfo1=device;
                        if (device.getDevice_id().equals(device_id2)) deviceInfo2=device;
                        if (device.getDevice_id().equals(device_id3)) deviceInfo3=device;
                    }
                    double x= getValueByThreePointWeighting(deviceInfo1.getPosition_x(),deviceInfo2.getPosition_x(),
                            deviceInfo3.getPosition_x(),getDistance(rssi1),getDistance(rssi2),getDistance(rssi3));
                    double y= getValueByThreePointWeighting(deviceInfo1.getPosition_y(),deviceInfo2.getPosition_y(),
                            deviceInfo3.getPosition_y(),getDistance(rssi1),getDistance(rssi2),getDistance(rssi3));
                    before_time=start;
                    before_trackPoint.setX(x);
                    before_trackPoint.setY(y);
                    TrackPoint trackPoint = new TrackPoint();
                    trackPoint.setX(x);
                    trackPoint.setY(y);
                    trackPoint.setTime(start);
                    list.add(trackPoint);
                }
                map.clear();
                i=i-1;
                start+=INTEVALSEC_TEST;

            }

        }
        return list;
    }

    /**
     * 根据计算过的数据计算动线的点集
     *
     */
    public List<TrackPoint> getTrackPointsByVisitorIdBetweenTimeRange2(String visitorId,Timestamp startTime,Timestamp endTime, String floor){
        List<TrackPoint> result = new ArrayList<>();
        BeaconVisitor bv = beaconInfoDao.getMacIdFromVisitorId(visitorId);
        List<Map> allList = beaconPointsDao.getHasComputeBetweenTimeRangeWithVisitor(
                new Timestamp(startTime.getTime() / (1000 * 60) * 1000 * 60),
                new Timestamp(endTime.getTime() / (1000 * 60) * 1000 * 60),
                floor,
                bv.getMac_id());

        long curStop = startTime.getTime() + 1000 * 60;
        int curLocation = 1;
        Map<Integer, Integer> visit = new HashMap<>();
        if (allList.size() == 0) {
            return result;
        }
        while (curStop <= endTime.getTime()) {
            while (curLocation < allList.size() && curStop > ((Timestamp)allList.get(curLocation).get("time")).getTime()) {
                curLocation++;
            }
            if (visit.get(curLocation - 1) == null) {
                TrackPoint p = new TrackPoint();
                p.setX((double) allList.get(curLocation - 1).get("x"));
                p.setY((double) allList.get(curLocation - 1).get("y"));
                p.setTime(((Timestamp) allList.get(curLocation - 1).get("time")).getTime());
                result.add(p);
                visit.put(curLocation - 1, 1);
            }
            curStop = curStop + 1000 * 60;
        }
        return result;
    }

    /**
     * 根据visitor_id搜索最近出现的位置(传数组)
     *
     */
    public List<Location> getLocationByVisitorId(String[] visitor_ids){
        List<Location> list = new ArrayList<>();
        List<DeviceInfo> deviceInfos = beaconInfoDao.getTotalDevice();
        for (int i=0;i<visitor_ids.length;i++){
            String visitor_id=visitor_ids[i];
            Location location = new Location();
            String mac_id=userDao.getMacIdByVisitorId(visitor_id);
            if (mac_id==null){
                continue;
            }
            Timestamp endTime=heatMapDao.getLatestTimeByVisitorId(visitor_id);
            if (endTime==null||endTime.equals("")){
                continue;
            }
            Timestamp startTime = new Timestamp(endTime.getTime()-1*30*1000);
            Timestamp history_startTime = new Timestamp(endTime.getTime()-1*60*1000);
            Visitor visitor = userDao.getVisitorById(visitor_id);
            List<Map> beconInfos=heatMapDao.getMaxRssiGroupByDeviceByMacId(mac_id, startTime, endTime);
            List<Map> historyInfos=heatMapDao.getMaxRssiGroupByDeviceByMacId(mac_id,history_startTime,startTime);//历史数据
            location.setTime(endTime);
            location.setTimeString(String.valueOf(endTime));
            if (visitor!=null){
                location.setName(visitor.getName());
            }

            if (beconInfos.size()==1){
                //一个基站采集到
                String device_id1 = (String) beconInfos.get(0).get("device_id");
                int rssi1 = (int) beconInfos.get(0).get("rssi");
                DeviceInfo deviceInfo1= new DeviceInfo();
                for (DeviceInfo device :deviceInfos){
                    if (device.getDevice_id().equals(device_id1)){
                        deviceInfo1=device;
                        break;
                    }
                }
                for (int j=0;j<historyInfos.size();j++){
                    String deviceId= (String) historyInfos.get(j).get("device_id");
                    if (deviceId.equals(device_id1)){
                        historyInfos.remove(historyInfos.get(j));
                        j--;
                    }
                }
                if (historyInfos.size()==0){
                    location.setX(deviceInfo1.getPosition_x());
                    location.setY(deviceInfo1.getPosition_y());
                }else if (historyInfos.size()==1){
                    DeviceInfo deviceInfo2=new DeviceInfo();
                    String device_id2= (String) historyInfos.get(0).get("device_id");
                    int rssi2= (int) historyInfos.get(0).get("rssi");
                    for (DeviceInfo device :deviceInfos){
                        if (device.getDevice_id().equals(device_id2)){
                            deviceInfo2=device;
                            break;
                        }
                    }
                    location.setX(getValueByTwoPointsWeighting(deviceInfo1.getPosition_x(),deviceInfo2.getPosition_x(),getDistance(rssi1),getDistance(rssi2)));
                    location.setY(getValueByTwoPointsWeighting(deviceInfo1.getPosition_y(),deviceInfo2.getPosition_y(),getDistance(rssi1),getDistance(rssi2)));
                }else if (historyInfos.size()>=2){
                    DeviceInfo deviceInfo2 =new DeviceInfo();
                    DeviceInfo deviceInfo3 =new DeviceInfo();
                    String device_id2= (String) historyInfos.get(0).get("device_id");
                    int rssi2 = (int) historyInfos.get(0).get("rssi");
                    String device_id3= (String) historyInfos.get(1).get("device_id");
                    int rssi3 = (int) historyInfos.get(1).get("rssi");
                    for (DeviceInfo device :deviceInfos){
                        if (device.getDevice_id().equals(device_id2)) deviceInfo2= device;
                        if (device.getDevice_id().equals(device_id3)) deviceInfo3= device;
                    }
                    location.setX(getValueByThreePointWeighting(deviceInfo1.getPosition_x(),deviceInfo2.getPosition_x(),deviceInfo3.getPosition_x(),
                            getDistance(rssi1),getDistance(rssi2),getDistance(rssi3)));
                    location.setY(getValueByThreePointWeighting(deviceInfo1.getPosition_y(),deviceInfo2.getPosition_y(),deviceInfo3.getPosition_y(),
                            getDistance(rssi1),getDistance(rssi2),getDistance(rssi3)));
                }
            }else if (beconInfos.size()==2){
                //两个基站采集到
                String device_id1= (String) beconInfos.get(0).get("device_id");
                int rssi1= (int) beconInfos.get(0).get("rssi");
                String device_id2= (String) beconInfos.get(1).get("device_id");
                int rssi2= (int) beconInfos.get(1).get("rssi");
                DeviceInfo deviceInfo1= new DeviceInfo();
                DeviceInfo deviceInfo2= new DeviceInfo();
                for (DeviceInfo device :deviceInfos){
                    if (device.getDevice_id().equals(device_id1)) deviceInfo1= device;
                    if (device.getDevice_id().equals(device_id2)) deviceInfo2= device;
                }
                for (int j=0;j<historyInfos.size();j++){
                    String deviceId= (String) historyInfos.get(j).get("device_id");
                    if (deviceId.equals(device_id1) || deviceId.equals(device_id2)){
                        historyInfos.remove(historyInfos.get(j));
                        j--;
                    }
                }
                if (historyInfos.size()==0){
                    location.setX(getValueByTwoPointsWeighting(deviceInfo1.getPosition_x(),deviceInfo2.getPosition_x(),getDistance(rssi1),getDistance(rssi2)));
                    location.setY(getValueByTwoPointsWeighting(deviceInfo1.getPosition_y(),deviceInfo2.getPosition_y(),getDistance(rssi1),getDistance(rssi2)));
                }else {
                    DeviceInfo deviceInfo3=new DeviceInfo();
                    String device_id3= (String) historyInfos.get(0).get("device_id");
                    int rssi3= (int) historyInfos.get(0).get("rssi");
                    for (DeviceInfo device :deviceInfos){
                        if (device.getDevice_id().equals(device_id3)){
                            deviceInfo3=device;
                            break;
                        }
                    }
                    location.setX(getValueByThreePointWeighting(deviceInfo1.getPosition_x(),deviceInfo2.getPosition_x(),deviceInfo3.getPosition_x(),
                            getDistance(rssi1),getDistance(rssi2),getDistance(-95)));
                    location.setY(getValueByThreePointWeighting(deviceInfo1.getPosition_y(),deviceInfo2.getPosition_y(),deviceInfo3.getPosition_y(),
                            getDistance(rssi1),getDistance(rssi2),getDistance(-95)));
                }
            }else if (beconInfos.size()>=3){
                //三个基站及以上采集到
                String device_id1= (String) beconInfos.get(0).get("device_id");
                int rssi1= (int) beconInfos.get(0).get("rssi");
                String device_id2= (String) beconInfos.get(1).get("device_id");
                int rssi2= (int) beconInfos.get(1).get("rssi");
                String device_id3= (String) beconInfos.get(2).get("device_id");
                int rssi3= (int) beconInfos.get(2).get("rssi");
                DeviceInfo deviceInfo1= new DeviceInfo();
                DeviceInfo deviceInfo2= new DeviceInfo();
                DeviceInfo deviceInfo3= new DeviceInfo();
                for (DeviceInfo device :deviceInfos){
                    if (device.getDevice_id().equals(device_id1)) deviceInfo1= device;
                    if (device.getDevice_id().equals(device_id2)) deviceInfo2= device;
                    if (device.getDevice_id().equals(device_id3)) deviceInfo3= device;
                }
                location.setX(getValueByThreePointWeighting(deviceInfo1.getPosition_x(),deviceInfo2.getPosition_x(),deviceInfo3.getPosition_x(),
                        getDistance(rssi1),getDistance(rssi2),getDistance(rssi3)));
                location.setY(getValueByThreePointWeighting(deviceInfo1.getPosition_y(),deviceInfo2.getPosition_y(),deviceInfo3.getPosition_y(),
                        getDistance(rssi1),getDistance(rssi2),getDistance(rssi3)));
            }
            list.add(location);
        }

        return list;
    }

    /**
     * 根据visitor_id搜索最近出现的位置(传String)
     */
    public Location getLocationByVisitorId(String visitor_id){
        List<DeviceInfo> deviceInfos = beaconInfoDao.getTotalDevice();
        Location location = new Location();
        String mac_id=userDao.getMacIdByVisitorId(visitor_id);
        if (mac_id==null){
            return location;
        }
        Timestamp endTime=heatMapDao.getLatestTimeByVisitorId(visitor_id);
        if (endTime==null||endTime.equals("")){
            return location;
        }
        Timestamp startTime = new Timestamp(endTime.getTime()-1*30*1000);//前30s
        Timestamp history_startTime = new Timestamp(endTime.getTime()-1*60*1000);
        Visitor visitor = userDao.getVisitorById(visitor_id);
        List<Map> beconInfos=heatMapDao.getMaxRssiGroupByDeviceByMacId(mac_id, startTime, endTime);
        List<Map> historyInfos=heatMapDao.getMaxRssiGroupByDeviceByMacId(mac_id,history_startTime,startTime);//历史数据
        location.setTime(endTime);
        location.setTimeString(String.valueOf(endTime));
        if (visitor!=null){
            location.setName(visitor.getName());
        }

        if (beconInfos.size()==1){
            //一个基站采集到
            String device_id1 = (String) beconInfos.get(0).get("device_id");
            int rssi1 = (int) beconInfos.get(0).get("rssi");
            DeviceInfo deviceInfo1= new DeviceInfo();
            for (DeviceInfo device :deviceInfos){
                if (device.getDevice_id().equals(device_id1)){
                    deviceInfo1=device;
                    break;
                }
            }
            for (int j=0;j<historyInfos.size();j++){
                String deviceId= (String) historyInfos.get(j).get("device_id");
                if (deviceId.equals(device_id1)){
                    historyInfos.remove(historyInfos.get(j));
                    j--;
                }
            }
            if (historyInfos.size()==0){
                location.setX(deviceInfo1.getPosition_x());
                location.setY(deviceInfo1.getPosition_y());
            }else if (historyInfos.size()==1){
                DeviceInfo deviceInfo2=new DeviceInfo();
                String device_id2= (String) historyInfos.get(0).get("device_id");
                int rssi2= (int) historyInfos.get(0).get("rssi");
                for (DeviceInfo device :deviceInfos){
                    if (device.getDevice_id().equals(device_id2)){
                        deviceInfo2=device;
                        break;
                    }
                }
                location.setX(getValueByTwoPointsWeighting(deviceInfo1.getPosition_x(),deviceInfo2.getPosition_x(),getDistance(rssi1),getDistance(rssi2)));
                location.setY(getValueByTwoPointsWeighting(deviceInfo1.getPosition_y(),deviceInfo2.getPosition_y(),getDistance(rssi1),getDistance(rssi2)));
            }else if (historyInfos.size()>=2){
                DeviceInfo deviceInfo2 =new DeviceInfo();
                DeviceInfo deviceInfo3 =new DeviceInfo();
                String device_id2= (String) historyInfos.get(0).get("device_id");
                int rssi2 = (int) historyInfos.get(0).get("rssi");
                String device_id3= (String) historyInfos.get(1).get("device_id");
                int rssi3 = (int) historyInfos.get(1).get("rssi");
                for (DeviceInfo device :deviceInfos){
                    if (device.getDevice_id().equals(device_id2)) deviceInfo2= device;
                    if (device.getDevice_id().equals(device_id3)) deviceInfo3= device;
                }
                location.setX(getValueByThreePointWeighting(deviceInfo1.getPosition_x(),deviceInfo2.getPosition_x(),deviceInfo3.getPosition_x(),
                        getDistance(rssi1),getDistance(rssi2),getDistance(rssi3)));
                location.setY(getValueByThreePointWeighting(deviceInfo1.getPosition_y(),deviceInfo2.getPosition_y(),deviceInfo3.getPosition_y(),
                        getDistance(rssi1),getDistance(rssi2),getDistance(rssi3)));
            }
        }else if (beconInfos.size()==2){
            //两个基站采集到
            String device_id1= (String) beconInfos.get(0).get("device_id");
            int rssi1= (int) beconInfos.get(0).get("rssi");
            String device_id2= (String) beconInfos.get(1).get("device_id");
            int rssi2= (int) beconInfos.get(1).get("rssi");
            DeviceInfo deviceInfo1= new DeviceInfo();
            DeviceInfo deviceInfo2= new DeviceInfo();
            for (DeviceInfo device :deviceInfos){
                if (device.getDevice_id().equals(device_id1)) deviceInfo1= device;
                if (device.getDevice_id().equals(device_id2)) deviceInfo2= device;
            }
            for (int j=0;j<historyInfos.size();j++){
                String deviceId= (String) historyInfos.get(j).get("device_id");
                if (deviceId.equals(device_id1) || deviceId.equals(device_id2)){
                    historyInfos.remove(historyInfos.get(j));
                    j--;
                }
            }
            if (historyInfos.size()==0){
                location.setX(getValueByTwoPointsWeighting(deviceInfo1.getPosition_x(),deviceInfo2.getPosition_x(),getDistance(rssi1),getDistance(rssi2)));
                location.setY(getValueByTwoPointsWeighting(deviceInfo1.getPosition_y(),deviceInfo2.getPosition_y(),getDistance(rssi1),getDistance(rssi2)));
            }else {
                DeviceInfo deviceInfo3=new DeviceInfo();
                String device_id3= (String) historyInfos.get(0).get("device_id");
                int rssi3= (int) historyInfos.get(0).get("rssi");
                for (DeviceInfo device :deviceInfos){
                    if (device.getDevice_id().equals(device_id3)){
                        deviceInfo3=device;
                        break;
                    }
                }
                location.setX(getValueByThreePointWeighting(deviceInfo1.getPosition_x(),deviceInfo2.getPosition_x(),deviceInfo3.getPosition_x(),
                        getDistance(rssi1),getDistance(rssi2),getDistance(-95)));
                location.setY(getValueByThreePointWeighting(deviceInfo1.getPosition_y(),deviceInfo2.getPosition_y(),deviceInfo3.getPosition_y(),
                        getDistance(rssi1),getDistance(rssi2),getDistance(-95)));//-95方向辅助
            }
        }else if (beconInfos.size()>=3){
            //三个基站及以上采集到
            String device_id1= (String) beconInfos.get(0).get("device_id");
            int rssi1= (int) beconInfos.get(0).get("rssi");
            String device_id2= (String) beconInfos.get(1).get("device_id");
            int rssi2= (int) beconInfos.get(1).get("rssi");
            String device_id3= (String) beconInfos.get(2).get("device_id");
            int rssi3= (int) beconInfos.get(2).get("rssi");
            DeviceInfo deviceInfo1= new DeviceInfo();
            DeviceInfo deviceInfo2= new DeviceInfo();
            DeviceInfo deviceInfo3= new DeviceInfo();
            for (DeviceInfo device :deviceInfos){
                if (device.getDevice_id().equals(device_id1)) deviceInfo1= device;
                if (device.getDevice_id().equals(device_id2)) deviceInfo2= device;
                if (device.getDevice_id().equals(device_id3)) deviceInfo3= device;
            }
            location.setX(getValueByThreePointWeighting(deviceInfo1.getPosition_x(),deviceInfo2.getPosition_x(),deviceInfo3.getPosition_x(),
                        getDistance(rssi1),getDistance(rssi2),getDistance(rssi3)));
            location.setY(getValueByThreePointWeighting(deviceInfo1.getPosition_y(),deviceInfo2.getPosition_y(),deviceInfo3.getPosition_y(),
                        getDistance(rssi1),getDistance(rssi2),getDistance(rssi3)));
            }
        return location;
    }

    /**
     * 得到热力点的X,Y坐标值(整数)
     * 不是四舍五入 ,2*2的点
     */
//    public double getXorY(Double i) {
//        return (int) ((i/3+1)*3) - 1.5;
//    }
    public double getXorY(Double i) {
        String t = i.toString();
        t = t.substring(0, t.indexOf("."));
        int d = Integer.parseInt(t);
        return (d / 3) * 3 + 1.5;
    }
    /**
     * 根据RSSI值划分距离
     */
    public double getDistance(int rssi) {
        double distance = 0;
        if (rssi >= -79) {
            distance = 1;
        } else if (rssi <= -80 && rssi >= -84) {
            distance = 2;
        } else if (rssi <= -85 && rssi >= -87) {
            distance = 3.5;
        } else if (rssi <= -88 && rssi >= -91) {
            distance = 6;
        }else if (rssi <= -92 && rssi >= -93) {
            distance = 7;
        }else if (rssi <= -93) {
            distance = 9;
        }
        return distance;
    }

    //大Beacon
    /*public int getDistance(int rssi){
        int distance = 0;
        if (rssi>=-78){
            distance=1;
        }else if (rssi<=-79 && rssi>=-82){
            distance=2;
        }else if (rssi<=-83 && rssi>=-85){
            distance=3;
        }else if (rssi<=-86 && rssi>=-88){
            distance=5;
        }else if (rssi<=-89 && rssi>=-91){
            distance=7;
        }else if (rssi<=-92 && rssi>=-94){
            distance=9;
        }else {
            distance=10;
        }
        return distance;
    }*/

    /**
     *两点加权求值
     */
    public Double getValueByTwoPointsWeighting(Double value1,Double value2,double d1,double d2){
        return ((value1/d1)+(value2/d2))/((1.0/d1)+(1.0/d2));
    }
    /**
     * 三点加权求值
     */
    public Double getValueByThreePointWeighting(Double value1,Double value2,Double value3,double d1,double d2,double d3){
        Double member1= value1/(d1+d2) + value2/(d2+d3) +value3/(d1+d3);
        Double member2= 1.0/(d1+d2) + 1.0/(d2+d3) +1.0/(d1+d3);
        return member1/member2;
    }

    /**
     * 将字符串时间转化为自定义格式的日期对象
     * @param strDate 字符串时间
     * @return Date 日期对象 yyyy-MM-dd 00:00:00
     */
    public static Date string2Date(String strDate,String patten) {
        DateFormat format = new SimpleDateFormat(patten);
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}
