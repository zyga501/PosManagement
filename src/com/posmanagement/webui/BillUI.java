package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.SQLUtils;
import com.posmanagement.utils.StringUtils;
import com.posmanagement.utils.UserUtils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

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
            double canUseMoney = ((long)(10 * (Double.valueOf(dbRet.get(index).get("CANUSEAMOUNT").toString()) +
                    Double.valueOf(dbRet.get(index).get("REPAYAMOUNT").toString()) - Double.valueOf(dbRet.get(index).get("SWINGAMOUNT").toString())))) / 10.0;
            double repayAmount = Double.valueOf(dbRet.get(index).get("REPAYAMOUNT").toString());
            double remainAmount = Double.valueOf(dbRet.get(index).get("BILLAMOUNT").toString()) - repayAmount;
            htmlString += new UIContainer("tr")
                    .addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("BANKNAME")))
                    .addElement("td", StringUtils.formatCardNO(StringUtils.convertNullableString(dbRet.get(index).get("CARDNO"))))
                    .addElement("td", (new SimpleDateFormat("yyyy-MM")).format(Date.valueOf(dbRet.get(index).get("BILLDATE").toString())))
                    .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("LASTREPAYMENTDATE")))
                    .addElement(new UIContainer("td")
                            .addElement(new UIContainer("label",StringUtils.convertNullableString(dbRet.get(index).get("BILLAMOUNT")).equals("")?"0":dbRet.get(index).get("BILLAMOUNT").toString())
                            .addAttribute("name","billamount")
                            .addAttribute("datav",StringUtils.convertNullableString(dbRet.get(index).get("UUID")))
                            .addAttribute("style","display:inline-block;")))
                    .addElement("td", String.valueOf(canUseMoney))
                    .addElement("td", String.valueOf(repayAmount))
                    .addElement("td", String.valueOf(Double.max(remainAmount, 0.0)))
                    .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("SALEMAN")))
                    .addElement("td", dbRet.get(index).get("SWINGCOUNT").toString().compareTo(dbRet.get(index).get("SWUNGCOUNT").toString()) == 0 &&
                                        dbRet.get(index).get("REPAYCOUNT").toString().compareTo(dbRet.get(index).get("REPAYEDCOUNT").toString()) == 0 ?
                            getText("bill.billfinished") : getText("bill.billunfinished"))
                    .addElement(new UIContainer("td").addElement(new UIContainer("input")
                        .addAttribute("class", dbRet.get(index).get("STATUS").equals("enable")?"btn btn-success radius":"btn btn-danger radius")
                        .addAttribute("type","button")
                            .addAttribute("title", dbRet.get(index).get("STATUS").equals("enable")?"已开启":"未开启")
                            .addAttribute("datav", StringUtils.convertNullableString(dbRet.get(index).get("UUID")))
                                    .addAttribute("value", dbRet.get(index).get("STATUS").equals("enable")?"Y":"N")
                            .addAttribute("onclick", "clickBill(this,'" + StringUtils.convertNullableString(dbRet.get(index).get("UUID")) + "')")));
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
        if (!whereSql.isEmpty()) {
            whereSql = " and " + whereSql;
        }
        String limitSql ="limit " + (pageIndex - 1) * DEFAULTITEMPERPAGE + "," + DEFAULTITEMPERPAGE;

        if (!UserUtils.isAdmin(userID_))
            whereSql += " and  (cardtb.salemanuuid in (select a.uid from salemantb a  where a.uid='"+userID_+"' )" +
                    " or cardtb.salemanuuid in(select salemanuuid from tellertb   where uid='"+userID_+"')) ";

        return PosDbManager.executeSql("SELECT  *,userinfo.unick SALEMAN,banktb.name bankname \n" +
                "from billtb ,cardtb , (select billtb.uuid billuuid,sum(CASE WHEN swingcard.swingstatus='enable' then swingcard.amount else 0 END) swingamount,"+
                "Sum(swingcard.amount) AS amount, " +
                "count(1) swingcount," +
                "sum(case when swingstatus='enable' then 1 else 0 END) swungcount,"+
                "(count(1) - sum(case when swingstatus='enable' then 1 else 0 END)) unfinished  ,"+
                "Sum(case when swingcard.amount*ratetb.rate/100>ratetb.MAXFEE and ratetb.maxfee>0 then "+
                "ratetb.maxfee else swingcard.amount*ratetb.rate/100  end)  paycharge "+
                "from  swingcard,billtb,ratetb,postb where swingcard.billuuid = billtb.uuid and   postb.uuid = swingcard.posuuid "+
                "and postb.rateuuid = ratetb.uuid group by billtb.uuid) swingcardtotal,"+
                "(select  billtb.uuid billuuid,count(repaytb.id) repaycount,"+
                "sum(CASE WHEN repaytb.tradestatus='enable' then 1 else 0 END) repayedcount,"+
                "sum(CASE WHEN repaytb.tradestatus='enable' then repaytb.trademoney else 0 END) repayamount "+
                "from  repaytb,billtb  where repaytb.billuuid = billtb.uuid  group by billtb.uuid  ) repaytbtotal,userinfo,  banktb \n"+
                "where userinfo.uid = cardtb.salemanuuid and cardtb.cardno = billtb.cardno and billtb.uuid=swingcardtotal.billuuid \n" +
                "and banktb.uuid = billtb.bankuuid and repaytbtotal.billuuid=billtb.uuid \n"+
                whereSql +
                "GROUP BY billtb.billdate, billtb.cardno \n" +
                "ORDER BY billtb.billdate desc\n" +
                limitSql);
    }

    public int fetchBillPageCount() throws Exception {
        String whereSql = SQLUtils.BuildWhereCondition(uiConditions_);
        if (!whereSql.isEmpty()) {
            whereSql = " where " + whereSql;
        }
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

    private String userID_;
}