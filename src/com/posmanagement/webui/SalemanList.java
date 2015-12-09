package com.posmanagement.webui;

import com.posmanagement.utils.DbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class SalemanList {
    public String generateHTMLString() throws Exception {
        return generateSalemanList();
    }

    private String generateSalemanList() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchSalemanList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString ="<table class=\"table table-border table-bordered table-hover\">";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString +="<tr class=\"text-c odd\" role=\"row\" onclick=\"clickSaleman('" + dbRet.get(index).get("UID") + "')\">"+
                    "<td>"+String.valueOf(index+1)+" </td>"+
                    "<td>"+dbRet.get(index).get("UNICK")+"</td>"+
                    "<td>"+dbRet.get(index).get("UNAME")+"</td></tr>";
        }
        return htmlString + "</table>";
    }

    private ArrayList<HashMap<String, Object>> fetchSalemanList() throws Exception {
        return DbManager.createPosDbManager().executeSql("select * from userinfo a,salesmantb b where a.uid=b.uid");
    }
}
