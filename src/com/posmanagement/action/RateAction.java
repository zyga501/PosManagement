package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;
import com.posmanagement.utils.UUIDUtils;
import com.posmanagement.webui.RateUI;

import java.util.HashMap;
import java.util.Map;

public class RateAction extends AjaxActionSupport {
    private final static String RATEMANAGER = "rateManager";

    private String rateList;

    public String getRateList() {
        return rateList;
    }

    public String Init() throws Exception {
        rateList = new RateUI().generateTable();
        return RATEMANAGER;
    }

    public String FetchRateList() throws Exception {
        Map map = new HashMap();
        if (StringUtils.convertNullableString(getParameter("uiMode")).compareTo("SELECTLIST") == 0) {
            map.put("rateList", new RateUI().generateSelect());
        }
        else {
            map.put("rateList", new RateUI().generateTable());
        }

        return AjaxActionComplete(map);
    }

    public String AddRate() throws Exception {
        Map map = new HashMap();
        try {
            String rate = StringUtils.convertNullableString(getParameter("rate"));
            String maxFee = StringUtils.convertNullableString(getParameter("maxFee"));
            Double.parseDouble(rate);
            Double.parseDouble(maxFee);
            Map parametMap = new HashMap();
            parametMap.put(1, UUIDUtils.generaterUUID());
            parametMap.put(2, rate);
            parametMap.put(3, maxFee);
            parametMap.put(4, StringUtils.convertNullableString(getParameter("rateEnabled")).compareTo("on") == 0 ?
                    new String("enable") : new String("disable"));
            PosDbManager.executeUpdate("insert into ratetb(uuid,rate,maxfee,status) values(?,?,?,?)", (HashMap<Integer, Object>) parametMap);
            map.put("rateList", new RateUI().generateTable());
        }
        catch (NumberFormatException exception) {
            map.put("errorMessage", getText("global.inputError"));
        }

        return AjaxActionComplete(map);
    }
}
