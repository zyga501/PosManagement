package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.webui.TellerUI;

import java.util.HashMap;
import java.util.Map;

public class TellerAction extends AjaxActionSupport {
    private final static String TELLERMANAGE = "tellerManager";

    public String tellerList;
    public String tellerID;

    public String getTellerList() {
        return tellerList;
    }

    public void setTellerID(String _tellerID) {
        tellerID = _tellerID;
    }

    public String Init() throws Exception {
        if (super.getUserName().equals("admin"))
            tellerList = new TellerUI().generateTable(null, false);
        else
            tellerList = new TellerUI().generateTable(super.getUserID(), false);
        return TELLERMANAGE;
    }

    public String FetchInfo() throws Exception {
        Map map = new HashMap();
        if (tellerID != null && tellerID.length() > 0) {
            map.put("tellerInfo", new TellerUI().generateInfoTable(tellerID));
        }

        return AjaxActionComplete(map);
    }

    public String FetchUnAssignTeller() throws Exception {
        Map map = new HashMap();
        map.put("tellerList", new TellerUI().generateTable(null, true));
        return AjaxActionComplete(map);
    }

    public String UpdateInfo() throws Exception {
        Map map = new HashMap();
        Map parametMap = new HashMap<Integer, Object>();
        try{
            parametMap.put(1, getParameter("cardID").toString().trim());
            parametMap.put(2, getParameter("contact").toString().trim());
            parametMap.put(3, null==getParameter("tellStatus")?"disable":"enable");
            parametMap.put(4, tellerID);
            if (!PosDbManager.executeUpdate("update tellertb set tcardno=?,contact=?,status=? where uid=?",
                    (HashMap<Integer, Object>) parametMap))
                map.put("ErrorMessage", getText("global.dofailed"));
        }
        catch (Exception e) {
            map.put("ErrorMessage", getText("global.dofailed"));
        }
        return AjaxActionComplete(map);
    }
}
