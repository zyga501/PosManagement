package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class SwingCardUI extends WebUI {
    public SwingCardUI(String uid){userID_=uid;}
    public String generateSummary() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchSwingCardSummary();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                    .addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addElement("td" , dbRet.get(index).get("BILLYEAR").toString() + "/" +
                            dbRet.get(index).get("BILLMONTH").toString())
                    .addElement("td" , dbRet.get(index).get("CARDNO").toString())
                    .addElement("td" , dbRet.get(index).get("CARDMASTER").toString())
                    .addElement("td" , dbRet.get(index).get("AMOUNT").toString())
                    .addElement(new UIContainer("td")
                                .addElement(
                                    new UIContainer("input")
                                        .addAttribute("type", "button")
                                        .addAttribute("value", "查看明细")
                                        .addAttribute("class", "btn radius")
                                        .addAttribute("onclick", "clickDetail('" + dbRet.get(index).get("CARDNO") +
                                                "','" + dbRet.get(index).get("BILLYEAR") +
                                                "','" + dbRet.get(index).get("BILLMONTH") + "')")
                                )
                    );
        }
        return htmlString;
    }

    public String generateDetail(String cardNO, String billYear, String billMonth) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchSwingCardDetail(cardNO, billYear, billMonth);
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                    .addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("BILLYEAR")) + "/" +
                            StringUtils.convertNullableString(dbRet.get(index).get("BILLMONTH")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("CARDNO")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("CARDMASTER")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("AMOUNT")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("SDATETM")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("POSNAME")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("UNICK")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("REALSDATETM")))
                    .addElement(new UIContainer("td")
                            .addElement(
                                    new UIContainer("input")
                                            .addAttribute("type", "checkbox")
                                            .addAttribute("value", StringUtils.convertNullableString(dbRet.get(index).get("ID")))
                                            .addAttribute("checked", "checked", StringUtils.convertNullableString(dbRet.get(index).get("VALIDSTATUS")).compareTo("enable") == 0)
                            )
                    )
                    .addElement(new UIContainer("td").addElement(new UIContainer("input")
                            .addAttribute("class" ,StringUtils.convertNullableString(dbRet.get(index).get("SWINGSTATUS")).equals("enable")?"btn btn-success radius":"btn btn-danger radius")
                            .addAttribute("type","button")
                            .addAttribute("title" ,StringUtils.convertNullableString(dbRet.get(index).get("SWINGSTATUS")).equals("enable")?"已刷":"未刷")
                            .addAttribute("datav", StringUtils.convertNullableString(dbRet.get(index).get("ID")))
                            .addAttribute("value" ,StringUtils.convertNullableString(dbRet.get(index).get("SWINGSTATUS")).equals("enable")?"Y":"N")
                            .addAttribute("onclick", "clickswing(this,'" + StringUtils.convertNullableString(dbRet.get(index).get("ID")) + "')")));
        }
        return htmlString;
    }

    private ArrayList<HashMap<String, Object>> fetchSwingCardSummary() throws Exception {
        String whereSql = "";
        if (null != userID_ && userID_.length() != 0)
            whereSql += "where cardtb.salesmanuuid in (select a.uid from salesmantb a  where a.uid='"+userID_+"' )" +
                    " or cardtb.salesmanuuid in(select salesman from tellertb   where uid='"+userID_+"') ";
        return PosDbManager.executeSql("SELECT " +
                "swingcard.billyear, " +
                "swingcard.billmonth, " +
                "swingcard.cardno, " +
                "swingcard.VALIDSTATUS, " +
                "Sum(swingcard.amount) AS amount, " +
                "cardtb.cardmaster " +
                "FROM " +
                "swingcard " +
                "INNER JOIN cardtb ON cardtb.cardno = swingcard.cardno " +
                whereSql +
                "GROUP BY " +
                "swingcard.billyear, " +
                "swingcard.billmonth, " +
                "swingcard.cardno  ORDER BY " +
                "swingcard.billyear ASC, " +
                "swingcard.billmonth ASC");
    }

    private ArrayList<HashMap<String, Object>> fetchSwingCardDetail(String cardNO, String billYear, String billMonth) throws Exception {
        String whereSql = "where swingcard.cardno='" + cardNO + "' and billyear='" + billYear + "' and billmonth='"+ billMonth + "' ";
        if (null != userID_ && userID_.length() != 0)
            whereSql = "and (cardtb.salesmanuuid in (select a.uid from salesmantb a  where a.uid='"+userID_+"' )" +
                    " or cardtb.salesmanuuid in(select salesman from tellertb   where uid='"+userID_+"')) ";

            return PosDbManager.executeSql("SELECT swingcard.id," +
                    "swingcard.billyear, " +
                    "swingcard.billmonth, " +
                    "swingcard.cardno, " +
                    "cardtb.cardmaster, " +
                    "swingcard.amount, " +
                    "swingcard.sdatetm, " +
                    "postb.posname, " +
                    "swingcard.userid, " +
                    "userinfo.unick, " +
                    "swingcard.realsdatetm, " +
                    "swingcard.validstatus, " +
                    "swingcard.SWINGSTATUS " +
                    "FROM " +
                    "swingcard " +
                    "INNER JOIN cardtb ON cardtb.cardno = swingcard.cardno " +
                    "INNER JOIN postb ON postb.uuid = swingcard.posuuid  " +
                    "left JOIN userinfo ON userinfo.uid = swingcard.userid " +
                    whereSql +
                    "ORDER BY swingcard.sdatetm");
    }

    private String userID_; // TODO for role
}
