package com.beacon.daoService;

import com.beacon.dao.DeviceInfoDao;
import com.beacon.entity.FloorInfo;
import com.beacon.entity.OriginData;
import com.beacon.model.*;
import com.beacon.utils.Constant;
import com.beacon.utils.HttpClientUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhanda on 2016/10/26.
 */



@Service("deviceInfoService")
public class DeviceInfoService {
    @Autowired
    private DeviceInfoDao deviceInfoDao;

    //保存deviceinfo
    public boolean inputDeviceInfo (DeviceInfo deviceInfo) {
        try {
            deviceInfoDao.inputDeviceInfo(deviceInfo);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //保存beacon设备信息
    @Transactional
    public boolean inputBeaconList (List<Beacon> beaconList) {
        try {
            for (Beacon b:beaconList) {
                System.out.println("");
                System.out.println("Beacon_mac_id:"+b.getMac_id());
                System.out.println("Beacon_uuid:"+b.getUuid());
                System.out.println("Beacon_major:"+b.getMajor());
                System.out.println("Beacon_minor:"+b.getMinor());
                deviceInfoDao.inputBeacon(b);
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }


    //保存beacon和visitor绑定的信息
    @Transactional
    public boolean inputBeaconVisitorList(List<BeaconVisitor> beaconVisitors) {
        try {
            for (BeaconVisitor b:beaconVisitors) {
                System.out.println("");
                System.out.println("Beacon_mac_id:"+b.getMac_id());
                System.out.println("Beacon_minor:"+b.getMinor());
                System.out.println("Beacon_visitor_id:"+b.getVisitor_id());
                deviceInfoDao.inputBeaconVisitor(b);
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    //保存visitor的信息
    @Transactional
    public boolean inputVisitorList(List<Visitor> visitors) {
        try {
            for (Visitor v:visitors) {
                System.out.println("");
                System.out.println("Visitor_id:"+v.getVisitor_id());
                System.out.println("Visitor_name:"+v.getName());
                deviceInfoDao.inputVisitor(v);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    //根据地图信息如building和floor获取基站信息
    public List<DeviceInfo> getDeviceInfoByBuildingAndFloor(String building, String floor) {
        return deviceInfoDao.getDeviceInfoListByBuidingAndFloor(building, floor);
    }


    //保存原始数据信息
    public List<String> inputOriginDataList(List<OriginData> originDatas) {
        List<String> access = new ArrayList<>();
        for (OriginData o:originDatas) {
            try {
                System.out.println("dssssss"+o.getBarcode());
                deviceInfoDao.inputOriginDataIntoBeaconAndVistor(o);
                access.add(o.getBarcode());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return access;
    }

    public List<Visitor>getVisitorByName(String name) {
        System.out.println(name);
        return deviceInfoDao.getVisitorListByName(name);
    }

    public List<Visitor> getVisitorsByNameOrVisitorId(String parameter){
        return deviceInfoDao.getVisitorsByNameOrVisitorId(parameter);
    }

    public List<FloorInfo> getFloorsByMapId(String mapId,String client,String vkey) throws Exception {
        List<FloorInfo> list = new ArrayList<>();
        mapId= Constant.MAPID;
        client="824";
        vkey="FFE58998-B203-B44E-A95B-8CA2D6CBCD65";
        String url = "http://123.57.46.160:8080/beacon/floor!search?client="+client+"&vkey="+vkey+"&place="+mapId+"";
        String result= HttpClientUtil.httpclientRequest("GET", url, "");
        JSONObject json = JSONObject.fromObject(result);
        if (json.get("success").equals(true)){
            JSONArray arry = json.getJSONArray("rows");
            list= (List<FloorInfo>) JSONArray.toCollection(arry,FloorInfo.class);
        }
        return list;
    }
}
