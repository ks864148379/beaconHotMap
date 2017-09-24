package com.beacon.daoService;

import com.beacon.dao.MessageDao;
import com.beacon.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by James on 2017/4/18.
 */
@Service("messageService")
public class MessageService {
    @Autowired
    MessageDao messageDao;

    public MessageDao getMessageDao() {
        return messageDao;
    }

    public void setMessageDao(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public boolean sendMessage(String msg, String stuffJsonList){
        Message message = new Message();
        message.setContent(msg);
        message.setStuffId(stuffJsonList);
        message.setCreateTime(new Timestamp(System.currentTimeMillis()));
        try {
//            System.out.println(message.getStuffId() + "->mes:" + message.getContent());
            messageDao.addMessage(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    public List<Message> getMsgList(){
        return messageDao.getAllMessage();
    }

}
