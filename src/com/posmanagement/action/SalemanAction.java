package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.webui.SalemanUI;
import com.posmanagement.webui.TellerUI;

import java.util.HashMap;
import java.util.Map;

public class SalemanAction extends AjaxActionSupport {
    private final static String SALEMANMANAGE = "salemanManager";

    private String salemanList;
    private String salemanID;
    private String salemanName;
    private String tellerID;
    private String uiMode;

    private String newid;

    public String getNewid() {
        return newid;
    }

    public void setNewid(String newid) {
        this.newid = newid;
    }

    public String getUiMode() {
        return uiMode;
    }

    public void setUiMode(String uiMode) {
        this.uiMode = uiMode;
    }

    public String getSalemanList() {
        return salemanList;
    }

    public void setSalemanID(String _salemanID) {
        salemanID = _salemanID;
    }

    public void setSalemanName(String _salemanName) {
        salemanName = _salemanName;
    }

    public void setTellerID(String _tellerID) {
        tellerID = _tellerID;
    }

    public String Init() throws Exception {
        salemanList = new SalemanUI().generateTable();
        return SALEMANMANAGE;
    }

    public String FetchInfo() throws Exception {
        Map map = new HashMap();
        if (salemanID != null && salemanID.length() > 0) {
            map.put("salemanInfo", new SalemanUI().generateInfoTable(salemanID));
            map.put("tellerList", new TellerUI().generateTable(salemanID, false));
        }
        return AjaxActionComplete(map);
    }

    public String FetchSalemanList() throws Exception {
        Map map = new HashMap();
        if (uiMode != null && uiMode.compareTo("SELECTLIST") == 0) {
            map.put("salemanList", new SalemanUI().generateSelect());
        }

        return AjaxActionComplete(map);
    }
    public String UpdateInfo() throws Exception {
        Map map = new HashMap();
        map.put("ErrorMessage", new String("UnImple"));
        // todo
        return AjaxActionComplete(map);
    }

    public String AddTeller() throws Exception {
        Map map = new HashMap();
        if (tellerID == null || tellerID.length() <= 0 || salemanID == null || salemanID.length() <= 0) {
            map.put("ErrorMessage", getText("SalemanAction.AddTellerError"));
        }
        else {
            Map parametMap = new HashMap<Integer, Object>();
            parametMap.put(1, salemanID);
            parametMap.put(2, tellerID);
            if (!PosDbManager.executeUpdate("update tellertb set salesman=? where uid=?", (HashMap<Integer, Object>) parametMap)) {
                map.put("ErrorMessage", getText("SalemanAction.AddTellerError"));
            }
            map.put("tellerList", new TellerUI().generateTable(salemanID, false));
        }

        return AjaxActionComplete(map);
    }
}
