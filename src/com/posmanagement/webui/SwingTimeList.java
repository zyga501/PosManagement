package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class SwingTimeList {
    public enum UIMode {
        TABLELIST,
        SELECTLIST
    }

    public SwingTimeList(UIMode _uiMode) {
        uiMode = _uiMode;
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
        ArrayList<HashMap<String, Object>> dbRet = fetchSwingTimeList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString +="<tr class=\"text-c odd\" role=\"row\">"+
                    "<td>"+ dbRet.get(index).get("SWINGTIME")+"</td>"+
                    "<td><input type=\"time\" value=\"" + dbRet.get(index).get("STARTTIME").toString() + "\" </td>"+
                    "<td><input type=\"time\" value=\"" + dbRet.get(index).get("ENDTIME").toString() + "\" </td>"+
                    "<td><input type=\"checkbox\"";
            if (dbRet.get(index).get("ENABLED").toString().compareTo("on") == 0) {
                htmlString += "checked=\"checked\"";
            }
            htmlString += " /></td></tr>";
        }
        return htmlString;
    }

    public String generateSelectList() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchSwingTimeList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            if (dbRet.get(index).get("ENABLED").toString().compareTo("on") == 0) {
                htmlString += "<option " +
                        "value=\"" + dbRet.get(index).get("TIMERID") + "\">" +
                        dbRet.get(index).get("SWINGTIME") + "</option>";
            }
        }

        return htmlString;
    }

    private ArrayList<HashMap<String, Object>> fetchSwingTimeList() throws Exception {
        return PosDbManager.executeSql("select * from swingtimetb");
    }

    private UIMode uiMode;
}
