package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.webui.PosServerList;
import com.posmanagement.webui.WebUI;

import java.util.HashMap;
import java.util.Map;

public class PosServerAction extends AjaxActionSupport {
    private final static String POSSERVERMANAGER = "posServerManager";

    private String posServerList;
    private String posServer;
    private String posServerEnabled;
    private String uiMode;

    public String getPosServerList() {
        return posServerList;
    }

    public void setPosServer(String _posServer) {
        posServer = _posServer;
    }

    public void setPosServerEnabled(String _posServerEnabled) {
        posServerEnabled = _posServerEnabled;
    }

    public void setUiMode(String _uiMode) {
        uiMode = _uiMode;
    }

    public String Init() throws Exception {
        posServerList = new PosServerList(WebUI.UIMode.TABLELIST).generateHTMLString();
        return POSSERVERMANAGER;
    }

    public String FetchPosServerList() throws Exception {
        Map map = new HashMap();
        if (uiMode != null && uiMode.compareTo("SELECTLIST") == 0) {
            map.put("posServerList", new PosServerList(WebUI.UIMode.SELECTLIST).generateHTMLString());
        }
        else {
            map.put("posServerList", new PosServerList(WebUI.UIMode.TABLELIST).generateHTMLString());
        }

        return AjaxActionComplete(map);
    }

    public String AddPosServer() throws Exception {
        Map map = new HashMap();
        if (posServer.length() == 0) {
            map.put("errorMessage", getText("addposserver.posserverError"));
        }
        else {
            Map parametMap = new HashMap();
            parametMap.put(1, posServer);
            if (posServerEnabled != null)
                parametMap.put(2, new String("on"));
            else
                parametMap.put(2, new String("off"));
            PosDbManager.executeUpdate("insert into posservertb(servername,enabled) values(?,?)", (HashMap<Integer, Object>) parametMap);
            map.put("posServerList", new PosServerList(WebUI.UIMode.TABLELIST).generateHTMLString());
        }

        return AjaxActionComplete(map);
    }
}
