package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class CardList {
    public String generateHTMLString() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchCardList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += "<tr class=\"text-c odd\" role=\"row\">" ;
            Object[] keystr= (Object[]) dbRet.get(index).keySet().toArray();
            for (int jindex = 0 ; jindex < keystr.length;++jindex){
                htmlString += "<td>" + dbRet.get(index).get(keystr[jindex]) + "</td>" ;
            }
            htmlString += "</tr>";
        }
        return htmlString;
    }

    public String generateMasterString(String cardno) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet =  PosDbManager.executeSql("select cardmaster from cardtb where cardno='"+cardno+"'");
        if (dbRet.size() > 0)
            return dbRet.get(0).get("CARDMASTER").toString();
        else
            return "Not exists this Card!";
    }

    private ArrayList<HashMap<String, Object>> fetchCardList() throws Exception {
        return PosDbManager.executeSql("select cardno,cardmaster,cmtel,cmseccontact,salesman from cardtb");
    }
}