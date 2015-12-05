package com.posmanagement.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.posmanagement.utils.DbManager;
import com.posmanagement.utils.LogManager;
import com.posmanagement.webui.UserList;
import com.posmanagement.webui.UserMenu;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserAction extends ActionSupport{
    private final static String LOGINSUCCESS = "loginSuccess";
    private final static String LOGINFAILURE = "loginFailure";
    private final static String MAGEPAGELOADED = "mainPageInited";
    private final static String LOGOUT = "logout";
    private final static String USERLIST = "userList";

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

        if (!verifyCode.toUpperCase().equals(session.getAttribute("verifyCode"))) {
            loginErrorMessage = getText("UserAction.verifyCodeError");
            session.removeAttribute("verifyCode");
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
            ArrayList<HashMap<String, Object>> dbRet = DbManager.getDbManager("").executeSql("select * from userinfo a left outer join usertrack b on a.uid=b.uid where uname=?", (HashMap<Integer, Object>) parametMap);
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
            if (DbManager.getDbManager("").executeUpdate("update userinfo set upwd=? where  upwd=? and uname=?", (HashMap<Integer, Object>) parametMap))
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

    public void ListSalesman() throws Exception {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ctx
                .get(ServletActionContext.HTTP_RESPONSE);
        HttpSession session = request.getSession(false);
        try {
            ArrayList<HashMap<String, Object>> dbRet = DbManager.getDbManager("").executeSql("select * from userinfo a,salesmantb b where a.uid=b.uid");
            if (null == dbRet || dbRet.size() < 1){
                return ;
            }
            String inputType = "";
            if (null!=request.getParameter("type") && request.getParameter("type").equals("1"))
                inputType="checkbox";
            else
                inputType="radio";
            userList = UserList.userListToHtml(dbRet,inputType);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(userList);
            response.getWriter().flush();
            response.getWriter().close();
        }
        catch (Exception e){
            return ;
        }
        return ;
    }

    public void ListTeller() throws Exception {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ctx
                .get(ServletActionContext.HTTP_RESPONSE);
        HttpSession session = request.getSession(false);
        String sqlstr ="";
        Map para = new HashMap<>();
        if (null!=request.getParameter("datas")){
            sqlstr ="select * from userinfo a,tellertb b where a.uid=b.uid and salesmanid=?";
            para.put(1,request.getParameter("datas").toString());
        }
        else
            sqlstr = "select * from userinfo a,tellertb b where a.uid=b.uid";
        try {
            ArrayList<HashMap<String, Object>> dbRet = DbManager.getDbManager("").executeSql(sqlstr, (HashMap<Integer, Object>) para);
            if (null == dbRet || dbRet.size() < 1){
                return ;
            }
            String inputType = "";
            if (null!=request.getParameter("type") && request.getParameter("type").equals("1"))
                inputType="checkbox";
            else
                inputType="radio";
            userList = UserList.userListToHtml(dbRet,inputType);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(userList);
            response.getWriter().flush();
            response.getWriter().close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return ;
        }
        return ;
    }

    public void InsertTeller() throws Exception {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ctx
                .get(ServletActionContext.HTTP_RESPONSE);
        HttpSession session = request.getSession(false);
        //// TODO: 2015-12-04    All database SQL DML operations need to be verified "admin"
        if (!session.getAttribute("userName").toString().toUpperCase().equals("ADMIN")) return;
        String datas= request.getParameter("datas").toString();
        String[] ararystr = datas.split(",");
        String outstr = "";
        if (ararystr.length!=2) return;
        try {
            Map para = new HashMap<>();
            para.put(1,ararystr[0]);
            para.put(2,ararystr[1]);
            if (!DbManager.getDbManager("").executeUpdate("update tellertb set salesmanid=? where uid=?",
                    (HashMap<Integer, Object>) para)) {
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("Error!");
                response.getWriter().flush();
                response.getWriter().close();
            }
        }
        catch (Exception e){
            return ;
        }
        return ;
    }

    public void SalemanProperty() throws Exception {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ctx
                .get(ServletActionContext.HTTP_RESPONSE);
        HttpSession session = request.getSession(false);
        String datas= request.getParameter("datas").toString();
        try {
            Map para = new HashMap<>();
            para.put(1,datas);
            ArrayList<HashMap<String, Object>> dbRet = DbManager.getDbManager("").executeSql("select * from userinfo a,salesmantb b where a.uid=b.uid and b.uid=?",
                    (HashMap<Integer, Object>) para);
            if (null == dbRet || dbRet.size() < 1){
                return ;
            }
            userList = UserList.salesmanPropertylistToTable(dbRet);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(userList);
            response.getWriter().flush();
            response.getWriter().close();
        }
        catch (Exception e){
            return ;
        }
        return ;
    }

    public void TellerProperty() throws Exception {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ctx
                .get(ServletActionContext.HTTP_RESPONSE);
        HttpSession session = request.getSession(false);
        String datas= request.getParameter("datas").toString();
        try {
            Map para = new HashMap<>();
            para.put(1,datas);
            ArrayList<HashMap<String, Object>> dbRet = DbManager.getDbManager("").executeSql("select * from userinfo a,tellertb b where a.uid=b.uid and b.uid=?",
                    (HashMap<Integer, Object>) para);
            if (null == dbRet || dbRet.size() < 1){
                return ;
            }
            userList = UserList.tellerPropertylistToTable(dbRet);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(userList);
            response.getWriter().flush();
            response.getWriter().close();
        }
        catch (Exception e){
            return ;
        }
        return ;
    }

    public void Register() throws Exception {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        HttpSession session = request.getSession(false);
        if (!session.getAttribute("userName").toString().toLowerCase().equals("admin")) return ;
        try {
            Map para = new HashMap<>();
            para.put(1,userName);
            ArrayList<HashMap<String, Object>> dbRet = DbManager.getDbManager("").executeSql(
                    "select 1 from userinfo where uname=?", (HashMap<Integer, Object>) para);
            if (dbRet.size()>0) return ;
            para.put(2,userPwd);
            para.put(3,userNickName);
            // para.put(4,userType);
            if  (!DbManager.getDbManager("").executeUpdate("insert into userinfo(uname,upwd,unick) values(?,?,?)", (HashMap<Integer, Object>) para))
                return  ;
            dbRet = DbManager.getDbManager("").executeSql(
                    "select uid from userinfo where uname=? and upwd=? and unick=?", (HashMap<Integer, Object>) para);
            if ("1".equals(userType))
                DbManager.getDbManager("").executeUpdate("insert into salesmantb(uid) values("+dbRet.get(0).get("UID")+")");
            else if ("2".equals(userType))
                DbManager.getDbManager("").executeUpdate("insert into tellertb(uid) values("+dbRet.get(0).get("UID")+")");
        }
        catch (Exception e){
            return  ;
        }
    }

    public void InsertSaleMan() throws Exception {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        HttpSession session = request.getSession(false);
        if (null==request.getParameter("newuserid")) return ;
        String params = request.getParameter("newuserid").toString();
        if (params.endsWith(","))
            params = params.substring(0,params.length()-1);
        Map para = new HashMap();
        para.put(1,params);
        try {
            if (DbManager.getDbManager("").executeUpdate("select uid from userinfo where uid in (?)", (HashMap<Integer, Object>) para)) {

            }
            else {
                int throwerror = 1 / 0;
            }
        }
        catch (Exception e){
            return ;
        }
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
