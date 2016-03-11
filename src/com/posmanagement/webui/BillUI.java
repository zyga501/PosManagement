package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.SQLUtils;
import com.posmanagement.utils.StringUtils;
import com.posmanagement.utils.UserUtils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BillUI extends WebUI {
    public BillUI(String userID) {
        userID_ = userID;
    }

    public String generateBillTable(int pageIndex) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchBillList(pageIndex);
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            double canUseMoney = ((long)(10 * (Double.valueOf(StringUtils.convertNullableString(dbRet.get(index).get("CANUSEAMOUNT"),"0")) +
                    Double.valueOf(StringUtils.convertNullableString(dbRet.get(index).get("REPAYAMOUNT"),"0")) -
                    Double.valueOf(StringUtils.convertNullableString(dbRet.get(index).get("SWINGAMOUNT"),"0"))))) / 10.0;
            double repayAmount = Double.valueOf(StringUtils.convertNullableString(dbRet.get(index).get("REPAYAMOUNT"),"0"));
            double remainAmount = Double.valueOf(StringUtils.convertNullableString(dbRet.get(index).get("BILLAMOUNT"),"0")) - repayAmount;
            UIContainer UI =new UIContainer("tr");
            UI.addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("BANKNAME")))
                    .addElement("td", StringUtils.formatCardNO(StringUtils.convertNullableString(dbRet.get(index).get("CARDNO"))))
                    .addElement("td", (new SimpleDateFormat("yyyy-MM")).format(Date.valueOf(dbRet.get(index).get("BILLDATE").toString())))
                    .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("LASTREPAYMENTDATE")))
                    .addElement(new UIContainer("td")
                            .addElement(new UIContainer("label",StringUtils.convertNullableString(dbRet.get(index).get("BILLAMOUNT")).equals("")?"0":dbRet.get(index).get("BILLAMOUNT").toString())
                                    .addAttribute("name","billamount")
                                    .addAttribute("datav",StringUtils.convertNullableString(dbRet.get(index).get("UUID")))
                                    .addAttribute("style","display:inline-block;")));
//                    .addElement("td", String.valueOf(canUseMoney))
//                    .addElement("td", String.valueOf(repayAmount))
//                    .addElement("td", String.valueOf(Double.max(remainAmount, 0.0)))
            if (UserUtils.isAdmin(userID_))
                UI.addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("SALEMAN")));
            UI.addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("SWINGCOUNT")).compareTo(StringUtils.convertNullableString(
                    dbRet.get(index).get("SWUNGCOUNT"))) == 0 &&
                            StringUtils.convertNullableString(dbRet.get(index).get("REPAYCOUNT")).compareTo(StringUtils.convertNullableString(
                                    dbRet.get(index).get("REPAYEDCOUNT"))) == 0 &&(!StringUtils.convertNullableString(dbRet.get(index).get("SWINGCOUNT")).equals("")) ?
                                    getText("bill.billfinished") : getText("bill.billunfinished"))
                    .addElement(new UIContainer("td").addElement(new UIContainer("input")
                            .addAttribute("class", dbRet.get(index).get("STATUS").equals("enable")?"btn btn-success radius":"btn btn-danger radius")
                            .addAttribute("type","button")
                            .addAttribute("title", dbRet.get(index).get("STATUS").equals("enable")?"已开启":"未开启")
                            .addAttribute("datav", StringUtils.convertNullableString(dbRet.get(index).get("UUID")))
                            .addAttribute("value", dbRet.get(index).get("STATUS").equals("enable")?"Y":"N")
                            .addAttribute("onclick", "clickBill(this,'" + StringUtils.convertNullableString(dbRet.get(index).get("UUID")) + "')")));
            htmlString +=UI.generateUI();
        }
        return htmlString;
    }

    public String generateTodayBillTable() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchBillList("curdate()");
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                    .addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addElement("td", String.valueOf(index+1))
                    .addElement("td", StringUtils.formatCardNO(StringUtils.convertNullableString(dbRet.get(index).get("CARDMASTER"))))
                    .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("BANKNAME")))
                    .addElement("td", StringUtils.formatCardNO(StringUtils.convertNullableString(dbRet.get(index).get("CARDNO"))));
        }
        return htmlString;
    }

    public String generateBillDetailList(int pageIndex) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchBillDetailList(pageIndex);
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            if (!(StringUtils.convertNullableString(dbRet.get(index).get("TRADEAMOUNT"),"0").equals("0")&&
                StringUtils.convertNullableString(dbRet.get(index).get("AMOUNT"),"0").equals("0")))
            htmlString += new UIContainer("li")
                    .addAttribute("class", "item")
                    .addAttribute("onclick","expandbill(this,'"+dbRet.get(index).get("BILLUUID")+"')")
                    .addElement(new UIContainer("h4")
                    .addElement(new UIContainer("i","&#xe6bf;")
                            .addAttribute("class","icon Hui-iconfont"))
                    .addElement(new UIContainer("fn",dbRet.get(index).get("CARDMASTER").toString()))
                    .addElement(new UIContainer("span","&nbsp;&nbsp;&nbsp;&nbsp;"))
                    .addElement(new UIContainer("card",dbRet.get(index).get("BANKNAME").toString()+" "+dbRet.get(index).get("CARDNO").toString()))
                    .addElement(new UIContainer("span","&nbsp;&nbsp;&nbsp;&nbsp;"))
                    .addElement(new UIContainer("repay","还"))
                    .addElement(new UIContainer("u",StringUtils.convertNullableString(dbRet.get(index).get("TRADEAMOUNT"),"0")))
                    .addElement(new UIContainer("swing","刷"))
                    .addElement(new UIContainer("u",StringUtils.convertNullableString(dbRet.get(index).get("AMOUNT"),"0")))
                    .addElement(new UIContainer("b","+")));
        }
        return htmlString;
    }


    public String generateSwingRepay(int pageIndex) throws Exception {
            ArrayList<HashMap<String, Object>> dbRet = fetchSwingRepaySummary(pageIndex);
            if (dbRet.size() <= 0)
                return new String("");

            String htmlString = "";
            for (int index = 0; index < dbRet.size(); ++index) {
                htmlString += new UIContainer("tr")
                        .addAttribute("class", "text-c odd")
                        .addAttribute("role", "row")
                        .addElement("td", dbRet.get(index).get("THEDATE").toString())
                        .addElement("td", StringUtils.formatCardNO(dbRet.get(index).get("CARDNO").toString()))
                        .addElement("td", dbRet.get(index).get("CARDMASTER").toString())
                        .addElement("td", dbRet.get(index).get("AMOUNT").toString())
                        .addElement("td", dbRet.get(index).get("PAYCHARGE").toString())
                        .addElement("td", dbRet.get(index).get("UNFINISHED").toString().equals("0")?
                                "<font color=#00ff00><i class=\"Hui-iconfont\">&#xe6a7;</i></font>" : "<font color=#ff0000><i class=\"Hui-iconfont\">&#xe6a6;</i></font>" )
                        .addElement(new UIContainer("td")
                                .addElement(
                                        new UIContainer("input")
                                                .addAttribute("type", "button")
                                                .addAttribute("value", "明细")
                                                .addAttribute("class", "btn radius")
                                                .addAttribute("onclick", "clickDetail('" + dbRet.get(index).get("CARDNO") +
                                                        "','" + dbRet.get(index).get("BILLUUID") + "')")
                                ).addElement("span", "完<b>"+StringUtils.convertNullableString(dbRet.get(index).get("FINISHED"))+
                                        "</b>,未<b>"+StringUtils.convertNullableString(dbRet.get(index).get("UNFINISHED"))+"</b>,共<b>"+
                                        StringUtils.convertNullableString(dbRet.get(index).get("TOTALCOUNT"))+"</b>笔")
                        );
            }
            return htmlString;
        }

    private ArrayList<HashMap<String, Object>> fetchBillList(int pageIndex) throws Exception {
        String whereSql = SQLUtils.BuildWhereCondition(uiConditions_);
        if (whereSql.isEmpty()) {
            whereSql = " where 1=1  " + whereSql;
        }
        else
            whereSql = " where  " + whereSql;

        String limitSql ="limit " + (pageIndex - 1) * DEFAULTITEMPERPAGE + "," + DEFAULTITEMPERPAGE;

        if (!UserUtils.isAdmin(userID_))
            whereSql += " and  (cardtb.salemanuuid in (select a.uid from salemantb a  where a.uid='"+userID_+"' )" +
                    " or cardtb.salemanuuid in(select salemanuuid from tellertb   where uid='"+userID_+"')) ";

        return PosDbManager.executeSql("SELECT  *,userinfo.unick SALEMAN,banktb.name bankname \n" +
                "from billtb inner join cardtb  on billtb.cardno=cardtb.cardno inner join userinfo on userinfo.uid = cardtb.salemanuuid \n" +
                " inner join banktb on banktb.uuid = billtb.bankuuid LEFT JOIN\n" +
                "(select billtb.uuid billuuid,sum(CASE WHEN swingcard.swingstatus='enable' then swingcard.amount else 0 END) swingamount,\n" +
                "Sum(swingcard.amount) AS amount, count(1) swingcount,sum(case when swingstatus='enable' then 1 else 0 END) swungcount,(count(1) - sum(case when swingstatus='enable' then 1 else 0 END))\n" +
                " unfinished  ,Sum(case when swingcard.amount*ratetb.rate/100>ratetb.MAXFEE and ratetb.maxfee>0 then ratetb.maxfee else swingcard.amount*ratetb.rate/100  end)  paycharge\n" +
                " from  swingcard,billtb,ratetb,postb where swingcard.billuuid = billtb.uuid and   postb.uuid = swingcard.posuuid and postb.rateuuid = ratetb.uuid \n" +
                "group by billtb.uuid) swingcardtotal on billtb.uuid=swingcardtotal.billuuid LEFT JOIN\n" +
                "(select  billtb.uuid billuuid,count(repaytb.id) repaycount,sum(CASE WHEN repaytb.tradestatus='enable' then 1 else 0 END) repayedcount,\n" +
                "sum(CASE WHEN repaytb.tradestatus='enable' then repaytb.trademoney else 0 END) repayamount from  repaytb,billtb  where repaytb.billuuid = billtb.uuid  group by billtb.uuid  ) repaytbtotal\n" +
                " on repaytbtotal.billuuid=billtb.uuid \n"+
                whereSql +
                "GROUP BY billtb.billdate, billtb.cardno \n" +
                "ORDER BY billtb.billdate desc\n" +
                limitSql);
    }


    private ArrayList<HashMap<String, Object>> fetchBillList( String datestr) throws Exception {
        String whereSql="";
        return PosDbManager.executeSql("SELECT  a.cardno,a.cardmaster,b.name bankname  \n" +
                "from banktb b ,cardtb a "+
                 "where a.status='enable' and a.salemanuuid='"+userID_+"' and a.bankuuid=b.uuid  and a.billdate=DAYOFMONTH("+datestr+") and  not exists " +
                "(select 1 from billtb c where c.cardno=a.cardno and c.billdate="+datestr+")" +
                "ORDER BY a.cardmaster desc\n"  );
    }

    public int fetchBillPageCount() throws Exception {
        String whereSql = SQLUtils.BuildWhereCondition(uiConditions_);
        if (whereSql.isEmpty()) {
            whereSql+= "  where 1=1   " ;
        }
        else
            whereSql=" where 1=1 and " +whereSql;
        if (!UserUtils.isAdmin(userID_)) {
            whereSql += "and billtb.salemanuuid='"+userID_+"'";
        }

        ArrayList<HashMap<String, Object>> resultMap =  PosDbManager.executeSql("SELECT count(*) CNT\n" +
           "FROM\n" +
                   "billtb\n" +
                   "INNER JOIN banktb ON banktb.uuid = billtb.bankuuid\n" +
                   "LEFT JOIN userinfo ON userinfo.uid = billtb.salemanuuid\n" +
                          whereSql +
                   "ORDER BY\n" +
                   "billtb.billdate DESC ");
        if (resultMap.size()<=0) {
            return 0;
        }
        return (Integer.parseInt(resultMap.get(0).get("CNT").toString())+WebUI.DEFAULTITEMPERPAGE-1)/ WebUI.DEFAULTITEMPERPAGE ;
    }

    public int fetchBillDetailPageCount() throws Exception {
        String whereSql = SQLUtils.BuildWhereCondition(uiConditions_);
        if (whereSql.isEmpty()) {
            whereSql+= "  where 1=1   " ;
        }
        else
            whereSql=" where 1=1 and " +whereSql;
        if (!UserUtils.isAdmin(userID_)) {
            whereSql += "and billtb.salemanuuid='"+userID_+"'";
        }

        ArrayList<HashMap<String, Object>> resultMap =  PosDbManager.executeSql("SELECT count( distinct billtb.uuid) CNT\n" +
                "FROM\n" +
                "billtb\n" +
                "INNER JOIN banktb ON banktb.uuid = billtb.bankuuid\n" +
                "INNER JOIN swingcard ON swingcard.billuuid = billtb.uuid\n" +
                "INNER JOIN repaytb ON repaytb.billuuid = billtb.uuid\n" +
                "LEFT JOIN cardtb ON cardtb.cardno = billtb.cardno\n" +
                whereSql +
                " ORDER BY\n" +
                " billtb.billdate DESC ");
        if (resultMap.size()<=0) {
            return 0;
        }
        return (Integer.parseInt(resultMap.get(0).get("CNT").toString())+WebUI.DEFAULTITEMPERPAGE-1)/ WebUI.DEFAULTITEMPERPAGE ;
    }


    public String wheresql1="";
    public String wheresql2="";
    private ArrayList<HashMap<String, Object>> fetchBillDetailList(int pageIndex) throws Exception {
        String whereSql =SQLUtils.BuildWhereCondition(uiConditions_);
        if (whereSql.isEmpty()) {
            whereSql = "  where 1=1   " ;
        }
        else
            whereSql = " where 1=1 and "+whereSql ;
        String limitSql ="limit " + (pageIndex - 1) * DEFAULTITEMPERPAGE + "," + DEFAULTITEMPERPAGE;

        if (!UserUtils.isAdmin(userID_))
            whereSql += "  and (cardtb.salemanuuid in (select a.uid from salemantb a  where a.uid='"+userID_+"' )" +
                    " or cardtb.salemanuuid in(select salemanuuid from tellertb   where uid='"+userID_+"')) ";

        return PosDbManager.executeSql("SELECT \n" +
                "billtb.cardno, \n" +
                "banktb.name bankname, \n" +
                "billtb.uuid billuuid, \n" +
                "(select Sum(swingcard.amount) from swingcard WHERE swingcard.billuuid=billtb.uuid "+wheresql1+")AS amount,\n"+
                "cardtb.cardmaster,\n"+
                "(select Sum(repaytb.trademoney) from repaytb WHERE repaytb.billuuid=billtb.uuid "+wheresql2+") AS tradeamount \n"+
                "FROM \n" +
                "cardtb  \n" +
                "INNER JOIN billtb ON   cardtb.cardno = billtb.cardno \n" +
                "INNER JOIN banktb ON   banktb.uuid = cardtb.bankuuid \n" +
                whereSql +
                "GROUP BY billtb.billdate, billtb.cardno \n"
                + limitSql);
    }

    public int fetchSwingRepayPageCount() throws Exception {
        String whereSql = SQLUtils.BuildWhereCondition(uiConditions_);
        if (whereSql.isEmpty()) {
            whereSql+= "  where 1=1   " ;
        }
        else
            whereSql=" where 1=1 and " +whereSql;
        if (!UserUtils.isAdmin(userID_)) {
            whereSql += " and  billtb.salemanuuid='"+userID_+"'";
        }
        ArrayList<HashMap<String, Object>> resultMap =  PosDbManager.executeSql("select sum(CNT) CNT from (SELECT count(*) CNT\n" +
                "FROM swingcard " +
                "INNER JOIN cardtb ON cardtb.cardno = swingcard.cardno \n" +
                        "INNER JOIN billtb ON swingcard.billuuid = billtb.uuid AND swingcard.cardno = billtb.cardno \n" +
                whereSql +
                " union all \n"+
                "SELECT count(*) CNT\n" +
                "FROM repaytb " +
                        "INNER JOIN cardtb ON cardtb.cardno = repaytb.cardno " +
                        "INNER JOIN billtb ON repaytb.billuuid = billtb.uuid AND repaytb.cardno = billtb.cardno " +
                whereSql+") a" );
        if (resultMap.size()<=0) {
            return 0;
        }
        return (Integer.parseInt(resultMap.get(0).get("CNT").toString())+WebUI.DEFAULTITEMPERPAGE-1)/ WebUI.DEFAULTITEMPERPAGE ;
    }

    private ArrayList<HashMap<String, Object>> fetchSwingRepaySummary(int pageIndex) throws Exception {
        String whereSql =SQLUtils.BuildWhereCondition(uiConditions_);
        if (whereSql.isEmpty()) {
            whereSql = "  where 1=1   " ;
        }
        else
            whereSql = " where 1=1 and "+whereSql ;
        String limitSql ="limit " + (pageIndex - 1) * DEFAULTITEMPERPAGE + "," + DEFAULTITEMPERPAGE;

        if (!UserUtils.isAdmin(userID_))
            whereSql += "  and (cardtb.salemanuuid in (select a.uid from salemantb a  where a.uid='"+userID_+"' )" +
                    " or cardtb.salemanuuid in(select salemanuuid from tellertb   where uid='"+userID_+"')) ";

        return PosDbManager.executeSql("select * from (SELECT \n" +
                "billtb.billdate as thedate, \n" +
                "swingcard.cardno, \n" +
                "billtb.uuid billuuid, \n" +
                "Sum(swingcard.amount) AS amount, \n" +
                "Sum(case when swingcard.amount*ratetb.rate/100>ratetb.MAXFEE and ratetb.maxfee>0 then " +
                "ratetb.maxfee else swingcard.amount*ratetb.rate/100  end)  paycharge, \n" +
                "cardtb.cardmaster, \n" +
                "count(1) totalcount,"+
                "(count(1) - sum(case when swingstatus='enable' then 1 else 0 END)) unfinished, \n" +
                "sum(case when swingstatus='enable' then 1 else 0 END) finished  \n" +
                "FROM \n" +
                "swingcard \n" +
                "INNER JOIN cardtb ON cardtb.cardno = swingcard.cardno \n" +
                "INNER JOIN postb ON postb.uuid = swingcard.posuuid \n" +
                "INNER JOIN ratetb ON postb.rateuuid = ratetb.uuid \n" +
                "INNER JOIN billtb ON swingcard.billuuid = billtb.uuid AND swingcard.cardno = billtb.cardno \n" +
                whereSql +
                "GROUP BY billtb.billdate, swingcard.cardno \n" +
                "union all "+
                " SELECT \n" +
                "billtb.billdate as thedate, \n" +
                "cardtb.cardno, \n" +
                "billtb.uuid billuuid, \n" +
                "SUM(repaytb.trademoney) amount, \n" +
                "cardtb.commissioncharge*SUM(repaytb.trademoney)/100 as paycharge, \n" +
                "cardtb.cardmaster, \n" +
                "count(*) totalcount, \n" +
                "(COUNT(1) - sum(case when tradestatus='enable' then 1 else 0 END)) unfinished, \n" +
                "(sum(case when tradestatus='enable' then 1 else 0 END)) finished \n" +
                "FROM repaytb \n" +
                "INNER JOIN cardtb ON cardtb.cardno = repaytb.cardno \n" +
                "INNER JOIN billtb ON repaytb.billuuid = billtb.uuid AND repaytb.cardno = billtb.cardno \n" +
                whereSql +
                "GROUP BY \n" +
                "billtb.billdate,\n" +
                "repaytb.cardno, \n" +
                "cardtb.commissioncharge) a \n"+
                "ORDER BY \n" +
                "thedate desc \n"
                + limitSql);
    }

    public String generateBillRepayDetail(String billuuid,String startdate,String enddate,String status) throws Exception {
        Map para = new HashMap();
        ArrayList<HashMap<String, Object>> dbRet ;
        int i = 1;
        para.put(i++,billuuid);
        if ((startdate.equals("")||enddate.equals("")))
            dbRet = PosDbManager.executeSql("SELECT trademoney,cardtb.commissioncharge * repaytb.trademoney/100 as charge," +
                    " thedate,tradestatus,id from repaytb,cardtb  where repaytb.cardno=cardtb.cardno and"+
                     " billuuid=? "+status,(HashMap<Integer, Object>) para);
        else {
            para.put(i++,startdate);
            para.put(i++,enddate+" 23:59:59");
            dbRet = PosDbManager.executeSql("SELECT trademoney,cardtb.commissioncharge * repaytb.trademoney/100 as charge," +
                    " thedate,tradestatus,id from repaytb,cardtb  where repaytb.cardno=cardtb.cardno and  billuuid=? " +
                    "and thedate between ? and ?"+status, (HashMap<Integer, Object>) para);
        }
        if (dbRet.size() <= 0)
            return new String("");
        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                    .addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addElement("td", String.valueOf(index+1))
                    .addElement("td", dbRet.get(index).get("TRADEMONEY").toString())
                    .addElement("td", StringUtils.formatMoney(dbRet.get(index).get("CHARGE").toString()))
                    .addElement("td", StringUtils.formatMoney(dbRet.get(index).get("THEDATE").toString()))
                    .addElement(new UIContainer("td").addElement(new UIContainer("input")
                            .addAttribute("class", StringUtils.convertNullableString(dbRet.get(index).get("TRADESTATUS")).equals("enable")?"btn btn-success radius size-MINI":"btn btn-danger radius size-MINI")
                            .addAttribute("type","button")
                            .addAttribute("title",  StringUtils.convertNullableString(dbRet.get(index).get("TRADESTATUS")).equals("enable")?"已开启":"未开启")
                            .addAttribute("datav", StringUtils.convertNullableString(dbRet.get(index).get("UUID")))
                            .addAttribute("value",  StringUtils.convertNullableString(dbRet.get(index).get("TRADESTATUS")).equals("enable")?"Y":"N")
                            .addAttribute("onclick", "clickRepayDetail(this,'" + StringUtils.convertNullableString(dbRet.get(index).get("ID")) + "')")));
        }
        return htmlString;
    }

    public String generateBillSwingDetail(String billuuid,String startdate,String enddate,String status) throws Exception {
        Map para = new HashMap();
        ArrayList<HashMap<String, Object>> dbRet ;
        int i =1;
        para.put(i++,billuuid);
        if ((startdate.equals("")||enddate.equals("")))
            dbRet = PosDbManager.executeSql("SELECT amount,swingcard.amount * rate * 0.01 as charge, sdatetm,posname,swingstatus,id from swingcard" +
                    ",postb,ratetb where ratetb.uuid=postb.rateuuid and postb.uuid=swingcard.posuuid  and  billuuid=? "+status,(HashMap<Integer, Object>) para);
        else {
            para.put(i++, startdate);
            para.put(i++, enddate+" 23:59:59");
            dbRet = PosDbManager.executeSql("SELECT  amount,swingcard.amount * rate * 0.01 as charge, sdatetm,posname,swingstatus,id from swingcard " +
                    " ,postb,ratetb where ratetb.uuid=postb.rateuuid and postb.uuid=swingcard.posuuid  and billuuid=? and sdatetm between ? and ?"+status, (HashMap<Integer, Object>) para);
        }
            if (dbRet.size() <= 0)
            return new String("");
        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                    .addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addElement("td", String.valueOf(index+1))
                    .addElement("td", dbRet.get(index).get("AMOUNT").toString())
                    .addElement("td", StringUtils.formatMoney(dbRet.get(index).get("CHARGE").toString()))
                    .addElement("td", StringUtils.formatMoney(dbRet.get(index).get("SDATETM").toString()))
                    .addElement("td", StringUtils.formatMoney(dbRet.get(index).get("POSNAME").toString()))
                    .addElement(new UIContainer("td").addElement(new UIContainer("input")
                            .addAttribute("class", StringUtils.convertNullableString(dbRet.get(index).get("SWINGSTATUS")).equals("enable")?"btn btn-success radius size-MINI":"btn btn-danger radius size-MINI")
                            .addAttribute("type","button")
                            .addAttribute("title",  StringUtils.convertNullableString(dbRet.get(index).get("SWINGSTATUS")).equals("enable")?"已开启":"未开启")
                            .addAttribute("datav", StringUtils.convertNullableString(dbRet.get(index).get("UUID")))
                            .addAttribute("value",  StringUtils.convertNullableString(dbRet.get(index).get("SWINGSTATUS")).equals("enable")?"Y":"N")
                            .addAttribute("onclick", "clickSwingDetail(this,'" + StringUtils.convertNullableString(dbRet.get(index).get("ID")) + "')")));
        }
        return htmlString;
    }
    private String userID_;
}