package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.SQLUtils;
import com.posmanagement.utils.StringUtils;
import com.posmanagement.utils.UserUtils;
import com.posmanagement.webui.RepayUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RepayAction extends AjaxActionSupport {
    private final static String REPAYMANAGER = "repayManager";
    private final static String REPAYDETAIL = "repayDetail";

    private String repayDetail;
    private String cardNO;
    private String billUUID;

    public String getRepayDetail() {
        return repayDetail;
    }

    public String getCardNO() { return cardNO; }

    public String getBillUUID() {
        return billUUID;
    }

    public String Init() throws Exception {
        getRequest().setAttribute("pagecount", (new RepayUI(getUserID()).fetchRepayPageCount())/RepayUI.pagecontent+1);
        return REPAYMANAGER;
    }

    public String FetchRepayList(){

        ArrayList<SQLUtils.WhereCondition> uiConditions = new ArrayList<SQLUtils.WhereCondition>() {
            {
                add(new SQLUtils.WhereCondition("cardtb.cardno", "like",
                        SQLUtils.ConvertToSqlString("%" + getParameter("cardno") + "%"), !StringUtils.convertNullableString(getParameter("cardno")).trim().isEmpty()));
                add(new SQLUtils.WhereCondition("banktb.name", "like",
                        SQLUtils.ConvertToSqlString("%" + getParameter("bankname") + "%"), !StringUtils.convertNullableString(getParameter("bankname")).trim().isEmpty()));
                add(new SQLUtils.WhereCondition("cardmaster", "like",
                        SQLUtils.ConvertToSqlString("%" + getParameter("cardmaster") + "%"), !StringUtils.convertNullableString(getParameter("cardmaster")).trim().isEmpty()));
                add(new SQLUtils.WhereCondition("billtb.lastrepaymentdate", "like",
                        SQLUtils.ConvertToSqlString("%" + getParameter("thedate") + "%"), !StringUtils.convertNullableString(getParameter("thedate")).trim().isEmpty()));
            }
        };

        Map map = new HashMap();
        RepayUI repayUI = new RepayUI(super.getUserID());
        repayUI.setUiConditions(uiConditions);
        try {
            map.put("pagecount",repayUI.fetchRepayPageCount());

        int curr = Integer.parseInt(null==getParameter("currpage")?"1":getParameter("currpage").toString());
            map.put("repaySummary",repayUI.generateSummary(curr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxActionComplete(map);
    }

    public String InitDetail() throws Exception {
        repayDetail = new RepayUI(super.getUserID()).generateDetail(getParameter("cardNO").toString(), getParameter("billUUID").toString());
        cardNO = getParameter("cardNO").toString();
        billUUID = getParameter("billUUID").toString();
        return REPAYDETAIL;
    }

    public String EditDetail() throws Exception{
        Map map =new HashMap();
        if (null==getParameter("status") || null==getParameter("repayId")) {
            map.put("errorMessage", getText("BillAction.InfoErro"));
        }
        else {
            Map parameterMap =new HashMap();
            parameterMap.put(1,super.getUserID());
            parameterMap.put(2,getParameter("repayId"));
            if (PosDbManager.executeUpdate("update repaytb\n" +
                    "INNER JOIN cardtb on cardtb.cardno=repaytb.cardno\n" +
                    "set \n" +
                    "tradestatus='enable',\n" +
                    "userid=?,\n" +
                    "tradetime=now(),\n" +
                    "charge=cardtb.commissioncharge * repaytb.trademoney\n" +
                    "where id=?",(HashMap<Integer, Object>)parameterMap)) {
                String salemanUUID = super.getUserID();
                if (!UserUtils.issaleman(salemanUUID)) {
                    parameterMap.clear();
                    parameterMap.put(1, salemanUUID);
                    ArrayList<HashMap<String, Object>> salemanRet = PosDbManager.executeSql("select salemanuuid from tellertb where uid=?", (HashMap<Integer, Object>)parameterMap);
                    if (salemanRet.size() > 0) {
                        salemanUUID = salemanRet.get(0).get("SALEMANUUID").toString();
                    }
                }
                parameterMap.clear();
                parameterMap.put(1, getParameter("repayId"));
                parameterMap.put(2, salemanUUID);
                String whereSql = new String();
                if (PosDbManager.executeUpdate("UPDATE assettb\n" +
                        "SET balance= balance - (select trademoney - charge from repaytb where id=?)\n" +
                        "where salemanuuid=?", (HashMap<Integer, Object>)parameterMap)) {
                    map.put("successMessage", getText("global.actionSuccess"));
                    map.put("repayDetail", new RepayUI(super.getUserID()).generateDetail(getParameter("cardNO").toString(), getParameter("billUUID").toString()));
                    parameterMap.clear();
                    parameterMap.put(1, getParameter("repayId"));
                    ArrayList<HashMap<String, Object>> repayRet = PosDbManager.executeSql("SELECT trademoney, cardNO, charge from repaytb where id=?", (HashMap<Integer, Object>)parameterMap);
                    parameterMap.clear();
                    parameterMap.put(1, super.getUserID());
                    ArrayList<HashMap<String, Object>> assetRet = PosDbManager.executeSql("SELECT balance from assettb where salemanuuid=?", (HashMap<Integer, Object>)parameterMap);
                    if (repayRet.size() > 0 && assetRet.size() > 0) {
                        double repayAmount = -Double.parseDouble(repayRet.get(0).get("TRADEMONEY").toString());
                        double charge = Double.parseDouble(repayRet.get(0).get("CHARGE").toString());
                        String cardNO = repayRet.get(0).get("CARDNO").toString();
                        double balance = Double.parseDouble(assetRet.get(0).get("BALANCE").toString());
                        parameterMap.clear();
                        parameterMap.put(1, repayAmount);
                        parameterMap.put(2, balance - charge);
                        parameterMap.put(3, cardNO);
                        parameterMap.put(4, salemanUUID);
                        PosDbManager.executeUpdate("insert into assetflowtb(time, type, amount, balance, remark, salemanuuid)\n" +
                                "VALUES(NOW(), '还款',?, ?, CONCAT(?,'还款'), ?);\n"
                                , (HashMap<Integer, Object>)parameterMap);
                        parameterMap.clear();
                        parameterMap.put(1, charge);
                        parameterMap.put(2, balance);
                        parameterMap.put(3, cardNO);
                        parameterMap.put(4, salemanUUID);
                        PosDbManager.executeUpdate("insert into assetflowtb(time, type, amount, balance, remark, salemanuuid)\n" +
                                "VALUES(NOW(), '还款收费',?, ?, CONCAT(?,'还款收费'), ?);"
                                , (HashMap<Integer, Object>)parameterMap);
                    }
                }
            }
        }
        return AjaxActionComplete(map);
    }
}