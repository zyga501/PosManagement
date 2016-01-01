package com.posmanagement.action;

import com.opensymphony.xwork2.ActionContext;
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
        ActionContext ctx = ActionContext.getContext();
        if (ctx.getSession().get("userName").toString().equals("admin"))
            tellerList = new TellerUI().generateTable(null, false);
        else
            tellerList = new TellerUI().generateTable(ctx.getSession().get("userID").toString(), false);
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
        map.put("ErrorMessage", new String("UnImple"));
        // todo
        return AjaxActionComplete(map);
    }
}
