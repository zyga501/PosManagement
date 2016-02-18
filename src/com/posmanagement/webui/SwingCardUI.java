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
                    .addElement("td", dbRet.get(index).get("AMOUNT").toString())
                    .addElement("td", dbRet.get(index).get("PAYCHARGE").toString())
                    .addElement("td", dbRet.get(index).get("BANKNAME").toString())
                    .addElement("td", dbRet.get(index).get("CARDMASTER").toString())
                    .addElement("td", dbRet.get(index).get("UNFINISHED").toString().equals("0")?
                            getText("swingcardsummary.swingfinished") : getText("swingcardsummary.swingunfinished"))
                    .addElement(new UIContainer("td")
                            .addElement(
                                    new UIContainer("input")
                                            .addAttribute("type", "button")
                                            .addAttribute("value", "查看明细")
                                            .addAttribute("class", "btn radius")
                                            .addAttribute("onclick", "clickDetail('" + dbRet.get(index).get("CARDNO") +
                                                    "','" + dbRet.get(index).get("BILLUUID") + "')")
                            ).addElement("span", "刷<b>"+StringUtils.convertNullableString(dbRet.get(index).get("FINISHED"))+
                                    "</b>,未<b>"+StringUtils.convertNullableString(dbRet.get(index).get("UNFINISHED"))+"</b>,共<b>"+
                                    StringUtils.convertNullableString(dbRet.get(index).get("TOTALCOUNT"))+"</b>笔")
                    );
        }
        return htmlString;
    }

    public String generateDetail(String cardNO, String billUUID) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchSwingCardDetail(cardNO, billUUID);
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
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("PAYCHARGE")))
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

        if (!UserUtils.isAdmin(userID_))
            whereSql += " and  (cardtb.salemanuuid in (select a.uid from salemantb a  where a.uid='"+userID_+"' )" +
                    " or cardtb.salemanuuid in(select salemanuuid from tellertb   where uid='"+userID_+"')) ";

        ArrayList<HashMap<String, Object>> resultMap = PosDbManager.executeSql("SELECT count(*) as cnt\n" +
                "FROM\n" +
                "swingcard\n" +
                "INNER JOIN cardtb ON cardtb.cardno = swingcard.cardno \n" +
                "INNER JOIN billtb ON swingcard.billuuid = billtb.uuid AND swingcard.cardno = billtb.cardno \n" +
                "INNER JOIN banktb on banktb.uuid=cardtb.bankuuid  \n" +
                whereSql +
                " GROUP BY\n" +
                "billtb.billdate,\n" +
                "swingcard.cardno\n");
        if (resultMap.size()<=0) {
            return 0;
        }
        return Integer.parseInt(resultMap.get(0).get("CNT").toString())/ WebUI.DEFAULTITEMPERPAGE + 1;
    }

    private ArrayList<HashMap<String, Object>> fetchSwingCardSummary(int pageIndex) throws Exception {
        String whereSql = SQLUtils.BuildWhereCondition(uiConditions_);
        if (!whereSql.isEmpty()) {
            whereSql = " where " + whereSql;
        }
        String limitSql ="limit " + (pageIndex - 1) * DEFAULTITEMPERPAGE + "," + DEFAULTITEMPERPAGE;

        if (!UserUtils.isAdmin(userID_))
            whereSql += " and  (cardtb.salemanuuid in (select a.uid from salemantb a  where a.uid='"+userID_+"' )" +
                    " or cardtb.salemanuuid in(select salemanuuid from tellertb   where uid='"+userID_+"')) ";

        return PosDbManager.executeSql("SELECT \n" +
                "SUBSTR(billtb.billdate,1,4) billyear, \n" +
                "SUBSTR(billtb.billdate,6,2) billmonth, \n" +
                "swingcard.cardno, \n" +
                "billtb.uuid billuuid, \n" +
                "Sum(swingcard.amount) AS amount, \n" +
                "Sum(case when swingcard.amount*ratetb.rate/100>ratetb.MAXFEE and ratetb.maxfee>0 then " +
                "ratetb.maxfee else swingcard.amount*ratetb.rate/100  end)  paycharge, \n" +
                "cardtb.cardmaster, \n" +
                "count(1) totalcount,"+
                "sum(case when swingstatus='enable' then 1 else 0 END) finished, \n" +
                "(count(1) - sum(case when swingstatus='enable' then 1 else 0 END)) unfinished, \n" +
                "banktb.name as bankname \n" +
                "FROM \n" +
                "swingcard \n" +
                "INNER JOIN cardtb ON cardtb.cardno = swingcard.cardno \n" +
                "INNER JOIN postb ON postb.uuid = swingcard.posuuid \n" +
                "INNER JOIN ratetb ON postb.rateuuid = ratetb.uuid \n" +
                "INNER JOIN billtb ON swingcard.billuuid = billtb.uuid AND swingcard.cardno = billtb.cardno \n" +
                "INNER JOIN banktb on banktb.uuid=cardtb.bankuuid  \n" +
                whereSql +
                "GROUP BY billtb.billdate, swingcard.cardno \n" +
                "ORDER BY billtb.billdate desc\n" +
                limitSql);
    }

    private ArrayList<HashMap<String, Object>> fetchSwingCardDetail(String cardNO, String billUUID) throws Exception {
        String whereSql = "where swingcard.cardno='" + cardNO + "' and billuuid='" + billUUID + "'";
        if (!UserUtils.isAdmin(userID_))
            whereSql += " and (cardtb.salemanuuid in (select a.uid from salemantb a  where a.uid='"+userID_+"' )" +
                    " or cardtb.salemanuuid in(select salemanuuid from tellertb   where uid='"+userID_+"')) ";

        return PosDbManager.executeSql("SELECT swingcard.id,\n" +
                "SUBSTR(billtb.billdate,1,4) billyear, \n" +
                "SUBSTR(billtb.billdate,6,2) billmonth, \n" +
                "swingcard.cardno, \n" +
                "cardtb.cardmaster, \n" +
                "swingcard.amount, \n" +
                "(case when swingcard.amount*ratetb.rate/100>ratetb.MAXFEE and ratetb.maxfee>0 then" +
                " ratetb.maxfee else swingcard.amount*ratetb.rate/100  end)  paycharge, \n" +
                "swingcard.sdatetm, \n" +
                "postb.posname, \n" +
                "userinfo.unick, \n" +
                "swingcard.realsdatetm, \n" +
                "swingcard.swingstatus \n" +
                "FROM \n" +
                "swingcard \n" +
                "INNER JOIN cardtb ON cardtb.cardno = swingcard.cardno \n" +
                "INNER JOIN postb ON postb.uuid = swingcard.posuuid \n" +
                "INNER JOIN ratetb ON postb.rateuuid = ratetb.uuid \n" +
                "INNER JOIN billtb ON swingcard.billuuid = billtb.uuid AND swingcard.cardno = billtb.cardno \n" +
                "left JOIN userinfo ON userinfo.uid = swingcard.userid \n" +
                whereSql +
                "ORDER BY \n" +
                "swingcard.realsdatetm, swingcard.sdatetm");
    }

    private String userID_;
}