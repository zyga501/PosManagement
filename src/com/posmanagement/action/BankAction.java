package com.posmanagement.action;

import com.posmanagement.utils.DbManager;
import com.posmanagement.webui.BankList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BankAction extends JsonActionSupport {
    private final static String BANKMANAGER = "bankManager";

    private String bankList;
    private String bankCode;
    private String bankName;

    public String getBankList() {
        return bankList;
    }

    public void setBankCode(String _bankCode) {
        bankCode = _bankCode;
    }

    public void setBankName(String _bankName) {
        bankName = _bankName;
    }

    public String Init() throws Exception {
        bankList = new BankList().generateHTMLString();
        return BANKMANAGER;
    }

    public String AddBank() throws Exception {
        Map map = new HashMap();
        if (bankCode.length() == 0 || bankName.length() == 0) {
            map.put("errorMessage", getText("addbank.bankCodeOrBankNameError"));
        }
        else {
            Map parametMap = new HashMap();
            parametMap.put(1, bankCode);
            ArrayList dbRet = DbManager.getDafaultDbManager().executeSql("select * from banktb where bankcode=?", (HashMap<Integer, Object>) parametMap);
            if (dbRet.size() >= 1) {
                map.put("errorMessage", getText("addbank.bankCodeConfilct"));
            }
            else {
                parametMap.put(2, bankName);
                DbManager.getDafaultDbManager().executeUpdate("insert into banktb(bankcode,bankname) values(?,?)", (HashMap<Integer, Object>) parametMap);
                map.put("bankList", new BankList().generateHTMLString());
            }
        }

        setAjaxActionResult(map);
        return ACTIONFINISHED;
    }
}
