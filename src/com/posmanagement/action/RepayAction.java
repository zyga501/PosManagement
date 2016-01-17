package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.SQLUtils;
import com.posmanagement.utils.StringUtils;
import com.posmanagement.webui.RepayUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RepayAction extends AjaxActionSupport {
    private final static String REPAYMANAGER = "repayManager";
    private final static String REPAYDETAIL = "repayDetail";

    private String repaySummary;
    private String repayDetail;
    private String cardNO;
    private String repayYear;
    private String repayMonth;

    public String getRepaySummary() {
        return repaySummary;
    }

    public String getRepayDetail() {
        return repayDetail;
    }

    public String getCardNO() { return cardNO; }

    public String getRepayYear() { return repayYear; }

    public String getRepayMonth() { return repayMonth; }

    public String Init() throws Exception {
        repaySummary = new RepayUI(getUserID()).generateSummary();
        getRequest().setAttribute("pagecount", (repaySummary.split("<tr").length-1)/RepayUI.pagecontent+1);
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
                add(new SQLUtils.WhereCondition("billtb.billdate", "like",
                        SQLUtils.ConvertToSqlString("%" + getParameter("thedate") + "%"), !StringUtils.convertNullableString(getParameter("thedate")).trim().isEmpty()));
            }
        };

        Map map = new HashMap();
        RepayUI repayUI = new RepayUI(super.getUserID());
        repayUI.setUiConditions(uiConditions);
        try {
            map.put("pagecount",repayUI.fetchRepayPageCount());

        int curr = Integer.parseInt(null==getParameter("currpage")?"1":getParameter("currpage").toString());
            repaySummary = repayUI.generateSummary(curr);
            map.put("repaySummary",repaySummary);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxActionComplete(map);
    }

    public String InitDetail() throws Exception {
        String userID = super.getUserID();
        if (super.getUserName().equals("admin")) {
            userID = "";
        }
        repayDetail = new RepayUI(userID).generateDetail(getParameter("cardNO").toString(), getParameter("repayYear").toString(), getParameter("repayMonth").toString());
        cardNO = getParameter("cardNO").toString();
        repayYear = getParameter("repayYear").toString();
        repayMonth = getParameter("repayMonth").toString();
        return REPAYDETAIL;
    }

    public String EditDetail() throws Exception{
        Map map =new HashMap();
        if (null==getParameter("status") || null==getParameter("repayId")) {
            map.put("errorMessage", getText("BillAction.InfoErro"));
        }
        else {
            Map para =new HashMap();
            para.put(1,"enable");
            para.put(2,super.getUserID());
            para.put(3,getParameter("repayId"));
            if (PosDbManager.executeUpdate("update repaytb set tradestatus=?,userid=?,tradetime=now() where id=?",(HashMap<Integer, Object>)para)) {
                map.put("successMessage", getText("BillAction.InfoSuccess"));
                String userID = super.getUserID();
                if (super.getUserName().equals("admin")) {
                    userID = "";
                }
                map.put("repayDetail", new RepayUI(userID).generateDetail(getParameter("cardNO").toString(), getParameter("repayYear").toString(), getParameter("repayMonth").toString()));
            }
        }
        return AjaxActionComplete(map);
    }
}