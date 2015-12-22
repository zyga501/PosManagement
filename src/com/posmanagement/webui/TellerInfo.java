package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TellerInfo {
    public TellerInfo(String _tellerID) {
        tellerID = _tellerID;
    }

    public String generateHTMLString() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchTellerInfo();
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

    private ArrayList<HashMap<String, Object>> fetchTellerInfo() throws Exception {
        Map parametMap = new HashMap<Integer, Object>();
        parametMap.put(1, tellerID);
        return PosDbManager.executeSql("select * from userinfo a,tellertb b where a.uid=b.uid and b.uid=?",
                (HashMap<Integer, Object>) parametMap);
    }

    private String tellerID;
}
