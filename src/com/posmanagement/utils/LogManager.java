package com.posmanagement.utils;

import java.sql.SQLException;
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
        Map parametsMap = new HashMap();
        parametsMap.put(1, userID);
        parametsMap.put(2, LogType.LOGONIN.ordinal());
        parametsMap.put(3, String.format("User:%d Login at:%s DateTime:%s", userID, location, datatime));
        writeToDB(parametsMap);
    }

    private void writeToDB(Map parametsMap) throws Exception {
        if (!DbManager.getDbManager("").executeUpdate("insert into usertrack(uid,ttype,tinfo) values(?,?,?)",
                (HashMap<Integer, Object>) parametsMap))
            throw new SQLException();
    }

    private static LogManager instance = null;
}
