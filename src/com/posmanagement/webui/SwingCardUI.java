package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.SQLUtils;
import com.posmanagement.utils.StringUtils;
import com.posmanagement.utils.UserUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class SwingCardUI extends WebUI {
    public SwingCardUI(String uid) {
        userID_=uid;
    }

    public String generateSummary() throws Exception {
        return generateSummary(-1);
    }

    public String generateSummary(int pageIndex) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchSwingCardSummary(pageIndex);
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                    .addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addElement("td", dbRet.get(index).get("BILLYEAR").toString() + "/" +
                            dbRet.get(index).get("BILLMONTH").toString())
                    .addElement("td", StringUtils.formatCardNO(dbRet.get(index).get("CARDNO").toString()))
                    .addElement("td", dbRet.get(index).get("BANKNAME").toString())
                    .addElement("td", dbRet.get(index).get("CARDMASTER").toString())
                    .addElement("td", dbRet.get(index).get("FINISHED").toString().equals("0")?
                            getText("swingcardsummary.swingfinished") : getText("swingcardsummary.swingunfinished"))
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
                    .addElement("td" , StringUtils.formatCardNO(StringUtils.convertNullableString(dbRet.get(index).get("CARDNO"))))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("CARDMASTER")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("AMOUNT")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("SDATETM")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("POSNAME")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("UNICK")), "○")
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("REALSDATETM")), "○")
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

    public int fetchSwingCardPageCount() throws Exception {
        String whereSql = SQLUtils.BuildWhereCondition(uiConditions_);
        if (!whereSql.isEmpty()) {
            whereSql = " where " + whereSql;
        }

        if (!(new UserUtils()).isAdmin(userID_))
            whereSql += " and  (cardtb.salesmanuuid in (select a.uid from salesmantb a  where a.uid='"+userID_+"' )" +
                    " or cardtb.salesmanuuid in(select salesman from tellertb   where uid='"+userID_+"')) ";

        ArrayList<HashMap<String, Object>> rect = PosDbManager.executeSql("SELECT count(*) as cnt " +
                "FROM " +
                "swingcard " +
                "INNER JOIN cardtb ON cardtb.cardno = swingcard.cardno " +
                "INNER JOIN billtb ON CONVERT(swingcard.billyear, SIGNED)= CONVERT(SUBSTR(billtb.billdate,1,4), SIGNED) AND convert(swingcard.billmonth, SIGNED)= convert(SUBSTR(billtb.billdate,6,2), SIGNED) AND swingcard.cardno = billtb.cardno "+
                "INNER JOIN banktb on banktb.uuid=cardtb.bankuuid "+
                whereSql +
                " GROUP BY " +
                "swingcard.billyear, " +
                "swingcard.billmonth, " +
                "swingcard.cardno  ORDER BY " +
                "swingcard.billyear desc, " +
                "swingcard.billmonth desc");
        if (rect.size()<=0) {
            return 0;
        }
        return Integer.parseInt(rect.get(0).get("CNT").toString())/ WebUI.DEFAULTITEMPERPAGE + 1;
    }

    private ArrayList<HashMap<String, Object>> fetchSwingCardSummary(int pageIndex) throws Exception {
        String whereSql = SQLUtils.BuildWhereCondition(uiConditions_);
        if (!whereSql.isEmpty()) {
            whereSql = " where " + whereSql;
        }
        String limitSql = new String();
        if (pageIndex > 0) {
            limitSql = "limit " + (pageIndex - 1) * DEFAULTITEMPERPAGE + "," + DEFAULTITEMPERPAGE;
        }

        if (!(new UserUtils()).isAdmin(userID_))
            whereSql += " and  (cardtb.salesmanuuid in (select a.uid from salesmantb a  where a.uid='"+userID_+"' )" +
                    " or cardtb.salesmanuuid in(select salesman from tellertb   where uid='"+userID_+"')) ";

        return PosDbManager.executeSql("SELECT " +
                "swingcard.billyear, " +
                "swingcard.billmonth, " +
                "swingcard.cardno, " +
                "Sum(swingcard.amount) AS amount, " +
                "cardtb.cardmaster, (count(1) -" +
                "sum(case when swingstatus='enable' then 1 else 0 END)) finished, " +
                "banktb.name as  bankname " +
                "FROM " +
                "swingcard " +
                "INNER JOIN cardtb ON cardtb.cardno = swingcard.cardno " +
                "INNER JOIN billtb ON CONVERT(swingcard.billyear, SIGNED)= CONVERT(SUBSTR(billtb.billdate,1,4), SIGNED) AND convert(swingcard.billmonth, SIGNED)= convert(SUBSTR(billtb.billdate,6,2), SIGNED) AND swingcard.cardno = billtb.cardno "+
                "INNER JOIN banktb on banktb.uuid=cardtb.bankuuid "+
                whereSql +
                " GROUP BY " +
                "swingcard.billyear, " +
                "swingcard.billmonth, " +
                "swingcard.cardno  ORDER BY " +
                "swingcard.billyear desc, " +
                "swingcard.billmonth desc "+
                limitSql);
    }

    private ArrayList<HashMap<String, Object>> fetchSwingCardDetail(String cardNO, String billYear, String billMonth) throws Exception {
        String whereSql = "where swingcard.cardno='" + cardNO + "' and billyear='" + billYear + "' and billmonth='"+ billMonth + "' ";
        if (null != userID_ && userID_.length() != 0)
            whereSql += "and (cardtb.salesmanuuid in (select a.uid from salesmantb a  where a.uid='"+userID_+"' )" +
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
                    "swingcard.swingstatus " +
                    "FROM " +
                    "swingcard " +
                    "INNER JOIN cardtb ON cardtb.cardno = swingcard.cardno " +
                    "INNER JOIN postb ON postb.uuid = swingcard.posuuid  " +
                    "left JOIN userinfo ON userinfo.uid = swingcard.userid " +
                    whereSql +
                    "ORDER BY swingcard.swingstatus, " +
                    "swingcard.sdatetm");
    }

    private String userID_;
}
