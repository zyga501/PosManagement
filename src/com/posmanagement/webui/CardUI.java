package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;
import com.posmanagement.utils.UserUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class CardUI extends WebUI {
    public CardUI(String uid){ userID_ = uid;   }
    public String generateCardTable(String wherestr) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchCardList(wherestr);
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            String cardstr =  dbRet.get(index).get("CARDNO").toString();
                    UIContainer UI = new UIContainer("tr");
                    UI.addAttribute("class", "text-c odd")
                    .addAttribute("role", "row")
                    .addElement(new UIContainer("td")
                        .addElement(
                            new UIContainer("input")
                                    .addAttribute("type", "radio")
                                    .addAttribute("name", "newid")
                                    .addAttribute("value", dbRet.get(index).get("CARDNO").toString())
                                    .addAttribute("checked", "checked",false)
                        ))
                    .addElement("td", StringUtils.formatCardNO(cardstr))
                    .addElement("td", dbRet.get(index).get("CREDITAMOUNT").toString())
                    .addElement("td", dbRet.get(index).get("BILLDATE").toString())
                    .addElement("td", dbRet.get(index).get("NAME").toString())
                    .addElement("td", dbRet.get(index).get("CARDMASTER").toString())
                    .addElement("td", dbRet.get(index).get("CMTEL").toString());
            if (UserUtils.isAdmin(userID_))
                    UI.addElement("td", dbRet.get(index).get("SALESMAN").toString());
            htmlString += UI;
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

    private ArrayList<HashMap<String, Object>> fetchCardList(String wherestr) throws Exception {
        if ( UserUtils.isAdmin(userID_))
            return PosDbManager.executeSql("select cardtb.uuid, cardno,cardmaster,cmtel,billdate,userinfo.unick salesman " +
                ",banktb.name,creditamount from cardtb inner join banktb " +
                "on cardtb.bankuuid=banktb.uuid inner join userinfo on userinfo.uid=cardtb.salesmanuuid "+wherestr);
        else
            return PosDbManager.executeSql("select cardtb.uuid, cardno,cardmaster,cmtel,billdate,userinfo.unick salesman " +
                    ",banktb.name,creditamount from cardtb inner join banktb " +
                    "on cardtb.bankuuid=banktb.uuid inner join userinfo on userinfo.uid=cardtb.salesmanuuid  where cardtb.salesmanuuid='"+
                    userID_+"'"+ wherestr.replaceAll("where","").replaceAll("1=1",""));
    }

    private String userID_; // TODO for role
}