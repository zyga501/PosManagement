package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class SalemanList {

    public String generateHTMLString() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchSalemanList();
        if (dbRet.size() <= 0)
            return new String("");

        UIContainer uiTable = new UIContainer("table")
                .addAttribute("class", "table table-border table-bordered table-hover");
        for (int index = 0; index < dbRet.size(); ++index) {
            uiTable.addElement(new UIContainer("tr")
                    .addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addAttribute("onclick", "clickSaleman('" + dbRet.get(index).get("UID") + "')")
                    .addElement("td", String.valueOf(index+1))
                    .addElement("td", dbRet.get(index).get("UNICK").toString())
                    .addElement("td", dbRet.get(index).get("UNAME").toString()));
        }
        return uiTable.generateUI() ;
    }

    public String generateListHTMLString() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchSalemanList();
        if (dbRet.size() <= 0)
            return new String("");

        UIContainer uiContainer = new UIContainer();
        for (int index = 0; index < dbRet.size(); ++index) {
            if (dbRet.get(index).get("STATUS").toString().compareTo("enable") == 0)
            {
                uiContainer.addElement(new UIContainer("option", dbRet.get(index).get("UNICK").toString())
                        .addAttribute("value", dbRet.get(index).get("UNAME").toString()));
            }
        }
        return uiContainer.generateUI();
    }

    private ArrayList<HashMap<String, Object>> fetchSalemanList() throws Exception {
        return PosDbManager.executeSql("select * from userinfo a,salesmantb b where a.uid=b.uid");
    }
}
