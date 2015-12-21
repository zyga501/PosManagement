package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.webui.SwingTimeList;
import com.posmanagement.webui.WebUI;

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
        swingTimeList = new SwingTimeList(WebUI.UIMode.TABLELIST).generateHTMLString();
        return SWINGTIMEMANAGER;
    }

    public String FetchSwingTimeList() throws Exception {
        Map map = new HashMap();
        if (uiMode != null && uiMode.compareTo("SELECTLIST") == 0) {
            map.put("swingTimeList", new SwingTimeList(WebUI.UIMode.SELECTLIST).generateHTMLString());
        }
        else {
            map.put("swingTimeList", new SwingTimeList(WebUI.UIMode.TABLELIST).generateHTMLString());
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
                PosDbManager.executeUpdate("insert into swingtimetb(swingTime,startTime,endTime,enabled) values(?,?,?,?)", (HashMap<Integer, Object>) parametMap);
                map.put("swingTimeList", new SwingTimeList(WebUI.UIMode.TABLELIST).generateHTMLString());
            }
            catch (IllegalArgumentException illegalException) {
                map.put("errorMessage", getText("addswingtime.timeFormatError"));
            }
        }

        return AjaxActionComplete(map);
    }
}
