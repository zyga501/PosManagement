package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;

import java.util.ArrayList;
import java.util.HashMap;

public class RuleList {
    public String generateHTMLString() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchRuleList();
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString +="<tr class=\"text-c odd\" role=\"row\">"+
                    "<td>"+ dbRet.get(index).get("RULENO")+"</td>"+
                    "<td>"+ dbRet.get(index).get("BANKNAME")+"</td>"+
                    "<td>"+ dbRet.get(index).get("POSSERVER")+"</td>"+
                    "<td>"+ dbRet.get(index).get("MINSWINGNUM")+"</td>"+
                    "<td>"+ dbRet.get(index).get("MAXSWINGNUM")+"</td>"+
                    "<td>"+ dbRet.get(index).get("SWINGTIME")+"</td>"+
                    "<td>"+ dbRet.get(index).get("MINSWINGMONEY")+"</td>"+
                    "<td>"+ dbRet.get(index).get("MAXSWINGMONEY")+"</td>"+
                    "<td>"+ dbRet.get(index).get("SWINGPERCENT")+"</td>"+
                    "<td>"+ dbRet.get(index).get("INDUSTRYNAME")+"</td>"+
                    "<td>"+ dbRet.get(index).get("INDUSTRYFRE")+"</td>"+
                    "<td>"+ dbRet.get(index).get("INDUSTRYINTERVAL")+"</td>"+
                    "<td>"+ dbRet.get(index).get("RATE")+"</td>"+
                    "<td>"+ dbRet.get(index).get("RATEFRE")+"</td>"+
                    "<td>"+ dbRet.get(index).get("RATEINTERVAL")+"</td>"+
                    "<td>"+ dbRet.get(index).get("MCC")+"</td>"+
                    "<td>"+ dbRet.get(index).get("MCCFRE")+"</td>"+
                    "<td>"+ dbRet.get(index).get("MCCINTERVAL")+"</td>"+
                    "<td>"+ dbRet.get(index).get("USEFRE")+"</td>"+
                    "<td>"+ dbRet.get(index).get("USEINTERVAL")+"</td>"+
                    "<td>"+ dbRet.get(index).get("RULEUSEFRE")+"</td>"+
                    "<td>"+ dbRet.get(index).get("RULEUSEINTERVAL")+"</td>"+
                    "<td><input type=\"checkbox\"";
            if (dbRet.get(index).get("STATUS").toString().compareTo("on") == 0) {
                htmlString += "checked=\"checked\"";
            }
            htmlString += " /></td></tr>";
        }
        return htmlString;
    }

    private ArrayList<HashMap<String, Object>> fetchRuleList() throws Exception {
        return PosDbManager.executeSql("select * from ruletb");
    }
}
