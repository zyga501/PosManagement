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
                            .addElement("td", dbRet.get(index).get("SERVERNAME").toString())
                            .addElement("td", dbRet.get(index).get("SWINGTIME").toString())
                            .addElement("td", dbRet.get(index).get("MINSWINGMONEY").toString())
                            .addElement("td", dbRet.get(index).get("MAXSWINGMONEY").toString())
                            .addElement("td", dbRet.get(index).get("INDUSTRYNAME").toString())
                            .addElement("td", dbRet.get(index).get("INDUSTRYFRE").toString())
                            .addElement("td", dbRet.get(index).get("INDUSTRYINTERVAL").toString())
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
                "ruletb.ruleno,\n" +
                "banktb.bankname,\n" +
                "posservertb.servername,\n" +
                "ruletb.swingtime,\n" +
                "ruletb.minswingmoney,\n" +
                "ruletb.maxswingmoney,\n" +
                "industrytb.industryname,\n" +
                "ruletb.industryfre,\n" +
                "ruletb.industryinterval,\n" +
                "ruletb.ruleusefre,\n" +
                "ruletb.ruleuseinterval,\n" +
                "ruletb.`status`,\n" +
                "industrytb.industrycode\n" +
                "FROM\n" +
                "ruletb\n" +
                "INNER JOIN banktb ON banktb.bankcode = ruletb.bankcode\n" +
                "INNER JOIN posservertb ON posservertb.servercode = ruletb.posservercode\n" +
                "INNER JOIN industrytb ON ruletb.industrycode = industrytb.industrycode\n");
    }
}
