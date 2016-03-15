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
    private String cardno;
    private String billUUID;

    public String getSwingCardDetail() {
        return swingCardDetail;
    }

    public String getCardNO() { return cardno; }

    public String getBillUUID() { return billUUID; }

    public String Init() throws Exception {
        getRequest().setAttribute("pagecount", (new SwingCardUI(super.getUserID()).fetchSwingCardPageCount()+WebUI.DEFAULTITEMPERPAGE-1)/WebUI.DEFAULTITEMPERPAGE);
        return SWINGCARDMANAGER;
    }

    public String InitDetail() throws Exception {
        if (StringUtils.convertNullableString(getParameter("cardno")).equals("")){
            FetchDetail();
        }else {
            swingCardDetail = new SwingCardUI(super.getUserID()).generateDetail(StringUtils.convertNullableString(getParameter("cardno")),
                    StringUtils.convertNullableString(getParameter("billUUID")));
            cardno = StringUtils.convertNullableString(getParameter("cardno"));
            billUUID = StringUtils.convertNullableString(getParameter("billUUID"));
        }
        return SWINGCARDDETAIL;
    }
    public String FetchDetail() throws Exception {
        ArrayList<SQLUtils.WhereCondition> uiConditions = new ArrayList<SQLUtils.WhereCondition>() {
            {
                if (!StringUtils.convertNullableString(getParameter("cardno"),"").equals(""))
                    add(new SQLUtils.WhereCondition("cardtb.cardno", "like",
                            SQLUtils.ConvertToSqlString("%" + getParameter("cardno") + "%"), !StringUtils.convertNullableString(getParameter("cardno")).trim().isEmpty()));
                if (!StringUtils.convertNullableString(getParameter("cardmaster"),"").equals(""))
                    add(new SQLUtils.WhereCondition("cardmaster", "like",
                            SQLUtils.ConvertToSqlString("%" + getParameter("cardmaster") + "%"), !StringUtils.convertNullableString(getParameter("cardmaster")).trim().isEmpty()));
                if (StringUtils.convertNullableString(getParameter("SWINGSTATUS"),"").equals("finished"))
                    add(new SQLUtils.WhereCondition("ifnull(SWINGSTATUS,'')", "=","'enable'"));
                else if (StringUtils.convertNullableString(getParameter("SWINGSTATUS"),"").equals("unfinished"))
                    add(new SQLUtils.WhereCondition("ifnull(SWINGSTATUS,'')", "<>","'enable'"));
            }
        };
        Map map = new HashMap();
        SwingCardUI swingCardUI = new SwingCardUI(super.getUserID());
        swingCardUI.setUiConditions(uiConditions);
        map.put("pagecount", swingCardUI.fetchSwingDetailPageCount());
        int curr = Integer.parseInt(null==getParameter("currpage")?"1":getParameter("currpage").toString());
        map.put("swingCardDetail",swingCardUI.generateDetail(curr));

        return AjaxActionComplete(map);
    }

    public String EditDetail() throws Exception{
        Map map =new HashMap();
        if (null==getParameter("status") || null==getParameter("swingId")) {
            map.put("errorMessage", getText("BillAction.InfoErro"));
        }
        else {
            Map parameterMap =new HashMap();
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
            parameterMap.put(1, salemanUUID);
            ArrayList<HashMap<String, Object>> assetRet = PosDbManager.executeSql("select * from assettb where salemanuuid=?", (HashMap<Integer, Object>)parameterMap);
            if (assetRet.size() <= 0) {
                map.put("errorMessage", "操作失败!资产信息不存在");
                return AjaxActionComplete(map);
            }

            if ((null!=getParameter("swingId"))&&(null!=getParameter("posUUID"))){
                parameterMap.clear();
                parameterMap.put(1, getParameter("amount"));
                parameterMap.put(2, getParameter("posUUID"));
                parameterMap.put(3, getParameter("swingId"));
                PosDbManager.executeUpdate("update swingcard set amount=? , posuuid=? where id=?",(HashMap<Integer, Object>)parameterMap);
            }

            parameterMap.clear();
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
                   // map.put("swingCardDetail", new SwingCardUI(super.getUserID()).generateDetail(getParameter("cardno").toString(), getParameter("billUUID").toString()));
                    parameterMap.clear();
                    parameterMap.put(1, getParameter("swingId"));
                    ArrayList<HashMap<String, Object>> swingRet = PosDbManager.executeSql("SELECT amount, charge, postb.posname from swingcard LEFT JOIN postb on swingcard.posuuid = postb.uuid where id=?", (HashMap<Integer, Object>)parameterMap);
                    parameterMap.clear();
                    parameterMap.put(1, salemanUUID);
                    assetRet = PosDbManager.executeSql("SELECT balance from assettb where salemanuuid=?", (HashMap<Integer, Object>)parameterMap);
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

    public String FetchSwingInfo() throws Exception {
        Map map = new HashMap();
        Map para = new HashMap();
        para.put(1,getParameter("swingid"));
        ArrayList<HashMap<String, Object>> Ret = PosDbManager.executeSql("select banktb.name,cardtb.cardno,cardtb.cardmaster,swingcard.sdatetm,swingcard.amount," +
                "swingcard.posuuid,swingcard.amount*ratetb.rate/100 as charge from swingcard,cardtb,banktb,postb,ratetb where " +
                " postb.rateuuid=ratetb.uuid and postb.uuid=swingcard.posuuid and banktb.uuid=cardtb.bankuuid" +
                " and swingcard.cardno=cardtb.cardno and swingcard.id=?", (HashMap<Integer, Object>) para);
        if (Ret.size()>0) {
            map.put("bankname", Ret.get(0).get("NAME"));
            map.put("cardno", Ret.get(0).get("CARDNO"));
            map.put("cardmaster", Ret.get(0).get("CARDMASTER"));
            map.put("thedate", Ret.get(0).get("SDATETM"));
            map.put("amount", Ret.get(0).get("AMOUNT"));
            map.put("charge", Ret.get(0).get("CHARGE"));
            map.put("posuuid", Ret.get(0).get("POSUUID"));
        }
        return AjaxActionComplete(map);
    }

    public String FetchSwingList() throws Exception {
        ArrayList<SQLUtils.WhereCondition> uiConditions = new ArrayList<SQLUtils.WhereCondition>() {
            {
                if (!StringUtils.convertNullableString(getParameter("cardno"),"").equals(""))
                add(new SQLUtils.WhereCondition("cardtb.cardno", "like",
                        SQLUtils.ConvertToSqlString("%" + getParameter("cardno") + "%"), !StringUtils.convertNullableString(getParameter("cardno")).trim().isEmpty()));
                if (!StringUtils.convertNullableString(getParameter("bankname"),"").equals(""))
                add(new SQLUtils.WhereCondition("banktb.name", "like",
                        SQLUtils.ConvertToSqlString("%" + getParameter("bankname") + "%"), !StringUtils.convertNullableString(getParameter("bankname")).trim().isEmpty()));
                if (!StringUtils.convertNullableString(getParameter("cardmaster"),"").equals(""))
                add(new SQLUtils.WhereCondition("cardmaster", "like",
                        SQLUtils.ConvertToSqlString("%" + getParameter("cardmaster") + "%"), !StringUtils.convertNullableString(getParameter("cardmaster")).trim().isEmpty()));
                if (!StringUtils.convertNullableString(getParameter("thedate"),"").equals(""))
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

    public String SwingCardDirectly() {
        Map map = new HashMap();
        try {
            Double swingMoney = Double.parseDouble(getParameter("swingMoney").toString());
            if (swingMoney < 0) {
                throw new IllegalArgumentException();
            }
            Map parametMap = new HashMap();
            String salemanUUID = super.getUserID();
            if (!UserUtils.issaleman(salemanUUID)) {
                parametMap.clear();
                parametMap.put(1, salemanUUID);
                ArrayList<HashMap<String, Object>> salemanRet = PosDbManager.executeSql("select salemanuuid from tellertb where uid=?", (HashMap<Integer, Object>)parametMap);
                if (salemanRet.size() > 0) {
                    salemanUUID = salemanRet.get(0).get("SALEMANUUID").toString();
                }
            }
            parametMap.clear();
            parametMap.put(1, swingMoney);
            parametMap.put(2, getParameter("posUUID"));
            parametMap.put(3, salemanUUID);
            PosDbManager.executeUpdate("insert into swingcard(amount,realsdatetm,posuuid,userid,swingstatus) " +
                            "values(?,NOW(),?,?,'enable')",
                    (HashMap<Integer, Object>)parametMap);
            parametMap.clear();
            parametMap.put(1, swingMoney);
            parametMap.put(2, salemanUUID);
            PosDbManager.executeUpdate("update assettb set balance=balance-? where uuid=?", (HashMap<Integer, Object>)parametMap);
            parametMap.clear();
            parametMap.put(1, salemanUUID);
            ArrayList<HashMap<String, Object>> assetRet = PosDbManager.executeSql("SELECT balance from assettb where salemanuuid=?", (HashMap<Integer, Object>)parametMap);
            double balance = Double.parseDouble(assetRet.get(0).get("BALANCE").toString());
            parametMap.put(1, swingMoney);
            parametMap.put(2, balance);
            parametMap.put(3, salemanUUID);
            PosDbManager.executeUpdate("insert into assetflowtb(time, type, amount, balance, remark, salemanuuid)\n" +
                            "VALUES(NOW(), '结算到账',?, ?, '直接刷卡', ?);\n"
                    , (HashMap<Integer, Object>)parametMap);
        }
        catch (Exception exception) {
            map.put("errorMessage", exception.getMessage());
        }
        return AjaxActionComplete(map);
    }
}