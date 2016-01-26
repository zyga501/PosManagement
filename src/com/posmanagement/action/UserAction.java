package com.posmanagement.action;

import com.posmanagement.utils.*;
import com.posmanagement.webui.SalemanUI;
import com.posmanagement.webui.TellerUI;
import com.posmanagement.webui.UserMenu;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserAction extends AjaxActionSupport{
    private final static String LOGINSUCCESS = "loginSuccess";
    private final static String LOGINFAILURE = "loginFailure";
    private final static String MAGEPAGELOADED = "mainPageInited";
    private final static String LOGOUT = "logout";

    private String userName ;
    private String userNickName;
    private String userPwd;
    private String userType;
    private String verifyCode;
    private String loginErrorMessage;
    private HashMap<String, Object> personInfo;
    private String userNewPwd;
    private String userList;
    private String userMenu;
    private String userLastLoginInfo;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setUserNewPwd(String userNewPwd) {
        this.userNewPwd = userNewPwd;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getLoginErrorMessage() {
        return loginErrorMessage;
    }

    public HashMap<String, Object> getPersonInfo() {
        return personInfo;
    }

    public String getUserList() {
        return userList;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public String getUserMenu() {
        return userMenu;
    }

    public String getUserLastLoginInfo() {
        return userLastLoginInfo;
    }

    public String InitMainPage() throws Exception {
        if (super.getUserID().isEmpty())
            return LOGINFAILURE;
        userMenu = new UserMenu(super.getUserID()).generateHTMLString();
        userNickName = super.getAttribute("userNick");
        return MAGEPAGELOADED;
    }

    public String Login() throws Exception {
        // Disable Now
        //if (!verifyCode.toUpperCase().equals(session.getAttribute("verifyCode"))) {
        //    loginErrorMessage = getText("UserAction.verifyCodeError");
        //    session.removeAttribute("verifyCode");
        //    return LOGINFAILURE;
        //}

        try {
            Map parametMap = new HashMap();
            ArrayList<HashMap<String, Object>> dbRet  = null;
            parametMap.put(1,userName);
            parametMap.put(2,userPwd);
            dbRet = PosDbManager.executeSql("select * from userinfo where uname=? and upwd=?", (HashMap<Integer, Object>) parametMap);
            if (null == dbRet || dbRet.size() < 1){
                loginErrorMessage = getText("UserAction.loginError");
                return LOGINFAILURE;
            }

            String userID = (dbRet.get(0).get("UID").toString());
            super.setUserID(userID);
            super.setUserName(userName);
            super.setAttribute("userNick", dbRet.get(0).get("UNICK").toString());
            super.setAttribute("lastLocation", StringUtils.convertNullableString(dbRet.get(0).get("LASTLOCATION"), "?.?.?.?"));
            super.setAttribute("lastTime", StringUtils.convertNullableString(dbRet.get(0).get("LASTTIME"), "????-??-?? ??:??:??.?"));
            super.setAttribute("roleId", dbRet.get(0).get("RID").toString());

            logLoginTrack(userID, super.getRequest().getRemoteAddr() );
        }
        catch (Exception e){
            super.removeAttribute("verifyCode");
            return LOGINFAILURE;
        }

        super.removeAttribute("verifyCode");
        return LOGINSUCCESS;
    }

    public String Logout() throws Exception {
        HttpSession session = super.getRequest().getSession(false);
        if (null!=session)
            session.invalidate();
        return LOGOUT;
    }

    public String FetchPersonInfo() throws Exception {
        try {
            Map parametMap = new HashMap();
            parametMap.put(1, super.getUserName());
            ArrayList<HashMap<String, Object>> dbRet = PosDbManager.executeSql("select * from userinfo a left outer join usertrack b on a.uid=b.uid where uname=?", (HashMap<Integer, Object>) parametMap);
            if (null == dbRet || dbRet.size() < 1){
                return LOGINFAILURE;
            }
            personInfo = dbRet.get(0);
            userLastLoginInfo = String.format(getText("UserAction.userLastLoginInfo"),
                    super.getAttribute("lastLocation"), super.getAttribute("lastTime"));
        }
        catch (Exception e){
            return LOGINFAILURE;
        }
        return "personInfo";
    }

    public void  ModifyPassword() throws Exception {
        String rtMsg = new String("");
        try {
            Map parametMap = new HashMap();
            parametMap.put(1,userNewPwd);
            parametMap.put(2,userPwd);
            parametMap.put(3,userName);
            if (PosDbManager.executeUpdate("update userinfo set upwd=? where  upwd=? and uname=?", (HashMap<Integer, Object>) parametMap))
                    rtMsg = "更改成功！";
            else
                rtMsg = "操做失败！";
        }
        catch (Exception e){
            return ;
        }
        super.getResponse().setCharacterEncoding("UTF-8");
        super.getResponse().getWriter().write(rtMsg);
        super.getResponse().getWriter().flush();
        super.getResponse().getWriter().close();
    }

    public String Register() throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = PosDbManager.executeSql(
                "select 1 from userinfo where uname='" + userName + "'");
        if (dbRet.size()>0) return AjaxActionComplete();

        Map parametMap = new HashMap<Integer, Object>();
        String UUID = UUIDUtils.generaterUUID();
        parametMap.put(1, UUID);
        parametMap.put(2,userPwd);
        parametMap.put(3,userNickName);
        if (UserUtils.isAdmin(super.getUserID())) {
             parametMap.put(4, userName);
            if (PosDbManager.executeUpdate("insert into userinfo(uid,upwd,unick,uname,rid) values(?,?,?,?,'69632ae8-7e48-4e72-ad58-1043ad655a4c')", (HashMap<Integer, Object>) parametMap)) {
                PosDbManager.executeUpdate("insert into salesmantb(uid) values('" + UUID + "')");
                Map resultMap = new HashMap();
                resultMap.put("userList", new SalemanUI().generateTable());
                return AjaxActionComplete(resultMap);
            }
        }
        else {
            parametMap.put(4, super.getUserName());
            parametMap.put(5, super.getUserID());
            if (PosDbManager.executeUpdate("insert into userinfo(uid,upwd,unick,uname,rid) select ?,?,?,CONCAT(?,count(*))" +
                    ",'f5466ce9-5e10-4443-aac8-5e385c3febb0' from tellertb a where a.salesman=?", (HashMap<Integer, Object>) parametMap)){
                PosDbManager.executeUpdate("insert into tellertb(uid,salesman) values('" + UUID + "','"+super.getUserID()+"')");
                Map resultMap = new HashMap();
                resultMap.put("userList", new TellerUI().generateTable(super.getUserID(), false));
                return AjaxActionComplete(resultMap);
            }
        }

        return AjaxActionComplete();
    }

    private void logLoginTrack(String userID, String location) throws Exception {
        Map parametMap = new HashMap();
        Date now = new Date();
        parametMap.put(1, location);
        parametMap.put(2, new java.sql.Timestamp((now.getTime())));
        parametMap.put(3,userName);
        parametMap.put(4,userPwd);
        if (!PosDbManager.executeUpdate("update userinfo set lastlocation=?,lasttime=? where uname=? and upwd=?", (HashMap<Integer, Object>) parametMap))
            throw new RuntimeException();
        LogManager.getInstance().writeLoginTrack(userID, location, now.toString());
    }

    public String FetchSalemanList() throws Exception {
        Map map = new HashMap();
        map.put("userList", new SalemanUI().generateSelect());

        return AjaxActionComplete(map);
    }
}
