package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.SQLUtils;
import com.posmanagement.utils.StringUtils;
import com.posmanagement.utils.UserUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class BillUI extends WebUI {
    public BillUI(String userID){
        userID_=userID;
    }

    public String generateBillTable(int pageIndex) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchBillList(pageIndex);
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                    .addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("BANKNAME")))
                    .addElement("td", StringUtils.formatCardNO(StringUtils.convertNullableString(dbRet.get(index).get("CARDNO"))))
                    .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("BILLDATE")).substring(0, 7))
                    .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("LASTREPAYMENTDATE")))
                    .addElement(new UIContainer("td")
                            .addElement(new UIContainer("label",StringUtils.convertNullableString(dbRet.get(index).get("BILLAMOUNT")).equals("")?"0":dbRet.get(index).get("BILLAMOUNT").toString())
                            .addAttribute("name","billamount")
                            .addAttribute("datav",StringUtils.convertNullableString(dbRet.get(index).get("UUID")))
                            .addAttribute("style","display:inline-block;")))
                    .addElement("td", String.valueOf(((long)(10 * (Double.valueOf(dbRet.get(index).get("CANUSEAMOUNT").toString()) +
                            Double.valueOf(dbRet.get(index).get("REPAYAMOUNT").toString()) - Double.valueOf(dbRet.get(index).get("SWINGAMOUNT").toString())))) / 10.0))
                    .addElement("td", String.valueOf(Double.valueOf(dbRet.get(index).get("REPAYAMOUNT").toString())))
                    .addElement("td", String.valueOf(Double.valueOf(dbRet.get(index).get("BILLAMOUNT").toString()) - Double.valueOf(dbRet.get(index).get("REPAYAMOUNT").toString())))
                    .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("SALEMAN")))
                    .addElement("td", dbRet.get(index).get("SWINGCOUNT").toString().compareTo(dbRet.get(index).get("SWUNGCOUNT").toString()) == 0 &&
                                        dbRet.get(index).get("REPAYCOUNT").toString().compareTo(dbRet.get(index).get("REPAYEDCOUNT").toString()) == 0 ?
                            getText("bill.billfinished") : getText("bill.billunfinished"))
                    .addElement(new UIContainer("td").addElement(new UIContainer("input")
                        .addAttribute("class", dbRet.get(index).get("STATUS").equals("enable")?"btn btn-success radius":"btn btn-danger radius")
                        .addAttribute("type","button")
                            .addAttribute("title", dbRet.get(index).get("STATUS").equals("enable")?"已开启":"未开启")
                            .addAttribute("datav", StringUtils.convertNullableString(dbRet.get(index).get("UUID")))
                                    .addAttribute("value", dbRet.get(index).get("STATUS").equals("enable")?"Y":"N")
                            .addAttribute("onclick", "clickBill(this,'" + StringUtils.convertNullableString(dbRet.get(index).get("UUID")) + "')")));
        }
        return htmlString;
    }

    private ArrayList<HashMap<String, Object>> fetchBillList(int pageIndex) throws Exception {
        String whereSql = SQLUtils.BuildWhereCondition(uiConditions_);
        if (!whereSql.isEmpty()) {
            whereSql = " where " + whereSql;
        }
        if (!UserUtils.isAdmin(userID_)) {
            whereSql += " and billtb.salesmanuuid='"+userID_+"'";
        }

        String limitSql = "limit " + (pageIndex - 1) * DEFAULTITEMPERPAGE + "," + DEFAULTITEMPERPAGE;

        return PosDbManager.executeSql("select billtb.*, \n" +
                "count(1) swingcount, \n" +
                "sum(CASE WHEN swingcard.swingstatus='enable' then 1 else 0 END) swungcount, \n" +
                "sum(CASE WHEN swingcard.swingstatus='enable' then swingcard.amount else 0 END) swingamount \n" +
                "from (\n" +
                "select billtb.*, \n" +
                "count(1) repaycount,\n" +
                "sum(CASE WHEN repaytb.tradestatus='enable' then 1 else 0 END) repayedcount,\n" +
                "sum(CASE WHEN repaytb.tradestatus='enable' then repaytb.trademoney else 0 END) repayamount \n" +
                "from \n" +
                "(\n" +
                "SELECT \n" +
                "billtb.uuid,\n" +
                "billtb.cardno,\n" +
                "billtb.billamount,\n" +
                "billtb.canuseamount,\n" +
                "billtb.`status`,\n" +
                "billtb.billdate,\n" +
                "banktb.`name` AS bankname,\n" +
                "billtb.lastrepaymentdate,\n" +
                "userinfo.unick saleman\n" +
                "FROM\n" +
                "billtb\n" +
                "INNER JOIN banktb ON banktb.uuid = billtb.bankuuid\n" +
                "LEFT JOIN userinfo ON userinfo.uid = billtb.salesmanuuid\n" +
                whereSql +
                " GROUP BY\n" +
                "billtb.cardno,\n" +
                "billtb.billdate\n" +
                "ORDER BY\n" +
                "billtb.billdate DESC) as billtb\n" +
                "LEFT JOIN\n" +
                "repaytb ON repaytb.billuuid = billtb.uuid\n" +
                "GROUP BY\n" +
                "billtb.cardno,\n" +
                "billtb.billdate\n" +
                ") billtb\n" +
                "LEFT JOIN swingcard ON swingcard.billuuid = billtb.uuid\n" +
                "GROUP BY\n" +
                "billtb.cardno,\n" +
                "billtb.billdate " +
                "ORDER BY " +
                "billtb.billdate DESC " + limitSql);
    }

    public int fetchBillPageCount() throws Exception {
        String whereSql = SQLUtils.BuildWhereCondition(uiConditions_);
        if (!whereSql.isEmpty()) {
            whereSql = " where " + whereSql;
        }
        if (!UserUtils.isAdmin(userID_)) {
            whereSql += "and billtb.salesmanuuid='"+userID_+"'";
        }

        ArrayList<HashMap<String, Object>> resultMap =  PosDbManager.executeSql("SELECT count(*) CNT\n" +
                "FROM\n" +
                "billtb\n" +
                "INNER JOIN banktb ON banktb.uuid = billtb.bankuuid\n" +
                "LEFT JOIN userinfo ON userinfo.uid = billtb.salesmanuuid\n" +
                whereSql +
                "ORDER BY\n" +
                "billtb.billdate DESC");
        if (resultMap.size()<=0) {
            return 0;
        }
        return Integer.parseInt(resultMap.get(0).get("CNT").toString())/ WebUI.DEFAULTITEMPERPAGE + 1;
    }

    private String userID_; // TODO for role
}