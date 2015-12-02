package com.posmanagement.action;

import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class MenuAction {
    private final static String SALEMAN = "saleMan";

    public String SaleMan() throws Exception {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        HttpSession session = request.getSession(false);

        return SALEMAN;
    }
}