package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class BillList {
    public String generateHTMLString() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchBillList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                                .addAttribute("class", "text-c odd")
                                .addAttribute("role", "row")
                                .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("BANKNAME")).toString())
                                .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("CARDNO")).toString())
                                .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("BILLDATE")).toString())
                                .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("LASTREPAYMENTDATE")).toString())
                                .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("BILLAMOUNT")).toString())
                                .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("CANUSEAMOUNT")).toString())
                                .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("BILLHADPAY")).toString())
                                .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("BILLNOPAY")).toString())
                                .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("LASTPAY")).toString())
                                .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("LASTPAYDATETM")).toString())
                                .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("LASTTELLER")).toString())
                                .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("SALESMAN")).toString())
                                .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("EXPIRED")).toString())
                                .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("STATUS")).toString());
        }
        return htmlString;
    }

    private ArrayList<HashMap<String, Object>> fetchBillList() throws Exception {
        return PosDbManager.executeSql("select * from billtb");
    }
}