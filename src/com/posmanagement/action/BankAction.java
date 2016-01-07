package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.UUIDUtils;
import com.posmanagement.webui.BankUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BankAction extends AjaxActionSupport {
    private final static String BANKMANAGER = "bankManager";
    private final static String ADDBANK  = "addBank";

    private String bankList;
    private String bankName;
    private String bankEnabled;
    private String uiMode;

    public String getBankList() {
        return bankList;
    }

    public void setBankName(String _bankName) {
        bankName = _bankName;
    }

    public void setBankEnabled(String _bankEnabled) {
        bankEnabled = _bankEnabled;
    }

    public void setUiMode(String _uiMode) {
        uiMode = _uiMode;
    }

    public String Init() throws Exception {
        bankList = new BankUI().generateBankTable();
        return BANKMANAGER;
    }

    public String FetchBankList() throws Exception {
        Map map = new HashMap();
        if (uiMode != null && uiMode.compareTo("SELECTLIST") == 0) {
            map.put("bankList", new BankUI().generateBankSelectList());
        }
        else {
            map.put("bankList", new BankUI().generateBankTable());
        }

        return AjaxActionComplete(map);
    }


    public String FetchBank() throws Exception {
        Map map = new HashMap();
        if (null!=getParameter("UUID")&&(!getParameter("UUID").equals(""))) {
                Map parametMap = new HashMap();
                parametMap.put(1, getParameter("UUID"));
                ArrayList<HashMap<String, Object>> dbRet = PosDbManager.executeSql("select  * from banktb where uuid=?",(HashMap<Integer, Object>) parametMap);
                if (dbRet.size()>0){
                        map.put("uuid",getParameter("UUID"));
                        map.put("name",dbRet.get(0).get("NAME"));
                        map.put("status",dbRet.get(0).get("STATUS").toString().toLowerCase().equals("enable")?"checked":"");
                        getRequest().setAttribute("bankproperty",map);
                    }
            }
        return ADDBANK;
    }

    public String EditBank() throws Exception {
        Map map = new HashMap();
        if (bankName.length() == 0) {
            map.put("errorMessage", getText("addbank.BankNameError"));
        }
        else {
            Map parametMap = new HashMap();
            parametMap.put(1, bankEnabled!=null?"enable":"disable");
            parametMap.put(2, getParameter("uuid"));
            PosDbManager.executeUpdate("update banktb set  status=? where uuid=? ", (HashMap<Integer, Object>) parametMap);
            map.put("bankList", new BankUI().generateBankTable());
        }

        return AjaxActionComplete(map);
    }

    public String AddBank() throws Exception {
        Map map = new HashMap();
        if (bankName.length() == 0) {
            map.put("errorMessage", getText("addbank.BankNameError"));
        }
        else {
            Map parametMap = new HashMap();
            parametMap.put(1, UUIDUtils.generaterUUID());
            parametMap.put(2, bankName);
            if (bankEnabled != null)
                parametMap.put(3, new String("enable"));
            else
                parametMap.put(3, new String("disable"));
            PosDbManager.executeUpdate("insert into banktb(uuid,name,status) values(?,?,?)", (HashMap<Integer, Object>) parametMap);
            map.put("bankList", new BankUI().generateBankTable());
        }

        return AjaxActionComplete(map);
    }
}
