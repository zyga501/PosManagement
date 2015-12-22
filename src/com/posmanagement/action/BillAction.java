package com.posmanagement.action;

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

    public String Init() throws Exception {
        billList = new BillList().generateHTMLString();
        return BILLMANAGER;
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
        String sqlString= "insert into billtb (bankname,cardno,billdate,lastrepaymentdate) " +
                "select bankname,cardno,'" + (new SimpleDateFormat("yyyy-MM-dd")).format(billd)+"',date_add('"+
        (new SimpleDateFormat("yyyy-MM-dd")).format(billd)+"',INTERVAL lastrepaymentdate DAY ) from cardtb a where status='enable' and billdate=" + day + wherestr+" and  not exists " +
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
}
