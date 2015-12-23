package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.webui.RateList;
import com.posmanagement.webui.WebUI;

import java.util.HashMap;
import java.util.Map;

public class RateAction extends AjaxActionSupport {
    private final static String RATEMANAGER = "rateManager";

    private String rateList;
    private String rate;
    private String rateEnabled;
    private String uiMode;

    public String getRateList() {
        return rateList;
    }

    public void setRate(String _rate) {
        rate = _rate;
    }

    public void setRateEnabled(String _rateEnabled) {
        rateEnabled = _rateEnabled;
    }

    public void setUiMode(String _uiMode) {
        uiMode = _uiMode;
    }

    public String Init() throws Exception {
        rateList = new RateList(WebUI.UIMode.TABLELIST).generateHTMLString();
        return RATEMANAGER;
    }

    public String FetchRateList() throws Exception {
        Map map = new HashMap();
        if (uiMode != null && uiMode.compareTo("SELECTLIST") == 0) {
            map.put("rateList", new RateList(WebUI.UIMode.SELECTLIST).generateHTMLString());
        }
        else {
            map.put("rateList", new RateList(WebUI.UIMode.TABLELIST).generateHTMLString());
        }

        return AjaxActionComplete(map);
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
                    parametMap.put(2, new String("enable"));
                else
                    parametMap.put(2, new String("disable"));
                PosDbManager.executeUpdate("insert into ratetb(rate,status) values(?,?)", (HashMap<Integer, Object>) parametMap);
                map.put("rateList", new RateList(WebUI.UIMode.TABLELIST).generateHTMLString());
            }
            catch (NumberFormatException exception) {
                map.put("errorMessage", getText("addrate.rateFormatError"));
            }
        }

        return AjaxActionComplete(map);
    }
}
