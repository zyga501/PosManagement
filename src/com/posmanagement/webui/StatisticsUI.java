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
                    .addElement("td", dbRet.get(index).get("REALSDATETM").toString())
                    .addElement("td", dbRet.get(index).get("POSNAME").toString())
                    .addElement("td", dbRet.get(index).get("RATE").toString())
                    .addElement("td", dbRet.get(index).get("AMOUNT").toString())
                    .addElement("td", dbRet.get(index).get("CHARGE").toString()) ;
        }
        return htmlString;
    }

    public float[] fetchSwingGeneralpagecount() throws Exception {
        String whereSql = SQLUtils.BuildWhereCondition(uiConditions_);
        if (!whereSql.isEmpty()) {
            whereSql = " where " + whereSql;
        }

        if (!UserUtils.isAdmin(userID_))
            whereSql += " and  (salemantb.uid='"+userID_+"' " +
                    " or userinfo.uid ='"+userID_+"') ";
        float[] rt = new float[4];
        ArrayList<HashMap<String, Object>> resultMap = PosDbManager.executeSql("SELECT count(*) as cnt,\n" +
                "sum(swingcard.amount) as amount, \n"+
                "sum(swingcard.charge) as charge \n"+
                "FROM\n" +
                "swingcard\n" +
                "INNER JOIN userinfo ON swingcard.userid = userinfo.uid \n" +
                "INNER JOIN postb on postb.uuid=swingcard.posuuid  \n" +
                "INNER JOIN userinfo salemantb on postb.salesmanuuid=salemantb.uid  \n" +
                whereSql );
        if (resultMap.size()<=0) {
            rt[0]=0;
            return rt;
        }
        rt[0]=Integer.parseInt(resultMap.get(0).get("CNT").toString())/ WebUI.DEFAULTITEMPERPAGE + 1;
        rt[1]=Integer.parseInt(resultMap.get(0).get("CNT").toString());
        rt[2]=Float.parseFloat(resultMap.get(0).get("AMOUNT").toString());
        rt[3]=Float.parseFloat(resultMap.get(0).get("CHARGE").toString());
        return rt;
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
        return PosDbManager.executeSql("SELECT   swingcard.*,postb.*,AMOUNT-charge INBANK,salemantb.unick as saleman," +
                "userinfo.unick as doer,ratetb.rate  " +
                "FROM\n" +
                        "swingcard\n" +
                        "INNER JOIN userinfo ON swingcard.userid = userinfo.uid \n" +
                        "INNER JOIN postb on postb.uuid=swingcard.posuuid  \n" +
                        "INNER JOIN userinfo salemantb on postb.salesmanuuid=salemantb.uid  \n" +
                        "left JOIN ratetb on ratetb.uuid=postb.rateuuid  \n" +
                        whereSql +limitSql);
    }
    private String userID_;
}