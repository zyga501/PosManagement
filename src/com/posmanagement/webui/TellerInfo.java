package com.posmanagement.webui;

import com.posmanagement.utils.DbManager;
import com.posmanagement.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TellerInfo {
    public TellerInfo(String _tellerID) {
        tellerID = _tellerID;
    }

    public String generateHTMLString() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchTellerInfo();
        if (dbRet.size() != 1)
            return new String("");

        String htmlString ="<form><table class=\"table table-border table-bordered table-hover\">";
            htmlString +="<tr><td><input type=\"hidden\" name=\"tellerID\" value=" + dbRet.get(0).get("UID") +
                    ">用户名称</td><td><input type=\"text\" name=\"tellerNickName\" class=\"input-text radius\" value=" +
                    StringUtils.convertNullableString(dbRet.get(0).get("UNICK"))+" </td></tr>"+
                    "<tr><td>卡号</td><td><input type=\"text\" name=\"cardID\" class=\"input-text radius\" VALUE=" +
                            StringUtils.convertNullableString(dbRet.get(0).get("SCARDNO"))+"></td></tr>"+
                    "<tr><td>状态</td><td><input type=\"text\" name=\"status\" class=\"input-text radius\" VALUE=" +
                            StringUtils.convertNullableString(dbRet.get(0).get("STATUS"))+"></td></tr>"+
                    "<tr><td>所属业务员</td><td><input type=\"text\" name=\"saleManID\" class=\"input-text radius\" readonly=\"readonly\" VALUE=" +
                            StringUtils.convertNullableString(dbRet.get(0).get("SALESMAN"))+"></td></tr>"+
                    "<tr><td>联系情况</td><td><input type=\"text\" name=\"contract\" class=\"input-text radius\" VALUE=" +
                            StringUtils.convertNullableString(dbRet.get(0).get("CONTRACT"))+"></td></tr>";

        return htmlString+"</table></form>";
    }

    private ArrayList<HashMap<String, Object>> fetchTellerInfo() throws Exception {
        Map parametMap = new HashMap<Integer, Object>();
        parametMap.put(1, tellerID);
        return DbManager.createPosDbManager().executeSql("select * from userinfo a,tellertb b where a.uid=b.uid and b.uid=?",
                (HashMap<Integer, Object>) parametMap);
    }

    private String tellerID;
}
