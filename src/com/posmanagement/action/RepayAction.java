package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.webui.RepayUI;

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
        String userID = super.getUserID();
        if (super.getUserName().equals("admin")) {
            userID = "";
        }
        repaySummary = new RepayUI(userID).generateSummary();
        return REPAYMANAGER;
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