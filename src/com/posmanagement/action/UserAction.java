package com.posmanagement.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.posmanagement.utils.DbManager;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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

    /*20151123
         *首先判断是否管理者
         *接着判断业务员表，最后才判断柜员表
        */
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
            ArrayList<HashMap<String, Object>> rtal  = null;
            para.put(1,userName);
            para.put(2,userPwd);
            rtal = db.executeSql("select  * from manager where uname=? and upwd=?", (HashMap<Integer, Object>) para);
            if (null==rtal || rtal.size()<1){
                return LOGINFAILURE;
            }
            session.setAttribute("userName",rtal.get(0).get("UNAME"));
            session.setAttribute("USERTYPE",999);//manager
        }
        catch (Exception e){
            return LOGINFAILURE;
        }

        return LOGINSUCCESS;
    }
}
