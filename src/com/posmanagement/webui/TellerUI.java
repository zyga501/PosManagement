package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TellerUI {
    public String generateTable(String salemanID, boolean unassigned) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchTellerList(salemanID, unassigned);
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

    public String generateInfoTable(String tellerID) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchTellerInfo(tellerID);
        if (dbRet.size() != 1)
            return new String("");

        return new UIContainer("form")
                .addElement(new UIContainer("table")
                        .addAttribute("class", "table table-border table-bordered table-hover")
                        .addElement(new UIContainer("tr")
                                .addElement(new UIContainer("td", "用户名称")
                                        .addElement(new UIContainer("input")
                                                .addAttribute("type", "hidden")
                                                .addAttribute("id", "tellerID")
                                                .addAttribute("name", "tellerID")
                                                .addAttribute("value", StringUtils.convertNullableString(dbRet.get(0).get("UID")))))
                                .addElement(new UIContainer("td")
                                        .addElement(new UIContainer("input")
                                                .addAttribute("type", "text")
                                                .addAttribute("name", "tellerNickName")
                                                .addAttribute("class", "input-text radius")
                                                .addAttribute("value", StringUtils.convertNullableString(dbRet.get(0).get("UNICK"))))))
                        .addElement(new UIContainer("tr")
                                .addElement("td", "卡号")
                                .addElement(new UIContainer("td")
                                        .addElement(new UIContainer("input")
                                                .addAttribute("type", "text")
                                                .addAttribute("name", "cardID")
                                                .addAttribute("class", "input-text radius")
                                                .addAttribute("value", StringUtils.convertNullableString(dbRet.get(0).get("SCARDNO"))))))
                        .addElement(new UIContainer("tr")
                                .addElement("td", "状态")
                                .addElement(new UIContainer("td")
                                        .addElement(new UIContainer("input")
                                                .addAttribute("type", "text")
                                                .addAttribute("name", "saleStatus")
                                                .addAttribute("class", "input-text radius")
                                                .addAttribute("value", StringUtils.convertNullableString(dbRet.get(0).get("STATUS"))))))
                        .addElement(new UIContainer("tr")
                                .addElement("td", "所属业务员")
                                .addElement(new UIContainer("td")
                                        .addElement(new UIContainer("input")
                                                .addAttribute("type", "text")
                                                .addAttribute("name", "saleManID")
                                                .addAttribute("class", "input-text radius")
                                                .addAttribute("value", StringUtils.convertNullableString(dbRet.get(0).get("SALESMAN"))))))
                        .addElement(new UIContainer("tr")
                                .addElement("td", "联系情况")
                                .addElement(new UIContainer("td")
                                        .addElement(new UIContainer("input")
                                                .addAttribute("type", "text")
                                                .addAttribute("name", "contract")
                                                .addAttribute("class", "input-text radius")
                                                .addAttribute("value", StringUtils.convertNullableString(dbRet.get(0).get("CONTRACT"))))))
                ).generateUI();
    }

    private ArrayList<HashMap<String, Object>> fetchTellerList(String salemanID, boolean unassigned) throws Exception {
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

    private ArrayList<HashMap<String, Object>> fetchTellerInfo(String tellerID) throws Exception {
        Map parametMap = new HashMap<Integer, Object>();
        parametMap.put(1, tellerID);
        return PosDbManager.executeSql("select * from userinfo a,tellertb b where a.uid=b.uid and b.uid=?",
                (HashMap<Integer, Object>) parametMap);
    }
}
