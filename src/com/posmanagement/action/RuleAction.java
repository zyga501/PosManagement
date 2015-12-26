package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.webui.RuleList;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RuleAction extends AjaxActionSupport {
    private final static String RULEMANAGER = "ruleManager";
    private String ruleList;

    public String  getRuleList() {
        return ruleList;
    }

    public String Init() throws Exception {
        ruleList = new RuleList().generateHTMLString();
        return RULEMANAGER;
    }

    public String AddRule() throws Exception {
        Map parametMap = new HashMap();
        parametMap.put(1, UUID.randomUUID().toString().toUpperCase());
        parametMap.put(2, (String) getParameter("bankCode"));
        parametMap.put(3, (String) getParameter("posServer"));
        parametMap.put(4, (String) getParameter("swingTime"));
        parametMap.put(5, (String) getParameter("minSwingMoney"));
        parametMap.put(6, (String) getParameter("maxSwingMoney"));
        parametMap.put(7, (String) getParameter("industryName"));
        parametMap.put(8, (String) getParameter("industryFre"));
        parametMap.put(9, (String) getParameter("industryInterval"));
        parametMap.put(10, (String) getParameter("rate"));
        parametMap.put(11, (String) getParameter("rateFre"));
        parametMap.put(12, (String) getParameter("rateInterval"));
        parametMap.put(13, (String) getParameter("mcc"));
        parametMap.put(14, (String) getParameter("mccFre"));
        parametMap.put(15, (String) getParameter("mccInterval"));
        parametMap.put(16, (String) getParameter("useFre"));
        parametMap.put(17, (String) getParameter("useInterval"));
        parametMap.put(18, (String) getParameter("ruleUseFre"));
        parametMap.put(19, (String) getParameter("ruleUseInterval"));
        if (getParameter("status") != null && getParameter("status").toString().compareTo("on") == 0) {
            parametMap.put(20, "enable");
        }
        else {
            parametMap.put(20, "disable");
        }

        Map map = new HashMap();
        if (PosDbManager.executeUpdate("insert into ruletb(ruleno,bankcode,posserver,swingtime," +
                "minswingmoney,maxswingmoney,industryname,industryfre,industryinterval,rate,ratefre,rateinterval,mcc," +
                "mccfre,mccinterval,usefre,useinterval,ruleusefre,ruleuseinterval,status)" +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", (HashMap<Integer, Object>)parametMap)) {
            map.put("ruleList", new RuleList().generateHTMLString());
        }

        return AjaxActionComplete(map);
    }
}
