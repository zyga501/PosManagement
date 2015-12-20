package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.webui.BankList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BankAction extends AjaxActionSupport {
    private final static String BANKMANAGER = "bankManager";

    private String bankList;
    private String bankCode;
    private String bankName;
    private String uiMode;

    public String getBankList() {
        return bankList;
    }

    public void setBankCode(String _bankCode) {
        bankCode = _bankCode;
    }

    public void setBankName(String _bankName) {
        bankName = _bankName;
    }

    public void setUiMode(String _uiMode) {
        uiMode = _uiMode;
    }

    public String Init() throws Exception {
        bankList = new BankList(BankList.UIMode.TABLELIST).generateHTMLString();
        return BANKMANAGER;
    }

    public String FetchBankList() throws Exception {
        Map map = new HashMap();
        if (uiMode != null && uiMode.compareTo("SELECTLIST") == 0) {
            map.put("bankList", new BankList(BankList.UIMode.SELECTLIST).generateHTMLString());
        }
        else {
            map.put("bankList", new BankList(BankList.UIMode.TABLELIST).generateHTMLString());
        }

        return AjaxActionComplete(map);
    }

    public String AddBank() throws Exception {
        Map map = new HashMap();
        if (bankCode.length() == 0 || bankName.length() == 0) {
            map.put("errorMessage", getText("addbank.bankCodeOrBankNameError"));
        }
        else {
            Map parametMap = new HashMap();
            parametMap.put(1, bankCode);
            ArrayList dbRet = PosDbManager.executeSql("select * from banktb where bankcode=?", (HashMap<Integer, Object>) parametMap);
            if (dbRet.size() >= 1) {
                map.put("errorMessage", getText("addbank.bankCodeConfilct"));
            }
            else {
                parametMap.put(2, bankName);
                PosDbManager.executeUpdate("insert into banktb(bankcode,bankname) values(?,?)", (HashMap<Integer, Object>) parametMap);
                map.put("bankList", new BankList(BankList.UIMode.TABLELIST).generateHTMLString());
            }
        }

        return AjaxActionComplete(map);
    }
}
