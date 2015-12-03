package com.posmanagement.action;

import com.opensymphony.xwork2.ActionSupport;
import com.posmanagement.webui.BankList;

public class BankAction extends ActionSupport {
    private final static String BANKMANAGER = "bankManager";
    private final static String BANKADDSUCCESS = "bankAddSuccess";
    private final static String BANKADDFAILURE = "bankAddFailure";

    private String bankList;
    private String bankCode;
    private String bankName;
    private String addBankErrorMessage;

    public String getBankList() {
        return bankList;
    }

    public void setBankCode(String _bankCode) {
        bankCode = _bankCode;
    }

    public void setBankName(String _bankName) {
        bankName = _bankName;
    }

    public String getAddBankErrorMessage() {
        return addBankErrorMessage;
    }

    public String Init() throws Exception {
        bankList = new BankList().generateHTMLString();
        return BANKMANAGER;
    }

    public void AddBank() {
        if (bankCode.length() == 0 || bankName.length() == 0) {
            addBankErrorMessage = "银行代码或银行名称不能为空";
            throw new IllegalArgumentException();
        }


    }
}
