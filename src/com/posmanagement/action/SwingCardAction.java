package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.webui.SwingCardUI;
import com.posmanagement.webui.SwingCardUI;

import java.util.ArrayList;
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
        swingCardSummary = new SwingCardUI(super.getUserID()).generateSummary("");
        getRequest().setAttribute("pagecount", (swingCardSummary.split("<tr").length-1)/SwingCardUI.pagecontent+1);
        return SWINGCARDMANAGER;
    }

    public String InitDetail() throws Exception {
        String userID = super.getUserID();
        if (super.getUserName().equals("admin")) {
            userID = "";
        }
        swingCardDetail = new SwingCardUI(userID).generateDetail(getParameter("cardNO").toString(), getParameter("billYear").toString(), getParameter("billMonth").toString());
        cardNO = getParameter("cardNO").toString();
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

    public String FetchSwingList(){
        String wherestr = " where 1=1 ";
        Map map = new HashMap();
        int i = 0;
        if (null!=getParameter("cardno") && (!getParameter("cardno").toString().trim().equals(""))){
            wherestr += "and cardno like '%"+getParameter("cardno")+"%'";
        }
        if (null!=getParameter("bankname")&& (!getParameter("bankname").toString().trim().equals(""))) {
            wherestr += "and banktb.name like '%"+getParameter("bankname")+"%'";
        }
        if (null!=getParameter("cardmaster")&& (!getParameter("cardmaster").toString().trim().equals(""))){
            wherestr += "and cardmaster  like '%"+getParameter("cardmaster")+"%'";
        }
        if (null!=getParameter("thedate")&& (!getParameter("thedate").toString().trim().equals(""))) {
            wherestr += "and userinfo.unick  like '%"+getParameter("thedate")+"%'";
        }
        if (null!=getParameter("statusenable")&& (!getParameter("statusenable").toString().trim().equals(""))) {
            wherestr += "and userinfo.unick  like '%"+getParameter("statusenable")+"%'";
        }

        try {
            ArrayList<HashMap<String, Object>> rect = PosDbManager.executeSql("select count(*) as cnt from swingcard" +
                    " inner join cardtb on cardtb.cardno = swingcard.cardno inner join banktb " +
                    "on cardtb.bankuuid=banktb.uuid" +
                    wherestr);
            if (rect.size()<=0)
                map.put("pagecount",0);
            map.put("pagecount",Integer.parseInt(rect.get(0).get("CNT").toString())/ SwingCardUI.pagecontent+1);
            int curr = Integer.parseInt(null==getParameter("currpage")?"1":getParameter("currpage").toString());
            swingCardSummary = new SwingCardUI(super.getUserID()).generateSummary(wherestr+" limit "+String.valueOf((curr-1)*SwingCardUI.pagecontent)+","+SwingCardUI.pagecontent);
            map.put("swingCardSummary",swingCardSummary);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxActionComplete(map);
    }

}