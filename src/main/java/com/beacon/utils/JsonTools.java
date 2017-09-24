package com.beacon.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.io.IOException;


/**
 * Created by LK on 2017/4/19.
 */
public class JsonTools {

    public static  String fromObject(Object obj){
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(obj);
        } catch (IOException e) {
            Logger logger = Logger.getLogger(JsonTools.class);
            logger.error("json error",e);
        }
        return json;
    }

}
