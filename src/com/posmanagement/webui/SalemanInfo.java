package com.posmanagement.webui;

import com.posmanagement.utils.DbManager;
import com.posmanagement.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SalemanInfo {
    public SalemanInfo(String _salemaneID) {
        salemanID = _salemaneID;
    }

    public String generateHTMLString() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchSalemanInfo();
        if (dbRet.size() != 1)
            return new String("");

        String htmlString ="<form><table class=\"table table-border table-bordered table-hover\">";
        htmlString +="<tr>"+
                "<td><input type=\"hidden\" id=\"salemanID\" name=\"salemanID\" value="+ StringUtils.convertNullableString(dbRet.get(0).get("UID"))+
                ">用户名称</td><td><input type=\"text\" name=\"salemanName\" class=\"input-text radius\" value=" +
                StringUtils.convertNullableString(dbRet.get(0).get("UNICK"))+" </td></tr>"+
                "<tr><td>卡号</td><td><input type=\"text\" name=\"cardID\" class=\"input-text radius\" VALUE=" +
                StringUtils.convertNullableString(dbRet.get(0).get("SCARDNO"))+"></td></tr>"+
                "<tr><td>状态</td><td><input type=\"text\" name=\"saleStatus\" class=\"input-text radius\" VALUE=" +
                StringUtils.convertNullableString(dbRet.get(0).get("STATUS"))+"></td></tr>"+
                "<tr><td>费用情况</td><td><input type=\"text\" name=\"feeQK\" class=\"input-text radius\" VALUE=" +
                StringUtils.convertNullableString(dbRet.get(0).get("FEEQK"))+"></td></tr>"+
                "<tr><td>支付情况</td><td><input type=\"text\" name=\"paymentTM\" class=\"input-text radius\" VALUE=" +
                StringUtils.convertNullableString(dbRet.get(0).get("PAYMENTTM"))+"></td></tr>"+
                "<tr><td>联系情况</td><td><input type=\"text\" name=\"contract\" class=\"input-text radius\" VALUE=" +
                StringUtils.convertNullableString(dbRet.get(0).get("CONTRACT"))+"></td></tr>";

        return htmlString+"</table></form>";
    }

    private ArrayList<HashMap<String, Object>> fetchSalemanInfo() throws Exception {
        Map parametMap = new HashMap<Integer, Object>();
        parametMap.put(1, salemanID);
        return DbManager.createPosDbManager().executeSql("select * from userinfo a,salesmantb b where a.uid=b.uid and b.uid=?",
                (HashMap<Integer, Object>) parametMap);
    }

    private String salemanID;
}
