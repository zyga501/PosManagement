package com.posmanagement.action;

import com.posmanagement.webui.SwingCardList;
import com.posmanagement.webui.WebUI;

public class SwingCardAction extends AjaxActionSupport {
    private final static String SWINGCARDMANAGER = "swingCardManager";

    private String swingCardList;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSwingCardList() {

        return swingCardList;
    }

    public void setSwingCardList(String swingCardList) {
        this.swingCardList = swingCardList;
    }

    public String Init() throws Exception {
        swingCardList = new SwingCardList(WebUI.UIMode.TABLELIST).generateHTMLString();
        return SWINGCARDMANAGER;
    }
}