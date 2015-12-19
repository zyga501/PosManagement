package com.posmanagement.action;

import com.posmanagement.utils.DbManager;
import com.posmanagement.webui.RateList;

import java.util.HashMap;
import java.util.Map;

public class RateAction extends AjaxActionSupport {
    private final static String RATEMANAGER = "rateManager";

    private String rateList;
    private String rate;
    private String rateEnabled;

    public String getRateList() {
        return rateList;
    }

    public void setRate(String _rate) {
        rate = _rate;
    }

    public void setRateEnabled(String _rateEnabled) {
        rateEnabled = _rateEnabled;
    }

    public String Init() throws Exception {
        rateList = new RateList().generateHTMLString();
        return RATEMANAGER;
    }

    public String AddRate() throws Exception {
        Map map = new HashMap();
        if (rate.length() == 0) {
            map.put("errorMessage", getText("addrate.rateError"));
        }
        else {
            try {
                Double.parseDouble(rate);
                Map parametMap = new HashMap();
                parametMap.put(1, rate);
                if (rateEnabled != null)
                    parametMap.put(2, new String("on"));
                else
                    parametMap.put(2, new String("off"));
                DbManager.createPosDbManager().executeUpdate("insert into ratetb(rate,enabled) values(?,?)", (HashMap<Integer, Object>) parametMap);
                map.put("rateList", new RateList().generateHTMLString());
            }
            catch (NumberFormatException exception) {
                map.put("errorMessage", getText("addrate.rateFormatError"));
            }
        }

        return AjaxActionComplete(map);
    }
}
