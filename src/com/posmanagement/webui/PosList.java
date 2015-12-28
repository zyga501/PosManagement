package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class PosList {
    public String generateHTMLString() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchPosList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                    .addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addElement(new UIContainer("td")
                            .addElement(
                                    new UIContainer("input")
                                            .addAttribute("type", "radio")
                                            .addAttribute("name", "posid")
                                            .addAttribute("value", dbRet.get(index).get("POSID").toString())
                                            .addAttribute("checked", "checked",false)
                            ))
                    .addElement("td", dbRet.get(index).get("POSNAME").toString())
                    .addElement("td", dbRet.get(index).get("INDEUSTRYNAME").toString())
                    .addElement("td", dbRet.get(index).get("FEE").toString())
                    .addElement("td", dbRet.get(index).get("POSSERVER").toString())
                    .addElement("td", dbRet.get(index).get("MCC").toString());
        }
        return htmlString;
    }

    public String generateMasterString(String posid) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet =  PosDbManager.executeSql("select posname from postb where posid='"+posid+"'");
        if (dbRet.size() > 0)
            return dbRet.get(0).get("POSNAME").toString();
        else
            return "Not exists this POS!";
    }

    private ArrayList<HashMap<String, Object>> fetchPosList() throws Exception {
        return PosDbManager.executeSql("select * from postb");
    }
}