package com.beacon.daoService;

import com.beacon.dao.CardScanDao;
import com.beacon.model.CardScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by James on 2017/6/30.
 */
@Service("cardScanService")
public class CardScanService {
    @Autowired
    CardScanDao cardScanDao;

    public CardScanDao getCardScanDao() {
        return cardScanDao;
    }

    public void setCardScanDao(CardScanDao cardScanDao) {
        this.cardScanDao = cardScanDao;
    }

    public List<CardScan> getCardList(){
        return cardScanDao.searchCardList();
    }

    public CardScan getCardById(String id){
        return cardScanDao.getCardDetail(id);
    }
}
