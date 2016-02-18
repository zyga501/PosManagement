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
                "INNER JOIN userinfo salemantb on postb.salemanuuid=salemantb.uid  \n" +
                whereSql );
        if (resultMap.size()<=0) {
            rt[0]=0;
            return rt;
        }
        try {
            rt[0] = Integer.parseInt(resultMap.get(0).get("CNT").toString()) / WebUI.DEFAULTITEMPERPAGE + 1;
            rt[1] = Integer.parseInt(resultMap.get(0).get("CNT").toString());
            rt[2] = Float.parseFloat(resultMap.get(0).get("AMOUNT").toString());
            rt[3] = Float.parseFloat(resultMap.get(0).get("CHARGE").toString());
        }
        catch (Exception e){

        }
        return rt;
    }


    public String generateRepaySummary(int pageIndex) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchRepayGeneralSummary(pageIndex);
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                    .addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addElement("td", dbRet.get(index).get("TRADETIME").toString())
                    .addElement("td", dbRet.get(index).get("CARDNO").toString())
                    .addElement("td", dbRet.get(index).get("CARDMASTER").toString())
                    .addElement("td", dbRet.get(index).get("COMMISSIONCHARGE").toString())
                    .addElement("td", dbRet.get(index).get("TRADEMONEY").toString())
                    .addElement("td", dbRet.get(index).get("CHARGE").toString()) ;
        }
        return htmlString;
    }

    public float[] fetchRepayGeneralpagecount() throws Exception {
        String whereSql = SQLUtils.BuildWhereCondition(uiConditions_);
        if (!whereSql.isEmpty()) {
            whereSql = " where " + whereSql;
        }

        if (!UserUtils.isAdmin(userID_))
            whereSql += " and  (userinfo.uid='"+userID_+"' " +
                    " or userinfo.uid ='"+userID_+"') ";
        float[] rt = new float[4];
        ArrayList<HashMap<String, Object>> resultMap = PosDbManager.executeSql("SELECT count(*) as cnt, \n" +
                "sum(trademoney) amount,"+
                "sum(charge) charge "+
                "from repaytb \n" +
                "INNER JOIN userinfo ON repaytb.userid = userinfo.uid \n" +
                "INNER JOIN cardtb on cardtb.cardno=repaytb.cardno  \n" +
                whereSql );
        if (resultMap.size()<=0) {
            rt[0]=0;
            return rt;
        }
        try {
            rt[0] = Integer.parseInt(resultMap.get(0).get("CNT").toString()) / WebUI.DEFAULTITEMPERPAGE + 1;
            rt[1] = Integer.parseInt(resultMap.get(0).get("CNT").toString());
            rt[2] = Float.parseFloat(resultMap.get(0).get("AMOUNT").toString());
            rt[3] = Float.parseFloat(resultMap.get(0).get("CHARGE").toString());
        }
        catch (Exception e){

        }
        return rt;
    }

    public String generateAsset(int pageIndex) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchAssetGeneralSummary(pageIndex);
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += new UIContainer("tr")
                    .addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addElement("td", dbRet.get(index).get("TIME").toString())
                    .addElement("td", dbRet.get(index).get("TYPE").toString())
                    .addElement("td", dbRet.get(index).get("AMOUNT").toString())
                    .addElement("td", dbRet.get(index).get("BALANCE").toString())
                    .addElement("td", dbRet.get(index).get("REMARK").toString()) ;
        }
        return htmlString;
    }

    public String[] fetchAssetGeneralpagecount() throws Exception {
        String whereSql = SQLUtils.BuildWhereCondition(uiConditions_);
        if (!whereSql.isEmpty()) {
            whereSql = " where " + whereSql;
        }

        if (!UserUtils.isAdmin(userID_))
            whereSql += " and  (assetflowtb.salemanuuid='"+userID_+"') ";
        String[] rt = new String[5];
        ArrayList<HashMap<String, Object>> resultMap = PosDbManager.executeSql("SELECT count(*) as cnt,\n" +
                "sum(amount) as amount, \n"+
                "sum(balance) as charge, \n"+
                "assetflowtb.type "+
                "FROM\n" +
                "assetflowtb \n" +
                "INNER JOIN userinfo  ON assetflowtb.salemanuuid = userinfo.uid \n" +
                whereSql +" group by type");
        if (resultMap.size()<=0) {
            rt[0]="0";
            return rt;
        }
        try {
            int _count = 0;
            float _amount = 0;
            String _string = "";
            for  (int i = 0;i < resultMap.size();i++){
                _count +=Integer.parseInt(resultMap.get(i).get("CNT").toString());
                _amount += Float.parseFloat(resultMap.get(i).get("AMOUNT").toString());
                _string += resultMap.get(i).get("TYPE").toString() +"<b>"+ resultMap.get(i).get("AMOUNT").toString()+"</b>";;
            }
            rt[0] = String.valueOf(_count / WebUI.DEFAULTITEMPERPAGE + 1);
            rt[1] ="共 <b>"+String.valueOf(_count)+"</b>条 " +_string+" 产生利润 <b>"+String.valueOf(_amount)+"</b>";
        }
        catch (Exception e){

        }
        return rt;
    }

    private ArrayList<HashMap<String, Object>>  fetchSwingGeneralSummary(int pageIndex) throws Exception {
        String whereSql = SQLUtils.BuildWhereCondition(uiConditions_);
        if (!whereSql.isEmpty()) {
            whereSql = " where " + whereSql;
        }
        String limitSql ="limit " + (pageIndex - 1) * DEFAULTITEMPERPAGE + "," + DEFAULTITEMPERPAGE;

        if (!UserUtils.isAdmin(userID_))
            whereSql += " and  (postb.salemanuuid='"+userID_+"' " +
                    " or userinfo.uid ='"+userID_+"') ";
        return PosDbManager.executeSql("SELECT   swingcard.*,postb.*,AMOUNT-charge INBANK,salemantb.unick as saleman," +
                "userinfo.unick as doer,ratetb.rate  " +
                "FROM\n" +
                "swingcard\n" +
                "INNER JOIN userinfo ON swingcard.userid = userinfo.uid \n" +
                "INNER JOIN postb on postb.uuid=swingcard.posuuid  \n" +
                "INNER JOIN userinfo salemantb on postb.salemanuuid=salemantb.uid  \n" +
                "left JOIN ratetb on ratetb.uuid=postb.rateuuid  \n" +
                whereSql +limitSql);
    }

    private ArrayList<HashMap<String, Object>>  fetchRepayGeneralSummary(int pageIndex) throws Exception {
        String whereSql = SQLUtils.BuildWhereCondition(uiConditions_);
        if (!whereSql.isEmpty()) {
            whereSql = " where " + whereSql;
        }
        String limitSql ="limit " + (pageIndex - 1) * DEFAULTITEMPERPAGE + "," + DEFAULTITEMPERPAGE;

        if (!UserUtils.isAdmin(userID_))
            whereSql += " and  (repaytb.userid='"+userID_+"' " +
                    " or userinfo.uid ='"+userID_+"') ";
        return PosDbManager.executeSql("SELECT   repaytb.*,cardtb.cardmaster,cardtb.commissioncharge," +
                "userinfo.unick as doer " +
                "FROM\n" +
                "repaytb \n" +
                "INNER JOIN userinfo ON repaytb.userid = userinfo.uid \n" +
                "INNER JOIN cardtb on cardtb.cardno=repaytb.cardno  \n" +
                whereSql +limitSql);
    }

    private ArrayList<HashMap<String, Object>>  fetchAssetGeneralSummary(int pageIndex) throws Exception {
        String whereSql = SQLUtils.BuildWhereCondition(uiConditions_);
        if (!whereSql.isEmpty()) {
            whereSql = " where " + whereSql;
        }
        String limitSql ="limit " + (pageIndex - 1) * DEFAULTITEMPERPAGE + "," + DEFAULTITEMPERPAGE;

        if (!UserUtils.isAdmin(userID_))
            whereSql += " and  (assetflowtb.salemanuuid='"+userID_+"' ) ";
        return PosDbManager.executeSql("SELECT  * " +
                "FROM\n" +
                "assetflowtb\n" +
                "INNER JOIN userinfo ON assetflowtb.salemanuuid = userinfo.uid \n" +
                whereSql +limitSql);
    }
    private String userID_;
}