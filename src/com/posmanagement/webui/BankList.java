package com.posmanagement.webui;

import com.posmanagement.utils.DbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class BankList {
    public String generateHTMLString() throws Exception {
        return generateBankList();
    }

    private String generateBankList() throws Exception {
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

    private ArrayList<HashMap<String, Object>> fetchBankList() throws Exception {
        return DbManager.getDafaultDbManager().executeSql("select * from banktb order by bankid");
    }
}
