package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.webui.SwingCardUI;

import java.util.HashMap;
import java.util.Map;

public class SwingCardAction extends AjaxActionSupport {
    private final static String SWINGCARDMANAGER = "swingCardManager";
    private final static String SWINGCARDDETAIL = "swingCardDetail";

    private String swingCardSummary;
    private String swingCardDetail;
    private String cardNO;
    private String billYear;
    private String billMonth;

    public String getSwingCardSummary() {
        return swingCardSummary;
    }

    public String getSwingCardDetail() {
        return swingCardDetail;
    }

    public String getCardNO() { return cardNO; }

    public String getBillYear() { return billYear; }

    public String getBillMonth() { return billMonth; }

    public String Init() throws Exception {
        String userID = super.getUserID();
        if (super.getUserName().equals("admin")) {
            userID = "";
        }
        swingCardSummary = new SwingCardUI(userID).generateSummary();
        return SWINGCARDMANAGER;
    }

    public String InitDetail() throws Exception {
        String userID = super.getUserID();
        if (super.getUserName().equals("admin")) {
            userID = "";
        }
        swingCardDetail = new SwingCardUI(userID).generateDetail(getParameter("CardNO").toString(), getParameter("billYear").toString(), getParameter("billMonth").toString());
        cardNO = getParameter("CardNO").toString();
        billYear = getParameter("billYear").toString();
        billMonth = getParameter("billMonth").toString();
        return SWINGCARDDETAIL;
    }

    public String EditDetail() throws Exception{
        Map map =new HashMap();
        if (null==getParameter("status") || null==getParameter("swingId")) {
            map.put("errorMessage", getText("BillAction.InfoErro"));
        }
        else {
            Map para =new HashMap();
            para.put(1,"enable");
            para.put(2,super.getUserID());
            para.put(3,getParameter("swingId"));
            if (PosDbManager.executeUpdate("update swingcard set swingstatus=?,userid=?,realsdatetm=now() where id=?",(HashMap<Integer, Object>)para)) {
                map.put("successMessage", getText("BillAction.InfoSuccess"));
                String userID = super.getUserID();
                if (super.getUserName().equals("admin")) {
                    userID = "";
                }
                map.put("swingCardDetail", new SwingCardUI(userID).generateDetail(getParameter("cardNO").toString(), getParameter("billYear").toString(), getParameter("billMonth").toString()));
            }
        }
        return AjaxActionComplete(map);
    }
}