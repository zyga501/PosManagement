package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;
import com.posmanagement.utils.UUIDUtils;
import com.posmanagement.webui.RateUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RateAction extends AjaxActionSupport {
    private final static String RATEMANAGER = "rateManager";
    private final static String ADDRATE = "addrate";

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

    public String FetchRate() throws Exception {
        Map map = new HashMap();
        if (null!=getParameter("UUID")&&(!getParameter("UUID").equals(""))) {
            Map parametMap = new HashMap();
            parametMap.put(1, getParameter("UUID"));
            ArrayList<HashMap<String, Object>> dbRet = PosDbManager.executeSql("select  * from ratetb where uuid=?",(HashMap<Integer, Object>) parametMap);
            if (dbRet.size()>0){
                map.put("uuid",getParameter("UUID"));
                map.put("rate",dbRet.get(0).get("RATE"));
                map.put("maxfee",dbRet.get(0).get("MAXFEE"));
                map.put("status",dbRet.get(0).get("STATUS").toString().toLowerCase().equals("enable")?"checked":"");
                getRequest().setAttribute("rateproperty",map);
            }
        }
        return ADDRATE;
    }

    public String EditRate() throws Exception {
        Map map = new HashMap();
        {
            Map parametMap = new HashMap();
            parametMap.put(1,getParameter("rateEnabled")!=null?"enable":"disable");
            parametMap.put(2, getParameter("maxFee"));
            parametMap.put(3, getParameter("rate"));
            parametMap.put(4, getParameter("uuid"));
            PosDbManager.executeUpdate("update ratetb set  status=?,maxfee=?,rate=? where uuid=? ", (HashMap<Integer, Object>) parametMap);
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
            parametMap.put(4, StringUtils.convertNullableString(getParameter("rateEnable")).compareTo("on") == 0 ?
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
