package com.posmanagement.action;

public class MenuAction {
    private final static String SALEMANMANAGE = "salemanManager";
    private final static String TELLERMANAGE = "tellerManager";
    private final static String BANKMANAGER = "bankManager";
    private final static String SWINGTIMEMANAGER = "swingTimeManager";
    private final static String MCCMANAGER = "mccManager";
    private final static String INDUSTRYMANAGER = "industryManager";
    private final static String RATEMANAGER = "rateManager";
    private final static String CARDMANAGER = "cardManager";
    private final static String ASSETMANAGER = "assetManager";
    private final static String POSSERVERMANAGER = "posServerManager";
    private final static String RULEMANAGER = "ruleManager";
    private final static String BILLMANAGER = "billManager";
    private final static String POSMANAGER = "posManager";
    private final static String SWINGCARDMANAGER = "swingCardManager";
    private final static String REPAYMANAGER="RepayManager";

    public String ManageSaleMan() {
        return SALEMANMANAGE;
    }

    public String ManageTeller() {
         return TELLERMANAGE;
    }

    public String ManageBank() {
        return BANKMANAGER;
    }

    public String ManageSwingTime()  {
        return SWINGTIMEMANAGER;
    }

    public String ManageMCC() {
        return MCCMANAGER;
    }

    public String ManageIndustry() {
        return INDUSTRYMANAGER;
    }

    public String ManageRate() {
        return RATEMANAGER;
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

    public String ManageRule() {
        return RULEMANAGER;
    }

    public String ManageBill() {
        return BILLMANAGER;
    }

    public String ManagePOS() {
        return POSMANAGER;
    }

    public String ManageSwingCard() {
        return SWINGCARDMANAGER;
    }
    public String ManageRepayment(){ return REPAYMANAGER;}
}