package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.UUIDUtils;
import com.posmanagement.webui.RuleList;

import java.util.HashMap;
import java.util.Map;

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
        parametMap.put(1, UUIDUtils.generaterUUID());
        parametMap.put(2, (String) getParameter("bankCode"));
        parametMap.put(3, (String) getParameter("posServer"));
        parametMap.put(4, (String) getParameter("swingTime"));
        parametMap.put(5, (String) getParameter("minSwingMoney"));
        parametMap.put(6, (String) getParameter("maxSwingMoney"));
        parametMap.put(7, (String) getParameter("industryName"));
        parametMap.put(18, (String) getParameter("ruleUseFre"));
        parametMap.put(19, (String) getParameter("ruleUseInterval"));
        if (getParameter("status") != null && getParameter("status").toString().compareTo("on") == 0) {
            parametMap.put(20, "enable");
        }
        else {
            parametMap.put(20, "disable");
        }

        Map map = new HashMap();
        if (PosDbManager.executeUpdate("insert into ruletb(ruleno,bankcode,posservercode,swingtimecode," +
                "minswingmoney,maxswingmoney,industrycode,ruleusefre,ruleuseinterval,status)" +
                "values(?,?,?,?,?,?,?,?,?,?)", (HashMap<Integer, Object>)parametMap)) {
            map.put("ruleList", new RuleList().generateHTMLString());
        }

        return AjaxActionComplete(map);
    }
}
