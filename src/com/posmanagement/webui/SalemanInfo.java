package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SalemanInfo {
    public SalemanInfo(String _salemaneID) {
        salemanID = _salemaneID;
    }

    public String generateHTMLString() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchSalemanInfo();
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

    private ArrayList<HashMap<String, Object>> fetchSalemanInfo() throws Exception {
        Map parametMap = new HashMap<Integer, Object>();
        parametMap.put(1, salemanID);
        return PosDbManager.executeSql("select * from userinfo a,salesmantb b where a.uid=b.uid and b.uid=?",
                (HashMap<Integer, Object>) parametMap);
    }

    private String salemanID;
}
