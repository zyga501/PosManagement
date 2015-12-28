package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.webui.BillList;
import com.posmanagement.webui.PosList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class POSAction extends AjaxActionSupport {
    public final static String POSMANAGER = "posManager";

    private String posList;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String Init() throws Exception {
        posList = new PosList().generateHTMLString();
        return POSMANAGER;
    }
/*
    public String editPos(){
        if ( null==cardno) return "";
        Map map = new HashMap();
        Map para = new HashMap();
        String sqlString="";
        try {
        if (null!=billamount ){
            Float.parseFloat(billamount);
            para.put(1,billamount);
            sqlString="update billtb set billamount=? where cardno=?";
        }
        else if (null!=status ){
            para.put(1,status);
            sqlString="update billtb set status=? where cardno=?";
        }
        else return "";
        para.put(2,cardno);
            if (PosDbManager.executeUpdate(sqlString,(HashMap<Integer, Object>)para))
                map.put("successMessage",getText("BillAction.InfoSuccess") );
        } catch (Exception e) {
            map.put("errorMessage", getText("BillAction.InfoError"));
            e.printStackTrace();
        }
        return AjaxActionComplete(map);
    }

    public void addPos(){
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
    }*/
}
