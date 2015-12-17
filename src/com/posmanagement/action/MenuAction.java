package com.posmanagement.action;

public class MenuAction {
    private final static String SALEMANMANAGE = "salemanManager";
    private final static String TELLERMANAGE = "tellerManager";
    private final static String BANKMANAGER = "bankManager";
    private final static String CARDTIMEMANAGER = "cardTimeManager";
    private final static String MCCMANAGER = "mccManager";
    private final static String INDUSTRYMANAGER = "industryManager";
    private final static String RATESMANAGER = "ratesManager";
    private final static String CARDMANAGER = "cardManager";
    private final static String ASSETMANAGER = "assetManager";
    private final static String POSSERVERMANAGER = "posServerManager";

    public String ManageSaleMan() {
        return SALEMANMANAGE;
    }

    public String ManageTeller() {
         return TELLERMANAGE;
    }

    public String ManageBank() {
        return BANKMANAGER;
    }

    public String ManageCardTime()  {
        return CARDTIMEMANAGER;
    }

    public String ManageMCC() {
        return MCCMANAGER;
    }

    public String ManageIndustry() {
        return INDUSTRYMANAGER;
    }

    public String ManageRates() {
        return RATESMANAGER;
    }

    public String ManageCard() {
        return CARDMANAGER;
    }

    public String ManageAsset() {
        return ASSETMANAGER;
    }

    public String ManagePosServer() {
        return POSSERVERMANAGER;
    }
}