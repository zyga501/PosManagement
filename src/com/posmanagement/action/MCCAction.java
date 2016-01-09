package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.UUIDUtils;
import com.posmanagement.webui.MCCUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MCCAction extends AjaxActionSupport {
    private final static String MCCMANAGER = "mccManager";
    private final static String ADDMCC = "addmcc";

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

    public String FetchMCC() throws Exception {
        Map map = new HashMap();
        if (null!=getParameter("UUID")&&(!getParameter("UUID").equals(""))) {
            Map parametMap = new HashMap();
            parametMap.put(1, getParameter("UUID"));
            ArrayList<HashMap<String, Object>> dbRet = PosDbManager.executeSql("select  * from mcctb where uuid=?",(HashMap<Integer, Object>) parametMap);
            if (dbRet.size()>0){
                map.put("uuid",getParameter("UUID"));
                map.put("mcccode",dbRet.get(0).get("MCC"));
                map.put("status",dbRet.get(0).get("STATUS").toString().toLowerCase().equals("enable")?"checked":"");
                getRequest().setAttribute("mccproperty",map);
            }
        }
        return ADDMCC;
    }

    public String EditMCC() throws Exception {
        Map map = new HashMap();
        if (mccCode.length() == 0) {
            map.put("errorMessage", getText("addbank.BankNameError"));
        }
        else {
            Map parametMap = new HashMap();
            parametMap.put(1, mccEnabled!=null?"enable":"disable");
            parametMap.put(2, getParameter("uuid"));
            PosDbManager.executeUpdate("update mcctb set  status=? where uuid=? ", (HashMap<Integer, Object>) parametMap);
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
