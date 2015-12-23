package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class SwingTimeList {
    public SwingTimeList(WebUI.UIMode _uiMode) {
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
            htmlString += new UIContainer("tr")
                    .addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addElement("td", dbRet.get(index).get("SWINGTIME").toString())
                    .addElement(new UIContainer("td")
                                    .addElement(
                                            new UIContainer("input")
                                                    .addAttribute("type", "time")
                                                    .addAttribute("value", dbRet.get(index).get("STARTTIME").toString())
                                    )
                    )
                    .addElement(new UIContainer("td")
                            .addElement(
                                    new UIContainer("input")
                                            .addAttribute("type", "time")
                                            .addAttribute("value", dbRet.get(index).get("ENDTIME").toString())
                            )
                    )
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
        ArrayList<HashMap<String, Object>> dbRet = fetchSwingTimeList();
        if (dbRet.size() <= 0)
            return new String("");

        UIContainer uiContainer = new UIContainer();
        for (int index = 0; index < dbRet.size(); ++index) {
            if (dbRet.get(index).get("STATUS").toString().compareTo("enable") == 0) {
                uiContainer.addElement(new UIContainer("option", dbRet.get(index).get("SWINGTIME").toString())
                                        .addAttribute("value", dbRet.get(index).get("TIMERID").toString()));
            }
        }

        return uiContainer.generateUI();
    }

    private ArrayList<HashMap<String, Object>> fetchSwingTimeList() throws Exception {
        return PosDbManager.executeSql("select * from swingtimetb");
    }

    private WebUI.UIMode uiMode;
}
