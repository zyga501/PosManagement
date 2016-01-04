package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class RepayUI {
    public RepayUI(String uid){userID_=uid;}
    public String generateSummary() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchRepaySummary();
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

    public String generateDetail() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchRepayDetail();
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
                    //.addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("SWINGSTATUS")))
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

    private ArrayList<HashMap<String, Object>> fetchRepaySummary() throws Exception {
        if (null==userID_ || userID_.equals(""))
        return PosDbManager.executeSql("SELECT " +
                "repayment.thedate, " +
                "repayment.incardno, " +
                "repayment.outcardno, " +
                "repayment.VALIDSTATUS, " +
                "repayment.tradestatus, " +
                "Sum(repayment.trademoney) AS amount, " +
                "cardtb.cardmaster " +
                "FROM " +
                "repayment " +
                "INNER JOIN cardtb ON cardtb.cardno = repayment.incardno " +
                "GROUP BY " +
                "repayment.billyear, " +
                "repayment.billmonth, " +
                "repayment.cardno " +
                "ORDER BY " +
                "repayment.thedate ASC, " +
                "repayment.incardno ASC");
        else
            return PosDbManager.executeSql("SELECT " +
                    "repayment.billyear, " +
                    "repayment.billmonth, " +
                    "repayment.cardno, " +
                    "repayment.VALIDSTATUS, " +
                    "Sum(repayment.amount) AS amount, " +
                    "cardtb.cardmaster " +
                    "FROM " +
                    "repayment " +
                    "INNER JOIN cardtb ON cardtb.cardno = repayment.cardno " +
                    "where cardtb.salesmanuuid in (select a.uid from salesmantb a  where a.uid='"+userID_+"' )" +
                    " or cardtb.salesmanuuid in(select salesman from tellertb   where uid='"+userID_+"') " +
                    "GROUP BY " +
                    "repayment.billyear, " +
                    "repayment.billmonth, " +
                    "repayment.cardno  ORDER BY " +
                    "repayment.billyear ASC, " +
                    "repayment.billmonth ASC");
    }

    private ArrayList<HashMap<String, Object>> fetchRepayDetail() throws Exception {
        if (null==userID_ || userID_.equals(""))
        return PosDbManager.executeSql("SELECT repayment.id," +
                "repayment.billyear, " +
                "repayment.billmonth, " +
                "repayment.cardno, " +
                "cardtb.cardmaster, " +
                "repayment.amount, " +
                "repayment.sdatetm, " +
                "postb.posname, " +
                "repayment.userid, " +
                "userinfo.unick, " +
                "repayment.realsdatetm, " +
                "repayment.validstatus, " +
                "repayment.SWINGSTATUS " +
                "FROM " +
                "repayment " +
                "INNER JOIN cardtb ON cardtb.cardno = repayment.cardno " +
                "INNER JOIN postb ON postb.uuid = repayment.posuuid " +
                "left JOIN userinfo ON userinfo.uid = repayment.userid " +
                "ORDER BY repayment.sdatetm");
        else
            return PosDbManager.executeSql("SELECT repayment.id," +
                    "repayment.billyear, " +
                    "repayment.billmonth, " +
                    "repayment.cardno, " +
                    "cardtb.cardmaster, " +
                    "repayment.amount, " +
                    "repayment.sdatetm, " +
                    "postb.posname, " +
                    "repayment.userid, " +
                    "userinfo.unick, " +
                    "repayment.realsdatetm, " +
                    "repayment.validstatus, " +
                    "repayment.SWINGSTATUS " +
                    "FROM " +
                    "repayment " +
                    "INNER JOIN cardtb ON cardtb.cardno = repayment.cardno " +
                    "INNER JOIN postb ON postb.uuid = repayment.posuuid  " +
                    "left JOIN userinfo ON userinfo.uid = repayment.userid " +
                    "where cardtb.salesmanuuid in (select a.uid from salesmantb a  where a.uid='"+userID_+"' )" +
                    " or cardtb.salesmanuuid in(select salesman from tellertb   where uid='"+userID_+"') " +
                    "ORDER BY repayment.sdatetm");
    }

    private String userID_; // TODO for role
}
