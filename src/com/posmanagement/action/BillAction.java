package com.posmanagement.action;

import com.posmanagement.policy.SwingCardPolicy;
import com.posmanagement.utils.PosDbManager;
import com.posmanagement.webui.BillList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        billList = new BillList().generateHTMLString();
        return BILLMANAGER;
    }

    public String editBill(){
        if (null == cardno) {
            return AjaxActionComplete();
        }
        Map map = new HashMap();
        Map para = new HashMap();
        String sqlString="";
        try {
            if (null != billamount ){
            Float.parseFloat(billamount);
            para.put(1,billamount);
            sqlString="update billtb set billamount=? where cardno=?";
        }
        else if (null != status ){
            para.put(1,status);
            sqlString="update billtb set status=? where cardno=?";
        }
        else {
            return "";
        }

        if (!generateSwingCard()) {
            return AjaxActionComplete();
        }

        para.put(2,cardno);
        if (PosDbManager.executeUpdate(sqlString,(HashMap<Integer, Object>)para))
                map.put("successMessage",getText("BillAction.InfoSuccess") );
        }
        catch (Exception e) {
            map.put("errorMessage", getText("BillAction.InfoError"));
            e.printStackTrace();
        }
        return AjaxActionComplete(map);
    }

    public void makeBill(){
        if (null==billDate) return ;
        Map map = new HashMap();
        Date billd= null ;
        try {
            billd = (new SimpleDateFormat("yyyy-MM-dd")).parse(billDate);
        } catch (ParseException e) {
            map.put("errorMessage", getText("AssetAction.InfoError"));
            e.printStackTrace();
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(billd);
        int day=cal.get(Calendar.DATE);
        int month=cal.get(Calendar.MONTH);
        int year=cal.get(Calendar.YEAR);
        String wherestr ="";
        if (null!=bankName && (!bankName.equals("")))
            wherestr = "  and bankname= '"+bankName+"'";
        else  if (null!=cardno && (!cardno.equals("")))
            wherestr = "  and cardno= '"+cardno+"'";
        String sqlString= "insert into billtb (bankname,cardno,billdate,billamount,lastrepaymentdate) " +
                "select bankname,cardno,'" + (new SimpleDateFormat("yyyy-MM-dd")).format(billd)+"',creditamount,date_add('"+
        (new SimpleDateFormat("yyyy-MM-dd")).format(billd)+"',INTERVAL billafterdate DAY ) from cardtb a where status='enable' and billdate=" + day + wherestr+" and  not exists " +
                "(select 1 from billtb b where a.cardno=b.cardno and b.billdate='" + (new SimpleDateFormat("yyyy-MM-dd")).format(billd)+"')";
        System.out.println(sqlString);
        try {
            if (PosDbManager.executeUpdate(sqlString))
            map.put("billList",  new BillList().generateHTMLString());
        } catch (Exception e) {
            map.put("errorMessage", getText("AssetAction.InfoError"));
            e.printStackTrace();
        }
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
            parametMap.put(4, swingList.cardMaster);
            parametMap.put(5, swingList.swingCardList.get(index).money);
            parametMap.put(6, swingList.swingCardList.get(index).swingDate);
            parametMap.put(7, swingList.swingCardList.get(index).ruleUUID);
            PosDbManager.executeUpdate("insert into swingcard(billyear,billmonth,cardno,cardmaster,amount,sdatetm,ruleUUID) " +
                            "values(?,?,?,?,?,?,?)",
                    (HashMap<Integer, Object>)parametMap);
        }

        return true;
    }
}
