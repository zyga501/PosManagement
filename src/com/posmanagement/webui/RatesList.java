package com.posmanagement.webui;

import com.posmanagement.utils.DbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class RatesList {
    public String generateHTMLString() throws Exception {
        return generateRatesList();
    }

    private String generateRatesList() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchRatesList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString +="<tr class=\"text-c odd\" role=\"row\">"+
                    "<td>"+ dbRet.get(index).get("RATES")+"</td>"+
                    "<td><input type=\"checkbox\"";
            if (dbRet.get(index).get("ENABLED").toString().compareTo("on") == 0) {
                htmlString += "checked=\"checked\"";
            }
            htmlString += " /></td></tr>";
        }
        return htmlString;
    }

    private ArrayList<HashMap<String, Object>> fetchRatesList() throws Exception {
        return DbManager.createPosDbManager().executeSql("select * from ratestb");
    }
}
