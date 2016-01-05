package com.posmanagement.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.posmanagement.utils.StringUtils;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

interface UserSession
{
    public HttpServletResponse getResponse();
    public HttpServletRequest getRequest();
    public String getUserID();
    public void setUserID(String userID);
    public String getUserName();
    public void setUserName(String userName);
    public String getAttribute(String key);
    public void setAttribute(String key, Object value);
    public void removeAttribute(String key);
}

public abstract class AjaxActionSupport extends ActionSupport implements UserSession {
    private final static String AJAXACTIONCOMPLETED = "ajaxActionCompleted";
    private String ajaxActionResult;
    private Map parameterMap;

    public HttpServletResponse getResponse() {
        return (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
    }

    public HttpServletRequest getRequest() {
        return (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
    }

    public String getUserID() {
        if (getRequest().getSession(false) != null) {
            return StringUtils.convertNullableString(getRequest().getSession(false).getAttribute("userID"));
        }
        return new String();
    }

    public void setUserID(String userID) {
        getRequest().getSession(true).setAttribute("userID", userID);
    }

    public String getUserName() {
        if (getRequest().getSession(false) != null) {
            return StringUtils.convertNullableString(getRequest().getSession(false).getAttribute("userName"));
        }
        return new String();
    }

    public void setUserName(String userName) {
        getRequest().getSession(true).setAttribute("userName", userName);
    }

    public String getAttribute(String key) {
        if (getRequest().getSession(false) != null) {
            return StringUtils.convertNullableString(getRequest().getSession(false).getAttribute(key));
        }
        return new String();
    }

    public void setAttribute(String key, Object value) {
        getRequest().getSession(true).setAttribute(key, value);
    }

    public void removeAttribute(String key) {
        if (getRequest().getSession(false) != null) {
            getRequest().getSession(false).removeAttribute(key);
        }
    }

    public void validate() {
        // lazyvalidate
        ActionContext context = ActionContext.getContext();
        String actionName = context.getName();
        if (getUserID().isEmpty() && (!actionName.toLowerCase().equals("user!login")) && (!actionName.toLowerCase().equals("user!logout"))) {
            try {
                getResponse().sendRedirect(getRequest().getContextPath()+"/login.jsp");
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        parameterMap = context.getParameters();
    }

    public Object getParameter(String paramterName) {
        if (parameterMap == null || parameterMap.size() == 0) {
            return null;
        }

        if (parameterMap.containsKey(paramterName)) {
            return ((Object[]) parameterMap.get(paramterName))[0];
        }

        return null;
    }

    public String getAjaxActionResult() {
        return ajaxActionResult;
    }
    public String AjaxActionComplete() {
        return AJAXACTIONCOMPLETED;
    }
    public String AjaxActionComplete( Map resultMap) {
        ajaxActionResult = JSONObject.fromObject(resultMap).toString();
        return AJAXACTIONCOMPLETED;
    }
}
