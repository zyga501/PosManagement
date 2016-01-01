package com.posmanagement.action;

import com.posmanagement.webui.SwingCardUI;

public class SwingCardAction extends AjaxActionSupport {
    private final static String SWINGCARDMANAGER = "swingCardManager";
    private final static String SWINGCARDDETAIL = "swingCardDetail";

    private String swingCardSummary;
    private String swingCardDetail;

    public String getSwingCardSummary() {
        return swingCardSummary;
    }

    public String getSwingCardDetail() {
        return swingCardDetail;
    }

    public String Init() throws Exception {
        swingCardSummary = new SwingCardUI().generateSummary();
        return SWINGCARDMANAGER;
    }

    public String InitDetail() throws Exception {
        swingCardDetail = new SwingCardUI().generateDetail();
        return SWINGCARDDETAIL;
    }
}