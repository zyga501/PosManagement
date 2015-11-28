package com.posmanagement.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.posmanagement.utils.DbManager;
import com.posmanagement.utils.LogManager;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String Login() throws Exception {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        HttpSession session = request.getSession(true);

        if (!verifyCode.toUpperCase().equals(session.getAttribute("verifyCode"))) {
            return LOGINFAILURE;
        }
        try {
            DbManager db = new DbManager();
            Map para = new HashMap();
            ArrayList<HashMap<String, Object>> dbRet  = null;
            para.put(1,userName);
            para.put(2,userPwd);
            dbRet = db.executeSql("select * from userinfo where uname=? and upwd=?", (HashMap<Integer, Object>) para);
            if (null == dbRet || dbRet.size() < 1){
                return LOGINFAILURE;
            }
            session.setAttribute("userName", dbRet.get(0).get("UNICK"));
            session.setAttribute("userLastLoginInfo", String.format("Last Login at:%s Timer:%s",
                    dbRet.get(0).get("LASTLOCATION"), dbRet.get(0).get("LASTTIME")));
            session.setAttribute("userType", 0);
            String location = request.getRemoteAddr();
            para.clear();
            Date now = new Date();
            para.put(1, request.getRemoteAddr());
            para.put(2, new java.sql.Timestamp((now.getTime())));
            para.put(3,userName);
            para.put(4,userPwd);
            if (!db.executeUpdate("update userinfo set lastlocation=?,lasttime=? where uname=? and upwd=?", (HashMap<Integer, Object>) para))
                throw new RuntimeException();
            LogManager.getInstance().writeLoginTrack(Integer.parseInt(dbRet.get(0).get("UID").toString()), location, now.toString());
        }
        catch (Exception e){
            return LOGINFAILURE;
        }

        return LOGINSUCCESS;
    }
}
