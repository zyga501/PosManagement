package com.posmanagement.action;

import com.posmanagement.utils.DbManager;
import com.posmanagement.webui.SwingTimeList;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class SwingTimeAction extends AjaxActionSupport {
    private final static String SWINGTIMEMANAGER = "swingTimeManager";

    private String swingTimeList;
    private String swingTime;
    private String startTime;
    private String endTime;
    private String timeEnabled;

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

    public String Init() throws Exception {
        swingTimeList = new SwingTimeList().generateHTMLString();
        return SWINGTIMEMANAGER;
    }

    public String AddSwingTime() throws Exception {
        Map map = new HashMap();
        if (swingTime.length() == 0) {
            map.put("errorMessage", getText("addswingtime.swingTimeError"));
        }
        else {
            try {
                Map parametMap = new HashMap();
                parametMap.put(1, swingTime);
                Time _startTime = Time.valueOf(startTime);
                Time _endTime = Time.valueOf(endTime);
                if (_startTime.after(_endTime)) {
                    map.put("errorMessage", getText("addswingtime.endTimeEarlierThanStartTime"));
                    return AjaxActionComplete(map);
                }
                parametMap.put(2, _startTime);
                parametMap.put(3, _endTime);
                if (timeEnabled != null)
                    parametMap.put(4, new String("on"));
                else
                    parametMap.put(4, new String("off"));
                DbManager.createPosDbManager().executeUpdate("insert into swingtimetb(swingTime,startTime,endTime,enabled) values(?,?,?,?)", (HashMap<Integer, Object>) parametMap);
                map.put("swingTimeList", new SwingTimeList().generateHTMLString());
            }
            catch (IllegalArgumentException illegalException) {
                map.put("errorMessage", getText("addswingtime.timeFormatError"));
            }
        }

        return AjaxActionComplete(map);
    }
}
