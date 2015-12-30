package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.UUIDUtils;
import com.posmanagement.webui.MCCUI;

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
        mccList = new MCCUI().generateMCCTable();
        return MCCMANAGER;
    }

    public String FetchMCCList() throws Exception {
        Map map = new HashMap();
        if (uiMode != null && uiMode.compareTo("SELECTLIST") == 0) {
            map.put("mccList", new MCCUI().generateMCCSelectList());
        }
        else {
            map.put("mccList", new MCCUI().generateMCCTable());
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
                parametMap.put(1, UUIDUtils.generaterUUID());
                parametMap.put(2, mccCode);
                if (mccEnabled != null)
                    parametMap.put(3, new String("enable"));
                else
                    parametMap.put(3, new String("disable"));
                PosDbManager.executeUpdate("insert into mcctb(uuid,mcc,status) values(?,?,?)", (HashMap<Integer, Object>) parametMap);
                map.put("mccList", new MCCUI().generateMCCTable());
            }
            catch (NumberFormatException exception) {
                map.put("errorMessage", getText("addmcc.mccCodeFormatError"));
            }
        }

        return AjaxActionComplete(map);
    }
}
