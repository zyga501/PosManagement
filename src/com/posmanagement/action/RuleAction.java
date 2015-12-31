package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.UUIDUtils;
import com.posmanagement.webui.RuleUI;

import java.util.HashMap;
import java.util.Map;

public class RuleAction extends AjaxActionSupport {
    private final static String RULEMANAGER = "ruleManager";
    private String ruleList;

    public String  getRuleList() {
        return ruleList;
    }

    public String Init() throws Exception {
        ruleList = new RuleUI().generateHTMLString();
        return RULEMANAGER;
    }

    public String AddRule() throws Exception {
        Map parametMap = new HashMap();
        parametMap.put(1, UUIDUtils.generaterUUID());
        parametMap.put(2, (String) getParameter("bankUUID"));
        parametMap.put(3, (String) getParameter("posServerUUID"));
        parametMap.put(4, (String) getParameter("swingTimeUUID"));
        parametMap.put(5, (String) getParameter("minSwingMoney"));
        parametMap.put(6, (String) getParameter("maxSwingMoney"));
        parametMap.put(7, (String) getParameter("industryUUID"));
        parametMap.put(8, (String) getParameter("ruleUseFre"));
        parametMap.put(9, (String) getParameter("ruleUseInterval"));
        if (getParameter("status") != null && getParameter("status").toString().compareTo("on") == 0) {
            parametMap.put(10, "enable");
        }
        else {
            parametMap.put(10, "disable");
        }

        Map map = new HashMap();
        if (PosDbManager.executeUpdate("insert into ruletb(uuid,bankuuid,posserveruuid,swingtimeuuid," +
                "minswingmoney,maxswingmoney,industryuuid,ruleusefre,ruleuseinterval,status)" +
                "values(?,?,?,?,?,?,?,?,?,?)", (HashMap<Integer, Object>)parametMap)) {
            map.put("ruleList", new RuleUI().generateHTMLString());
        }

        return AjaxActionComplete(map);
    }
}
