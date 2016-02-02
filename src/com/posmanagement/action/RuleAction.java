package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.UUIDUtils;
import com.posmanagement.webui.BankUI;
import com.posmanagement.webui.RuleUI;
import com.posmanagement.webui.SalemanUI;

import java.util.ArrayList;
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
    private String ruleEnabled;

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

    public String getRuleEnabled() {
        return ruleEnabled;
    }

    public String Init() throws Exception {
        ruleList = new RuleUI().generateTable();
        return RULEMANAGER;
    }

    public String AddRule() throws Exception {
        Map map = new HashMap();
        try {
            String posServerUUID = getParameter("posServerUUID").toString();
            String swingTimeUUID = getParameter("swingTimeUUID").toString();
            String minSwingMoney = getParameter("minSwingMoney").toString();
            String maxSwingMoney = getParameter("maxSwingMoney").toString();
            String industryUUID = getParameter("industryUUID").toString();
            String rateUUID = getParameter("rateUUID").toString();
            String mccUUID = getParameter("mccUUID").toString();
            String ruleUseFre = getParameter("ruleUseFre").toString();
            String ruleUseInterval = getParameter("ruleUseInterval").toString();
            Map parametMap = new HashMap();
            int i = 0;
            parametMap.put(++i, UUIDUtils.generaterUUID());
            parametMap.put(++i, posServerUUID);
            parametMap.put(++i, swingTimeUUID);
            parametMap.put(++i, minSwingMoney);
            parametMap.put(++i, maxSwingMoney);
            parametMap.put(++i, industryUUID);
            parametMap.put(++i, rateUUID);
            parametMap.put(++i, mccUUID);
            parametMap.put(++i, ruleUseFre);
            parametMap.put(++i, ruleUseInterval);
            if (getParameter("status") != null && getParameter("status").toString().compareTo("on") == 0) {
                parametMap.put(++i, "enable");
            }
            else {
                parametMap.put(++i, "disable");
            }

            if (PosDbManager.executeUpdate("insert into ruletb(uuid,posserveruuid,swingtimeuuid," +
                    "minswingmoney,maxswingmoney,industryuuid,rateuuid,mccuuid,ruleusefre,ruleuseinterval,status)" +
                    "values(?,?,?,?,?,?,?,?,?,?,?)", (HashMap<Integer, Object>)parametMap)) {
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
        String selectedbankList = new BankUI().generateRuleBankList(ruleUUID);
        salemanList = new SalemanUI().generateSelect();
        String selectedsalemanList = new SalemanUI().generateRuleSalesmasnSelect(ruleUUID);
        getRequest().setAttribute("selectedbankList",selectedbankList);
        getRequest().setAttribute("selectedsalemanList",selectedsalemanList);
        ArrayList<HashMap<String, Object>> dbRet = PosDbManager.executeSql("select status from ruletb where uuid='" + ruleUUID + "'");
        if (dbRet.size() == 1) {
            ruleEnabled = dbRet.get(0).get("STATUS").toString().compareTo("enable") == 0 ? "checked" : "unchecked";
        }
        return RULEASSIGN;
    }

    public String AssignRule() throws Exception {
        String ruleUUID = getParameter("ruleUUID").toString();
        String[] bankUUID = getParameter("bankList").toString().split(",");

        PosDbManager.executeUpdate("update ruletb set status='" +
                (getParameter("ruleEnabled") != null ? "enable" : "disable")
                + "' where uuid='" + ruleUUID + "'"
        );
        Map parametMap = new HashMap();
        for (int index = 0; index < bankUUID.length; ++index) {
            parametMap.clear();
            parametMap.put(1, ruleUUID);
            parametMap.put(2, bankUUID[index]);
            try {
                PosDbManager.executeUpdate("insert into rulebank(ruleuuid, bankuuid) values(?,?)", (HashMap<Integer, Object>) parametMap);
            }
            catch (Exception exception) {

            }
        }

        String[] salemanUUID = getParameter("salemanList").toString().split(",");
        for (int index = 0; index < salemanUUID.length; ++index) {
            parametMap.clear();
            parametMap.put(1, ruleUUID);
            parametMap.put(2, salemanUUID[index]);
            try {
                PosDbManager.executeUpdate("insert into rulesaleman(ruleuuid, salemanuuid) values(?, ?)", (HashMap<Integer, Object>)parametMap);
            }
            catch (Exception excetipn) {
            }
        }

        return AjaxActionComplete();
    }
}
