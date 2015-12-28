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
                            .addElement("td", dbRet.get(index).get("UUID").toString())
                            .addElement("td", dbRet.get(index).get("BANKNAME").toString())
                            .addElement("td", dbRet.get(index).get("POSSERVERNAME").toString())
                            .addElement("td", dbRet.get(index).get("SWINGTIMENAME").toString())
                            .addElement("td", dbRet.get(index).get("MINSWINGMONEY").toString())
                            .addElement("td", dbRet.get(index).get("MAXSWINGMONEY").toString())
                            .addElement("td", dbRet.get(index).get("INDUSTRYNAME").toString())
                            .addElement("td", dbRet.get(index).get("RULEUSEFRE").toString())
                            .addElement("td", dbRet.get(index).get("RULEUSEINTERVAL").toString())
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

    private ArrayList<HashMap<String, Object>> fetchRuleList() throws Exception {
        return PosDbManager.executeSql("SELECT\n" +
                "ruletb.uuid,\n" +
                "banktb.name bankname,\n" +
                "posservertb.name posservername,\n" +
                "swingtimetb.name swingtimename,\n" +
                "ruletb.minswingmoney,\n" +
                "ruletb.maxswingmoney,\n" +
                "industrytb.name industryname,\n" +
                "ruletb.ruleusefre,\n" +
                "ruletb.ruleuseinterval,\n" +
                "ruletb.`status`\n" +
                "FROM\n" +
                "ruletb\n" +
                "INNER JOIN banktb ON banktb.uuid = ruletb.bankuuid\n" +
                "INNER JOIN posservertb ON posservertb.uuid = ruletb.posserveruuid\n" +
                "INNER JOIN industrytb ON ruletb.industryuuid = industrytb.uuid\n" +
                "INNER JOIN swingtimetb ON swingtimetb.uuid = ruletb.swingtimeuuid\n");
    }
}
