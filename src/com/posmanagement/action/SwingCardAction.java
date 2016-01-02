package com.posmanagement.action;

import com.opensymphony.xwork2.ActionContext;
import com.posmanagement.utils.PosDbManager;
import com.posmanagement.webui.SwingCardUI;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SwingCardAction extends AjaxActionSupport {
    private final static String SWINGCARDMANAGER = "swingCardManager";
    private final static String SWINGCARDDETAIL = "swingCardDetail";

    private String swingCardSummary;
    private String swingCardDetail;

    public String getSwingCardSummary() {
        return swingCardSummary;
    }

    public String getSwingCardDetail() {
        return swingCardDetail;
    }

    public String Init() throws Exception {
        if (ActionContext.getContext().getSession().get("userName").toString().equals("admin"))
            swingCardSummary = new SwingCardUI("").generateSummary();
        else
            swingCardSummary = new SwingCardUI(ActionContext.getContext().getSession().get("userID").toString()).generateSummary();
        return SWINGCARDMANAGER;
    }

    public String InitDetail() throws Exception {
        if (ActionContext.getContext().getSession().get("userName").toString().equals("admin"))
            swingCardDetail = new SwingCardUI("").generateDetail();
        else
            swingCardDetail = new SwingCardUI(ActionContext.getContext().getSession().get("userID").toString()).generateDetail();
        return SWINGCARDDETAIL;
    }

    public String editDetail() throws Exception{
        Map map =new HashMap();
        if (null==getParameter("status") || null==getParameter("swingId")) {
            map.put("errorMessage", getText("BillAction.InfoErro"));
        }
        else {
            Map para =new HashMap();
            para.put(1,"enable");
            para.put(2,ActionContext.getContext().getSession().get("userID").toString());
            para.put(3,getParameter("swingId"));
            if (PosDbManager.executeUpdate("update swingcard set swingstatus=?,userid=?，realsdatetm=now() where id=?",(HashMap<Integer, Object>)para))
                map.put("successMessage",getText("BillAction.InfoSuccess") );
        }
        return AjaxActionComplete(map);
    }

    public String enableDetail() throws Exception{
        Map map =new HashMap();
        if (null==getParameter("swingIdList")|| null==getParameter("swingIdNOList")) {
            map.put("errorMessage", getText("BillAction.InfoErro"));
        }
        else {
            Map para =new HashMap();
            if (!getParameter("swingIdList").equals("")) {
                para.put(1, "enable");
                para.put(2, ActionContext.getContext().getSession().get("userID").toString());
              //  para.put(3, getParameter("swingIdList").toString().substring(0, getParameter("swingIdList").toString().length() - 1));
                PosDbManager.executeUpdate("update swingcard a inner join  cardtb b on a.cardno=b.cardno inner join  salesmantb c " +
                        "on c.uid=b.salesmanuuid set a.VALIDSTATUS=? where c.uid=? and  a.id in ("+
                        getParameter("swingIdList").toString().substring(0, getParameter("swingIdList").toString().length() - 1)+")", (HashMap<Integer, Object>) para);
                PosDbManager.executeUpdate("update swingcard a inner join  cardtb b on a.cardno=b.cardno inner join  tellertb  c " +
                        "on c.salesman=b.salesmanuuid set a.VALIDSTATUS=? where c.uid=? and  a.id in ("+
                        getParameter("swingIdList").toString().substring(0, getParameter("swingIdList").toString().length() - 1)+")", (HashMap<Integer, Object>) para);
            }
            if (!getParameter("swingIdNOList").equals("")) {
                para.clear();
                para.put(1, "disable");
                para.put(2, ActionContext.getContext().getSession().get("userID").toString());
               // para.put(3, getParameter("swingIdNOList").toString().substring(0, getParameter("swingIdNOList").toString().length() - 1));
                PosDbManager.executeUpdate("update swingcard a inner join  cardtb b on a.cardno=b.cardno inner join  salesmantb c " +
                        "on c.uid=b.salesmanuuid set a.VALIDSTATUS=? where c.uid=? and  a.id in ("+
                        getParameter("swingIdNOList").toString().substring(0, getParameter("swingIdNOList").toString().length() - 1)+")", (HashMap<Integer, Object>) para);
                PosDbManager.executeUpdate("update swingcard a inner join  cardtb b on a.cardno=b.cardno inner join  tellertb  c " +
                        "on c.salesman=b.salesmanuuid set a.VALIDSTATUS=? where c.uid=? and  a.id in ("+
                        getParameter("swingIdNOList").toString().substring(0, getParameter("swingIdNOList").toString().length() - 1)+")", (HashMap<Integer, Object>) para);
            }
                map.put("successMessage",getText("BillAction.InfoSuccess") );
        }
        return AjaxActionComplete(map);
    }
}