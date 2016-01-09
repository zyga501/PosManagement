package com.posmanagement.policy;

import com.posmanagement.utils.PosDbManager;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class SwingCardPolicy {
    public static void main(String[] args) throws Exception {
        SwingCardPolicy policy = new SwingCardPolicy("");
        policy.generateSwingList("1");
    }

    public class SwingList {
        public String cardNO;
        public int billYear;
        public int billMonth;
        public int repayYear;
        public int repayMonth;
        public ArrayList<SwingCardInfo> swingCardList;
        public ArrayList<RepayInfo> repayList;
    }

    private class BillInfo {
        public String bankUUID;
        public String cardNO;
        public Date billDate;
        public Date lastRepayDate;
        public double billAmount;
        public double canUseAmount;
    }

    private class PolicyInfo {
        public int useNumber;
        public double useInterval;
        public double coolingTime;
    }

    private class RuleInfo extends PolicyInfo {
        public String ruleUUID;
        public String bankUUID;
        public String posServerUUID;
        public Time swingStartTime;
        public Time swingEndTime;
        public double minSwingMoney;
        public double maxSwingMoney;
        public String industryUUID;
        public String rateUUID;
        public String mccUUID;
        public int ruleUseFre;
        public double ruleUseInterval;
    }

    private class CardInfo extends PolicyInfo {
        public String cardNO;
        public double creditAmount;
        public int repayNum;
        public int repayInterval;
    }

    public class SwingCardInfo {
        public String ruleUUID;
        public double money;
        public Date swingDate;
        public Time swingTime;
        public String posUUID;
    }

    public class RepayInfo {
        public double money;
        public Date repayDate;
    }

    public SwingCardPolicy(String _salemanID) {
        salemanID = _salemanID;
    }

    public SwingList generateSwingList(String billNumber) throws Exception {
        billInfo_ = fetchBillInfo(billNumber);
        if (billInfo_ == null) {
            return null;
        }
        cardInfo_ = fetchCardInfo(billInfo_.cardNO);
        if (cardInfo_ == null) {
            return null;
        }

        ruleList_ = fetchRuleList(billInfo_.bankUUID);
        if (ruleList_.size() <= 0) {
            return null;
        }

        initPolicyInfo();

        double remainBillAmount = billInfo_.billAmount;
        double canUseAmount = billInfo_.canUseAmount;
        double maxSwingAmount = generaterSwingAmount();
        ArrayList<SwingCardInfo> swingCardList = new ArrayList<SwingCardInfo>();
        ArrayList<RepayInfo> repayList = new ArrayList<RepayInfo>();

        if (cardInfo_.repayNum < 1) {
            return null;
        }
        else if (cardInfo_.repayNum == 1){
            canUseAmount += remainBillAmount;
            repayList.add(generateLastRepay(remainBillAmount, 0));
            remainBillAmount = 0.0;
            updateCardInfo();
        }

        double dateLimit = (billInfo_.lastRepayDate.getTime() - billInfo_.billDate.getTime()) / (24 * 60 * 60 * 1000);
        if (dateLimit < 0) {
            return null;
        }

        double curDate = random.nextDouble() * cardInfo_.repayInterval + 1;
        while (curDate < dateLimit) {
            RuleInfo ruleInfo = nextRule();
            if (ruleInfo == null) {
                break;
            }

            boolean swingDone = true;
            double nextDateLimit = nextDateLimit();
            try {
                SwingCardInfo swingCardInfo = generateSwingInfo(ruleInfo, curDate);
                if (swingCardInfo.money > canUseAmount) {
                    RepayInfo repayInfo = generateRepayInfo(curDate);
                    if (repayInfo == null)
                        continue;
                    updateCardInfo();
                    canUseAmount += repayInfo.money;
                    remainBillAmount -= repayInfo.money;
                    repayList.add(repayInfo);
                }

                if (swingCardInfo.money < canUseAmount) {
                    if (maxSwingAmount < swingCardInfo.money && maxSwingAmount < ruleInfo.minSwingMoney) {
                        swingDone = false;
                        continue;
                    }
                    updateRuleInfo(ruleInfo);
                    canUseAmount -= swingCardInfo.money;
                    maxSwingAmount -= swingCardInfo.money;
                    swingCardList.add(swingCardInfo);
                }
            }
            finally {
                if (swingDone) {
                    updateCoolingTime(nextDateLimit);
                    curDate += nextDateLimit;
                }
            }
        }

        if (remainBillAmount > 0.0) {
            repayList.add(generateLastRepay(remainBillAmount, dateLimit));
        }

        return generateSwingList( swingCardList, repayList);
    }

    private void initPolicyInfo() {
        PolicyInfo policyInfo = cardInfo_;
        policyInfo.useNumber = cardInfo_.repayNum;
        policyInfo.useInterval = cardInfo_.repayInterval;
        policyInfo.coolingTime = 0.0;

        Iterator<RuleInfo> iterator = ruleList_.iterator();
        while (iterator.hasNext()) {
            RuleInfo ruleInfo = iterator.next();
            policyInfo = ruleInfo;
            policyInfo.useNumber = ruleInfo.ruleUseFre;
            policyInfo.useInterval = ruleInfo.ruleUseInterval;
            policyInfo.coolingTime = 0.0;
        }
    }

    private SwingList generateSwingList( ArrayList<SwingCardInfo> swingCardList, ArrayList<RepayInfo> repayList) {
        SwingList swingList = new SwingList();
        swingList.billYear = billInfo_.billDate.getYear() + 1900;
        swingList.billMonth = billInfo_.billDate.getMonth() + 1;
        swingList.repayYear = billInfo_.lastRepayDate.getYear() + 1900;
        swingList.repayMonth = billInfo_.lastRepayDate.getMonth() + 1;
        swingList.cardNO = cardInfo_.cardNO;
        if (swingCardList.size() == 0) {
            swingCardList.add(new SwingCardInfo());
        }
        swingList.swingCardList = swingCardList;
        swingList.repayList = repayList;
        return swingList;
    }

    private SwingCardInfo generateSwingInfo(RuleInfo ruleInfo, double curDate) throws Exception {
        SwingCardInfo swingCardInfo = new SwingCardInfo();
        swingCardInfo.money = (long)(random.nextDouble() * (ruleInfo.maxSwingMoney - ruleInfo.minSwingMoney) + ruleInfo.minSwingMoney);
        swingCardInfo.swingTime = new Time((long)(random.nextDouble() * ((ruleInfo.swingEndTime.getTime()) - ruleInfo.swingStartTime.getTime())  + ruleInfo.swingStartTime.getTime())) ;
        swingCardInfo.ruleUUID = ruleInfo.ruleUUID;
        swingCardInfo.swingDate = new Date(billInfo_.billDate.getTime() + (long)curDate * 24 * 60 * 60 * 1000);
        ArrayList<HashMap<String, Object>> posList = fetchPosList(ruleInfo);
        if (posList.size() > 0) {
            swingCardInfo.posUUID = posList.get(random.nextInt(posList.size())).get("UUID").toString();
        }
        return swingCardInfo;
    }

    private RepayInfo generateRepayInfo(double curDate) {
        PolicyInfo policyInfo = cardInfo_;
        if (policyInfo.useNumber <= 1 || policyInfo.coolingTime > 0.0) {
            return null;
        }

        double fixedLimit = random.nextDouble() * REPAYFIXEDLIMIT;
        double rate = 1.0 / policyInfo.useNumber;
        if (random.nextDouble() > 0.5) {
            rate += rate * fixedLimit;
        }
        else {
            rate -= rate * fixedLimit;
        }

        RepayInfo repayInfo = new RepayInfo();
        repayInfo.money = ((long)(billInfo_.billAmount * rate / 100) * 100);
        repayInfo.repayDate = new Date(billInfo_.billDate.getTime() + (long)curDate * 24 * 60 * 60 * 1000);
        return repayInfo;
    }

    private RepayInfo generateLastRepay(double remainMoney, double curDate) {
        RepayInfo repayInfo = new RepayInfo();
        repayInfo.money = remainMoney;
        repayInfo.repayDate = new Date(billInfo_.billDate.getTime() + (long)curDate * 24 * 60 * 60 * 1000);
        return repayInfo;
    }

    private double generaterSwingAmount() {
        double rate = 1.0;
        if (random.nextDouble() > 0.5) {
            rate += random.nextDouble() * SWINGAMOUNTFIXEDLIMIT;
        }
        else {
            rate -= random.nextDouble() * SWINGAMOUNTFIXEDLIMIT;
        }

        return ((long)(billInfo_.billAmount * rate / 100) * 100);
    }

    private void updateCardInfo() {
        PolicyInfo policyInfo = cardInfo_;
        policyInfo.useNumber--;
        policyInfo.coolingTime = policyInfo.useInterval;
    }

    private void updateRuleInfo(RuleInfo ruleInfo) {
        PolicyInfo policyInfo = ruleInfo;
        policyInfo.useNumber--;
        policyInfo.coolingTime = policyInfo.useInterval;
    }

    private void updateCoolingTime(double nextDateLimit) {
        PolicyInfo policyInfo = cardInfo_;
        if (policyInfo.useNumber > 0) {
            policyInfo.coolingTime -= nextDateLimit;
        }

        Iterator<RuleInfo> iterator = ruleList_.iterator();
        while (iterator.hasNext()) {
            policyInfo = iterator.next();
            if (policyInfo.useNumber > 0) {
                policyInfo.coolingTime -= nextDateLimit;
            }
        }
    }

    private RuleInfo nextRule() {
        ArrayList<Integer> randomList = new ArrayList<Integer>();
        while (true) {
            int randomNum = random.nextInt(ruleList_.size());
            if (randomList.contains(randomNum)) {
                if (randomList.size() == ruleList_.size()) {
                    return null;
                }
                continue;
            }
            randomList.add(randomNum);

            PolicyInfo policyInfo = ruleList_.get(randomNum);
            if (policyInfo.useNumber <= 0)
                continue;
            if (policyInfo.coolingTime > 0.0)
                continue;

            return (RuleInfo)policyInfo;
        }
    }

    private double nextDateLimit() {
        return (Double.max((long)(random.nextDouble() * cardInfo_.repayInterval * NEXTDATEFIXEDLIMIT), 0.5) * 10) / 10;
    }

    private BillInfo fetchBillInfo(String billNumber) throws Exception {
        return convertBillInfo(PosDbManager.executeSql("select * from billtb where uuid='" + billNumber + "'"));
    }

    private BillInfo convertBillInfo(ArrayList<HashMap<String, Object>> sqlBillInfo) throws Exception {
        if (sqlBillInfo == null || sqlBillInfo.size() != 1)
            return null;

        BillInfo billInfo = new BillInfo();
        billInfo.bankUUID = sqlBillInfo.get(0).get("BANKUUID").toString();
        billInfo.cardNO = sqlBillInfo.get(0).get("CARDNO").toString();
        billInfo.billDate = Date.valueOf(sqlBillInfo.get(0).get("BILLDATE").toString());
        billInfo.lastRepayDate = Date.valueOf(sqlBillInfo.get(0).get("LASTREPAYMENTDATE").toString());
        billInfo.billAmount = Double.parseDouble(sqlBillInfo.get(0).get("BILLAMOUNT").toString());
        billInfo.canUseAmount = Double.parseDouble(sqlBillInfo.get(0).get("CANUSEAMOUNT").toString());
        return billInfo;
    }

    private ArrayList<RuleInfo> fetchRuleList(String bankUUID) throws Exception {
        return convertRuleList((ArrayList<HashMap<String, Object>>)PosDbManager.executeSql("SELECT\n" +
                "ruletb.uuid,\n" +
                "ruletb.bankuuid,\n" +
                "swingtimetb.startTime,\n" +
                "swingtimetb.endTime,\n" +
                "ruletb.posserveruuid,\n" +
                "ruletb.minswingmoney,\n" +
                "ruletb.maxswingmoney,\n" +
                "ruletb.industryuuid,\n" +
                "ruletb.rateuuid,\n" +
                "ruletb.mccuuid,\n" +
                "ruletb.ruleusefre,\n" +
                "ruletb.ruleuseinterval\n" +
                "FROM\n" +
                "ruletb\n" +
                "INNER JOIN swingtimetb ON swingtimetb.uuid = ruletb.swingtimeuuid\n" +
                "where bankuuid='" + bankUUID + "'"));
    }

    private ArrayList<RuleInfo> convertRuleList(ArrayList<HashMap<String, Object>> ruleList) {
        ArrayList<RuleInfo> ruleInfoList = new ArrayList<RuleInfo>();
        if (ruleList != null) {
            Iterator<HashMap<String, Object>> iterator = ruleList.iterator();
            while (iterator.hasNext()) {
                RuleInfo ruleInfo = new RuleInfo();
                HashMap<String, Object> sqlRuleInfo = iterator.next();
                ruleInfo.ruleUUID = sqlRuleInfo.get("UUID").toString();
                ruleInfo.bankUUID = sqlRuleInfo.get("BANKUUID").toString();
                ruleInfo.industryUUID = sqlRuleInfo.get("INDUSTRYUUID").toString();
                ruleInfo.posServerUUID = sqlRuleInfo.get("POSSERVERUUID").toString();
                ruleInfo.swingStartTime = Time.valueOf(sqlRuleInfo.get("STARTTIME").toString());
                ruleInfo.swingEndTime = Time.valueOf(sqlRuleInfo.get("ENDTIME").toString());
                ruleInfo.minSwingMoney = Double.parseDouble(sqlRuleInfo.get("MINSWINGMONEY").toString());
                ruleInfo.maxSwingMoney = Double.parseDouble(sqlRuleInfo.get("MAXSWINGMONEY").toString());
                ruleInfo.rateUUID = sqlRuleInfo.get("RATEUUID").toString();
                ruleInfo.mccUUID = sqlRuleInfo.get("MCCUUID").toString();
                ruleInfo.ruleUseFre = Integer.parseInt(sqlRuleInfo.get("RULEUSEFRE").toString());
                ruleInfo.ruleUseInterval = Double.parseDouble(sqlRuleInfo.get("RULEUSEINTERVAL").toString());
                ruleInfoList.add(ruleInfo);
            }
        }
        return ruleInfoList;
    }

    private CardInfo fetchCardInfo(String cardNumber) throws Exception {
        return convertCardInfo(PosDbManager.executeSql("select * from cardtb where cardno='" + cardNumber + "'"));
    }

    private CardInfo convertCardInfo(ArrayList<HashMap<String, Object>> sqlCardInfo) throws Exception {
        if (sqlCardInfo == null || sqlCardInfo.size() != 1)
            return null;

        CardInfo cardInfo = new CardInfo();
        cardInfo.cardNO = sqlCardInfo.get(0).get("CARDNO").toString();
        cardInfo.creditAmount = Double.parseDouble(sqlCardInfo.get(0).get("CREDITAMOUNT").toString());
        cardInfo.repayNum = Integer.parseInt(sqlCardInfo.get(0).get("REPAYNUM").toString());
        cardInfo.repayInterval = Integer.parseInt(sqlCardInfo.get(0).get("REPAYINTERVAL").toString());
        return cardInfo;
    }

    private ArrayList<HashMap<String, Object>> fetchPosList(RuleInfo ruleInfo) throws Exception {
        return (ArrayList<HashMap<String, Object>>)PosDbManager.executeSql("select * from postb where " +
                "industryuuid='" + ruleInfo.industryUUID +
                "' and posserveruuid='" + ruleInfo.posServerUUID +
                "' and rateuuid='" + ruleInfo.rateUUID +
                "' and mccuuid='" + ruleInfo.mccUUID +
                "'");
    }

    private String salemanID;
    private BillInfo billInfo_;
    private CardInfo cardInfo_;
    ArrayList<RuleInfo> ruleList_;
    private static Random random = new Random();
    private final double REPAYFIXEDLIMIT = 0.3;
    private final double NEXTDATEFIXEDLIMIT = 1.3;
    private final double SWINGAMOUNTFIXEDLIMIT = 0.1;
}