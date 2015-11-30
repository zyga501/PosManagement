package com.posmanagement.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.posmanagement.utils.DbManager;
import com.posmanagement.utils.LogManager;
import com.posmanagement.webui.UserMenu;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserAction extends ActionSupport{
    private final static String LOGINSUCCESS = "loginSuccess";
    private final static String LOGINFAILURE = "loginFailure";

    private String userName ;
    private String userPwd;
    private String verifyCode;
    private String loginErrorMessage;
    private String userMenu;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getLoginErrorMessage() {
        return loginErrorMessage;
    }

    public String Login() throws Exception {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        HttpSession session = request.getSession(true);

        if (!verifyCode.toUpperCase().equals(session.getAttribute("verifyCode"))) {
            loginErrorMessage = getText("UserAction.verifyCodeError");
            return LOGINFAILURE;
        }
        try {
            Map parametMap = new HashMap();
            ArrayList<HashMap<String, Object>> dbRet  = null;
            parametMap.put(1,userName);
            parametMap.put(2,userPwd);
            dbRet = DbManager.getDbManager("").executeSql("select * from userinfo where uname=? and upwd=?", (HashMap<Integer, Object>) parametMap);
            if (null == dbRet || dbRet.size() < 1){
                loginErrorMessage = getText("UserAction.loginError");
                return LOGINFAILURE;
            }

            int userID = Integer.parseInt(dbRet.get(0).get("UID").toString());
            session.setAttribute("userName", dbRet.get(0).get("UNICK"));
            session.setAttribute("userLastLoginInfo", String.format(getText("UserAction.lastLoginInfoFormat"),
                    dbRet.get(0).get("LASTLOCATION"), dbRet.get(0).get("LASTTIME")));
            session.setAttribute("userType", 0);
            session.setAttribute("userMenu", new UserMenu(userID).generateHTMLString());

            logLoginTrack(userID, request.getRemoteAddr() );
        }
        catch (Exception e){
            return LOGINFAILURE;
        }

        return LOGINSUCCESS;
    }

    private void logLoginTrack(int userID, String location) throws Exception {
        Map parametMap = new HashMap();
        Date now = new Date();
        parametMap.put(1, location);
        parametMap.put(2, new java.sql.Timestamp((now.getTime())));
        parametMap.put(3,userName);
        parametMap.put(4,userPwd);
        if (!DbManager.getDbManager("").executeUpdate("update userinfo set lastlocation=?,lasttime=? where uname=? and upwd=?", (HashMap<Integer, Object>) parametMap))
            throw new RuntimeException();
        LogManager.getInstance().writeLoginTrack(userID, location, now.toString());
    }
}
