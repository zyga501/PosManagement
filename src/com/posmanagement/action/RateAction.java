package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.UUIDUtils;
import com.posmanagement.webui.RateUI;

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
        rateList = new RateUI().generateTable();
        return RATEMANAGER;
    }

    public String FetchRateList() throws Exception {
        Map map = new HashMap();
        if (uiMode != null && uiMode.compareTo("SELECTLIST") == 0) {
            map.put("rateList", new RateUI().generateSelect());
        }
        else {
            map.put("rateList", new RateUI().generateTable());
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
                parametMap.put(1, UUIDUtils.generaterUUID());
                parametMap.put(2, rate);
                if (rateEnabled != null)
                    parametMap.put(3, new String("enable"));
                else
                    parametMap.put(3, new String("disable"));
                PosDbManager.executeUpdate("insert into ratetb(uuid,rate,status) values(?,?,?)", (HashMap<Integer, Object>) parametMap);
                map.put("rateList", new RateUI().generateTable());
            }
            catch (NumberFormatException exception) {
                map.put("errorMessage", getText("addrate.rateFormatError"));
            }
        }

        return AjaxActionComplete(map);
    }
}
