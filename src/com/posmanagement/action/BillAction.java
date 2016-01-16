package com.posmanagement.action;

import com.posmanagement.policy.SwingCardPolicy;
import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.UUIDUtils;
import com.posmanagement.webui.BillUI;
import com.posmanagement.webui.WebUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BillAction extends AjaxActionSupport {
    public final static String BILLMANAGER = "billManager";

    private String billList;
    private String cardno;
    private String bankName;
    private String billDate;
    private String billamount;
    private String status;
    private String billNO;

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

    public String getBillList() {
        return billList;
    }

    public void setBillNO(String _billNO) {
        billNO = _billNO;
    }

    public String Init() throws Exception {
        billList = new BillUI(super.getUserID()).generateBillTable("");
        getRequest().setAttribute("pagecount", (billList.split("<tr").length-1)/WebUI.DEFAULTITEMPERPAGE+1);
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
                sqlString ="update billtb a inner join cardtb b on  a.cardno=b.cardno set a.billamount=?,a.canuseamount=b.creditamount-? where a.UUID=? ";
            } else {
                return "";
            }
            if (PosDbManager.executeUpdate(sqlString,(HashMap<Integer, Object>)para)) {
                    billList = new BillUI(super.getUserID()).generateBillTable("");
                map.put("billList", billList);
                map.put("successMessage", getText("BillAction.InfoSuccess"));
            }
        }
        catch (Exception e) {
            map.put("errorMessage", getText("BillAction.InfoError"));
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
            if (null != status ){
                para.put(1,status);
                sqlString="update billtb set status=? where UUID=?";
            }
            else {
                return "";
            }
            if (!generateSwingCard()) {
                return AjaxActionComplete();
            }
            para.put(2,billNO);
            if (PosDbManager.executeUpdate(sqlString,(HashMap<Integer, Object>)para))
                map.put("successMessage",getText("BillAction.InfoSuccess") );
        }
        catch (Exception e) {
            map.put("errorMessage", getText("BillAction.InfoError"));
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
            sqlString = "insert into billtb (uuid,bankuuid,cardno,billdate,billamount,lastrepaymentdate) " +
                    "select '" + UUIDUtils.generaterUUID() + "', bankuuid,cardno,'" + (new SimpleDateFormat("yyyy-MM-dd")).format(billd) + "'," +
                    "creditamount,date_add('" +
                    (new SimpleDateFormat("yyyy-MM-dd")).format(billd) + "',INTERVAL billafterdate DAY ) from cardtb a " +
                    " where a.status='enable'  and billdate=" + day + wherestr + " and  not exists " +
                    "(select 1 from billtb b where a.cardno=b.cardno and b.billdate='" + (new SimpleDateFormat("yyyy-MM-dd")).format(billd) + "')";
        }
        else {
            Calendar cal = Calendar.getInstance(); //cal.getTime()
            int day = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH)+1;
            int year = cal.get(Calendar.YEAR);
            sqlString = "insert into billtb (uuid,bankuuid,cardno,billdate,billamount,lastrepaymentdate) " +
                    "select '" + UUIDUtils.generaterUUID() + "', bankuuid,cardno,concat('" + String.valueOf(year)+
                    "','-','"+String.valueOf(month)+"','-',billdate),creditamount,date_add("+
            "concat('" + String.valueOf(year)+"','-','"+String.valueOf(month)+"','-',billdate),INTERVAL billafterdate DAY ) from cardtb a " +
                    " where a.status='enable' " +  wherestr + " and  not exists " +
                    "(select 1 from billtb b where a.cardno=b.cardno and b.billdate=concat('" + String.valueOf(year)+"','-','"+String.valueOf(month)+"','-',billdate))";
        }
        System.out.println(sqlString);
        try {
            if (PosDbManager.executeUpdate(sqlString))
                map.put("billList",  new BillUI(super.getUserID()).generateBillTable(""));
        } catch (Exception e) {
            map.put("errorMessage", getText("AssetAction.InfoError"));
            e.printStackTrace();
        }
        return AjaxActionComplete(map);
    }


    public String FetchBillList(){
        String wherestr = " where 1=1 ";
        Map map = new HashMap();
        int i = 0;
        if (null!=getParameter("cardno") && (!getParameter("cardno").toString().trim().equals(""))){
            wherestr += "and cardtb.cardno like '%"+getParameter("cardno")+"%'";
        }
        if (null!=getParameter("bankname")&& (!getParameter("bankname").toString().trim().equals(""))) {
            wherestr += "and banktb.name like '%"+getParameter("bankname")+"%'";
        }
        if (null!=getParameter("salesman")&& (!getParameter("salesman").toString().trim().equals(""))){
            wherestr += "and userinfo.unick  like '%"+getParameter("salesman")+"%'";
        }
        if (null!=getParameter("thedate")&& (!getParameter("thedate").toString().trim().equals(""))) {
            wherestr += "and billdate  like '%"+getParameter("thedate")+"%'";
        }
//        if (null!=getParameter("billstatus")&& (!getParameter("billstatus").toString().trim().equals(""))) {
//            wherestr += "and   billtb.status "+(getParameter("billstatus").toString().equals("0")?"=0":"<>0")+" ";
//        }

        try {
            ArrayList<HashMap<String, Object>> rect = PosDbManager.executeSql("select count(*) as cnt from   " +
                            "billtb " +
                            "LEFT JOIN repaytb ON CONVERT(repaytb.repayyear, SIGNED)= CONVERT(SUBSTR(billtb.lastrepaymentdate,1,4), SIGNED) AND convert(repaytb.repaymonth, SIGNED)= convert(SUBSTR(billtb.lastrepaymentdate,6,2), SIGNED) " +
                            "INNER JOIN banktb ON banktb.uuid = billtb.bankuuid  " +
                            "inner join cardtb on cardtb.cardno=billtb.cardno "+
                            "inner  JOIN userinfo ON userinfo.uid = cardtb.salesmanuuid  " +
                            wherestr+
                            " GROUP BY " +
                            "billtb.cardno, " +
                            "SUBSTR(billtb.lastrepaymentdate,1,4), " +
                            "SUBSTR(billtb.lastrepaymentdate,6,2) " +
                            "ORDER BY " +
                            "billtb.billdate DESC  " );
            if (rect.size()<=0)
                map.put("pagecount",0);
            map.put("pagecount",Integer.parseInt(rect.get(0).get("CNT").toString())/ WebUI.DEFAULTITEMPERPAGE+1);
            int curr = Integer.parseInt(null==getParameter("currpage")?"1":getParameter("currpage").toString());
            billList = new BillUI(super.getUserID()).generateBillTable(wherestr+" limit "+String.valueOf((curr-1)*WebUI.DEFAULTITEMPERPAGE)+","+WebUI.DEFAULTITEMPERPAGE);
            map.put("billList",billList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxActionComplete(map);
    }

    private boolean generateSwingCard() throws Exception {
        Map map = new HashMap();
        if (billNO == null || billNO.length() <= 0) {
            return false;
        }

        SwingCardPolicy swingCardPolicy = new SwingCardPolicy("");
        SwingCardPolicy.SwingList swingList = swingCardPolicy.generateSwingList(billNO);
        if (swingList == null) {
            return false;
        }

        for (int index = 0; index < swingList.swingCardList.size(); ++index) {
            Map parametMap = new HashMap();
            parametMap.put(1, swingList.billYear);
            parametMap.put(2, swingList.billMonth);
            parametMap.put(3, swingList.cardNO);
            parametMap.put(4, swingList.swingCardList.get(index).money);
            parametMap.put(5, swingList.swingCardList.get(index).swingDate + "/" + swingList.swingCardList.get(index).swingTime);
            if (swingList.swingCardList.get(index).posUUID == null)
                swingList.swingCardList.get(index).posUUID = new String();
            parametMap.put(6, swingList.swingCardList.get(index).posUUID);
            parametMap.put(7, swingList.swingCardList.get(index).ruleUUID);
            PosDbManager.executeUpdate("insert into swingcard(billyear,billmonth,cardno,amount,sdatetm,posuuid,ruleuuid) " +
                            "values(?,?,?,?,?,?,?)",
                    (HashMap<Integer, Object>)parametMap);
        }

        for (int index = 0; index < swingList.repayList.size(); ++index) {
            Map parametMap = new HashMap();
            parametMap.put(1, swingList.repayYear);
            parametMap.put(2, swingList.repayMonth);
            parametMap.put(3, swingList.repayList.get(index).money);
            parametMap.put(4, swingList.repayList.get(index).repayDate);
            parametMap.put(5, swingList.cardNO);
            PosDbManager.executeUpdate("insert into repaytb(repayyear,repaymonth,trademoney,thedate,cardno) " +
                            "values(?,?,?,?,?)",
                    (HashMap<Integer, Object>)parametMap);
        }

        return true;
    }
}
