package com.posmanagement.action;

import com.opensymphony.xwork2.ActionSupport;
import com.posmanagement.utils.DbManager;
import com.posmanagement.webui.CardTimerList;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CardTimerAction extends ActionSupport {
    private final static String CARDTIMERMANAGER = "cardTimerManager";
    private final static String ACTIONFINISHED = "actionFinished";

    private String cardTimerList;
    private String cardTimer;
    private String timerEnabled;
    private String actionResult;

    public String getCardTimerList() {
        return cardTimerList;
    }

    public void setCardTimer(String _cardTimer) {
        cardTimer = _cardTimer;
    }

    public void setTimerEnabled(String enabled) {
        timerEnabled = enabled;
    }

    public String getActionResult() {
        return actionResult;
    }

    public String Init() throws Exception {
        cardTimerList = new CardTimerList().generateHTMLString();
        return CARDTIMERMANAGER;
    }

    public String AddCardTimer() throws Exception {
        Map map = new HashMap();
        if (cardTimer.length() == 0) {
            map.put("errorMessage", getText("addcardtimer.timerError"));
        }
        else {
            Map parametMap = new HashMap();
            parametMap.put(1, cardTimer);
            if (timerEnabled != null)
                parametMap.put(2, new String("on"));
            else
                parametMap.put(2, new String("off"));
            DbManager.getDafaultDbManager().executeUpdate("insert into cardtimertb(cardtimer,enabled) values(?,?)", (HashMap<Integer, Object>) parametMap);
            map.put("cardTimerList", new CardTimerList().generateHTMLString());
        }

        actionResult = JSONObject.fromObject(map).toString();
        return ACTIONFINISHED;
    }
}
