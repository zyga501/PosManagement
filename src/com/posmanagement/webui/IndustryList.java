package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class IndustryList {
    public IndustryList(WebUI.UIMode _uidMode) {
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
        ArrayList<HashMap<String, Object>> dbRet = fetchIndustryList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                            .addAttribute("class", "text-c odd")
                            .addAttribute("role", "row")
                            .addElement("td", dbRet.get(index).get("NAME").toString())
                            .addElement(new UIContainer("td")
                                        .addElement(
                                                new UIContainer("input")
                                                    .addAttribute("type", "checkbox")
                                                    .addAttribute("checked", "checked", dbRet.get(index).get("STATUS").toString().compareTo("enable") == 0)
                                        )
                            );
        }
        return htmlString;
    }

    public String generateSelectList() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchIndustryList();
        if (dbRet.size() <= 0)
            return new String("");

        UIContainer uiContainer = new UIContainer();
        uiContainer.addElement(new UIContainer("option","")
                .addAttribute("value", ""));
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

    private WebUI.UIMode uiMode;
}
