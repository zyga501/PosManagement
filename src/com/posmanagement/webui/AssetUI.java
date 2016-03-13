package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.UserUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class AssetUI {
    public AssetUI(String userID) {
        userID_ = userID;
    }

    public String generateAssetTable() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchAssetList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                            .addAttribute("class", "text-c odd")
                            .addAttribute("role", "row")
                            .addElement("td", dbRet.get(index).get("UNICK").toString())
                            .addElement("td", dbRet.get(index).get("BANKNAME").toString())
                            .addElement("td", dbRet.get(index).get("CARDNO").toString())
                            .addElement("td", dbRet.get(index).get("BALANCE").toString())
                            .addElement("td", dbRet.get(index).get("EBANKSIGNPWD").toString())
                            .addElement("td", dbRet.get(index).get("EBANKCASHPWD").toString())
                            .addElement("td", dbRet.get(index).get("EBANKTRANSFERPWD").toString())
                            .addElement("td", dbRet.get(index).get("ATMCASHPWD").toString())
                            .addElement(new UIContainer("td").addElement(
                                            new UIContainer("input")
                                                    .addAttribute("type", "button")
                                                    .addAttribute("value", "对冲")
                                                    .addAttribute("class", "btn radius")
                                                    .addAttribute("onclick", "hedgeAsset('" + dbRet.get(index).get("UUID") + "')")));
        }
        return htmlString;
    }

    public String generateAssetSelectList() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchAssetList();
        if (dbRet.size() <= 0)
            return new String("");

        UIContainer uiContainer = new UIContainer();
        uiContainer.addElement(new UIContainer("option", "-").addAttribute("value", ""));
        for (int index = 0; index < dbRet.size(); ++index) {
            uiContainer.addElement(new UIContainer("option", dbRet.get(index).get("BANKNAME").toString()+dbRet.get(index).get("CARDNO").toString())
                    .addAttribute("value", dbRet.get(index).get("UUID").toString()));
        }

        return uiContainer.generateUI();
    }
    private ArrayList<HashMap<String, Object>> fetchAssetList() throws Exception {
        String whereSql = new String();
        if (!UserUtils.isAdmin(userID_)) {
            whereSql += " where assettb.salemanuuid='"+userID_+"'";
        }

        return PosDbManager.executeSql("SELECT\n" +
                "assettb.uuid,\n" +
                "assettb.cardno,\n" +
                "assettb.balance,\n" +
                "assettb.ebanksignpwd,\n" +
                "assettb.ebankcashpwd,\n" +
                "assettb.ebanktransferpwd,\n" +
                "assettb.atmcashpwd,\n" +
                "banktb.`name` bankname,\n" +
                "userinfo.unick\n" +
                "FROM\n" +
                "assettb\n" +
                "INNER JOIN userinfo ON userinfo.uid = assettb.salemanuuid\n" +
                "INNER JOIN banktb ON banktb.uuid = assettb.bankuuid" +
                whereSql);
    }

    private String userID_;
}