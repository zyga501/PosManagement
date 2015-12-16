package com.posmanagement.webui;

import com.posmanagement.utils.DbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class AssetList {
    public String generateHTMLString() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchAssetList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString +="<tr class=\"text-c odd\" role=\"row\">"+
                    "<td>"+ dbRet.get(index).get("CARDMASTER")+"</td>" +
                    "<td>"+ dbRet.get(index).get("BANKNAME")+"</td>" +
                    "<td>"+ dbRet.get(index).get("CARDNO")+"</td>" +
                    "<td>"+ dbRet.get(index).get("FIRSTBALANCE")+"</td>" +
                    "<td>"+ dbRet.get(index).get("EBANKSIGNPWD")+"</td>" +
                    "<td>"+ dbRet.get(index).get("EBANKCASHPWD")+"</td>" +
                    "<td>"+ dbRet.get(index).get("EBANKTRANSFERPWD")+"</td>" +
                    "<td>"+ dbRet.get(index).get("ATMCASHPWD")+"</td>" +
                    "</tr>";
        }
        return htmlString;
    }

    private ArrayList<HashMap<String, Object>> fetchAssetList() throws Exception {
        return DbManager.createPosDbManager().executeSql("select * from assettb");
    }
}