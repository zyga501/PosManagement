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

    private class RuleInfo {
        public String ruleUUID;
        public String bankUUID;
        public String posServerUUID;
        public Time swingStartTime;
        public Time swingEndTime;
        public double minSwingMoney;
        public double maxSwingMoney;
        public String industryUUID;
        public String rateUUID;
        public int ruleUseFre;
        public double ruleUseInterval;
        public double ruleCoolDown;
    }

    private class CardInfo {
        public String cardNO;
        public double creditAmount;
        public int repayNum;
        public int repayInterval;
        public double repayCoolDown;
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
        BillInfo billInfo = fetchBillInfo(billNumber);
        if (billInfo == null) {
            return null;
        }
        CardInfo cardInfo = fetchCardInfo(billInfo.cardNO);
        if (cardInfo == null) {
            return null;
        }
        ArrayList<RuleInfo> ruleList = fetchRuleList(billInfo.bankUUID);
        if (ruleList.size() <= 0) {
            return null;
        }
        double dateLimit = (billInfo.lastRepayDate.getTime() - billInfo.billDate.getTime()) / (24 * 60 * 60 * 1000);
        if (dateLimit < 0) {
            return null;
        }

        double remainBillAmount = billInfo.billAmount;
        double canUseAmount = billInfo.canUseAmount;
        ArrayList<SwingCardInfo> swingCardList = new ArrayList<SwingCardInfo>();
        ArrayList<RepayInfo> repayList = new ArrayList<RepayInfo>();
        double curDate = random.nextDouble() * cardInfo.repayInterval + 1;

        while (curDate < dateLimit) {
            RuleInfo ruleInfo = nextRule(ruleList);
            if (ruleInfo == null) {
                break;
            }

            double nextDateLimit = nextDateLimit(cardInfo);
            try {
                SwingCardInfo swingCardInfo = generateSwingInfo(ruleInfo, billInfo, curDate);
                if (swingCardInfo.money > canUseAmount) {
                    RepayInfo repayInfo = generateRepayInfo(cardInfo, billInfo, curDate);
                    if (repayInfo == null)
                        continue;
                    updateCardInfo(cardInfo, nextDateLimit);
                    canUseAmount += repayInfo.money;
                    remainBillAmount -= repayInfo.money;
                    repayList.add(repayInfo);
                }

                if (swingCardInfo.money < canUseAmount) {
                    updateRuleInfo(ruleInfo, nextDateLimit);
                    canUseAmount -= swingCardInfo.money;
                    swingCardList.add(swingCardInfo);
                }
            }
            finally {
                curDate += nextDateLimit;
            }
        }

        repayList.add(generateLastRepay(remainBillAmount, billInfo, curDate));

        return generateSwingList(billInfo, cardInfo, swingCardList, repayList);
    }

    private SwingList generateSwingList(BillInfo billInfo, CardInfo cardInfo, ArrayList<SwingCardInfo> swingCardList, ArrayList<RepayInfo> repayList) {
        SwingList swingList = new SwingList();
        swingList.billYear = billInfo.billDate.getYear() + 1900;
        swingList.billMonth = billInfo.billDate.getMonth();
        swingList.repayYear = billInfo.lastRepayDate.getYear() + 1900;
        swingList.repayMonth = billInfo.lastRepayDate.getMonth();
        swingList.cardNO = cardInfo.cardNO;
        swingList.swingCardList = swingCardList;
        swingList.repayList = repayList;
        return swingList;
    }

    private SwingCardInfo generateSwingInfo(RuleInfo ruleInfo, BillInfo billInfo, double curDate) throws Exception {
        SwingCardInfo swingCardInfo = new SwingCardInfo();
        swingCardInfo.money = (long)(random.nextDouble() * (ruleInfo.maxSwingMoney - ruleInfo.minSwingMoney) + ruleInfo.minSwingMoney);
        swingCardInfo.swingTime = new Time((long)(random.nextDouble() * ((ruleInfo.swingEndTime.getTime()) - ruleInfo.swingStartTime.getTime())  + ruleInfo.swingStartTime.getTime())) ;
        swingCardInfo.ruleUUID = ruleInfo.ruleUUID;
        swingCardInfo.swingDate = new Date(billInfo.billDate.getTime() + (long)curDate * 24 * 60 * 60 * 1000);
        ArrayList<HashMap<String, Object>> posList = fetchPosList(ruleInfo);
        if (posList.size() > 0) {
            swingCardInfo.posUUID = posList.get(random.nextInt(posList.size())).get("UUID").toString();
        }
        return swingCardInfo;
    }

    private RepayInfo generateRepayInfo(CardInfo cardInfo, BillInfo billInfo, double curDate) {
        if (cardInfo.repayNum < 2) {
            return null;
        }

        if (cardInfo.repayCoolDown > 0.0) {
            return null;
        }

        double fixedLimit = random.nextDouble() * REPAYFIXEDLIMIT;
        double rate = 1.0 / cardInfo.repayNum;
        if (random.nextDouble() > 0.5) {
            rate += rate * fixedLimit;
        }
        else {
            rate -= rate * fixedLimit;
        }

        RepayInfo repayInfo = new RepayInfo();
        repayInfo.money = ((long)(billInfo.billAmount * rate / 100) * 100);
        repayInfo.repayDate = new Date(billInfo.billDate.getTime() + (long)curDate * 24 * 60 * 60 * 1000);
        return repayInfo;
    }

    private RepayInfo generateLastRepay(double remainMoney, BillInfo billInfo, double curDate) {
        RepayInfo repayInfo = new RepayInfo();
        repayInfo.money = remainMoney;
        repayInfo.repayDate = new Date(billInfo.billDate.getTime() + (long)curDate * 24 * 60 * 60 * 1000);
        return repayInfo;
    }

    private void updateCardInfo(CardInfo cardInfo, double nextDateLimit) {
        cardInfo.repayCoolDown -= nextDateLimit;
        cardInfo.repayNum--;
    }

    private void updateRuleInfo(RuleInfo ruleInfo, double nextDateLimit) {
        ruleInfo.ruleCoolDown = ruleInfo.ruleUseInterval - nextDateLimit;
        ruleInfo.ruleUseFre--;
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
            randomList.add(randomNum);

            RuleInfo ruleInfo = ruleList.get(randomNum);
            if (ruleInfo.ruleUseFre <= 0)
                continue;;
            if (ruleInfo.ruleCoolDown > 0.0)
                continue;

            Iterator<RuleInfo> iterator = ruleList.iterator();
            while (iterator.hasNext()) {
                iterator.next().ruleCoolDown--;
            }

            return ruleInfo;
        }
    }

    private double nextDateLimit(CardInfo cardInfo) {
        return (Double.max((long)(random.nextDouble() * cardInfo.repayInterval * NEXTDATEFIXEDLIMIT), 0.5) * 10) / 10;
    }

    private BillInfo fetchBillInfo(String billNumber) throws Exception {
        return convertBillInfo(PosDbManager.executeSql("select * from billtb where uuid=" + billNumber));
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
                ruleInfo.ruleUseFre = Integer.parseInt(sqlRuleInfo.get("RULEUSEFRE").toString());
                ruleInfo.ruleUseInterval = Double.parseDouble(sqlRuleInfo.get("RULEUSEINTERVAL").toString());
                ruleInfo.ruleCoolDown = 0.0;
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
        cardInfo.repayCoolDown = 0.0;
        return cardInfo;
    }

    private ArrayList<HashMap<String, Object>> fetchPosList(RuleInfo ruleInfo) throws Exception {
        return (ArrayList<HashMap<String, Object>>)PosDbManager.executeSql("select * from postb where " +
                "industryuuid='" + ruleInfo.industryUUID +
                "' and posserveruuid='" + ruleInfo.posServerUUID +
                "' and rateuuid='" + ruleInfo.rateUUID + "'");
    }

    private String salemanID;
    private static Random random = new Random();
    private final double REPAYFIXEDLIMIT = 0.3;
    private final double NEXTDATEFIXEDLIMIT = 1.3;
}