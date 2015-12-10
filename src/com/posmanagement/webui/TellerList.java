package com.posmanagement.webui;

import com.posmanagement.utils.DbManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TellerList {
    public TellerList() { }
    public TellerList(String _salemanID) {
        salemanID = _salemanID;
    }

    public String generateHTMLString() throws Exception {
        return generateTellerList();
    }

    private String generateTellerList() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchTellerList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString ="<table class=\"table table-border table-bordered table-hover\">";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString +="<tr class=\"text-c odd\" role=\"row\" onclick=\"clickTeller('" + dbRet.get(index).get("UID") + "')\">"+
                    "<td>"+String.valueOf(index+1)+" </td>"+
                    "<td>"+dbRet.get(index).get("UNICK")+"</td>"+
                    "<td>"+dbRet.get(index).get("UNAME")+"</td></tr>";
        }

        return htmlString + "</table>";
    }

    private ArrayList<HashMap<String, Object>> fetchTellerList() throws Exception {
        String sql = "select * from userinfo a,tellertb b where a.uid=b.uid";
        Map parametMap = new HashMap<>();
        if (salemanID != null) {
            sql += " and salesmanid=?";
            parametMap.put(1, salemanID);
        }
        return DbManager.createPosDbManager().executeSql(sql, (HashMap<Integer, Object>)parametMap);
    }

    private String salemanID;
}
