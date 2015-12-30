package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class SwingCardUI {
    public String generateDetail() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchSwingCardDetail();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                    .addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addElement(new UIContainer("td")
                            .addElement(
                                    new UIContainer("input")
                                            .addAttribute("type", "radio")
                                            .addAttribute("name", "scid")
                                            .addAttribute("value" ,StringUtils.convertNullableString(dbRet.get(index).get("ID")))
                                            .addAttribute("checked", "checked",false)
                            ))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("BILLYEAR")) + "/" +
                            StringUtils.convertNullableString(dbRet.get(index).get("BILLMONTH")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("CARDNO")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("CARDMASTER")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("AMOUNT")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("SDATETM")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("POSUUID")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("POSNAME")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("SWINGSTATUS")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("TELLER")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("REALSDATETM")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("SALESMAN")))
                    .addElement(new UIContainer("td").addElement(new UIContainer("input")
                            .addAttribute("class" ,StringUtils.convertNullableString(dbRet.get(index).get("VALIDSTATUS")).equals("enable")?"btn btn-success radius":"btn btn-danger radius")
                            .addAttribute("type","button")
                            .addAttribute("title" ,StringUtils.convertNullableString(dbRet.get(index).get("VALIDSTATUS")).equals("enable")?"已开启":"未开启")
                            .addAttribute("datav", StringUtils.convertNullableString(dbRet.get(index).get("ID")))
                            .addAttribute("value" ,StringUtils.convertNullableString(dbRet.get(index).get("VALIDSTATUS")).equals("enable")?"Y":"N")));
        }
        return htmlString;
    }

    private ArrayList<HashMap<String, Object>> fetchSwingCardSummary() throws Exception {
        return PosDbManager.executeSql("SELECT\n" +
                "swingcard.billyear,\n" +
                "swingcard.billmonth,\n" +
                "swingcard.cardno,\n" +
                "cardtb.cardmaster\n" +
                "FROM\n" +
                "swingcard\n" +
                "INNER JOIN cardtb ON cardtb.cardno = swingcard.cardno\n" +
                "GROUP BY\n" +
                "swingcard.billyear,\n" +
                "swingcard.billmonth,\n" +
                "swingcard.cardno\n" +
                "ORDER BY\n" +
                "swingcard.sdatetm");
    }

    private ArrayList<HashMap<String, Object>> fetchSwingCardDetail() throws Exception {
        return PosDbManager.executeSql("SELECT\n" +
                "swingcard.billyear,\n" +
                "swingcard.billmonth,\n" +
                "swingcard.cardno,\n" +
                "cardtb.cardmaster,\n" +
                "swingcard.amount,\n" +
                "swingcard.sdatetm,\n" +
                "postb.uuid posuuid,\n" +
                "postb.posname,\n" +
                "swingcard.telleruuid,\n" +
                "swingcard.realsdatetm,\n" +
                "swingcard.salesmanuuid,\n" +
                "swingcard.validstatus\n" +
                "FROM\n" +
                "swingcard\n" +
                "INNER JOIN cardtb ON cardtb.cardno = swingcard.cardno\n" +
                "INNER JOIN postb ON postb.uuid = swingcard.posuuid\n" +
                "ORDER BY swingcard.sdatetm");
    }
}
