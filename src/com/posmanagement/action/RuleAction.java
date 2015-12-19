package com.posmanagement.action;

import com.posmanagement.webui.RuleList;

public class RuleAction extends AjaxActionSupport {
    private final static String RULEMANAGER = "ruleManager";
    private String ruleList;

    public String  getRuleList() {
        return ruleList;
    }

    public String Init() throws Exception {
        ruleList = new RuleList().generateHTMLString();
        return RULEMANAGER;
    }
}
