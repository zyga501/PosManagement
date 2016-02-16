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
            if (PosDbManager.executeUpdate("update swingcard\n" +
                    "INNER JOIN postb ON postb.uuid = swingcard.posuuid\n" +
                    "INNER JOIN ratetb ON ratetb.uuid = postb.rateuuid\n" +
                    "set \n" +
                    "swingstatus='enable',\n" +
                    "userid=?,\n" +
                    "realsdatetm=now(),\n" +
                    "charge=\n" +
                    "(\n" +
                    "case when \n" +
                    "(maxfee='' or maxfee=NULL) \n" +
                    "then swingcard.amount * rate * 0.01\n" +
                    "else \n" +
                    "(\n" +
                    "case when maxfee>(swingcard.amount * rate * 0.01)\n" +
                    "then \n" +
                    "(swingcard.amount * rate * 0.01) \n" +
                    "else \n" +
                    "maxfee \n" +
                    "end)\n" +
                    "end)" +
                    " where id=?",(HashMap<Integer, Object>)parameterMap)) {
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
                parameterMap.put(1, getParameter("swingId"));
                parameterMap.put(2, salemanUUID);
                if (PosDbManager.executeUpdate("update assettb\n" +
                        "set balance=balance +\n" +
                        "(SELECT swingcard.amount - swingcard.charge\n" +
                        "FROM\n" +
                        "swingcard\n" +
                        "where id = ?\n" +
                        ")\n" +
                        "where salemanuuid=?", (HashMap<Integer, Object>)parameterMap)) {
                    map.put("successMessage", getText("global.actionSuccess"));
                    map.put("swingCardDetail", new SwingCardUI(super.getUserID()).generateDetail(getParameter("cardNO").toString(), getParameter("billUUID").toString()));
                    parameterMap.clear();
                    parameterMap.put(1, getParameter("swingId"));
                    ArrayList<HashMap<String, Object>> swingRet = PosDbManager.executeSql("SELECT amount, charge, postb.posname from swingcard LEFT JOIN postb on swingcard.posuuid = postb.uuid where id=?", (HashMap<Integer, Object>)parameterMap);
                    parameterMap.clear();
                    parameterMap.put(1, super.getUserID());
                    ArrayList<HashMap<String, Object>> assetRet = PosDbManager.executeSql("SELECT balance from assettb where salemanuuid=?", (HashMap<Integer, Object>)parameterMap);
                    if (swingRet.size() > 0 && assetRet.size() > 0) {
                        double swingAmount = Double.parseDouble(swingRet.get(0).get("AMOUNT").toString());
                        double charge = Double.parseDouble(swingRet.get(0).get("CHARGE").toString());
                        String posName = swingRet.get(0).get("POSNAME").toString();
                        double balance = Double.parseDouble(assetRet.get(0).get("BALANCE").toString());
                        parameterMap.clear();
                        parameterMap.put(1, swingAmount);
                        parameterMap.put(2, balance + charge);
                        parameterMap.put(3, posName);
                        parameterMap.put(4, salemanUUID);
                        PosDbManager.executeUpdate("insert into assetflowtb(time, type, amount, balance, remark, salemanuuid)\n" +
                                        "VALUES(NOW(), '结算到账',?, ?, CONCAT(?,'结算款'), ?);\n"
                                , (HashMap<Integer, Object>)parameterMap);
                        parameterMap.clear();
                        parameterMap.put(1, -charge);
                        parameterMap.put(2, balance);
                        parameterMap.put(3, posName);
                        parameterMap.put(4, salemanUUID);
                        PosDbManager.executeUpdate("insert into assetflowtb(time, type, amount, balance, remark, salemanuuid)\n" +
                                        "VALUES(NOW(), '刷卡扣费',?, ?, CONCAT(?,'费率扣款'), ?);"
                                , (HashMap<Integer, Object>)parameterMap);
                    }
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