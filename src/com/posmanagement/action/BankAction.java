package com.posmanagement.action;

import com.opensymphony.xwork2.ActionSupport;
import com.posmanagement.webui.BankList;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BankAction extends ActionSupport {
    private final static String BANKMANAGER = "bankManager";
    private final static String BANKADDSUCCESS = "bankAddSuccess";
    private final static String BANKADDFAILURE = "bankAddFailure";

    private String bankList;
    private String bankCode;
    private String bankName;
    private String actionResult;

    public String getBankList() {
        return bankList;
    }

    public void setBankCode(String _bankCode) {
        bankCode = _bankCode;
    }

    public void setBankName(String _bankName) {
        bankName = _bankName;
    }

    public String getToAddBankResult() {
        return actionResult;
    }

    public String Init() throws Exception {
        bankList = new BankList().generateHTMLString();
        return BANKMANAGER;
    }

    public String AddBank() {
        Map map = new HashMap();
        if (bankCode.length() == 0 || bankName.length() == 0) {
            map.put("success", false);
            map.put("addBankErrorMessage", getText("addbank.addBankErrorMessage"));
            actionResult = JSONObject.fromObject(map).toString();
            return BANKADDFAILURE;
        }

        map.put("addBankErrorMessage", "添加成功");
        actionResult = JSONObject.fromObject(map).toString();
        return BANKADDSUCCESS;
    }
}
