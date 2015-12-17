package com.posmanagement.action;

import com.posmanagement.utils.DbManager;
import com.posmanagement.webui.PosServerList;

import java.util.HashMap;
import java.util.Map;

public class PosServerAction extends AjaxActionSupport {
    private final static String POSSERVERMANAGER = "posServerManager";

    private String posServerList;
    private String posServer;
    private String posServerEnabled;

    public String getPosServerList() {
        return posServerList;
    }

    public void setPosServer(String _posServer) {
        posServer = _posServer;
    }

    public void setPosServerEnabled(String _posServerEnabled) {
        posServerEnabled = _posServerEnabled;
    }

    public String Init() throws Exception {
        posServerList = new PosServerList().generateHTMLString();
        return POSSERVERMANAGER;
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
            DbManager.createPosDbManager().executeUpdate("insert into posservertb(servername,enabled) values(?,?)", (HashMap<Integer, Object>) parametMap);
            map.put("posServerList", new PosServerList().generateHTMLString());
        }

        return AjaxActionComplete(map);
    }
}
