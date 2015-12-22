package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class AssetList {
    public String generateHTMLString() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchAssetList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                            .addAttribute("class", "text-c odd")
                            .addAttribute("role", "row")
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
        return PosDbManager.executeSql("select * from assettb");
    }
}