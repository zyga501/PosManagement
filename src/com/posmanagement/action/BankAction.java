package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.UUIDUtils;
import com.posmanagement.webui.BankList;
import com.posmanagement.webui.WebUI;

import java.util.HashMap;
import java.util.Map;

public class BankAction extends AjaxActionSupport {
    private final static String BANKMANAGER = "bankManager";

    private String bankList;
    private String bankName;
    private String bankEnabled;
    private String uiMode;

    public String getBankList() {
        return bankList;
    }

    public void setBankName(String _bankName) {
        bankName = _bankName;
    }

    public void setBankEnabled(String _bankEnabled) {
        bankEnabled = _bankEnabled;
    }

    public void setUiMode(String _uiMode) {
        uiMode = _uiMode;
    }

    public String Init() throws Exception {
        bankList = new BankList(WebUI.UIMode.TABLELIST).generateHTMLString();
        return BANKMANAGER;
    }

    public String FetchBankList() throws Exception {
        Map map = new HashMap();
        if (uiMode != null && uiMode.compareTo("SELECTLIST") == 0) {
            map.put("bankList", new BankList(WebUI.UIMode.SELECTLIST).generateHTMLString());
        }
        else {
            map.put("bankList", new BankList(WebUI.UIMode.TABLELIST).generateHTMLString());
        }

        return AjaxActionComplete(map);
    }

    public String AddBank() throws Exception {
        Map map = new HashMap();
        if (bankName.length() == 0) {
            map.put("errorMessage", getText("addbank.BankNameError"));
        }
        else {
            Map parametMap = new HashMap();
            parametMap.put(1, UUIDUtils.generaterUUID());
            parametMap.put(2, bankName);
            if (bankEnabled != null)
                parametMap.put(3, new String("enable"));
            else
                parametMap.put(3, new String("disable"));
            PosDbManager.executeUpdate("insert into banktb(uuid,name,status) values(?,?,?)", (HashMap<Integer, Object>) parametMap);
            map.put("bankList", new BankList(WebUI.UIMode.TABLELIST).generateHTMLString());
        }

        return AjaxActionComplete(map);
    }
}
