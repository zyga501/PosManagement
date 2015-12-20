package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.webui.MCCList;

import java.util.HashMap;
import java.util.Map;

public class MCCAction extends AjaxActionSupport {
    private final static String MCCMANAGER = "mccManager";

    private String mccList;
    private String mccCode;
    private String mccEnabled;
    private String uiMode;

    public String getMccList() {
        return mccList;
    }

    public void setMccCode(String _mccCode) {
        mccCode = _mccCode;
    }

    public void setMccEnabled(String _mccEnabled) {
        mccEnabled = _mccEnabled;
    }

    public void setUiMode(String _uiMode) {
        uiMode = _uiMode;
    }

    public String Init() throws Exception{
        mccList = new MCCList(MCCList.UIMode.TABLELIST).generateHTMLString();
        return MCCMANAGER;
    }

    public String FetchMCCList() throws Exception {
        Map map = new HashMap();
        if (uiMode != null && uiMode.compareTo("SELECTLIST") == 0) {
            map.put("mccList", new MCCList(MCCList.UIMode.SELECTLIST).generateHTMLString());
        }
        else {
            map.put("mccList", new MCCList(MCCList.UIMode.TABLELIST).generateHTMLString());
        }

        return AjaxActionComplete(map);
    }

    public String AddMCC() throws Exception {
        Map map = new HashMap();
        if (mccCode.length() == 0) {
            map.put("errorMessage", getText("addmcc.mccCodeError"));
        }
        else {
            try {
                Double.parseDouble(mccCode);
                Map parametMap = new HashMap();
                parametMap.put(1, mccCode);
                if (mccEnabled != null)
                    parametMap.put(2, new String("on"));
                else
                    parametMap.put(2, new String("off"));
                PosDbManager.executeUpdate("insert into mcctb(mcc,enabled) values(?,?)", (HashMap<Integer, Object>) parametMap);
                map.put("mccList", new MCCList(MCCList.UIMode.TABLELIST).generateHTMLString());
            }
            catch (NumberFormatException exception) {
                map.put("errorMessage", getText("addmcc.mccCodeFormatError"));
            }
        }

        return AjaxActionComplete(map);
    }
}
