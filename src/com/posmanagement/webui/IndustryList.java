package com.posmanagement.webui;

import com.posmanagement.utils.DbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class IndustryList {
    public String generateHTMLString() throws Exception {
        return generateIndustryList();
    }

    private String generateIndustryList() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchIndustryList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString +="<tr class=\"text-c odd\" role=\"row\">"+
                    "<td>"+ dbRet.get(index).get("NAME")+"</td>"+
                    "<td><input type=\"checkbox\"";
            if (dbRet.get(index).get("ENABLED").toString().compareTo("on") == 0) {
                htmlString += "checked=\"checked\"";
            }
            htmlString += " /></td></tr>";
        }
        return htmlString;
    }

    private ArrayList<HashMap<String, Object>> fetchIndustryList() throws Exception {
        return DbManager.createPosDbManager().executeSql("select * from industrytb");
    }
}
