package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class RepayUI extends WebUI {
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
                    .addElement("td" , StringUtils.formatCardNO(dbRet.get(index).get("CARDNO").toString()))
                    .addElement("td" , dbRet.get(index).get("CARDMASTER").toString())
                    .addElement("td" , dbRet.get(index).get("TRADEMONEY").toString())
                    .addElement("td", dbRet.get(index).get("FINISHED").toString().equals("0") ?
                            getText("repaysummary.repayfinished") : getText("repaysummary.repayunfinished"))
                    .addElement(new UIContainer("td")
                                .addElement(
                                    new UIContainer("input")
                                        .addAttribute("type", "button")
                                        .addAttribute("value", "查看明细")
                                        .addAttribute("class", "btn radius")
                                        .addAttribute("onclick", "clickDetail('" + dbRet.get(index).get("CARDNO") +
                                                "','" + dbRet.get(index).get("REPAYYEAR") +
                                                "','" + dbRet.get(index).get("REPAYMONTH") + "')")
                                )
                    );
        }
        return htmlString;
    }

    public String generateDetail(String cardNO, String repayYear, String repayMonth) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchRepayDetail(cardNO, repayYear, repayMonth);
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                    .addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("REPAYYEAR")) + "/" +
                            StringUtils.convertNullableString(dbRet.get(index).get("REPAYMONTH")))
                    .addElement("td" , StringUtils.formatCardNO(StringUtils.convertNullableString(dbRet.get(index).get("CARDNO"))))
                    .addElement("td" , StringUtils.convertNullableString(dbRet.get(index).get("CARDMASTER")))
                    .addElement("td" , StringUtils.convertNullableString(dbRet.get(index).get("TRADEMONEY")))
                    .addElement("td" , StringUtils.convertNullableString(dbRet.get(index).get("THEDATE")))
                    .addElement("td" , StringUtils.convertNullableString(dbRet.get(index).get("UNICK")), "○")
                    .addElement("td" , StringUtils.convertNullableString(dbRet.get(index).get("TRADETIME")), "○")
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
                "SUM(repaytb.trademoney) trademoney, " +
                "(COUNT(1) - " +
                "sum(case when tradestatus='enable' then 1 else 0 END)) finished " +
                "FROM " +
                "repaytb " +
                "INNER JOIN cardtb ON cardtb.cardno = repaytb.cardno " +
                whereSql +
                "GROUP BY " +
                "repaytb.repayyear, " +
                "repaytb.repaymonth, " +
                "repaytb.cardno " +
                "ORDER BY " +
                "repaytb.repayyear desc, " +
                "repaytb.repaymonth desc, " +
                "repaytb.thedate desc");
    }

    private ArrayList<HashMap<String, Object>> fetchRepayDetail(String cardNO, String repayYear, String repayMonth) throws Exception {
        String whereSql = "where repaytb.cardno='" + cardNO + "' and repayyear='" + repayYear + "' and repaymonth='"+ repayMonth + "' ";
        if (null != userID_ && userID_.length() != 0)
            whereSql += " and (cardtb.salesmanuuid in (select a.uid from salesmantb a  where a.uid='"+userID_+"' )" +
                    " or cardtb.salesmanuuid in(select salesman from tellertb   where uid='"+userID_+"')) ";

        return PosDbManager.executeSql("SELECT repaytb.id," +
                "repaytb.repayyear, " +
                "repaytb.repaymonth, " +
                "repaytb.cardno, " +
                "cardtb.cardmaster, " +
                "repaytb.trademoney, " +
                "repaytb.thedate, " +
                "repaytb.tradestatus, " +
                "userinfo.unick, " +
                "repaytb.tradetime " +
                "FROM " +
                "repaytb " +
                "INNER JOIN cardtb ON cardtb.cardno = repaytb.cardno " +
                "left JOIN userinfo ON userinfo.uid = repaytb.userid " +
                whereSql +
                "ORDER BY repaytb.tradestatus," +
                "repaytb.thedate");
    }

    private String userID_; // TODO for role
}
