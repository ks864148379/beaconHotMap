package com.beacon.daoService;

import com.beacon.dao.InfoCountDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by James on 2017/7/19.
 */
public class InfoCountService {
    @Autowired
    InfoCountDAO infoCountDAO;

    public InfoCountDAO getInfoCountDAO() {
        return infoCountDAO;
    }

    public void setInfoCountDAO(InfoCountDAO infoCountDAO) {
        this.infoCountDAO = infoCountDAO;
    }
}
