package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class AssetUI {
    public String generateAssetTable() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchAssetList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                            .addAttribute("class", "text-c odd")
                            .addAttribute("role", "row")
                            .addAttribute("value", dbRet.get(index).get("UUID").toString())
                            .addElement("td", dbRet.get(index).get("CARDMASTER").toString())
                            .addElement("td", dbRet.get(index).get("BANKNAME").toString())
                            .addElement("td", dbRet.get(index).get("CARDNO").toString())
                            .addElement("td", dbRet.get(index).get("FIRSTBALANCE").toString())
                            .addElement("td", dbRet.get(index).get("EBANKSIGNPWD").toString())
                            .addElement("td", dbRet.get(index).get("EBANKCASHPWD").toString())
                            .addElement("td", dbRet.get(index).get("EBANKTRANSFERPWD").toString())
                            .addElement("td", dbRet.get(index).get("ATMCASHPWD").toString());
        }
        return htmlString;
    }

    private ArrayList<HashMap<String, Object>> fetchAssetList() throws Exception {
        return PosDbManager.executeSql("SELECT assettb.uuid,\n" +
                "assettb.cardmaster,\n" +
                "banktb.`name` bankname,\n" +
                "assettb.cardno,\n" +
                "assettb.firstbalance,\n" +
                "assettb.ebanksignpwd,\n" +
                "assettb.ebankcashpwd,\n" +
                "assettb.ebanktransferpwd,\n" +
                "assettb.atmcashpwd " +
                "FROM \n" +
                "assettb \n" +
                "INNER JOIN banktb ON banktb.uuid = assettb.bankuuid");
    }
}