package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.SQLUtils;
import com.posmanagement.utils.StringUtils;
import com.posmanagement.utils.UserUtils;
import com.posmanagement.webui.SwingCardUI;
import com.posmanagement.webui.WebUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SwingCardAction extends AjaxActionSupport {
    private final static String SWINGCARDMANAGER = "swingCardManager";
    private final static String SWINGCARDDETAIL = "swingCardDetail";

    private String swingCardDetail;
    private String cardNO;
    private String billUUID;

    public String getSwingCardDetail() {
        return swingCardDetail;
    }

    public String getCardNO() { return cardNO; }

    public String getBillUUID() { return billUUID; }

    public String Init() throws Exception {
        getRequest().setAttribute("pagecount", (new SwingCardUI(super.getUserID()).fetchSwingCardPageCount())/WebUI.DEFAULTITEMPERPAGE+1);
        return SWINGCARDMANAGER;
    }

    public String InitDetail() throws Exception {
        swingCardDetail = new SwingCardUI(super.getUserID()).generateDetail(getParameter("cardNO").toString(), getParameter("billUUID").toString());
        cardNO = getParameter("cardNO").toString();
        billUUID = getParameter("billUUID").toString();
        return SWINGCARDDETAIL;
    }

    public String EditDetail() throws Exception{
        Map map =new HashMap();
        if (null==getParameter("status") || null==getParameter("swingId")) {
            map.put("errorMessage", getText("BillAction.InfoErro"));
        }
        else {
            Map parameterMap =new HashMap();
            parameterMap.put(1,super.getUserID());
            parameterMap.put(2,getParameter("swingId"));
            if (PosDbManager.executeUpdate("update swingcard set swingstatus='enable',userid=?,realsdatetm=now() where id=?",(HashMap<Integer, Object>)parameterMap)) {
                parameterMap.clear();
                parameterMap.put(1, getParameter("swingId"));
                parameterMap.put(2, super.getUserID());
                String whereSql = new String();
                if (UserUtils.isSalesman(super.getUserID())) {
                    whereSql = "where salesmanuuid=?";
                }
                else {
                    whereSql = "where salesmanuuid=(select salesmanuuid from tellertb where uid=?)";
                }
                if (PosDbManager.executeUpdate("update assettb\n" +
                        "set balance=balance +\n" +
                        "(SELECT swingcard.amount -\n" +
                        "(case when (maxfee='' or maxfee=NULL) \n" +
                        "then swingcard.amount * rate * 0.01\n" +
                        "else \n" +
                        "(case when maxfee>(swingcard.amount * rate * 0.01)\n" +
                        "then (swingcard.amount * rate * 0.01) \n" +
                        "else maxfee \n" +
                        "end) \n" +
                        "end) as fee\n" +
                        "FROM\n" +
                        "swingcard\n" +
                        "INNER JOIN postb ON postb.uuid = swingcard.posuuid\n" +
                        "INNER JOIN ratetb ON ratetb.uuid = postb.rateuuid\n" +
                        "where id = ?\n" +
                        ")\n" +
                        whereSql, (HashMap<Integer, Object>)parameterMap)) {
                    map.put("successMessage", getText("global.actionSuccess"));
                    map.put("swingCardDetail", new SwingCardUI(super.getUserID()).generateDetail(getParameter("cardNO").toString(), getParameter("billUUID").toString()));
                }
            }
        }
        return AjaxActionComplete(map);
    }

    public String FetchSwingList() throws Exception {
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
        SwingCardUI swingCardUI = new SwingCardUI(super.getUserID());
        swingCardUI.setUiConditions(uiConditions);
        map.put("pagecount", swingCardUI.fetchSwingCardPageCount());
        int curr = Integer.parseInt(null==getParameter("currpage")?"1":getParameter("currpage").toString());
        map.put("swingCardSummary",swingCardUI.generateSummary(curr));

        return AjaxActionComplete(map);
    }
}