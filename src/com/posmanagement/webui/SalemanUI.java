package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SalemanUI {
    public String generateTable() throws Exception {
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
                    .addElement("td", dbRet.get(index).get("UID").toString()));
        }
        return uiTable.generateUI() ;
    }

    public String generateSelect() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchSalemanList();
        if (dbRet.size() <= 0)
            return new String("");

        UIContainer uiContainer = new UIContainer();
        for (int index = 0; index < dbRet.size(); ++index) {
            if (dbRet.get(index).get("STATUS").toString().compareTo("enable") == 0)
            {
                uiContainer.addElement(new UIContainer("option", dbRet.get(index).get("UNICK").toString())
                        .addAttribute("value", dbRet.get(index).get("UID").toString()));
            }
        }
        return uiContainer.generateUI();
    }

    public String generateInfoTable(String salemanID) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchSalemanInfo(salemanID);
        if (dbRet.size() != 1)
            return new String("");

        return new UIContainer("form")
                .addElement(new UIContainer("table")
                        .addAttribute("class", "table table-border table-bordered table-hover")
                        .addElement(new UIContainer("tr")
                                .addElement(new UIContainer("td", "用户名称")
                                        .addElement(new UIContainer("input")
                                                .addAttribute("type", "hidden")
                                                .addAttribute("id", "salemanID")
                                                .addAttribute("name", "salemanID")
                                                .addAttribute("value", StringUtils.convertNullableString(dbRet.get(0).get("UID")))))
                                .addElement(new UIContainer("td")
                                        .addElement(new UIContainer("input")
                                                .addAttribute("type", "text")
                                                .addAttribute("name", "salemanName")
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
                                .addElement("td", "费用情况")
                                .addElement(new UIContainer("td")
                                        .addElement(new UIContainer("input")
                                                .addAttribute("type", "text")
                                                .addAttribute("name", "feeQK")
                                                .addAttribute("class", "input-text radius")
                                                .addAttribute("value", StringUtils.convertNullableString(dbRet.get(0).get("FEEQK"))))))
                        .addElement(new UIContainer("tr")
                                .addElement("td", "支付情况")
                                .addElement(new UIContainer("td")
                                        .addElement(new UIContainer("input")
                                                .addAttribute("type", "text")
                                                .addAttribute("name", "paymentTM")
                                                .addAttribute("class", "input-text radius")
                                                .addAttribute("value", StringUtils.convertNullableString(dbRet.get(0).get("PAYMENTTM"))))))
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

    private ArrayList<HashMap<String, Object>> fetchSalemanInfo(String salemanID) throws Exception {
        Map parametMap = new HashMap<Integer, Object>();
        parametMap.put(1, salemanID);
        return PosDbManager.executeSql("select * from userinfo a,salesmantb b where a.uid=b.uid and b.uid=?",
                (HashMap<Integer, Object>) parametMap);
    }

    private ArrayList<HashMap<String, Object>> fetchSalemanList() throws Exception {
        return PosDbManager.executeSql("select * from userinfo a,salesmantb b where a.uid=b.uid");
    }
}
