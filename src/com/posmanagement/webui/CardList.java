package com.posmanagement.webui;

import com.posmanagement.utils.DbManager;

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

    private ArrayList<HashMap<String, Object>> fetchCardList() throws Exception {
        return DbManager.createPosDbManager().executeSql("select cardno,cardmaster,cmtel,cmseccontact,salesman from cardtb");
    }
}