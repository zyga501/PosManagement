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
                parameterMap.clear();
                parameterMap.put(1, getParameter("repayId"));
                parameterMap.put(2, super.getUserID());
                String whereSql = new String();
                if (UserUtils.isSalesman(super.getUserID())) {
                    whereSql = "where salesmanuuid=?";
                }
                else {
                    whereSql = "where salesmanuuid=(select salesmanuuid from tellertb where uid=?)";
                }
                if (PosDbManager.executeUpdate("UPDATE assettb\n" +
                        "SET balance= balance - (select trademoney - charge from repaytb where id=?)\n" +
                        whereSql, (HashMap<Integer, Object>)parameterMap)) {
                    map.put("successMessage", getText("global.actionSuccess"));
                    map.put("repayDetail", new RepayUI(super.getUserID()).generateDetail(getParameter("cardNO").toString(), getParameter("billUUID").toString()));
                }
            }
        }
        return AjaxActionComplete(map);
    }
}