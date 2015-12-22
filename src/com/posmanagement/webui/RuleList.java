package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class RuleList {
    public String generateHTMLString() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchRuleList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                            .addAttribute("class", "text-c odd")
                            .addAttribute("role", "row")
                            .addElement("td", dbRet.get(index).get("RULENO").toString())
                            .addElement("td", dbRet.get(index).get("BANKNAME").toString())
                            .addElement("td", dbRet.get(index).get("POSSERVER").toString())
                            .addElement("td", dbRet.get(index).get("MINSWINGNUM").toString())
                            .addElement("td", dbRet.get(index).get("MAXSWINGNUM").toString())
                            .addElement("td", dbRet.get(index).get("SWINGTIME").toString())
                            .addElement("td", dbRet.get(index).get("MINSWINGMONEY").toString())
                            .addElement("td", dbRet.get(index).get("MAXSWINGMONEY").toString())
                            .addElement("td", dbRet.get(index).get("SWINGPERCENT").toString())
                            .addElement("td", dbRet.get(index).get("INDUSTRYNAME").toString())
                            .addElement("td", dbRet.get(index).get("INDUSTRYFRE").toString())
                            .addElement("td", dbRet.get(index).get("INDUSTRYINTERVAL").toString())
                            .addElement("td", dbRet.get(index).get("RATE").toString())
                            .addElement("td", dbRet.get(index).get("RATEFRE").toString())
                            .addElement("td", dbRet.get(index).get("RATEINTERVAL").toString())
                            .addElement("td", dbRet.get(index).get("MCC").toString())
                            .addElement("td", dbRet.get(index).get("MCCFRE").toString())
                            .addElement("td", dbRet.get(index).get("MCCINTERVAL").toString())
                            .addElement("td", dbRet.get(index).get("USEFRE").toString())
                            .addElement("td", dbRet.get(index).get("USEINTERVAL").toString())
                            .addElement("td", dbRet.get(index).get("RULEUSEFRE").toString())
                            .addElement("td", dbRet.get(index).get("RULEUSEINTERVAL").toString())
                            .addElement(new UIContainer("td")
                                        .addElement(
                                                new UIContainer("input")
                                                .addAttribute("type", "checkbox")
                                                .addAttribute("checked", "checked", dbRet.get(index).get("STATUS").toString().compareTo("on") == 0)
                                        )
                            );
        }
        return htmlString;
    }

    private ArrayList<HashMap<String, Object>> fetchRuleList() throws Exception {
        return PosDbManager.executeSql("select * from ruletb");
    }
}
