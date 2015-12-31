package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.UUIDUtils;
import com.posmanagement.webui.PosServerUI;

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
        posServerList = new PosServerUI().generateTable();
        return POSSERVERMANAGER;
    }

    public String FetchPosServerList() throws Exception {
        Map map = new HashMap();
        if (uiMode != null && uiMode.compareTo("SELECTLIST") == 0) {
            map.put("posServerList", new PosServerUI().generateSelect());
        }
        else {
            map.put("posServerList", new PosServerUI().generateTable());
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
            parametMap.put(1, UUIDUtils.generaterUUID());
            parametMap.put(2, posServer);
            if (posServerEnabled != null)
                parametMap.put(3, new String("enable"));
            else
                parametMap.put(3, new String("disable"));
            PosDbManager.executeUpdate("insert into posservertb(uuid,name,status) values(?,?,?)", (HashMap<Integer, Object>) parametMap);
            map.put("posServerList", new PosServerUI().generateTable());
        }

        return AjaxActionComplete(map);
    }
}
