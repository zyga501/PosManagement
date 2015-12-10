package com.posmanagement.webui;

import java.util.ArrayList;
import java.util.HashMap;

public class UserList {
    private static String checknull(Object obj){
        if (null==obj)
            return "";
        else return obj.toString();
    }
    public static String  userListToHtml( ArrayList<HashMap<String, Object>> dbRet,String inputType){
        String result ="<table class=\"table table-border table-bordered table-hover\">";
        for (int index = 0; index < dbRet.size(); ++index) {
            result +="<tr class=\"text-c odd\" role=\"row\">"+
                    "<td><input type=\"hidden\" name=\"userpick\" id=pick"+String.valueOf(index)+"  value="+dbRet.get(index).get("UID")+
                    ">"+String.valueOf(index+1)+" </td>"+
                    "<td>"+dbRet.get(index).get("UNICK")+"</td>"+
                    "<td>"+dbRet.get(index).get("UNAME")+"</td></tr>";
        }
        return result+"</table>";
    }

    public static String  tellerPropertylistToTable( ArrayList<HashMap<String, Object>> dbRet){
        String result ="<form><table class=\"table table-border table-bordered table-hover\">";
        for (int index = 0; index < dbRet.size(); ++index) {
            result +="<tr>"+
                    "<td><input type=\"hidden\" name=\"uid\" id=ppick"+String.valueOf(index)+"  value="+dbRet.get(index).get("UID")+
                    ">用户名称</td><td><input type=\"text\" class=\"input-text radius\" value="+checknull(dbRet.get(index).get("UNICK"))+" </td></tr>"+
                    "<tr><td>卡号</td><td><input type=\"text\" class=\"input-text radius\" VALUE="+checknull(dbRet.get(index).get("SCARDNO"))+"></td></tr>"+
                    "<tr><td>状态</td><td><input type=\"text\" class=\"input-text radius\" VALUE="+checknull(dbRet.get(index).get("STATUS"))+"></td></tr>"+
                    "<tr><td>所属业务员</td><td><input type=\"text\" class=\"input-text radius\" readonly=\"readonly\" VALUE="+checknull(dbRet.get(index).get("SALESMAN"))+"></td></tr>"+
                    "<tr><td>联系情况</td><td><input type=\"text\" class=\"input-text radius\" VALUE="+checknull(dbRet.get(index).get("CONTRACT"))+"></td></tr>";
        }
        return result+"</table></form>";
    }
}
