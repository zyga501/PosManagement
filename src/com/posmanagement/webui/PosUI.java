package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;
import com.posmanagement.utils.UserUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class PosUI extends WebUI {
    public PosUI (String UID) { userID_=UID;       }

    public String generateSelect() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchPosList("");
        if (dbRet.size() <= 0)
            return new String("");

        UIContainer uiContainer = new UIContainer();
        uiContainer.addElement(new UIContainer("option","")
                .addAttribute("value", ""));
        ;
        for (int index = 0; index < dbRet.size(); ++index) {
            if (dbRet.get(index).get("STATUS").toString().compareTo("enable") == 0) {
                uiContainer.addElement(new UIContainer("option", dbRet.get(index).get("POSNAME").toString())
                        .addAttribute("value", dbRet.get(index).get("UUID").toString()));
            }
        }

        return uiContainer.generateUI();
    }


    public String generateTable(String wherestr) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchPosList(wherestr);
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            String[] usevalue;
            if (null==dbRet.get(index).get("USED"))
            usevalue = " ; ".split(";") ;
                else
            usevalue = dbRet.get(index).get("USED").toString().split(";");
            htmlString += new UIContainer("tr")
                    .addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addAttribute("value", dbRet.get(index).get("UUID").toString())
                    .addElement("td", dbRet.get(index).get("POSNAME").toString())
                    .addElement("td", StringUtils.convertNullableString( dbRet.get(index).get("INDUSTRYNAME")))
                    .addElement("td",  StringUtils.convertNullableString(dbRet.get(index).get("RATE"))+"|"+StringUtils.convertNullableString(dbRet.get(index).get("MAXFEE")))
                    .addElement("td",  StringUtils.convertNullableString(dbRet.get(index).get("MCC")))
                    .addElement("td", getText(dbRet.get(index).get("STATUS").toString().compareTo("enable") == 0 ? "global.enable" : "global.disable")
                    );
        }
        return htmlString;
    }

    public String generatePosList(String posuuid) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet =  PosDbManager.executeSql("select posname from postb where uuid='"+posuuid+"'");
        if (dbRet.size() > 0)
            return dbRet.get(0).get("POSNAME").toString();
        else
            return "Not exists this POS!";
    }

    private ArrayList<HashMap<String, Object>> fetchPosList(String wherestr) throws Exception {
        if (UserUtils.isAdmin(userID_))
           return PosDbManager.executeSql("SELECT " +
                "POSTB.uuid, " +
                "POSTB.posname, " +
                "userinfo.unick saleman, " +
                "posservertb.name posservername, " +
                "mcctb.mcc mcc, " +
                "industrytb.name industryname, " +
                "POSTB.`status`," +
                "ratetb.rate, " +
                "ratetb.maxfee,POSTB.startdatetm, " +
                "(select CONCAT(COUNT(b.amount),';',SUM(b.amount))" +
                "  from  swingcard b where POSTB.uuid=b.posuuid ) used "+
                "FROM  " +
                "POSTB  " +
                "inner JOIN userinfo ON POSTB.salemanuuid = userinfo.uid  " +
                "left JOIN posservertb ON posservertb.uuid = POSTB.posserveruuid  " +
                "left JOIN industrytb ON POSTB.industryuuid = industrytb.uuid  " +
                "left JOIN ratetb ON POSTB.rateuuid = ratetb.uuid " +
                "left JOIN mcctb ON mcctb.uuid = POSTB.mccuuid  "+wherestr);
        else
            return PosDbManager.executeSql("SELECT " +
                "POSTB.uuid, " +
                "POSTB.posname, " +
                "userinfo.unick saleman, " +
                "posservertb.name posservername, " +
                "mcctb.mcc mcc, " +
                "industrytb.name industryname, " +
                "POSTB.`status`," +
                "ratetb.rate , " +
                "ratetb.maxfee ,POSTB.startdatetm," +
                "(select CONCAT(COUNT(b.amount),';',SUM(b.amount))" +
                "  from  swingcard b where POSTB.uuid=b.posuuid ) used "+
                "FROM  " +
                "POSTB  " +
                "inner JOIN userinfo ON POSTB.salemanuuid = userinfo.uid  " +
                "left JOIN mcctb ON mcctb.uuid = POSTB.mccuuid "+
                "left JOIN posservertb ON posservertb.uuid = POSTB.posserveruuid  " +
                "left JOIN industrytb ON POSTB.industryuuid = industrytb.uuid  " +
                "left JOIN ratetb ON POSTB.rateuuid = ratetb.uuid " +
                " where POSTB.salemanuuid= '"+userID_+"'"+ wherestr.replaceAll("where","").replaceAll("1=1",""));

    }
    private String userID_; // TODO for role
}