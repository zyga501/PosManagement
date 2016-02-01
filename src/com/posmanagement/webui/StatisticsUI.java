package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.SQLUtils;
import com.posmanagement.utils.StringUtils;
import com.posmanagement.utils.UserUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class StatisticsUI extends WebUI {
    public StatisticsUI(String userID) {
        userID_ = userID;
    }

    public String generateSummary(int pageIndex) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchSwingGeneralSummary(pageIndex);
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                    .addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addElement("td", dbRet.get(index).get("TOTALCOUNT").toString())
                    .addElement("td", dbRet.get(index).get("TOTALAMOUNT").toString())
                    .addElement("td", dbRet.get(index).get("TOTALCHARGE").toString())
                    .addElement("td", dbRet.get(index).get("SALEMAN").toString())
                    .addElement("td", dbRet.get(index).get("DOER").toString());
        }
        return htmlString;
    }

    public int fetchSwingGeneralpagecount() throws Exception {
        String whereSql = SQLUtils.BuildWhereCondition(uiConditions_);
        if (!whereSql.isEmpty()) {
            whereSql = " where " + whereSql;
        }

        if (!UserUtils.isAdmin(userID_))
            whereSql += " and  (salemantb.uid='"+userID_+"' " +
                    " or userinfo.uid ='"+userID_+"') ";

        ArrayList<HashMap<String, Object>> resultMap = PosDbManager.executeSql("SELECT count(*) as cnt\n" +
                "FROM\n" +
                "swingcard\n" +
                "INNER JOIN userinfo ON swingcard.userid = userinfo.uid \n" +
                "INNER JOIN postb on postb.uuid=swingcard.posuuid  \n" +
                "INNER JOIN userinfo salemantb on postb.salesmanuuid=salemantb.uid  \n" +
                whereSql +
                " GROUP BY\n" +
                "userinfo.unick\n");
        if (resultMap.size()<=0) {
            return 0;
        }
        return resultMap.size()/ WebUI.DEFAULTITEMPERPAGE + 1;
    }

    private ArrayList<HashMap<String, Object>>  fetchSwingGeneralSummary(int pageIndex) throws Exception {
        String whereSql = SQLUtils.BuildWhereCondition(uiConditions_);
        if (!whereSql.isEmpty()) {
            whereSql = " where " + whereSql;
        }
        String limitSql ="limit " + (pageIndex - 1) * DEFAULTITEMPERPAGE + "," + DEFAULTITEMPERPAGE;

        if (!UserUtils.isAdmin(userID_))
            whereSql += " and  (salemantb.uid='"+userID_+"' " +
                    " or userinfo.uid ='"+userID_+"') ";
        return PosDbManager.executeSql("SELECT  sum(swingcard.amount) as totalamount,count(*) as totalcount," +
                "sum(swingcard.charge) as totalcharge,salemantb.unick as saleman,userinfo.unick as doer  " +
                "FROM\n" +
                        "swingcard\n" +
                        "INNER JOIN userinfo ON swingcard.userid = userinfo.uid \n" +
                        "INNER JOIN postb on postb.uuid=swingcard.posuuid  \n" +
                        "INNER JOIN userinfo salemantb on postb.salesmanuuid=salemantb.uid  \n" +
                        whereSql +
                        " GROUP BY\n" +
                        "userinfo.unick\n" +limitSql);
    }
    private String userID_;
}