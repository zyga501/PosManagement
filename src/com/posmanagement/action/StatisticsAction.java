package com.posmanagement.action;

import com.posmanagement.utils.*;
import com.posmanagement.webui.SalemanUI;
import com.posmanagement.webui.TellerUI;
import com.posmanagement.webui.UIContainer;
import com.posmanagement.webui.UserMenu;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StatisticsAction extends AjaxActionSupport{
    private final static String GENERALINFO = "welcomeinfo";
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
                            "select SUM(t.firstbalance) cnt,'资产总额' as nm  from  assettb t where t.salesman='"+getUserID()+"'");
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
        return GENERALINFO;
    }
}
