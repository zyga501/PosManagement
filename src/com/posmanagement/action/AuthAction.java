package com.posmanagement.action;

import com.posmanagement.utils.VerifyCode;

public class AuthAction extends AjaxActionSupport {
    public void GenerateVerifyCode() throws Exception {
        super.getResponse().setHeader("Pragma", "No-cache");
        super.getResponse().setHeader("Cache-Control", "no-cache");
        super.getResponse().setDateHeader("Expires", 0);
        super.getResponse().setContentType("image/jpeg");
        int defaultWidth = 100, defaultHeight = 40;
        VerifyCode verifyCode = new VerifyCode(4);
        super.setAttribute("verifyCode", verifyCode.generateImage(defaultWidth, defaultHeight, super.getResponse().getOutputStream()));
    }
}
