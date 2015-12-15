package com.posmanagement.action;

import com.opensymphony.xwork2.ActionContext;
import com.posmanagement.utils.DbManager;
import com.posmanagement.utils.LogManager;
import com.posmanagement.webui.SalemanList;
import com.posmanagement.webui.TellerList;
import com.posmanagement.webui.UserMenu;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        HttpSession session = request.getSession(true);
        if (session.getAttribute("userID") == null)
            return LOGINFAILURE;
        userMenu = new UserMenu(Integer.parseInt(session.getAttribute("userID").toString())).generateHTMLString();
        userNickName = session.getAttribute("userNick").toString();
        return MAGEPAGELOADED;
    }

    public String Login() throws Exception {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        HttpSession session = request.getSession(true);

        // Disabled Now
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
            dbRet = DbManager.createPosDbManager().executeSql("select * from userinfo where uname=? and upwd=?", (HashMap<Integer, Object>) parametMap);
            if (null == dbRet || dbRet.size() < 1){
                loginErrorMessage = getText("UserAction.loginError");
                return LOGINFAILURE;
            }

            int userID = Integer.parseInt(dbRet.get(0).get("UID").toString());
            session.setAttribute("userID", userID);
            session.setAttribute("userName", userName);
            session.setAttribute("userNick", dbRet.get(0).get("UNICK"));
            session.setAttribute("lastLocation", dbRet.get(0).get("LASTLOCATION"));
            session.setAttribute("lastTime", dbRet.get(0).get("LASTTIME"));
            session.setAttribute("userType", 0);

            logLoginTrack(userID, request.getRemoteAddr() );
        }
        catch (Exception e){
            session.removeAttribute("verifyCode");
            return LOGINFAILURE;
        }

        session.removeAttribute("verifyCode");
        return LOGINSUCCESS;
    }

    public String Logout() throws Exception {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
        HttpSession session = request.getSession(false);
        if (null!=session)
            session.invalidate();
        return LOGOUT;
    }

    public String FetchPersonInfo() throws Exception {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx
                        .get(ServletActionContext.HTTP_REQUEST);
        HttpSession session = request.getSession(false);
        try {
            Map parametMap = new HashMap();
            parametMap.put(1,session.getAttribute("userName"));
            ArrayList<HashMap<String, Object>> dbRet = DbManager.createPosDbManager().executeSql("select * from userinfo a left outer join usertrack b on a.uid=b.uid where uname=?", (HashMap<Integer, Object>) parametMap);
            if (null == dbRet || dbRet.size() < 1){
                return LOGINFAILURE;
            }
            personInfo = dbRet.get(0);
            userLastLoginInfo = String.format(getText("UserAction.userLastLoginInfo"),
                    session.getAttribute("lastLocation").toString(), session.getAttribute("lastTime").toString());
        }
        catch (Exception e){
            return LOGINFAILURE;
        }
        return "personInfo";
    }

    public void  ModifyPassword() throws Exception {
        ActionContext ctx = ActionContext.getContext();
        HttpServletResponse response = (HttpServletResponse) ctx
                        .get(ServletActionContext.HTTP_RESPONSE);
        String rtMsg = new String("");
        try {
            Map parametMap = new HashMap();
            parametMap.put(1,userNewPwd);
            parametMap.put(2,userPwd);
            parametMap.put(3,userName);
            if (DbManager.createPosDbManager().executeUpdate("update userinfo set upwd=? where  upwd=? and uname=?", (HashMap<Integer, Object>) parametMap))
                    rtMsg = "更改成功！";
            else
                rtMsg = "操做失败！";
        }
        catch (Exception e){
            return ;
        }
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(rtMsg);
        response.getWriter().flush();
        response.getWriter().close();
    }

    public String Register() throws Exception {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        HttpSession session = request.getSession(false);
        if (!session.getAttribute("userName").toString().toLowerCase().equals("admin")) return AjaxActionComplete();
        Map parametMap = new HashMap<Integer, Object>();
        parametMap.put(1,userName);
        ArrayList<HashMap<String, Object>> dbRet = DbManager.createPosDbManager().executeSql(
                "select 1 from userinfo where uname=?", (HashMap<Integer, Object>) parametMap);
        if (dbRet.size()>0) return AjaxActionComplete();

        parametMap.put(2,userPwd);
        parametMap.put(3,userNickName);

        if  (!DbManager.createPosDbManager().executeUpdate("insert into userinfo(uname,upwd,unick) values(?,?,?)", (HashMap<Integer, Object>) parametMap))
            return AjaxActionComplete();
        dbRet = DbManager.createPosDbManager().executeSql(
                "select uid from userinfo where uname=? and upwd=? and unick=?", (HashMap<Integer, Object>) parametMap);

        Map resultMap = new HashMap();

        if ("1".equals(userType)) {
            DbManager.createPosDbManager().executeUpdate("insert into salesmantb(uid) values(" + dbRet.get(0).get("UID") + ")");
            resultMap.put("userList", new SalemanList().generateHTMLString());
        }
        else if ("2".equals(userType)) {
            DbManager.createPosDbManager().executeUpdate("insert into tellertb(uid) values(" + dbRet.get(0).get("UID") + ")");
            resultMap.put("userList", new TellerList().generateHTMLString());
        }

        return AjaxActionComplete(resultMap);
    }

    private void logLoginTrack(int userID, String location) throws Exception {
        Map parametMap = new HashMap();
        Date now = new Date();
        parametMap.put(1, location);
        parametMap.put(2, new java.sql.Timestamp((now.getTime())));
        parametMap.put(3,userName);
        parametMap.put(4,userPwd);
        if (!DbManager.createPosDbManager().executeUpdate("update userinfo set lastlocation=?,lasttime=? where uname=? and upwd=?", (HashMap<Integer, Object>) parametMap))
            throw new RuntimeException();
        LogManager.getInstance().writeLoginTrack(userID, location, now.toString());
    }
}
