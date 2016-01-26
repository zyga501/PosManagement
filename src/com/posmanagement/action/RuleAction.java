package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.UUIDUtils;
import com.posmanagement.webui.BankUI;
import com.posmanagement.webui.RuleUI;
import com.posmanagement.webui.SalemanUI;

import java.util.HashMap;
import java.util.Map;

public class RuleAction extends AjaxActionSupport {
    private final static String RULEMANAGER = "ruleManager";
    private final static String RULEASSIGN = "ruleAssign";
    private String ruleList;
    private String ruleUUID;
    private String ruleInfo;
    private String bankList;
    private String salemanList;

    public String  getRuleList() {
        return ruleList;
    }

    public String getRuleUUID() {
        return ruleUUID;
    }

    public void setRuleUUID(String _ruleUUID) {
        ruleUUID = _ruleUUID;
    }

    public String getRuleInfo() {
        return ruleInfo;
    }

    public String getBankList() {
        return bankList;
    }

    public String getSalemanList() {
        return salemanList;
    }

    public String Init() throws Exception {
        ruleList = new RuleUI().generateTable();
        return RULEMANAGER;
    }

    public String AddRule() throws Exception {
        Map map = new HashMap();
        try {
            String bankUUID = getParameter("bankUUID").toString();
            String posServerUUID = getParameter("posServerUUID").toString();
            String swingTimeUUID = getParameter("swingTimeUUID").toString();
            String minSwingMoney = getParameter("minSwingMoney").toString();
            String maxSwingMoney = getParameter("maxSwingMoney").toString();
            String industryUUID = getParameter("industryUUID").toString();
            String rateUUID = getParameter("rateUUID").toString();
            String mccUUID = getParameter("mccUUID").toString();
            String ruleUseFre = getParameter("ruleUseFre").toString();
            String ruleUseInterval = getParameter("ruleUseInterval").toString();
            if (bankUUID.isEmpty()) {
                throw new IllegalArgumentException();
            }
            Map parametMap = new HashMap();
            parametMap.put(1, UUIDUtils.generaterUUID());
            parametMap.put(2, bankUUID);
            parametMap.put(3, posServerUUID);
            parametMap.put(4, swingTimeUUID);
            parametMap.put(5, minSwingMoney);
            parametMap.put(6, maxSwingMoney);
            parametMap.put(7, industryUUID);
            parametMap.put(8, rateUUID);
            parametMap.put(9, mccUUID);
            parametMap.put(10, ruleUseFre);
            parametMap.put(11, ruleUseInterval);
            if (getParameter("status") != null && getParameter("status").toString().compareTo("on") == 0) {
                parametMap.put(12, "enable");
            }
            else {
                parametMap.put(12, "disable");
            }

            if (PosDbManager.executeUpdate("insert into ruletb(uuid,bankuuid,posserveruuid,swingtimeuuid," +
                    "minswingmoney,maxswingmoney,industryuuid,rateuuid,mccuuid,ruleusefre,ruleuseinterval,status)" +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?)", (HashMap<Integer, Object>)parametMap)) {
                map.put("ruleList", new RuleUI().generateTable());
            }
        }
        catch (IllegalArgumentException exception) {
            map.put("errorMessage", getText("global.inputError"));
        }

        return AjaxActionComplete(map);
    }

    public String InitRuleAssign() throws Exception {
        ruleInfo = new RuleUI().generaterInfo(ruleUUID);
        bankList = new BankUI().generateBankSelectList();
        salemanList = new SalemanUI().generateSelect();
        return RULEASSIGN;
    }
}
