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

    public String getRepaySummary() {
        return repaySummary;
    }

    public String getRepayDetail() {
        return repayDetail;
    }

    public String Init() throws Exception {
        if (super.getUserName().equals("admin"))
            repaySummary = new RepayUI("").generateSummary();
        else
            repaySummary = new RepayUI(super.getUserID()).generateSummary();
        return REPAYMANAGER;
    }

    public String InitDetail() throws Exception {
        if (super.getUserName().equals("admin"))
            repayDetail = new RepayUI("").generateDetail(getParameter("CardNO").toString(), getParameter("repayYear").toString(), getParameter("repayMonth").toString());
        else
            repayDetail = new RepayUI(super.getUserID()).generateDetail(getParameter("CardNO").toString(), getParameter("repayYear").toString(), getParameter("repayMonth").toString());
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
            if (PosDbManager.executeUpdate("update repaytb set tradestatus=?,userid=?,tradetime=now() where id=?",(HashMap<Integer, Object>)para))
                map.put("successMessage",getText("BillAction.InfoSuccess") );
        }
        return AjaxActionComplete(map);
    }


    public String enableDetail() throws Exception{
        Map map =new HashMap();
        if (null==getParameter("repayIdList")|| null==getParameter("repayIdNOList")) {
            map.put("errorMessage", getText("BillAction.InfoErro"));
        }
        else {
            Map para =new HashMap();
            if (!getParameter("repayIdList").equals("")) {
                para.put(1, "enable");
                para.put(2, super.getUserID());
                PosDbManager.executeUpdate("update repaytb a inner join  cardtb b on a.cardno=b.cardno inner join  salesmantb c " +
                        "on c.uid=b.salesmanuuid set a.VALIDSTATUS=? where c.uid=? and  a.id in ("+
                        getParameter("repayIdList").toString().substring(0, getParameter("repayIdList").toString().length() - 1)+")", (HashMap<Integer, Object>) para);
                PosDbManager.executeUpdate("update repaytb a inner join  cardtb b on a.cardno=b.cardno inner join  tellertb  c " +
                        "on c.salesman=b.salesmanuuid set a.VALIDSTATUS=? where c.uid=? and  a.id in ("+
                        getParameter("repayIdList").toString().substring(0, getParameter("repayIdList").toString().length() - 1)+")", (HashMap<Integer, Object>) para);
            }
            if (!getParameter("repayIdNOList").equals("")) {
                para.clear();
                para.put(1, "disable");
                para.put(2, super.getUserID());
                PosDbManager.executeUpdate("update repaytb a inner join  cardtb b on a.cardno=b.cardno inner join  salesmantb c " +
                        "on c.uid=b.salesmanuuid set a.VALIDSTATUS=? where c.uid=? and  a.id in ("+
                        getParameter("repayIdNOList").toString().substring(0, getParameter("repayIdNOList").toString().length() - 1)+")", (HashMap<Integer, Object>) para);
                PosDbManager.executeUpdate("update repaytb a inner join  cardtb b on a.cardno=b.cardno inner join  tellertb  c " +
                        "on c.salesman=b.salesmanuuid set a.VALIDSTATUS=? where c.uid=? and  a.id in ("+
                        getParameter("repayIdNOList").toString().substring(0, getParameter("repayIdNOList").toString().length() - 1)+")", (HashMap<Integer, Object>) para);
            }
            map.put("successMessage",getText("BillAction.InfoSuccess") );
        }
        return AjaxActionComplete(map);
    }
}