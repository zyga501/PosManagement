package com.posmanagement.action;

public class MenuAction {
    private final static String SALEMAN = "saleManManager";
    private final static String BANKMANAGER = "bankManager";

    public String ManageSaleMan() throws Exception {
        return SALEMAN;
    }

    public String ManageBank() throws Exception {
        return BANKMANAGER;
    }
}