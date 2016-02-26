package com.posmanagement.action;

import com.posmanagement.policy.SwingCardPolicy;
import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.SQLUtils;
import com.posmanagement.utils.StringUtils;
import com.posmanagement.utils.UUIDUtils;
import com.posmanagement.webui.BillUI;
import com.posmanagement.webui.WebUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BillAction extends AjaxActionSupport {
    public final static String BILLMANAGER = "billManager";
    public final static String SWINGREPAYPAGE = "swingRepayPage";

    private String cardno;
    private String bankName;
    private String billDate;
    private String billamount;
    private String status;
    private String billNO;
    private String swingcardErrorMessage;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getBillamount() {
        return billamount;
    }

    public void setBillamount(String billamount) {
        this.billamount = billamount;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getCardno() {
        return cardno;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public void setBillNO(String _billNO) {
        billNO = _billNO;
    }

    public String Init() throws Exception {
        getRequest().setAttribute("pagecount", (new BillUI(super.getUserID()).fetchBillPageCount() + WebUI.DEFAULTITEMPERPAGE -1)/WebUI.DEFAULTITEMPERPAGE);
        return BILLMANAGER;
    }
    
    public String modifyBill() {
        Map map = new HashMap();
        Map para = new HashMap();
        String sqlString = "";
        try {
            if (null != billamount) {
                Float.parseFloat(billamount);
                para.put(1, billamount);
                para.put(2, billamount);
                para.put(3,billNO);
                sqlString ="update billtb a inner join cardtb b on  a.cardno=b.cardno set a.billamount=?,a.canuseamount=b.creditamount-? where a.status<>'enable' and  a.UUID=? ";
            } else {
                return "";
            }
            if (PosDbManager.executeUpdate(sqlString,(HashMap<Integer, Object>)para)) {
                map.put("billList", new BillUI(super.getUserID()).generateBillTable(1));
                map.put("successMessage", getText("global.actionSuccess"));
            }
        }
        catch (Exception e) {
            map.put("errorMessage", getText("global.actionFailed"));
            e.printStackTrace();
            return AjaxActionComplete(map);
        }
        return AjaxActionComplete(map);
    }


    public String editBill(){ 
        Map map = new HashMap();
        Map para = new HashMap();


        String sqlString="";
        try {

            ArrayList<HashMap<String, Object>> assetRet = PosDbManager.executeSql("select * from assettb where salemanuuid='"+getUserID()+"'");
            if (assetRet.size() <= 0) {
                map.put("errorMessage", "操作失败!资产信息不存在");
                return AjaxActionComplete(map);
            }
            if (null != status ){
                para.put(1,status);
                sqlString="update billtb set status=? where UUID=?";
            }
            else {
                return "";
            }


            if (!generateSwingCard()) {
                map.put("errorMessage", getText("global.actionFailed") + swingcardErrorMessage);
                return AjaxActionComplete(map);
            }
            para.put(2,billNO);
            if (PosDbManager.executeUpdate(sqlString,(HashMap<Integer, Object>)para))
                map.put("successMessage",getText("global.actionSuccess") );
        }
        catch (Exception e) {
            map.put("errorMessage", getText("global.actionFailed")+ swingcardErrorMessage);
            e.printStackTrace();
        }
        return AjaxActionComplete(map);
    }

    public String makeBill(){
        Map map = new HashMap();
        String wherestr ="";
        if (null!=bankName && (!bankName.equals("")))
            wherestr = "  and bankuuid= '"+bankName+"'";
        else  if (null!=cardno && (!cardno.equals("")))
            wherestr = "  and cardno= '"+cardno+"'";
        String sqlString = "";
        if ((null!=billDate)&&(!billDate.trim().equals(""))) {
            Date billd = null;
            try {
                billd = (new SimpleDateFormat("yyyy-MM-dd")).parse(billDate);
            } catch (ParseException e) {
                map.put("errorMessage", getText("AssetAction.InfoError"));
                e.printStackTrace();
                return AjaxActionComplete(map);
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(billd);
            int day = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH)+1;
            int year = cal.get(Calendar.YEAR);
            sqlString = "insert into billtb (uuid,bankuuid,cardno,billdate,billamount,lastrepaymentdate,salemanuuid) " +
                    "select '" + UUIDUtils.generaterUUID() + "', bankuuid,cardno,'" + (new SimpleDateFormat("yyyy-MM-dd")).format(billd) + "'," +
                    "creditamount,date_add('" +
                    (new SimpleDateFormat("yyyy-MM-dd")).format(billd) + "',INTERVAL billafterdate DAY ),'"+getUserID()+"' from cardtb a " +
                    " where a.status='enable'  and billdate=" + day + wherestr + " and a.salemanuuid='"+getUserID()+"' and  not exists " +
                    "(select 1 from billtb b where a.cardno=b.cardno and b.billdate='" + (new SimpleDateFormat("yyyy-MM-dd")).format(billd) + "')";
        }
        else {
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH)+1;
            int year = cal.get(Calendar.YEAR);
            sqlString = "insert into billtb (uuid,bankuuid,cardno,billdate,billamount,lastrepaymentdate,salemanuuid) " +
                    "select uuid(), bankuuid,cardno,concat('" + String.valueOf(year)+
                    "','-','"+String.valueOf(month)+"','-',billdate),creditamount,date_add("+
            "concat('" + String.valueOf(year)+"','-','"+String.valueOf(month)+"','-',billdate),INTERVAL billafterdate DAY ),'"+getUserID()+"' from cardtb a " +
                    " where a.status='enable' and a.salemanuuid='"+getUserID()+"' " +  wherestr + " and  not exists " +
                    "(select 1 from billtb b where a.cardno=b.cardno and b.billdate=concat('" + String.valueOf(year)+"','-','"+String.valueOf(month)+"','-',billdate))";
        }
        System.out.println(sqlString);
        try {
            if (PosDbManager.executeUpdate(sqlString))
                map.put("billList",  new BillUI(super.getUserID()).generateBillTable(1));
        } catch (Exception e) {
            map.put("errorMessage", getText("AssetAction.InfoError"));
            e.printStackTrace();
        }
        return AjaxActionComplete(map);
    }


    public String FetchBillList() throws Exception {
        ArrayList<SQLUtils.WhereCondition> uiConditions = new ArrayList<SQLUtils.WhereCondition>() {
            {
                if (!StringUtils.convertNullableString(getParameter("cardno")).trim().equals(""))
                add(new SQLUtils.WhereCondition("billtb.cardno", "like",
                        SQLUtils.ConvertToSqlString("%" + getParameter("cardno") + "%"), !StringUtils.convertNullableString(getParameter("cardno")).trim().isEmpty()));
                if (!StringUtils.convertNullableString(getParameter("bankname")).trim().equals(""))
                add(new SQLUtils.WhereCondition("banktb.name", "like",
                        SQLUtils.ConvertToSqlString("%" + getParameter("bankname") + "%"), !StringUtils.convertNullableString(getParameter("bankname")).trim().isEmpty()));
                if (!StringUtils.convertNullableString(getParameter("saleman")).trim().equals(""))
                add(new SQLUtils.WhereCondition("userinfo.unick", "like",
                        SQLUtils.ConvertToSqlString("%" + getParameter("saleman") + "%"), !StringUtils.convertNullableString(getParameter("saleman")).trim().isEmpty()));
                if (!StringUtils.convertNullableString(getParameter("billdate")).trim().equals(""))
                add(new SQLUtils.WhereCondition("billtb.billdate", "like",
                        SQLUtils.ConvertToSqlString("%" + getParameter("billdate") + "%"), !StringUtils.convertNullableString(getParameter("billdate")).trim().isEmpty()));

                if (StringUtils.convertNullableString(getParameter("billstatus")).trim().equals("finished")){
                    add(new  SQLUtils.WhereCondition("billtb.status", "=","'enable' "));
                }else if (StringUtils.convertNullableString(getParameter("billstatus")).trim().equals("unfinished")) {
                    add(new  SQLUtils.WhereCondition("billtb.status", "<>","'enable' "));
                }
                else {
                }
            }
        };

        Map map = new HashMap();
        BillUI billUI = new BillUI(super.getUserID());
        billUI.setUiConditions(uiConditions);
        map.put("pagecount", billUI.fetchBillPageCount());
        int curr = Integer.parseInt(null==getParameter("currpage")?"1":getParameter("currpage").toString());
        map.put("billList",billUI.generateBillTable(curr));

        return AjaxActionComplete(map);
    }

    public String FetchTodayBillList() throws Exception{
        Map map = new HashMap();
        BillUI billUI = new BillUI(super.getUserID());
        map.put("billList",billUI.generateTodayBillTable());
        return AjaxActionComplete(map);
    }

    private boolean generateSwingCard() throws Exception {
        Map map = new HashMap();
        if (billNO == null || billNO.length() <= 0) {
            return false;
        }

        SwingCardPolicy swingCardPolicy = new SwingCardPolicy(super.getUserID());
        SwingCardPolicy.SwingList swingList = swingCardPolicy.generateSwingList(billNO);
        if (swingList == null) {
            swingcardErrorMessage = swingCardPolicy.getLastError();
            return false;
        }

        for (int index = 0; index < swingList.swingCardList.size(); ++index) {
            Map parametMap = new HashMap();
            parametMap.put(1, billNO);
            parametMap.put(2, swingList.cardNO);
            parametMap.put(3, swingList.swingCardList.get(index).money);
            parametMap.put(4, swingList.swingCardList.get(index).swingDate + "/" + swingList.swingCardList.get(index).swingTime);
            if (swingList.swingCardList.get(index).posUUID == null)
                swingList.swingCardList.get(index).posUUID = new String();
            parametMap.put(5, swingList.swingCardList.get(index).posUUID);
            parametMap.put(6, swingList.swingCardList.get(index).ruleUUID);
            PosDbManager.executeUpdate("insert into swingcard(billuuid,cardno,amount,sdatetm,posuuid,ruleuuid) " +
                            "values(?,?,?,?,?,?)",
                    (HashMap<Integer, Object>)parametMap);
        }

        for (int index = 0; index < swingList.repayList.size(); ++index) {
            Map parametMap = new HashMap();
            parametMap.put(1, billNO);
            parametMap.put(2, swingList.repayList.get(index).money);
            parametMap.put(3, swingList.repayList.get(index).repayDate);
            parametMap.put(4, swingList.cardNO);
            PosDbManager.executeUpdate("insert into repaytb(billuuid,trademoney,thedate,cardno) " +
                            "values(?,?,?,?)",
                    (HashMap<Integer, Object>)parametMap);
        }

        return true;
    }

    public String SwingRepay(){
        try {
            getRequest().setAttribute("pagecount", (new BillUI(super.getUserID()).fetchSwingRepayPageCount()+WebUI.DEFAULTITEMPERPAGE-1)/WebUI.DEFAULTITEMPERPAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SWINGREPAYPAGE;
    }

    public  String FetchSwingRepayList(){
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
        BillUI billUI = new BillUI(super.getUserID());
        billUI.setUiConditions(uiConditions);
        try {
            map.put("pagecount", billUI.fetchSwingRepayPageCount());
            int curr = Integer.parseInt(null==getParameter("currpage")?"1":getParameter("currpage").toString());
            map.put("swingrepaySummary",billUI.generateSwingRepay(curr));
        } catch (Exception e) {
            e.printStackTrace();
        }
       return AjaxActionComplete(map);
    }
}
