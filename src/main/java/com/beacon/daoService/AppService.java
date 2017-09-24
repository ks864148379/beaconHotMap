package com.beacon.daoService;

import com.beacon.dao.AppDao;
import com.beacon.dao.UserDao;
import com.beacon.entity.AppMessage;
import com.beacon.entity.AppTask;
import com.beacon.model.DyBeaconInfo;
import com.beacon.model.TaskManager;
import com.beacon.model.Visitor;
import com.beacon.utils.Constant;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;
import java.io.File;

/**
 * Created by LK on 2017/4/19.
 */
@Service("appService")
public class AppService {
    @Autowired
    private AppDao appDao;
    @Autowired
    private UserDao userDao;

    @Transactional
    public boolean addDyBeaconInfoList(List<DyBeaconInfo> dyBeaconList){
        try {
            for (DyBeaconInfo b : dyBeaconList){
                appDao.addDyBeaconInfo(b);
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public List<AppTask> getTasksByVisitorId(String visitor_id,Timestamp time){
        List<AppTask> list = new ArrayList<>();
        List<Map> maps = appDao.getTasksByVisitorId(visitor_id,time);
        for (int i=0;i<maps.size();i++){
            AppTask task = new AppTask();
            Map map =maps.get(i);
            task.setVisitor_id((String) map.get("customer_id"));
            task.setTask_title((String) map.get("task_title"));
            task.setTask_description((String) map.get("task_content"));
            task.setCreate_time(String.valueOf(map.get("create_time")));
            list.add(task);
        }
        return list;
    }

    public List<Visitor> getCustomersByVisitorId(String visitor_id,Timestamp time){
        List<Visitor> visitors= appDao.getCustomersByVisitorId(visitor_id,time);
        for (Visitor visitor :visitors){
            visitor.setMac_id(userDao.getMacIdByVisitorId(visitor.getVisitor_id()));
        }
        return visitors;
    }

    public List<Visitor> getCustomersByVisitorId1(String visitor_id,Timestamp time){
        List<Visitor> visitors = new ArrayList<>();
        List<Map> mapList= appDao.getCustomersByVisitorId1(visitor_id, time);
        for (int i=0;i<mapList.size();i++){
            Visitor visitor = new Visitor();
            Map map = mapList.get(i);
            visitor.setVisitor_id((String) map.get("visitor_id"));
            visitor.setMac_id((String) map.get("mac_id"));
            visitor.setName((String) map.get("name"));
            visitor.setCity((String) map.get("city"));
            visitor.setCorporation((String) map.get("corporation"));
            visitor.setCountry((String) map.get("country"));
            visitor.setEmail((String) map.get("email"));
            visitor.setGender((Integer) map.get("gender"));
            visitor.setTel((String) map.get("tel"));
            visitor.setPosition((String) map.get("position"));
            visitor.setProvince((String) map.get("province"));
            visitors.add(visitor);
        }
        return visitors;
    }

    public String getVisitorIdByMacId(String mac_id){
        return userDao.getVisitorIdByMacId(mac_id);
    }

    public String getVisitorFloorByVisitorId(String visitor_id) {
        String macId = userDao.getMacIdByVisitorId(visitor_id);
        List<Map> result = appDao.getFloorByMacId(macId);
        String floor = "Floor1";
        if (result.size() > 0) {
            floor = (String)result.get(0).get("floor");
        }
        return floor;
    }

    public String getVisitorFloorByMacId(String mac_id) {
        //String macId = userDao.getMacIdByVisitorId(visitor_id);
        List<Map> result = appDao.getFloorByMacId(mac_id);
        String floor = "Floor1";
        if (result.size() > 0) {
            floor = (String)result.get(0).get("floor");
        }
        return floor;
    }

    public List<AppMessage> getMessageByVisitorId(String vistor_id,Timestamp time){
        List<Map> maps = appDao.getMessageByVisitorId(vistor_id, time);
        List<AppMessage> list = new ArrayList<>();
        for (int i=0;i<maps.size();i++){
            AppMessage appMessage = new AppMessage();
            Map map=maps.get(i);
            appMessage.setContent((String) map.get("content"));
            appMessage.setCreate_time(String.valueOf(map.get("create_time")));
            list.add(appMessage);
        }
        return list;
    }

    public TaskManager getTaskManagerByStuffIdAndCustomerId(String stuff_id,String customer_id) {
        return appDao.getTaskManage(stuff_id,customer_id);
    }

    @Transactional
    public void updateTaskManage(TaskManager taskManager){
        appDao.updateTaskManage(taskManager);
    }

    public JSONArray writeImageToDisk(List<MultipartFile> fileList) {
        JSONArray imageArray=new JSONArray();
        for (MultipartFile file : fileList) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is =  file.getInputStream();
                String fileName=renameFileName()+".jpg";
                fos = new FileOutputStream(Constant.IMAGE_PATH+fileName);
                byte[] buffer = new byte[1024];
                int len=0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.flush();
                imageArray.add(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return imageArray;
    }

    public JSONArray writeAudioToDisk(List<MultipartFile> fileList) {
        JSONArray audioArray=new JSONArray();
        for (MultipartFile file : fileList) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is =  file.getInputStream();
                String name=renameFileName();
                String fileName=name+".amr";
                String realFileName=name+".mp3";
                fos = new FileOutputStream(Constant.AMR_PATH+fileName);
                byte[] buffer = new byte[1024];
                int len=0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.flush();
                changeToMp3(Constant.AMR_PATH + fileName, Constant.AUDIO_PATH + realFileName);
                audioArray.add(realFileName);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return audioArray;
    }

    public JSONArray writeCardToDisk(List<MultipartFile> fileList) {
        JSONArray imageArray=new JSONArray();
        for (MultipartFile file : fileList) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is =  file.getInputStream();
                String fileName=renameFileName()+".jpg";
                fos = new FileOutputStream(Constant.CARD_PATH+fileName);
                byte[] buffer = new byte[1024];
                int len=0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.flush();
                imageArray.add(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return imageArray;
    }

    /**
     * 重命名上传文件
     */
    public static String renameFileName() {
        String formatDate = new SimpleDateFormat("yyMMddHHmmss").format(new Date()); // 当前时间字符串
        int random = new Random().nextInt(10000);
        return formatDate + random ;
    }

    public static void changeToMp3(String sourcePath, String targetPath) {
        File source = new File(sourcePath);
        File target = new File(targetPath);
        AudioAttributes audio = new AudioAttributes();
        Encoder encoder = new Encoder();

        audio.setCodec("libmp3lame");
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp3");
        attrs.setAudioAttributes(audio);

        try {
            encoder.encode(source, target, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
