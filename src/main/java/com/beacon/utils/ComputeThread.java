package com.beacon.utils;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.beacon.dao.BeaconInfoDao;
import com.beacon.dao.BeaconPointsDao;
import com.beacon.dao.HeatMapDao;
import com.beacon.dao.TestDataDao;
import com.beacon.daoService.HeatMapService;
import com.beacon.model.BeaconPointInfo;
import com.beacon.model.*;
import com.sun.xml.internal.fastinfoset.algorithm.IntEncodingAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ks on 2017/6/29.
 */
@Component
public class ComputeThread {
    @Autowired
    private BeaconPointsDao beaconPointsDao;
    @Autowired
    private HeatMapService heatMapService;
    @Autowired
    private TestDataDao testDataDao;

    private Date date;
    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp chaoTime;
    private int num;

    public ComputeThread() {
        date = new Date();
        //startTime = new Timestamp((date.getTime() - 2 * 1000 * 60) / (1000 * 60) * 1000 * 60);
        //startTime = new Timestamp(string2Date("2017-07-20 16:44:00", "yyyy-MM-dd HH:mm:ss").getTime());
        startTime = new Timestamp((date.getTime() - 2 * 1000 * 60) / (1000 * 60) * 1000 * 60);
        endTime = new Timestamp(startTime.getTime() + 1000 * 60);
        chaoTime = new Timestamp(startTime.getTime());
        num = 0;
    }

    public void work()
    {
//        if (num == 0) {
//            testData();
//        }
//        num += 1;
        System.out.println("work done----------" + new Date().getTime());
        computeCurentLocation();
        startTime = new Timestamp(startTime.getTime() + 1000 * 60);
        endTime = new Timestamp(endTime.getTime() + 1000 * 60);
    }

    public void computeCurentLocation() {
        List<BeaconPointInfo> beaconPoints = new ArrayList<>();
        beaconPoints.addAll(heatMapService.getBeaconInfoBetweenTimeRangeNew(startTime, endTime));
        for (int i = 0; i < beaconPoints.size(); i++) {
            beaconPointsDao.addBeaconPoints(beaconPoints.get(i));
        }
    }

    public void testData() {
        Map<Long, Map<String, List<Integer>> > queue = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            queue.put((long)i, new HashMap<String, List<Integer>>());
        }
        Map<String, Integer> sum = new HashMap<>();
        Map<String, Integer> count = new HashMap<>();
        List<Map> result = testDataDao.getBeaconInfoData();
        int cur = 0;
        System.out.println(result.get(cur).get("collectTime"));
        try {
            while (chaoTime.getTime() < string2Date("2017-07-20 17:10:00", "yyyy-MM-dd HH:mm:ss").getTime()) {
                long index = chaoTime.getTime() / 1000 % 5;
                Map<String, List<Integer>> curList = queue.getOrDefault(index, new HashMap<String, List<Integer>>());
                for (Map.Entry<String, List<Integer>> entry : curList.entrySet()) {
                    //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    List<Integer> nowList = entry.getValue();
                    for (int i = 0; i < nowList.size(); i++) {
                        sum.put(entry.getKey(), sum.getOrDefault(entry.getKey(), 0) - nowList.get(i));
                        count.put(entry.getKey(), count.getOrDefault(entry.getKey(), 0) - 1);
                    }
                }

                queue.getOrDefault(index, new HashMap<String, List<Integer>>()).clear();

                while (chaoTime.getTime() / 1000 * 1000 == ((Timestamp) result.get(cur).get("collectTime")).getTime() / 1000 * 1000) {

                    String macAndDevice = (String) result.get(cur).get("mac_id") + (String) result.get(cur).get("device_id");
                    int rssi = (Integer) result.get(cur).get("rssi");

                    if (queue.get(index).get(macAndDevice) == null) {
                        queue.get(index).put(macAndDevice, new ArrayList<Integer>());
                    }
                    queue.get(index).get(macAndDevice).add(rssi);

                    sum.put(macAndDevice, sum.getOrDefault(macAndDevice, 0) + rssi);
                    count.put(macAndDevice, count.getOrDefault(macAndDevice, 0) + 1);

                    TestDataInfo testDataInfo = new TestDataInfo();
                    testDataInfo.setCollectTime((Timestamp) result.get(cur).get("collectTime"));
                    testDataInfo.setDevice_id((String) result.get(cur).get("device_id"));
                    testDataInfo.setMac_id((String) result.get(cur).get("mac_id"));
                    testDataInfo.setMajor((Integer) result.get(cur).get("major"));
                    testDataInfo.setMinor((Integer) result.get(cur).get("minor"));
                    System.out.println(sum.get(macAndDevice));
                    System.out.println(count.get(macAndDevice));
                    System.out.println(sum.get(macAndDevice) / count.get(macAndDevice));
                    testDataInfo.setRssi(sum.get(macAndDevice) / count.get(macAndDevice));
                    testDataInfo.setUuid((String) result.get(cur).get("uuid"));
                    testDataDao.addTestData(testDataInfo);
                    cur++;
                }

                chaoTime = new Timestamp(chaoTime.getTime() + 1000);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
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
