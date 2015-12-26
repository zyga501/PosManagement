package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class SwingCardAction {
    private final static String SWINGCARDMANAGER = "swingCardManager";

    public String Init() {
        return SWINGCARDMANAGER;
    }
}

class SwingCardPolicy {
    public static void main(String[] args) throws Exception {
        SwingCardPolicy policy = new SwingCardPolicy("");
        policy.generateSwingList("1");
    }

    private class BillInfo {
        public String bankCode;
        public String cardNumber;
        public Date billDate;
        public Date lastRepayDate;
        public double billAmount;
        public double canUseAmount;
    }

    private class RuleInfo {
        public String bankCode;
        public String posServerCode;
        public String swingTimeCode;
        public double minSwingMoney;
        public double maxSwingMoney;
        public String industryCode;
        public int ruleUseFre;
        public double ruleUseInterval;
        public double ruleCoolDown;
    }

    private class CardInfo {
        public int repayNum;
        public int repayInterval;
    }

    public SwingCardPolicy(String _salemanID) {
        salemanID = _salemanID;
    }

    public boolean generateSwingList(String billNumber) throws Exception {
        BillInfo billInfo = fetchBillInfo(billNumber);
        if (billInfo == null) {
            return false;
        }
        CardInfo cardInfo = fetchCardInfo(billInfo.cardNumber);
        if (cardInfo == null) {
            return false;
        }
        ArrayList<RuleInfo> ruleList = fetchRuleList(billInfo.bankCode);
        if (ruleList.size() <= 0) {
            return false;
        }

        int reTryCount = RETRYCOUNT;
        while (reTryCount-- != 0) {
            ArrayList<RuleInfo> ruleListClone = (ArrayList<RuleInfo>)ruleList.clone();
            double currentBillAmount = billInfo.billAmount;
            double canuseAmount = billInfo.canUseAmount;
            while (currentBillAmount < 0.0) {
                RuleInfo ruleInfo = nextRule(ruleList);
                if (ruleInfo == null) {
                    break;

                    // TODO
                }
            }
        }

        return false;
    }

    private RuleInfo nextRule(ArrayList<RuleInfo> ruleList) {
        ArrayList<Integer> randomList = new ArrayList<Integer>();
        while (true) {
            int randomNum = random.nextInt(ruleList.size());
            if (randomList.contains(randomNum)) {
                if (randomList.size() == ruleList.size()) {
                    return null;
                }
                continue;
            }

            // check rule Valid
            RuleInfo ruleInfo = ruleList.get(randomNum);
            if (ruleInfo.ruleUseFre <= 0)
                continue;;
            if (ruleInfo.ruleCoolDown > 0)
                continue;

            // update ruleinfo
            ruleInfo.ruleCoolDown = ruleInfo.ruleUseInterval;
            Iterator<RuleInfo> iterator = ruleList.iterator();
            while (iterator.hasNext()) {
                iterator.next().ruleCoolDown--;
            }

            return ruleInfo;
        }
    }

    private BillInfo fetchBillInfo(String billNumber) throws Exception {
        return convertBillInfo(PosDbManager.executeSql("select * from billtb where id=" + billNumber));
    }

    private BillInfo convertBillInfo(ArrayList<HashMap<String, Object>> sqlBillInfo) throws Exception {
        if (sqlBillInfo == null || sqlBillInfo.size() != 1)
            return null;

        BillInfo billInfo = new BillInfo();
        billInfo.bankCode = sqlBillInfo.get(0).get("BANKCODE").toString();
        billInfo.cardNumber = sqlBillInfo.get(0).get("CARDNO").toString();
        billInfo.billDate = Date.valueOf(sqlBillInfo.get(0).get("BILLDATE").toString());
        billInfo.lastRepayDate = Date.valueOf(sqlBillInfo.get(0).get("LASTREPAYMENTDATE").toString());
        billInfo.billAmount = Double.parseDouble(sqlBillInfo.get(0).get("BILLAMOUNT").toString());
        billInfo.canUseAmount = Double.parseDouble(sqlBillInfo.get(0).get("CANUSEAMOUNT").toString());
        return billInfo;
    }

    private ArrayList<RuleInfo> fetchRuleList(String bankCode) throws Exception {
        return convertRuleList(PosDbManager.executeSql("select * from ruletb where bankcode='" + bankCode + "'"));
    }

    private ArrayList<RuleInfo> convertRuleList(ArrayList<HashMap<String, Object>> ruleList) {
        ArrayList<RuleInfo> ruleInfoList = new ArrayList<RuleInfo>();
        if (ruleList != null) {
            Iterator<HashMap<String, Object>> iterator = ruleList.iterator();
            while (iterator.hasNext()) {
                RuleInfo ruleInfo = new RuleInfo();
                HashMap<String, Object> sqlRuleInfo = iterator.next();
                ruleInfo.bankCode = sqlRuleInfo.get("BANKCODE").toString();
                ruleInfo.industryCode = sqlRuleInfo.get("INDUSTRYCODE").toString();
                ruleInfo.posServerCode = sqlRuleInfo.get("POSSERVERCODE").toString();
                ruleInfo.swingTimeCode = sqlRuleInfo.get("SWINGTIMECODE").toString();
                ruleInfo.minSwingMoney = Double.parseDouble(sqlRuleInfo.get("MINSWINGMONEY").toString());
                ruleInfo.maxSwingMoney = Double.parseDouble(sqlRuleInfo.get("MAXSWINGMONEY").toString());
                ruleInfo.ruleUseFre = Integer.parseInt(sqlRuleInfo.get("RULEUSEFRE").toString());
                ruleInfo.ruleUseInterval = Double.parseDouble(sqlRuleInfo.get("RULEUSEINTERVAL").toString());
                ruleInfo.ruleCoolDown = 0;
                ruleInfoList.add(ruleInfo);
            }
        }
        return ruleInfoList;
    }

    private CardInfo fetchCardInfo(String cardNumber) throws Exception {
        return convertCardInof(PosDbManager.executeSql("select * from cardtb where cid='" + cardNumber + "'"));
    }

    private CardInfo convertCardInof(ArrayList<HashMap<String, Object>> sqlCardInfo) throws Exception {
        if (sqlCardInfo == null || sqlCardInfo.size() != 1)
            return null;

        CardInfo cardInfo = new CardInfo();
        cardInfo.repayNum = Integer.parseInt(sqlCardInfo.get(0).get("REPAYNUM").toString());
        cardInfo.repayInterval = Integer.parseInt(sqlCardInfo.get(0).get("REPAYINTERVAL").toString());
        return cardInfo;
    }

    private String salemanID;
    private static Random random = new Random();
    private final int RETRYCOUNT = 5;
}