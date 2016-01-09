package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class IndustryUI extends WebUI {
    public String generateIndustryTable() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchIndustryList();
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

    public String generateIndustrySelectList() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchIndustryList();
        if (dbRet.size() <= 0)
            return new String("");

        UIContainer uiContainer = new UIContainer();
        for (int index = 0; index < dbRet.size(); ++index) {
            if (dbRet.get(index).get("STATUS").toString().compareTo("enable") == 0) {
                uiContainer.addElement(new UIContainer("option", dbRet.get(index).get("NAME").toString())
                                        .addAttribute("value", dbRet.get(index).get("UUID").toString()));
            }
        }

        return uiContainer.generateUI();
    }

    private ArrayList<HashMap<String, Object>> fetchIndustryList() throws Exception {
        return PosDbManager.executeSql("select * from industrytb");
    }
}
