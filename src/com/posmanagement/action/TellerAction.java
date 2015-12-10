package com.posmanagement.action;

import com.posmanagement.webui.TellerInfo;
import com.posmanagement.webui.TellerList;

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
        tellerList = new TellerList().generateHTMLString();
        return TELLERMANAGE;
    }

    public String FetchInfo() throws Exception {
        Map map = new HashMap();
        if (tellerID != null && tellerID.length() > 0) {
            map.put("tellerInfo", new TellerInfo(tellerID).generateHTMLString());
        }

        return AjaxActionComplete(map);
    }

    public String FetchTellerList() throws Exception {
        Map map = new HashMap();
        map.put("tellerList", new TellerList().generateHTMLString());
        return AjaxActionComplete(map);
    }

    public String UpdateInfo() throws Exception {
        Map map = new HashMap();
        map.put("ErrorMessage", new String("UnImple"));
        // todo
        return AjaxActionComplete(map);
    }
}
