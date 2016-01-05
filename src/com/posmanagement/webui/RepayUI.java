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
                    .addElement("td" , dbRet.get(index).get("REPAYYEAR").toString() + "/" +
                            dbRet.get(index).get("REPAYMONTH").toString())
                    .addElement("td" , dbRet.get(index).get("CARDNO").toString())
                    .addElement("td" , dbRet.get(index).get("CARDMASTER").toString())
                    .addElement("td" , dbRet.get(index).get("TRADEMONEY").toString())
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
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("REPAYYEAR")) + "/" +
                            StringUtils.convertNullableString(dbRet.get(index).get("REPAYMONTH")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("CARDNO")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("CARDMASTER")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("TRADEMONEY")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("THEDATE")))
                    .addElement("td" ,"")
                    .addElement("td" ,"")
                    .addElement(new UIContainer("td")
                            .addElement(
                                    new UIContainer("input")
                                            .addAttribute("type", "checkbox")
                                            .addAttribute("value", StringUtils.convertNullableString(dbRet.get(index).get("ID")))
                                            .addAttribute("checked", "checked", StringUtils.convertNullableString(dbRet.get(index).get("VALIDSTATUS")).compareTo("enable") == 0)
                            )
                    )
                    .addElement(new UIContainer("td").addElement(new UIContainer("input")
                            .addAttribute("class" ,StringUtils.convertNullableString(dbRet.get(index).get("TRADESTATUS")).equals("enable")?"btn btn-success radius":"btn btn-danger radius")
                            .addAttribute("type","button")
                            .addAttribute("title" ,StringUtils.convertNullableString(dbRet.get(index).get("TRADESTATUS")).equals("enable")?"已刷":"未刷")
                            .addAttribute("datav", StringUtils.convertNullableString(dbRet.get(index).get("ID")))
                            .addAttribute("value" ,StringUtils.convertNullableString(dbRet.get(index).get("TRADESTATUS")).equals("enable")?"Y":"N")
                            .addAttribute("onclick", "clickrepay(this,'" + StringUtils.convertNullableString(dbRet.get(index).get("ID")) + "')")));
        }
        return htmlString;
    }

    private ArrayList<HashMap<String, Object>> fetchRepaySummary() throws Exception {
        String whereSql = "";
        if (null != userID_ && userID_.length() != 0)
            whereSql += "where cardtb.salesmanuuid in (select a.uid from salesmantb a  where a.uid='"+userID_+"' )" +
                    " or cardtb.salesmanuuid in(select salesman from tellertb   where uid='"+userID_+"') ";

        return PosDbManager.executeSql("SELECT " +
                "repaytb.repayyear, " +
                "repaytb.repaymonth, " +
                "repaytb.thedate, " +
                "cardtb.cardno, " +
                "cardtb.cardmaster, " +
                "SUM(repaytb.trademoney) trademoney " +
                "FROM " +
                "repaytb " +
                "INNER JOIN cardtb ON cardtb.cardno = repaytb.cardno " +
                whereSql +
                "GROUP BY " +
                "repaytb.repayyear, " +
                "repaytb.repaymonth, " +
                "repaytb.cardno " +
                "ORDER BY " +
                "repaytb.repayyear ASC, " +
                "repaytb.repaymonth ASC, " +
                "repaytb.thedate ASC");
    }

    private ArrayList<HashMap<String, Object>> fetchRepayDetail() throws Exception {
        String whereSql = "";
        if (null != userID_ && userID_.length() != 0)
            whereSql += "where cardtb.salesmanuuid in (select a.uid from salesmantb a  where a.uid='"+userID_+"' )" +
                    " or cardtb.salesmanuuid in(select salesman from tellertb   where uid='"+userID_+"') ";

        return PosDbManager.executeSql("SELECT repaytb.id," +
                "repaytb.repayyear, " +
                "repaytb.repaymonth, " +
                "repaytb.cardno, " +
                "cardtb.cardmaster, " +
                "repaytb.trademoney, " +
                "repaytb.thedate, " +
                "repaytb.validstatus, " +
                "repaytb.tradestatus " +
                "FROM " +
                "repaytb " +
                "INNER JOIN cardtb ON cardtb.cardno = repaytb.cardno " +
                "left JOIN userinfo ON userinfo.uid = repaytb.userid " +
                whereSql +
                "ORDER BY repaytb.thedate");
    }

    private String userID_; // TODO for role
}
