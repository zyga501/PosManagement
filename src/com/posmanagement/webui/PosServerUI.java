package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class PosServerUI extends WebUI {
    public String generateTable() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchPosServerList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                    .addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addAttribute("value", dbRet.get(index).get("UUID").toString())
                    .addElement("td", dbRet.get(index).get("NAME").toString())
                    .addElement("td", getText(dbRet.get(index).get("STATUS").toString().compareTo("enable") == 0 ? "global.enable" : "global.disable")
                    );
        }
        return htmlString;
    }

    public String generateSelect() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchPosServerList();
        if (dbRet.size() <= 0)
            return new String("");

        UIContainer uiContainer = new UIContainer();
        uiContainer.addElement(new UIContainer("option","")
                .addAttribute("value", ""));        ;
        for (int index = 0; index < dbRet.size(); ++index) {
            if (dbRet.get(index).get("STATUS").toString().compareTo("enable") == 0) {
                uiContainer.addElement(new UIContainer("option", dbRet.get(index).get("NAME").toString())
                                        .addAttribute("value", dbRet.get(index).get("UUID").toString()));
            }
        }

        return uiContainer.generateUI();
    }

    private ArrayList<HashMap<String, Object>> fetchPosServerList() throws Exception {
        return PosDbManager.executeSql("select * from posservertb");
    }
}
