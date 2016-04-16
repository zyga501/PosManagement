package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.UUIDUtils;
import com.posmanagement.utils.UserUtils;
import com.posmanagement.webui.AssetUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssetAction extends AjaxActionSupport {
    public final static String ASSETMANAGER = "assetManager";
    public final static String ASSETHEDGE = "assetHedge";

    private String assetList;
    private String assetUUID;

    public String getAssetList() {
        return assetList;
    }

    public String getAssetUUID() {
        return assetUUID;
    }

    public void setAssetUUID(String _assetUUID) {
        assetUUID = _assetUUID;
    }

    public String Init() throws Exception {
        assetList = new AssetUI(getUserID()).generateAssetTable();
        return ASSETMANAGER;
    }

    public String addAsset() throws Exception {
        Map map = new HashMap();
        try {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(getParameter("cardCode").toString());
            if (!isNum.matches()) {
                throw new IllegalArgumentException();
            }
            Map parametMap = new HashMap();
            parametMap.put(1, UUIDUtils.generaterUUID());
            parametMap.put(2, getParameter("bankUUID").toString());
            parametMap.put(3, getParameter("cardCode").toString());
            parametMap.put(4, getParameter("balance").toString());
            parametMap.put(5, getParameter("signPwd").toString());
            parametMap.put(6, getParameter("cashPwd").toString());
            parametMap.put(7, getParameter("transferPwd").toString());
            parametMap.put(8, getParameter("atmCashPwd").toString());
            parametMap.put(9, super.getUserID());
            PosDbManager.executeUpdate("insert into assettb(uuid,bankuuid,cardno,balance,ebanksignpwd,ebankcashpwd," +
                    "ebanktransferpwd,atmcashpwd,salemanuuid) values(?,?,?,?,?,?,?,?,?)", (HashMap<Integer, Object>)parametMap);
            map.put("assetList", new AssetUI(getUserID()).generateAssetTable());
        }
        catch (Exception exception) {
            map.put("errorMessage", getText("AssetAction.InfoError"));
        }

        return AjaxActionComplete(map);
    }

    public String HedgeAsset() {
        return ASSETHEDGE;
    }

    public String hedgeAsset() {
        Map map = new HashMap();
        try {
            Double hedgeMoney = Double.parseDouble(getParameter("hedgeMoney").toString());
            Map parametMap = new HashMap();
            parametMap.put(1, hedgeMoney);
            parametMap.put(2, getParameter("assetUUID"));
            PosDbManager.executeUpdate("update assettb set balance=balance+? where uuid=?", (HashMap<Integer, Object>)parametMap);
            String salemanUUID = super.getUserID();
            if (!UserUtils.issaleman(salemanUUID)) {
                parametMap.clear();
                parametMap.put(1, salemanUUID);
                ArrayList<HashMap<String, Object>> salemanRet = PosDbManager.executeSql("select salemanuuid from tellertb where uid=?", (HashMap<Integer, Object>)parametMap);
                if (salemanRet.size() > 0) {
                    salemanUUID = salemanRet.get(0).get("SALEMANUUID").toString();
                }
            }
            parametMap.clear();
            parametMap.clear();
            parametMap.put(1, getParameter("assetUUID"));
            ArrayList<HashMap<String, Object>> assetRet = PosDbManager.executeSql("select * from assettb where uuid=?", (HashMap<Integer, Object>)parametMap);
            double balance = Double.parseDouble(assetRet.get(0).get("BALANCE").toString());
            String cardno = assetRet.get(0).get("CARDNO").toString();
            parametMap.put(1, hedgeMoney);
            parametMap.put(2, balance);
            parametMap.put(3, cardno);
            parametMap.put(4, salemanUUID);
            PosDbManager.executeUpdate("insert into assetflowtb(time, type, amount, balance, remark, salemanuuid)\n" +
                            "VALUES(NOW(), '资产对冲',?, ?, CONCAT(?,'对冲'), ?);\n"
                    , (HashMap<Integer, Object>)parametMap);
            map.put("assetList", new AssetUI(getUserID()).generateAssetTable());
        }
        catch (Exception exception) {
            map.put("errorMessage", exception.getMessage());
        }
        return AjaxActionComplete(map);
    }

    public String FetchAssetList(){
        Map map = new HashMap();
        if (getParameter("uiMode") != null && getParameter("uiMode").toString().compareTo("SELECTLIST") == 0) {
            try {
                map.put("assetList", new AssetUI(UserUtils.getsalemanid(getUserID())).generateAssetSelectList());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return AjaxActionComplete(map);
    }
}
