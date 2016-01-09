package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.UUIDUtils;
import com.posmanagement.webui.SwingTimeUI;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SwingTimeAction extends AjaxActionSupport {
    private final static String SWINGTIMEMANAGER = "swingTimeManager";
    private final static String ADDMANAGER = "addswingTime";

    private String swingTimeList;
    private String swingTime;
    private String startTime;
    private String endTime;
    private String timeEnabled;
    private String uiMode;

    public String getSwingTimeList() {
        return swingTimeList;
    }

    public void setSwingTime(String _swingTime) {
        swingTime = _swingTime;
    }

    public void setStartTime(String _startTime) {
        startTime = _startTime;
    }

    public void setEndTime(String _endTime) {
        endTime = _endTime;
    }

    public void setTimeEnabled(String enabled) {
        timeEnabled = enabled;
    }

    public void setUiMode(String _uiMode) {
        uiMode = _uiMode;
    }

    public String Init() throws Exception {
        swingTimeList = new SwingTimeUI().generateTable();
        return SWINGTIMEMANAGER;
    }

    public String FetchSwingTimeList() throws Exception {
        Map map = new HashMap();
        if (uiMode != null && uiMode.compareTo("SELECTLIST") == 0) {
            map.put("swingTimeList", new SwingTimeUI().generateSelect());
        }
        else {
            map.put("swingTimeList", new SwingTimeUI().generateTable());
        }

        return AjaxActionComplete(map);
    }

    public String FetchSwingTime() throws Exception {
        Map map = new HashMap();
        if (null!=getParameter("UUID")&&(!getParameter("UUID").equals(""))) {
            Map parametMap = new HashMap();
            parametMap.put(1, getParameter("UUID"));
            ArrayList<HashMap<String, Object>> dbRet = PosDbManager.executeSql("select  * from swingtimetb where uuid=?",(HashMap<Integer, Object>) parametMap);
            if (dbRet.size()>0){
                map.put("uuid",getParameter("UUID"));
                map.put("swingtime",dbRet.get(0).get("NAME"));
                map.put("starttime",dbRet.get(0).get("STARTTIME"));
                map.put("endtime",dbRet.get(0).get("ENDTIME"));
                map.put("status",dbRet.get(0).get("STATUS").toString().toLowerCase().equals("enable")?"checked":"");
                getRequest().setAttribute("swingtimeproperty",map);
            }
        }
        return ADDMANAGER;
    }

    public String EditSwingTime() throws Exception {
        Map map = new HashMap();
        if (swingTime.length() == 0) {
            map.put("errorMessage", getText("addbank.BankNameError"));
        }
        else {
            Map parametMap = new HashMap();
            parametMap.put(1, swingTime);
            parametMap.put(2, timeEnabled!=null?"enable":"disable");
            parametMap.put(3, startTime);
            parametMap.put(4, endTime);
            parametMap.put(5, getParameter("uuid"));
            PosDbManager.executeUpdate("update swingtimetb set name=?, status=?,starttime=?,endtime=? where uuid=? ", (HashMap<Integer, Object>) parametMap);
            map.put("swingTimeList", new SwingTimeUI().generateTable());
        }

        return AjaxActionComplete(map);
    }

    public String AddSwingTime() throws Exception {
        Map map = new HashMap();
        if (swingTime.length() == 0) {
            map.put("errorMessage", getText("addswingtime.swingTimeError"));
        }
        else {
            try {
                Map parametMap = new HashMap();
                parametMap.put(1, UUIDUtils.generaterUUID());
                parametMap.put(2, swingTime);
                Time _startTime = Time.valueOf(startTime);
                Time _endTime = Time.valueOf(endTime);
                if (_startTime.after(_endTime)) {
                    map.put("errorMessage", getText("addswingtime.endTimeEarlierThanStartTime"));
                    return AjaxActionComplete(map);
                }
                parametMap.put(3, _startTime);
                parametMap.put(4, _endTime);
                if (timeEnabled != null)
                    parametMap.put(5, new String("enable"));
                else
                    parametMap.put(5, new String("disable"));
                PosDbManager.executeUpdate("insert into swingtimetb(uuid,name,startTime,endTime,status) values(?,?,?,?,?)", (HashMap<Integer, Object>) parametMap);
                map.put("swingTimeList", new SwingTimeUI().generateTable());
            }
            catch (IllegalArgumentException illegalException) {
                map.put("errorMessage", getText("addswingtime.timeFormatError"));
            }
        }

        return AjaxActionComplete(map);
    }
}
