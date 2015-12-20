package com.posmanagement.webui;

import com.posmanagement.utils.DbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class RateList {
    public enum UIMode {
        TABLELIST,
        SELECTLIST
    }

    public RateList(UIMode _uidMode) {
        uiMode = _uidMode;
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
        ArrayList<HashMap<String, Object>> dbRet = fetchRateList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString +="<tr class=\"text-c odd\" role=\"row\">"+
                    "<td>"+ dbRet.get(index).get("RATE")+"</td>"+
                    "<td><input type=\"checkbox\"";
            if (dbRet.get(index).get("ENABLED").toString().compareTo("on") == 0) {
                htmlString += "checked=\"checked\"";
            }
            htmlString += " /></td></tr>";
        }
        return htmlString;
    }

    public String generateSelectList() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchRateList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            if (dbRet.get(index).get("ENABLED").toString().compareTo("on") == 0) {
                htmlString +="<option " +
                        "value=\""+dbRet.get(index).get("RID")+"\">"+
                        dbRet.get(index).get("RATE")+"</option>";
            }
        }

        return htmlString;
    }

    private ArrayList<HashMap<String, Object>> fetchRateList() throws Exception {
        return DbManager.createPosDbManager().executeSql("select * from ratetb");
    }

    private UIMode uiMode;
}
