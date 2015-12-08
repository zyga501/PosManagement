package com.posmanagement.action;

import com.posmanagement.utils.DbManager;
import com.posmanagement.webui.CardTimeList;

import java.util.HashMap;
import java.util.Map;

public class CardTimeAction extends AjaxActionSupport {
    private final static String CARDTIMEMANAGER = "cardTimeManager";

    private String cardTimeList;
    private String cardTime;
    private String timeEnabled;

    public String getCardTimeList() {
        return cardTimeList;
    }

    public void setCardTime(String _cardTime) {
        cardTime = _cardTime;
    }

    public void setTimeEnabled(String enabled) {
        timeEnabled = enabled;
    }

    public String Init() throws Exception {
        cardTimeList = new CardTimeList().generateHTMLString();
        return CARDTIMEMANAGER;
    }

    public String AddCardTime() throws Exception {
        Map map = new HashMap();
        if (cardTime.length() == 0) {
            map.put("errorMessage", getText("addcardtime.timeError"));
        }
        else {
            Map parametMap = new HashMap();
            parametMap.put(1, cardTime);
            if (timeEnabled != null)
                parametMap.put(2, new String("on"));
            else
                parametMap.put(2, new String("off"));
            DbManager.createPosDbManager().executeUpdate("insert into cardtimetb(cardTime,enabled) values(?,?)", (HashMap<Integer, Object>) parametMap);
            map.put("cardTimeList", new CardTimeList().generateHTMLString());
        }

        setAjaxActionResult(map);
        return AjaxActionSupport.ACTIONFINISHED;
    }
}
