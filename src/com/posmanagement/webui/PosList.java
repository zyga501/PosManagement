package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class PosList {
    private WebUI.UIMode uiMode;
    public PosList(WebUI.UIMode _uidMode) {
        uiMode = _uidMode;
    }

    public String generateHTMLString() throws Exception {
        switch (uiMode) {
            case TABLELIST:
                return generateTableList();
            case SELECTLIST:
                return generateSelectList();
        }

        return "";
    }
    public String generateSelectList() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchPosList();
        if (dbRet.size() <= 0)
            return new String("");

        UIContainer uiContainer = new UIContainer();
        ;
        for (int index = 0; index < dbRet.size(); ++index) {
            if (dbRet.get(index).get("STATUS").toString().compareTo("enable") == 0) {
                uiContainer.addElement(new UIContainer("option", dbRet.get(index).get("POSNAME").toString())
                        .addAttribute("value", dbRet.get(index).get("UUID").toString()));
            }
        }

        return uiContainer.generateUI();
    }


    public String generateTableList() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchPosList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                    .addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addElement(new UIContainer("td")
                            .addElement(
                                    new UIContainer("input")
                                            .addAttribute("type", "radio")
                                            .addAttribute("name", "newid")
                                            .addAttribute("value", dbRet.get(index).get("UUID").toString())
                                            .addAttribute("checked", "checked",false)
                            ))
                    .addElement("td", dbRet.get(index).get("POSNAME").toString())
                    .addElement("td", dbRet.get(index).get("INDUSTRYNAME").toString())
                    .addElement("td", dbRet.get(index).get("RATE").toString())
                    .addElement("td", dbRet.get(index).get("POSSERVERNAME").toString())
                    .addElement("td", dbRet.get(index).get("MCC").toString())
                    .addElement(new UIContainer("td")
                            .addElement(
                                    new UIContainer("input")
                                            .addAttribute("type", "checkbox")
                                            .addAttribute("checked", "checked", dbRet.get(index).get("STATUS").toString().compareTo("enable") == 0)
                            )
                    );
        }
        return htmlString;
    }

    public String generateMasterString(String posuuid) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet =  PosDbManager.executeSql("select posname from postb where uuid='"+posuuid+"'");
        if (dbRet.size() > 0)
            return dbRet.get(0).get("POSNAME").toString();
        else
            return "Not exists this POS!";
    }

    private ArrayList<HashMap<String, Object>> fetchPosList() throws Exception {
        return PosDbManager.executeSql("SELECT " +
                "POSTB.uuid, " +
                "POSTB.posname, " +
              //  "banktb.name bankname, " +
                "posservertb.name posservername, " +
                "mcctb.mcc mcc, " +
                "industrytb.name industryname, " +
                "POSTB.`status`," +
                "ratetb.rate " +
                "FROM  " +
                "POSTB  " +
               // "INNER JOIN banktb ON banktb.uuid = POSTB.recipientbankuuid  " +
                "INNER JOIN posservertb ON posservertb.uuid = POSTB.posserveruuid  " +
                "INNER JOIN industrytb ON POSTB.industryuuid = industrytb.uuid  " +
                "INNER JOIN ratetb ON POSTB.rateuuid = ratetb.uuid " +
                "INNER JOIN userinfo ON POSTB.salesmanuname = userinfo.uname  " +
                "INNER JOIN mcctb ON mcctb.uuid = POSTB.mccuuid  ");
    }
}