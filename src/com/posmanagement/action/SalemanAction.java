package com.posmanagement.action;

import com.posmanagement.webui.SalemanInfo;
import com.posmanagement.webui.SalemanList;

import java.util.HashMap;
import java.util.Map;

public class SalemanAction extends AjaxActionSupport {
    private final static String SALEMANMANAGE = "salemanManager";

    private String salemanList;
    private String salemanID;
    private String salemanName;

    public String getSalemanList() {
        return salemanList;
    }

    public void setSalemanID(String _salemanID) {
        salemanID = _salemanID;
    }

    public void setSaleName(String _saleName) {
        salemanName = _saleName;
    }

    public String Init() throws Exception {
        salemanList = new SalemanList().generateHTMLString();
        return SALEMANMANAGE;
    }

    public String FetchInfo() throws Exception {
        Map map = new HashMap();
        if (salemanID != null && salemanID.length() > 0) {
            map.put("salemanInfo", new SalemanInfo(salemanID).generateHTMLString());
        }

        return AjaxActionComplete(map);
    }

    public String UpdateInfo() throws Exception {
        Map map = new HashMap();
        map.put("ErrorMessage", new String("UnImple"));
        // todo
        return AjaxActionComplete(map);
    }
}
