package com.posmanagement.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LogManager {
    public static synchronized LogManager getInstance() throws  Exception {
        if (instance == null) {
            instance = new LogManager();
        }
        return instance;
    }

    private enum LogType
    {
        LOGONIN,
    }

    public void writeLoginTrack(int userID, String location, String datatime) throws Exception {
        Map para = new HashMap();
        para.put(1, userID);
        para.put(2, LogType.LOGONIN.ordinal());
        para.put(3, String.format("User:%d Login at:%s DateTime:%s", userID, location, datatime));
        ArrayList<HashMap<String, Object>> dbRet  = null;
        if (!dbManager.executeUpdate("insert into usertrack(uid,ttype,tinfo) values(?,?,?)",
                (HashMap<Integer, Object>) para))
            throw new RuntimeException();
    }

    private LogManager() throws Exception {
        dbManager = new DbManager();
    }

    private DbManager dbManager;
    private static LogManager instance = null;
}
