package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class BillUI extends WebUI {
    public BillUI(String userID){userID_=userID;}
    public String generateBillTable() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchBillList();
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
                    .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("CANUSEAMOUNT")))
                    .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("REPAYAMOUNT")))
                    .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("REMAINAMOUNT")))
                    .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("SALEMAN")))
                    .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("REMAINAMOUNT")).compareTo("0") == 0 ?
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

    private ArrayList<HashMap<String, Object>> fetchBillList() throws Exception {
        String whereSql = "";
        if (userID_ != null && !userID_.isEmpty()) {
            whereSql = "where billtb.salesmanuuid='\"+userID_+\"'";
        }

        return PosDbManager.executeSql("SELECT\n" +
                "billtb.uuid,\n" +
                "billtb.cardno,\n" +
                "billtb.billamount,\n" +
                "billtb.`status`,\n" +
                "billtb.billdate,\n" +
                "Sum(CASE WHEN repaytb.tradestatus='enable' then repaytb.trademoney else 0 END) AS repayamount,\n" +
                "SUM(CASE WHEN repaytb.tradestatus='enable' then repaytb.trademoney else 0 END) + billtb.canuseamount AS canuseamount,\n" +
                "billamount - SUM(CASE WHEN repaytb.tradestatus='enable' then repaytb.trademoney else 0 END) AS remainamount,\n" +
                "banktb.`name` AS bankname,\n" +
                "billtb.lastrepaymentdate,\n" +
                "userinfo.unick AS saleman\n" +
                "FROM\n" +
                "billtb\n" +
                "INNER JOIN repaytb ON CONVERT(repaytb.repayyear, SIGNED)= CONVERT(SUBSTR(billtb.lastrepaymentdate,1,4), SIGNED) AND convert(repaytb.repaymonth, SIGNED)= convert(SUBSTR(billtb.lastrepaymentdate,6,2), SIGNED)\n" +
                "INNER JOIN banktb ON banktb.uuid = billtb.bankuuid\n" +
                "LEFT JOIN userinfo ON userinfo.uid = billtb.salesmanuuid\n" +
                whereSql +
                "GROUP BY\n" +
                "billtb.cardno,\n" +
                "SUBSTR(billtb.lastrepaymentdate,1,4),\n" +
                "SUBSTR(billtb.lastrepaymentdate,6,2)\n" +
                "ORDER BY\n" +
                "billtb.billdate DESC");
    }

    private String userID_; // TODO for role
}