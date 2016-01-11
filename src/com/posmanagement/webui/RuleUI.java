package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;

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
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("BANKNAME")))
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("POSSERVERNAME")), "○")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("SWINGTIMENAME")), "○")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("MINSWINGMONEY")), "○")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("MAXSWINGMONEY")), "○")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("INDUSTRYNAME")), "○")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("RATE")), "○")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("MCC")), "○")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("RULEUSEFRE")), "○")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("RULEUSEINTERVAL")), "○")
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
                "LEFT JOIN banktb ON banktb.uuid = ruletb.bankuuid " +
                "LEFT JOIN posservertb ON posservertb.uuid = ruletb.posserveruuid " +
                "LEFT JOIN industrytb ON ruletb.industryuuid = industrytb.uuid " +
                "LEFT JOIN swingtimetb ON swingtimetb.uuid = ruletb.swingtimeuuid " +
                "LEFT JOIN mcctb ON mcctb.uuid = ruletb.mccuuid " +
                "LEFT JOIN ratetb ON ratetb.uuid = ruletb.rateuuid");
    }
}
