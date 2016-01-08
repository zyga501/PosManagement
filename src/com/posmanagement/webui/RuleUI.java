package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class RuleUI extends WebUI {
    public String generateHTMLString() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchRuleList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                            .addAttribute("class", "text-c odd")
                            .addAttribute("role", "row")
                            .addElement("td", dbRet.get(index).get("BANKNAME").toString())
                            .addElement("td", dbRet.get(index).get("POSSERVERNAME").toString())
                            .addElement("td", dbRet.get(index).get("SWINGTIMENAME").toString())
                            .addElement("td", dbRet.get(index).get("MINSWINGMONEY").toString())
                            .addElement("td", dbRet.get(index).get("MAXSWINGMONEY").toString())
                            .addElement("td", dbRet.get(index).get("INDUSTRYNAME").toString())
                            .addElement("td", dbRet.get(index).get("RATE").toString())
                            .addElement("td", dbRet.get(index).get("MCC").toString())
                            .addElement("td", dbRet.get(index).get("RULEUSEFRE").toString())
                            .addElement("td", dbRet.get(index).get("RULEUSEINTERVAL").toString())
                            .addElement("td", getText(dbRet.get(index).get("STATUS").toString().compareTo("enable") == 0 ? "global.enable" : "global.disable")
                            );
        }
        return htmlString;
    }

    private ArrayList<HashMap<String, Object>> fetchRuleList() throws Exception {
        return PosDbManager.executeSql("SELECT " +
                "ruletb.uuid, " +
                "banktb.name bankname, " +
                "posservertb.name posservername, " +
                "swingtimetb.name swingtimename, " +
                "ruletb.minswingmoney, " +
                "ruletb.maxswingmoney, " +
                "industrytb.name industryname, " +
                "mcctb.mcc," +
                "ratetb.rate, " +
                "ruletb.ruleusefre, " +
                "ruletb.ruleuseinterval, " +
                "ruletb.`status` " +
                "FROM " +
                "ruletb " +
                "INNER JOIN banktb ON banktb.uuid = ruletb.bankuuid " +
                "INNER JOIN posservertb ON posservertb.uuid = ruletb.posserveruuid " +
                "INNER JOIN industrytb ON ruletb.industryuuid = industrytb.uuid " +
                "INNER JOIN swingtimetb ON swingtimetb.uuid = ruletb.swingtimeuuid " +
                "INNER JOIN mcctb ON mcctb.uuid = ruletb.mccuuid " +
                "INNER JOIN ratetb ON ratetb.uuid = ruletb.rateuuid");
    }
}
