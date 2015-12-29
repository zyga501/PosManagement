package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class SwingCardList {
    public SwingCardList(WebUI.UIMode _uidMode) {
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

    public String generateTableList() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchPosServerList();
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
                                            .addAttribute("name", "scid")
                                            .addAttribute("value" ,StringUtils.convertNullableString(dbRet.get(index).get("ID")))
                                            .addAttribute("checked", "checked",false)
                            ))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("BILLYEAR")) + "/" +
                            StringUtils.convertNullableString(dbRet.get(index).get("BILLMONTH")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("CARDNO")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("CARDMASTER")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("AMOUNT")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("SDATETM")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("MACHINENO")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("MACHINENAME")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("SWINGSTATUS")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("TELLER")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("REALSDATETM")))
                    .addElement("td" ,StringUtils.convertNullableString(dbRet.get(index).get("SALESMAN")))
                    .addElement(new UIContainer("td").addElement(new UIContainer("input")
                            .addAttribute("class" ,StringUtils.convertNullableString(dbRet.get(index).get("STATUS")).equals("enable")?"btn btn-success radius":"btn btn-danger radius")
                            .addAttribute("type","button")
                            .addAttribute("title" ,StringUtils.convertNullableString(dbRet.get(index).get("STATUS")).equals("enable")?"已开启":"未开启")
                            .addAttribute("datav", StringUtils.convertNullableString(dbRet.get(index).get("ID")))
                            .addAttribute("value" ,StringUtils.convertNullableString(dbRet.get(index).get("STATUS")).equals("enable")?"Y":"N")));
        }
        return htmlString;
    }

    public String generateSelectList() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchPosServerList();
        if (dbRet.size() <= 0)
            return new String("");

        UIContainer uiContainer = new UIContainer();
        uiContainer.addElement("option","");
        for (int index = 0; index < dbRet.size(); ++index) {
            if (StringUtils.convertNullableString(dbRet.get(index).get("STATUS")).compareTo("enable") == 0) {
                uiContainer.addElement(new UIContainer("option" ,StringUtils.convertNullableString(dbRet.get(index).get("SERVERNAME")))
                                        .addAttribute("value" ,StringUtils.convertNullableString(dbRet.get(index).get("SERVERCODE"))));
            }
        }

        return uiContainer.generateUI();
    }

    private ArrayList<HashMap<String, Object>> fetchPosServerList() throws Exception {
        return PosDbManager.executeSql("select * from swingcard ");
    }

    private WebUI.UIMode uiMode;
}
