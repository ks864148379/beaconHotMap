package com.beacon.controller;

import com.beacon.daoService.CardScanService;
import com.beacon.model.CardScan;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by James on 2017/6/30.
 */
@RequestMapping("/api")
@Controller
public class CardController {

    @Autowired
    private CardScanService cardScanService;

    @ResponseBody
    @RequestMapping(value = "/card/cardList")
    public JSONObject cardScan(String name) {
        JSONObject result = new JSONObject();
        List<CardScan> list = cardScanService.getCardList();
        result.put("list", list);
        result.put("success",true);
        result.put("length",list.size());
        return result;
    }


}
