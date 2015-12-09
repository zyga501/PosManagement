package com.posmanagement.action;

import com.posmanagement.utils.DbManager;
import com.posmanagement.webui.CardTimeList;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class CardTimeAction extends AjaxActionSupport {
    private final static String CARDTIMEMANAGER = "cardTimeManager";

    private String cardTimeList;
    private String cardTime;
    private String startTime;
    private String endTime;
    private String timeEnabled;

    public String getCardTimeList() {
        return cardTimeList;
    }

    public void setCardTime(String _cardTime) {
        cardTime = _cardTime;
    }

    public void setStartTime(String _startTime) {
        startTime = _startTime;
    }

    public void setEndTime(String _endTime) {
        endTime = _endTime;
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
            map.put("errorMessage", getText("addcardtime.cardTimeError"));
        }
        else {
            try {
                Map parametMap = new HashMap();
                parametMap.put(1, cardTime);
                Time _startTime = Time.valueOf(startTime);
                Time _endTime = Time.valueOf(endTime);
                if (_startTime.after(_endTime)) {
                    map.put("errorMessage", getText("addcardtime.endTimeEarlierThanStartTime"));
                    setAjaxActionResult(map);
                    return AjaxActionSupport.ACTIONFINISHED;
                }
                parametMap.put(2, _startTime);
                parametMap.put(3, _endTime);
                if (timeEnabled != null)
                    parametMap.put(4, new String("on"));
                else
                    parametMap.put(4, new String("off"));
                DbManager.createPosDbManager().executeUpdate("insert into cardtimetb(cardTime,startTime,endTime,enabled) values(?,?,?,?)", (HashMap<Integer, Object>) parametMap);
                map.put("cardTimeList", new CardTimeList().generateHTMLString());
            }
            catch (IllegalArgumentException illegalException) {
                map.put("errorMessage", getText("addcardtime.timeFormatError"));
            }
        }

        setAjaxActionResult(map);
        return AjaxActionSupport.ACTIONFINISHED;
    }
}
