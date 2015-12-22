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
            htmlString +="<tr class=\"text-c odd\" role=\"row\">"+ 
                    "<td>"+ StringUtils.convertNullableString(dbRet.get(index).get("BANKNAME"))+"</td>" +
                    "<td>"+ StringUtils.convertNullableString(dbRet.get(index).get("CARDNO"))+"</td>" +
                    "<td>"+ StringUtils.convertNullableString(dbRet.get(index).get("BILLDATE"))+"</td>" +
                    "<td>"+ StringUtils.convertNullableString(dbRet.get(index).get("LASTREPAYMENTDATE"))+"</td>" +
                    "<td>"+ StringUtils.convertNullableString(dbRet.get(index).get("BILLAMOUNT"))+"</td>" +
                    "<td>"+ StringUtils.convertNullableString(dbRet.get(index).get("CANUSEAMOUNT"))+"</td>" +
                    "<td>"+ StringUtils.convertNullableString(dbRet.get(index).get("BILLHADPAY"))+"</td>" +
                    "<td>"+ StringUtils.convertNullableString(dbRet.get(index).get("BILLNOPAY"))+"</td>" +
                    "<td>"+ StringUtils.convertNullableString(dbRet.get(index).get("LASTPAY"))+"</td>" +
                    "<td>"+ StringUtils.convertNullableString(dbRet.get(index).get("LASTPAYDATETM"))+"</td>" +
                    "<td>"+ StringUtils.convertNullableString(dbRet.get(index).get("LASTTELLER"))+"</td>" +
                    "<td>"+ StringUtils.convertNullableString(dbRet.get(index).get("SALESMAN"))+"</td>" +
                    "<td>"+ StringUtils.convertNullableString(dbRet.get(index).get("EXPIRED"))+"</td>" +
                    "<td>"+ StringUtils.convertNullableString(dbRet.get(index).get("STATUS"))+"</td>" +
                    "</tr>";
        }
        return htmlString;
    }

    private ArrayList<HashMap<String, Object>> fetchBillList() throws Exception {
        return PosDbManager.executeSql("select * from billtb");
    }
}