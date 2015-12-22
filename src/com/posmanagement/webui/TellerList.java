package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TellerList {
    public TellerList() { }

    public TellerList(String _salemanID) {
        salemanID = _salemanID;
        unassigned = false;
    }

    public TellerList(boolean _unassigned) {
        unassigned = _unassigned;
    }

    public String generateHTMLString() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchTellerList();
        if (dbRet.size() <= 0)
            return new String("");

        UIContainer uiTable = new UIContainer("table")
                .addAttribute("class", "table table-border table-bordered table-hover");
        for (int index = 0; index < dbRet.size(); ++index) {
            uiTable.addElement(new UIContainer("tr")
                                .addAttribute("class", "text-c odd")
                                .addAttribute("role", "row")
                                .addAttribute("onclick", "clickTeller('" + dbRet.get(index).get("UID") + "')")
                                .addElement("td", String.valueOf(index+1).toString())
                                .addElement("td", dbRet.get(index).get("UNICK").toString())
                                .addElement("td", dbRet.get(index).get("UNAME").toString()));
        }

        return uiTable.generateUI();
    }

    private ArrayList<HashMap<String, Object>> fetchTellerList() throws Exception {
        String sql = "select * from userinfo a,tellertb b where a.uid=b.uid";
        Map parametMap = new HashMap<Integer, Object>();
        if (unassigned) {
            sql += " and (salesman is null or salesman=\"\" )";
        }
        else {
            if (salemanID != null) {
                sql += " and salesman=?";
                parametMap.put(1, salemanID);
            }
        }
        return PosDbManager.executeSql(sql, (HashMap<Integer, Object>)parametMap);
    }

    private String salemanID;
    private boolean unassigned;
}
