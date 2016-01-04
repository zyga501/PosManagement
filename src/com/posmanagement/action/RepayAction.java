package com.posmanagement.action;

import com.posmanagement.webui.RepayUI;

public class RepayAction extends AjaxActionSupport {
    private final static String REPAYMANAGER = "repayManager";
    private final static String REPAYDETAIL = "repayDetail";

    private String repaySummary;
    private String repayDetail;

    public String getRepaySummary() {
        return repaySummary;
    }

    public String getRepayDetail() {
        return repayDetail;
    }

    public String Init() throws Exception {
        if (getSession().get("userName").toString().equals("admin"))
            repaySummary = new RepayUI("").generateSummary();
        else
            repaySummary = new RepayUI(getSession().get("userID").toString()).generateSummary();
        return REPAYMANAGER;
    }

    public String InitDetail() throws Exception {
        if (getSession().get("userName").toString().equals("admin"))
            repayDetail = new RepayUI("").generateDetail();
        else
            repayDetail = new RepayUI(getSession().get("userID").toString()).generateDetail();
        return REPAYDETAIL;
    }
}