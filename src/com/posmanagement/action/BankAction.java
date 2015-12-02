package com.posmanagement.action;

import com.opensymphony.xwork2.ActionSupport;

public class BankAction extends ActionSupport {
    private final static String BANKMANAGER = "bankManager";
    private String bankList;

    public String Init() {
        bankList = new String("当前有3家银行");
        return BANKMANAGER;
    }

    public String getBankList() {
        return bankList;
    }
}
