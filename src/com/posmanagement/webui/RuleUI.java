package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class RuleUI extends WebUI {
    public String generateTable() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchRuleList(null);
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                            .addAttribute("class", "text-c odd")
                            .addAttribute("role", "row")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("POSSERVERNAME")), "○")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("SWINGTIMENAME")), "○")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("MINSWINGMONEY")), "○")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("MAXSWINGMONEY")), "○")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("INDUSTRYNAME")), "○")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("RATE")), "○")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("MCC")), "○")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("RULEUSEFRE")), "○")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("RULEUSEINTERVAL")), "○")
                            .addElement(new UIContainer("td")
                                    .addElement(
                                            new UIContainer("input")
                                                    .addAttribute("type", "button")
                                                    .addAttribute("value", "分配")
                                                    .addAttribute("class", "btn radius")
                                                    .addAttribute("onclick", "clickRule('" + dbRet.get(index).get("UUID") + "')"))
                            );
        }
        return htmlString;
    }

    public String generaterInfo(String ruleUUID) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchRuleList(ruleUUID);
        if (dbRet.size() != 1)
            return new String("");

        String htmlString = new String();
        if (StringUtils.convertNullableString(dbRet.get(0).get("POSSERVERNAME")).length() > 0) {
            htmlString += dbRet.get(0).get("POSSERVERNAME").toString() + "<br>";
        }
        if (StringUtils.convertNullableString(dbRet.get(0).get("SWINGTIMENAME")).length() > 0) {
            htmlString += dbRet.get(0).get("SWINGTIMENAME").toString() + "<br>";
        }
        if (StringUtils.convertNullableString(dbRet.get(0).get("INDUSTRYNAME")).length() > 0) {
            htmlString += dbRet.get(0).get("INDUSTRYNAME").toString() + "<br>";
        }
        if (StringUtils.convertNullableString(dbRet.get(0).get("RATE")).length() > 0) {
            htmlString += dbRet.get(0).get("RATE").toString();
            if (StringUtils.convertNullableString(dbRet.get(0).get("MAXFEE")).length() > 0) {
                htmlString += " 封顶" + dbRet.get(0).get("MAXFEE").toString();
            }
            else
                htmlString += " 非封顶";
            htmlString += "<br>";
        }
        return htmlString;
    }

    private ArrayList<HashMap<String, Object>> fetchRuleList(String ruleUUID) throws Exception {
        String whereSql = new String();
        if (ruleUUID != null && ruleUUID.length() != 0) {
            whereSql += " where ruletb.uuid='" + ruleUUID + "'";
        }

        return PosDbManager.executeSql("SELECT " +
                "ruletb.uuid, " +
                "posservertb.name posservername, " +
                "swingtimetb.name swingtimename, " +
                "ruletb.minswingmoney, " +
                "ruletb.maxswingmoney, " +
                "industrytb.name industryname, " +
                "mcctb.mcc," +
                "ratetb.rate, " +
                "ratetb.maxfee, " +
                "ruletb.ruleusefre, " +
                "ruletb.ruleuseinterval, " +
                "ruletb.`status` " +
                "FROM " +
                "ruletb " +
                "LEFT JOIN posservertb ON posservertb.uuid = ruletb.posserveruuid " +
                "LEFT JOIN industrytb ON ruletb.industryuuid = industrytb.uuid " +
                "LEFT JOIN swingtimetb ON swingtimetb.uuid = ruletb.swingtimeuuid " +
                "LEFT JOIN mcctb ON mcctb.uuid = ruletb.mccuuid " +
                "LEFT JOIN ratetb ON ratetb.uuid = ruletb.rateuuid" +
                whereSql);
    }
}
