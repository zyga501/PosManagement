package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.SQLUtils;
import com.posmanagement.utils.StringUtils;
import com.posmanagement.utils.UserUtils;
import com.posmanagement.webui.StatisticsUI;
import com.posmanagement.webui.UIContainer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatisticsAction extends AjaxActionSupport{
    private final static String GENERALINFO = "welcomeinfo";
    private final static String SWINGGENERAL="swinggeneral";
    private String welcomeuser = "";
    private String generalinfo = "";

    public String getWelcomeuser() {
        return welcomeuser;
    }

    public void setWelcomeuser(String welcomeuser) {
        this.welcomeuser = welcomeuser;
    }

    public String getGeneralinfo() {
        return generalinfo;
    }

    public void setGeneralinfo(String generalinfo) {
        this.generalinfo = generalinfo;
    }

    public String SwingGeneral(){
        return SWINGGENERAL;
    }

    public String obtainGeneralinfo(){
        welcomeuser = super.getAttribute("userNick");
        if  (UserUtils.isAdmin(getUserID())){
            try {
                ArrayList<HashMap<String, Object>> dbRet = PosDbManager.executeSql("select  count(*) as cnt,a.rolename as nm from  " +
                        "userinfo t,roletb a where a.rid=t.rid  group by a.rolename  " +
                        "union all  " +
                        "select  count(*) as cnt,'卡片' as nm  from  cardtb t   " +
                        "union all  " +
                        "select  count(*) as cnt,'规则' as nm  from  ruletb t ");
                for (int index = 0; index < dbRet.size(); ++index) {
                    generalinfo +=new UIContainer("tr")
                            .addAttribute("class", "text-c odd")
                            .addElement("td", dbRet.get(index).get("NM").toString())
                            .addElement("td", dbRet.get(index).get("CNT").toString());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else
        if  (UserUtils.isSalesman(getUserID())) {
                ArrayList<HashMap<String, Object>> dbRet = null;
                try {
                    dbRet = PosDbManager.executeSql("select  count(*) cnt,'本月未审核账单' as nm from  billtb t  where t.salesmanuuid='"+getUserID()+"'  and `status`<>'enable' " +
                            "and billdate " +
                            "union all  " +
                            "select  count(*) cnt,'本月未生成账单' as nm  from  cardtb t where t.salesmanuuid='"+getUserID()+"' and  t.cardno not in " +
                            "(select cardno  from billtb where  " +
                            "  YEAR(billdate)=YEAR(CURRENT_DATE) and MONTH(billdate)=month(CURRENT_DATE) ) " +
                            "union all  " +
                            "select  count(*) cnt,'卡片数量' as nm  from  cardtb t  where t.salesmanuuid='"+getUserID()+"'" +
                            "union all  " +
                            "select  SUM(t.creditamount)-SUM(b.amount)+SUM(r.trademoney) cnt,'信用总额' as nm  from cardtb t LEFT OUTER JOIN  " +
                            "(select SUM(amount) amount,cardno  from  swingcard where  swingstatus='enable'" +
                            " group by cardno ) b on (b.cardno=t.cardno)  " +
                            "LEFT JOIN (select sum(trademoney) trademoney,cardno from repaytb where" +
                            "  tradestatus='enable' group by cardno) r on (r.cardno=t.cardno ) where t.salesmanuuid='"+getUserID()+"'" +
                            "union all  " +
                            "select SUM(t.balance) cnt,'资产总额' as nm  from  assettb t where t.salesmanuuid='"+getUserID()+"'");
                for (int index = 0; index < dbRet.size(); ++index) {
                    generalinfo +=new UIContainer("tr")
                            .addAttribute("class", "text-c odd")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(index).get("NM")))
                            .addElement("td",StringUtils.convertNullableString(dbRet.get(index).get("CNT")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            ArrayList<HashMap<String, Object>> dbRet = null;
            try {
                generalinfo +=new UIContainer("tr")
                        .addAttribute("class", "text-c odd")
                        .addElement("td","")
                        .addElement("td","昨天")
                        .addElement("td","今天")
                        .addElement("td","本月");
                dbRet = PosDbManager.executeSql(
                        "select  sum(case when date_sub('now()',INTERVAL 1 DAY)=date_format(a.sdatetm,'%Y-%m-%d') then 1 else 0 end ) zt ,\n" +
                                "sum(case when date_format(NOW(),'%Y-%m-%d')=date_format(a.sdatetm,'%Y-%m-%d') then 1 else 0 end ) jt ," +
                                "sum(case when date_format(NOW(),'%Y-%m')=date_format(a.sdatetm,'%Y-%m') then 1 else 0 end ) byue \n" +
                                " from swingcard a,cardtb b,tellertb c \n" +
                                "WHERE a.cardno=b.cardno  and c.salesman=b.salesmanuuid  and \n" +
                                "c.uid='"+getUserID()+"'");
                if (dbRet.size()>0) {
                    generalinfo +=new UIContainer("tr")
                            .addAttribute("class", "text-c odd")
                            .addElement("td","未刷卡")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(0).get("ZT")))
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(0).get("JT")))
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(0).get("BYUE")));
                };
                dbRet = PosDbManager.executeSql(
                        "select  sum(case when date_sub(now(),INTERVAL 1 DAY)=date_format(a.thedate,'%Y-%m-%d') then 1 else 0 end ) zt ,\n" +
                                "sum(case when date_format(NOW(),'%Y-%m-%d')=date_format(a.thedate,'%Y-%m-%d') then 1 else 0 end ) jt ," +
                                "sum(case when date_format(NOW(),'%Y-%m')=date_format(a.thedate,'%Y-%m') then 1 else 0 end ) byue \n" +
                                " from repaytb a,cardtb b,tellertb c \n" +
                                "WHERE a.cardno=b.cardno  and c.salesman=b.salesmanuuid  and \n" +
                                "c.uid='"+getUserID()+"'");
                if (dbRet.size()>0) {
                    generalinfo +=new UIContainer("tr")
                            .addAttribute("class", "text-c odd")
                            .addElement("td","未还款")
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(0).get("ZT")))
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(0).get("JT")))
                            .addElement("td", StringUtils.convertNullableString(dbRet.get(0).get("BYUE")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return GENERALINFO;
    }

    public  String FetchSwingGeneral(){

        ArrayList<SQLUtils.WhereCondition> uiConditions = new ArrayList<SQLUtils.WhereCondition>() {
            {
                if (!StringUtils.convertNullableString(getParameter("sdate")).trim().isEmpty())
                    add(new SQLUtils.WhereCondition("swingcard.realsdatetm", " >",
                            SQLUtils.ConvertToSqlString(getParameter("sdate").toString().trim())));
                if (!StringUtils.convertNullableString(getParameter("edate")).trim().isEmpty())
                    add(new SQLUtils.WhereCondition("swingcard.realsdatetm", " <",
                            SQLUtils.ConvertToSqlString(getParameter("edate").toString().trim())));
                if (!StringUtils.convertNullableString(getParameter("saleman")).trim().isEmpty()) {
                    add(new SQLUtils.WhereCondition("salemantb.unick", " =",
                            SQLUtils.ConvertToSqlString(getParameter("saleman").toString().trim())));
                    add(new SQLUtils.WhereCondition("salemantb.rid", " =","'69632ae8-7e48-4e72-ad58-1043ad655a4c'"));
                }
                if (!StringUtils.convertNullableString(getParameter("teller")).trim().isEmpty()) {
                    add(new SQLUtils.WhereCondition("userinfo.unick", " =",
                            SQLUtils.ConvertToSqlString(getParameter("teller").toString().trim())));
                }
            }
        };

        Map map = new HashMap();
        StatisticsUI statisticsUI = new StatisticsUI(super.getUserID());
        statisticsUI.setUiConditions(uiConditions);
        try {
            float[] rt = statisticsUI.fetchSwingGeneralpagecount();
            map.put("pagecount",rt[0]);
            map.put("cnt",rt[1]);
            map.put("amount",rt[2]);
            map.put("charge",rt[3]);
            map.put("inbank",rt[2]-rt[3]);
            int curr = Integer.parseInt(null==getParameter("currpage")?"1":getParameter("currpage").toString());
            String  v=statisticsUI.generateSummary(curr);
            map.put("swingGeneral",v);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxActionComplete(map);
    }
}
