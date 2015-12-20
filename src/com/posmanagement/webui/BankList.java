package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class BankList {
    public enum UIMode {
        TABLELIST,
        SELECTLIST
    }

    public BankList(UIMode _uimode) {
        uiMode = _uimode;
    }

    public String generateHTMLString() throws Exception {
        switch (uiMode) {
            case TABLELIST:
                return generateTableList();
            case SELECTLIST:
                return generateSelectList();
        }

        return "";
    }

    public String generateTableList() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchBankList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString +="<tr class=\"text-c odd\" role=\"row\">"+
                    "<td>"+dbRet.get(index).get("BANKCODE")+"</td>"+
                    "<td>"+dbRet.get(index).get("BANKNAME")+"</td></tr>";
        }
        return htmlString;
    }

    public String generateSelectList() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchBankList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString +="<option " +
                    "value=\""+dbRet.get(index).get("BANKCODE")+"\">"+
                    dbRet.get(index).get("BANKNAME")+"</option>";
        }

        return htmlString;
    }

    private ArrayList<HashMap<String, Object>> fetchBankList() throws Exception {
        return PosDbManager.executeSql("select * from banktb order by bankid");
    }

    private UIMode uiMode;
}
