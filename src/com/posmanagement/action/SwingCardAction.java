package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;

import java.sql.Date;
import java.sql.Time;
import java.util.*;

public class SwingCardAction extends AjaxActionSupport {
    private final static String SWINGCARDMANAGER = "swingCardManager";

    private String billNO;

    public void setBillNO(String _billNO) {
        billNO = _billNO;
    }

    public String Init() {
        return SWINGCARDMANAGER;
    }

    public String GenerateSwingCard() throws Exception {
        Map map = new HashMap();
        if (billNO == null || billNO.length() <= 0) {
            map.put("errorMessage", getText("SwingCardAction.billNOError"));
        }

        SwingCardPolicy swingCardPolicy = new SwingCardPolicy("");
        SwingCardPolicy.SwingList swingList = swingCardPolicy.generateSwingList(billNO);
        for (int index = 0; index < swingList.swingCardList.size(); ++index) {
            Map parametMap = new HashMap();
            parametMap.put(1, swingList.billMonth + "月份");
            parametMap.put(2, swingList.cardNO);
            parametMap.put(3, swingList.cardMaster);
            parametMap.put(4, swingList.swingCardList.get(index).money);
            parametMap.put(5, swingList.swingCardList.get(index).swingDate);
            parametMap.put(6, swingList.swingCardList.get(index).ruleNO);
            PosDbManager.executeUpdate("insert into swingcard(thedate,cardno,cardmaster,amount,sdatetm,ruleno) " +
                    "values(?,?,?,?,?,?)",
                    (HashMap<Integer, Object>)parametMap);
        }

        return AjaxActionComplete(map);
    }
}

class SwingCardPolicy {
    public static void main(String[] args) throws Exception {
        SwingCardPolicy policy = new SwingCardPolicy("");
        policy.generateSwingList("1");
    }

    public class SwingList {
        public String cardNO;
        public String cardMaster;
        public int billMonth;
        ArrayList<SwingCardInfo> swingCardList;
        ArrayList<RepayInfo> repayList;
    }

    private class BillInfo {
        public String bankCode;
        public String cardNO;
        public Date billDate;
        public Date lastRepayDate;
        public double billAmount;
        public double canUseAmount;
    }

    private class RuleInfo {
        public String ruleNO;
        public String bankCode;
        public String posServerCode;
        public Time swingStartTime;
        public Time swingEndTime;
        public double minSwingMoney;
        public double maxSwingMoney;
        public String industryCode;
        public int ruleUseFre;
        public double ruleUseInterval;
        public double ruleCoolDown;
    }

    private class CardInfo {
        public String cardNO;
        public String cardMaster;
        public double creditAmount;
        public int repayNum;
        public int repayInterval;
        public double repayCoolDown;
    }

    public class SwingCardInfo {
        public String ruleNO;
        public double money;
        public Date swingDate;
        public Time swingTime;
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
        ArrayList<HashMap<String, Object>> sqlRuleList = fetchRuleList(billInfo.bankCode);
        if (sqlRuleList.size() <= 0) {
            return null;
        }

        double dateLimit = (billInfo.lastRepayDate.getTime() - billInfo.billDate.getTime()) / (24 * 60 * 60 * 1000);
        int reTryCount = RETRYCOUNT;
        while (reTryCount-- != 0) {
            ArrayList<RuleInfo> ruleList = convertRuleList(sqlRuleList);
            double currentBillAmount = billInfo.billAmount;
            double canuseAmount = cardInfo.creditAmount;
            ArrayList<SwingCardInfo> swingCardList = new ArrayList<SwingCardInfo>();
            ArrayList<RepayInfo> repayList = new ArrayList<RepayInfo>();
            double curDate = 0.0;
            while (currentBillAmount > 0.0 && curDate++ < dateLimit) {
                RuleInfo ruleInfo = nextRule(ruleList);
                if (ruleInfo == null) {
                    break;
                }

                SwingCardInfo swingCardInfo = generateSwingInfo(ruleInfo);
                cardInfo.repayCoolDown--;
                if (swingCardInfo.money > canuseAmount) {
                    RepayInfo repayInfo = generateRepayInfo(cardInfo);
                    if (repayInfo == null)
                        continue;
                    canuseAmount += repayInfo.money;
                    repayInfo.repayDate = new Date(billInfo.billDate.getTime() + (int)curDate * 24 * 60 * 60 * 1000);
                    repayList.add(repayInfo);
                }

                if (swingCardInfo.money < canuseAmount) {
                    if (currentBillAmount < swingCardInfo.money) {
                        if (currentBillAmount < ruleInfo.minSwingMoney) {
                            continue;
                        }
                        swingCardInfo.money = currentBillAmount;
                    }

                    canuseAmount -= swingCardInfo.money;
                    currentBillAmount -= swingCardInfo.money;
                    ruleInfo.ruleCoolDown = ruleInfo.ruleUseInterval + COOLDOWNFIXED;
                    ruleInfo.ruleUseFre--;
                    swingCardInfo.ruleNO = ruleInfo.ruleNO;
                    swingCardInfo.swingDate = new Date(billInfo.billDate.getTime() + (int)curDate * 24 * 60 * 60 * 1000);
                    swingCardList.add(swingCardInfo);
                }
            }

            if (currentBillAmount <= 0.0) {
                SwingList swingList = new SwingList();
                swingList.billMonth = billInfo.billDate.getMonth();
                swingList.cardNO = cardInfo.cardNO;
                swingList.cardMaster = cardInfo.cardMaster;
                swingList.swingCardList = swingCardList;
                swingList.repayList = repayList;
                return swingList;
            }
        }

        return null;
    }

    private SwingCardInfo generateSwingInfo(RuleInfo ruleInfo) {
        SwingCardInfo swingCardInfo = new SwingCardInfo();
        swingCardInfo.money = (int)(random.nextDouble() * (ruleInfo.maxSwingMoney - ruleInfo.minSwingMoney) + ruleInfo.minSwingMoney);
        swingCardInfo.swingTime = new Time((long)(random.nextDouble() * ((ruleInfo.swingEndTime.getTime()) - ruleInfo.swingStartTime.getTime())  + ruleInfo.swingStartTime.getTime())) ;
        return swingCardInfo;
    }

    private RepayInfo generateRepayInfo(CardInfo cardInfo) {
        if (cardInfo.repayNum < 2) {
            throw new IllegalArgumentException("Repay Number Can not be less then 2!");
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
        repayInfo.money = cardInfo.creditAmount * rate;
        return repayInfo;
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

    private BillInfo fetchBillInfo(String billNumber) throws Exception {
        return convertBillInfo(PosDbManager.executeSql("select * from billtb where id=" + billNumber));
    }

    private BillInfo convertBillInfo(ArrayList<HashMap<String, Object>> sqlBillInfo) throws Exception {
        if (sqlBillInfo == null || sqlBillInfo.size() != 1)
            return null;

        BillInfo billInfo = new BillInfo();
        billInfo.bankCode = sqlBillInfo.get(0).get("BANKCODE").toString();
        billInfo.cardNO = sqlBillInfo.get(0).get("CARDNO").toString();
        billInfo.billDate = Date.valueOf(sqlBillInfo.get(0).get("BILLDATE").toString());
        billInfo.lastRepayDate = Date.valueOf(sqlBillInfo.get(0).get("LASTREPAYMENTDATE").toString());
        billInfo.billAmount = Double.parseDouble(sqlBillInfo.get(0).get("BILLAMOUNT").toString());
        billInfo.canUseAmount = Double.parseDouble(sqlBillInfo.get(0).get("CANUSEAMOUNT").toString());
        return billInfo;
    }

    private ArrayList<HashMap<String, Object>> fetchRuleList(String bankCode) throws Exception {
        return (ArrayList<HashMap<String, Object>>)PosDbManager.executeSql("SELECT\n" +
                "ruletb.ruleno,\n" +
                "ruletb.bankcode,\n" +
                "swingtimetb.startTime,\n" +
                "swingtimetb.endTime,\n" +
                "ruletb.posservercode,\n" +
                "ruletb.minswingmoney,\n" +
                "ruletb.maxswingmoney,\n" +
                "ruletb.industrycode,\n" +
                "ruletb.ruleusefre,\n" +
                "ruletb.ruleuseinterval\n" +
                "FROM\n" +
                "ruletb\n" +
                "INNER JOIN swingtimetb ON swingtimetb.swingCode = ruletb.swingtimecode\n" +
                "where bankcode='" + bankCode + "'");
    }

    private ArrayList<RuleInfo> convertRuleList(ArrayList<HashMap<String, Object>> ruleList) {
        ArrayList<RuleInfo> ruleInfoList = new ArrayList<RuleInfo>();
        if (ruleList != null) {
            Iterator<HashMap<String, Object>> iterator = ruleList.iterator();
            while (iterator.hasNext()) {
                RuleInfo ruleInfo = new RuleInfo();
                HashMap<String, Object> sqlRuleInfo = iterator.next();
                ruleInfo.ruleNO = sqlRuleInfo.get("RULENO").toString();
                ruleInfo.bankCode = sqlRuleInfo.get("BANKCODE").toString();
                ruleInfo.industryCode = sqlRuleInfo.get("INDUSTRYCODE").toString();
                ruleInfo.posServerCode = sqlRuleInfo.get("POSSERVERCODE").toString();
                ruleInfo.swingStartTime = Time.valueOf(sqlRuleInfo.get("STARTTIME").toString());
                ruleInfo.swingEndTime = Time.valueOf(sqlRuleInfo.get("ENDTIME").toString());
                ruleInfo.minSwingMoney = Double.parseDouble(sqlRuleInfo.get("MINSWINGMONEY").toString());
                ruleInfo.maxSwingMoney = Double.parseDouble(sqlRuleInfo.get("MAXSWINGMONEY").toString());
                ruleInfo.ruleUseFre = Integer.parseInt(sqlRuleInfo.get("RULEUSEFRE").toString());
                ruleInfo.ruleUseInterval = Double.parseDouble(sqlRuleInfo.get("RULEUSEINTERVAL").toString());
                ruleInfo.ruleCoolDown = COOLDOWNFIXED;
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
        cardInfo.cardMaster = sqlCardInfo.get(0).get("CARDMASTER").toString();
        cardInfo.creditAmount = Double.parseDouble(sqlCardInfo.get(0).get("CREDITAMOUNT").toString());
        cardInfo.repayNum = Integer.parseInt(sqlCardInfo.get(0).get("REPAYNUM").toString());
        cardInfo.repayInterval = Integer.parseInt(sqlCardInfo.get(0).get("REPAYINTERVAL").toString());
        cardInfo.repayCoolDown = COOLDOWNFIXED;
        return cardInfo;
    }

    private String salemanID;
    private static Random random = new Random();
    private final int RETRYCOUNT = 5;
    private final double COOLDOWNFIXED = -0.5;
    private final double REPAYFIXEDLIMIT = 0.4;
}