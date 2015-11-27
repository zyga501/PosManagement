package com.posmanagement.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.posmanagement.utils.VerifyCode;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthAction extends ActionSupport {
    public void GenerateVerifyCode() throws Exception {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse)ctx.get(ServletActionContext.HTTP_RESPONSE);
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        HttpSession session = request.getSession(true);
        int defaultWidth = 80, defaultHeight = 40;
        VerifyCode verifyCode = new VerifyCode(4);
        session.setAttribute("verifyCode", verifyCode.generateImage(defaultWidth, defaultHeight, response.getOutputStream()));
    }
}
