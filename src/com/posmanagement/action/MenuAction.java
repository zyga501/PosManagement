package com.posmanagement.action;

public class MenuAction {
    private final static String SALEMANAGE = "saleManager";
    private final static String TELLERMANAGE = "tellerManager";
    private final static String BANKMANAGER = "bankManager";
    private final static String CARDTIMERMANAGER = "cardTimerManager";

    public String ManageSaleMan() throws Exception {
        return SALEMANAGE;
    }

    public String ManageTeller() throws Exception {
         return TELLERMANAGE;
    }

    public String ManageBank() throws Exception {
        return BANKMANAGER;
    }

    public String ManageCardTimer() throws Exception {
        return CARDTIMERMANAGER;
    }
}