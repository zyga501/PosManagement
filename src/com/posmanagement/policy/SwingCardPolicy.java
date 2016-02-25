package com.posmanagement.policy;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.SQLUtils;
import com.posmanagement.utils.StringUtils;
import com.posmanagement.utils.UserUtils;

import java.sql.Date;
import java.sql.Time;
import java.util.*;

public class SwingCardPolicy {
    public static void main(String[] args) throws Exception {
        SwingCardPolicy policy = new SwingCardPolicy("3977725B-C162-4CF6-8FB9-48BC163B2549");
        policy.generateSwingList("14829bb5-d55a-11e5-804b-0030487c8b4b");
    }

    public class SwingList {
        public String cardNO;
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
        public String posUUID;
        public Time swingStartTime;
        public Time swingEndTime;
        public double minSwingMoney;
        public double maxSwingMoney;
        public String posServerUUID;
        public String industryUUID;
        public String rateUUID;
        public String mccUUID;
        public String posPosServerUUID;
        public String posIndustryUUID;
        public String posRateUUID;
        public String posMccUUID;
        public int ruleUseFre;
        public double ruleUseInterval;
    }

    private class CardInfo extends PolicyInfo {
        public String cardNO;
        public double creditAmount;
        public int repayNum;
        public int repayInterval;
        public double maxPosMean;
        public double reservedSwingMoney;
        public double reservedSwingCount;
    }

    public class SwingCardInfo {
        public String ruleUUID;
        public double money;
        public Date swingDate;
        public Time swingTime;
        public double posRate;
        public String posUUID;
    }

    public class RepayInfo {
        public double money;
        public Date repayDate;
    }

    public SwingCardPolicy(String _salemanID) {
        salemanID = _salemanID;
    }

    public String getLastError() { return lastError; }

    public SwingList generateSwingList(String billNumber) throws Exception {
        try {
            billInfo_ = fetchBillInfo(billNumber);
            if (billInfo_ == null) {
                throw new NullPointerException();
            }
        }
        catch (NullPointerException exception) {
            lastError = "无法找到有效账单信息";
            return null;
        }

        try {
            cardInfo_ = fetchCardInfo(billInfo_.cardNO);
            if (cardInfo_ == null) {
                throw new NullPointerException();
            }
        }
        catch (NullPointerException exception) {
            lastError = "无法找到有效卡信息";
            return null;
        }

        try {
            if (fetchRuleList(billInfo_.bankUUID).size() <= 0) {
                throw new NullPointerException();
            }
        }
        catch (NullPointerException exception) {
            lastError = "无法找到有效规则信息";
            return null;
        }

        if (cardInfo_.repayNum < 1) {
            lastError = "当前账单关联卡还款次数少于1";
            return null;
        }

        double dateLimit = (billInfo_.lastRepayDate.getTime() - billInfo_.billDate.getTime()) / ONEDAYMILLIONSECOND;
        if (dateLimit < 0) {
            lastError = "最后还款日与账单日时间错误";
            return null;
        }

        ArrayList<Double> posRateList = fetchPosRateList();
        Collections.sort(posRateList);
        if (posRateList.get(0) > cardInfo_.maxPosMean) {
            lastError = "无法找到小于最大成本费率的Pos机";
            return null;
        }

        ArrayList<Double> swingPosRateList = new ArrayList<>();
        return generateSwingList(dateLimit, posRateList);
    }

    public SwingList generateSwingList(double dateLimit, ArrayList<Double> posRateList) throws Exception {
        ArrayList<RepayInfo> repayList = new ArrayList<RepayInfo>();
        // generate repay list
        while (true) {
            initRepayInfo();
            repayList.clear();
            double curDate = nextDateLimit(dateLimit);
            double remainBillAmount = billInfo_.billAmount;
            while (curDate < dateLimit && remainBillAmount > 0.0) {
                try {
                    RepayInfo repayInfo = generateRepayInfo(curDate);
                    if (repayInfo == null)
                        break;
                    if (remainBillAmount < repayInfo.money || cardInfo_.repayNum == 1) {
                        repayInfo.money = (long)(remainBillAmount / 10 + 1) * 10;
                        remainBillAmount = 0;
                    }
                    updateRepayInfo();
                    remainBillAmount -= repayInfo.money;
                    repayList.add(repayInfo);
                }
                finally {
                    double nextDateLimit = nextDateLimit(dateLimit);
                    updateRepayCoolingTime(nextDateLimit);
                    curDate += nextDateLimit;
                }
            }

            if (remainBillAmount <= 0) {
                break;
            }
        }

        // generate swing card list
        double reservedSwingMoney = 0.0;
        ArrayList<SwingCardInfo> swingCardList = new ArrayList<SwingCardInfo>();
        double usableMoney = 0.0;
        Iterator<RepayInfo> iterator = repayList.iterator();
        while (iterator.hasNext()) {
            RepayInfo repayInfo = iterator.next();
            usableMoney += repayInfo.money;
            SwingCardInfo swingCardInfo = generateSwingMoney(repayInfo, usableMoney, !iterator.hasNext());
            if (!iterator.hasNext()) {
                if (swingCardInfo.money > cardInfo_.reservedSwingMoney) {
                    swingCardInfo.money -= cardInfo_.reservedSwingMoney;
                    reservedSwingMoney = cardInfo_.reservedSwingMoney;
                }
                else {
                    reservedSwingMoney = swingCardInfo.money;
                    continue;
                }
            }
            usableMoney -= swingCardInfo.money;
            swingCardList.add(swingCardInfo);
        }

        ArrayList<Double> swingPostRateList = new ArrayList<>();
        do {
            while (true) {
                for (int index = 0; index < swingCardList.size(); ++index) {
                    swingPostRateList.add(posRateList.get(random.nextInt(posRateList.size())));
                }

                double rateSum = 0;
                for (int index = 0; index < swingPostRateList.size(); ++index) {
                    rateSum += swingPostRateList.get(index);
                }

                rateSum /= swingPostRateList.size();
                if (rateSum  < cardInfo_.maxPosMean) {
                    break;
                }
            }
        } while (!intelligentAssignPos(swingCardList, swingPostRateList));

        return generateSwingList(swingCardList, repayList);
    }

    private boolean intelligentAssignPos(ArrayList<SwingCardInfo> swingCardList, ArrayList<Double> swingPostRateList) throws Exception {
        Collections.sort(swingPostRateList);
        Collections.sort(swingCardList, new Comparator<SwingCardInfo>() {
            @Override
            public int compare(SwingCardInfo o1, SwingCardInfo o2) {
                return o1.money < o2.money ? 1 : -1;
            }
        });

        for (int index = 0; index < swingCardList.size(); ++index) {
            swingCardList.get(index).posRate = swingPostRateList.get(index);
        }

        Collections.sort(swingCardList, new Comparator<SwingCardInfo>() {
            @Override
            public int compare(SwingCardInfo o1, SwingCardInfo o2) {
                return o1.swingDate.getTime() > o2.swingDate.getTime() ? 1 : -1;
            }
        });

        ArrayList<Double> tempSwingPosList = new ArrayList<>();
        for (int index = 0; index < swingPostRateList.size(); index++) {
            if (!tempSwingPosList.contains(swingPostRateList.get(index))) {
                tempSwingPosList.add(swingPostRateList.get(index));
            }
        }
        swingPostRateList = tempSwingPosList;
        for (int posRateIndex = 0; posRateIndex < swingPostRateList.size(); ++posRateIndex) {
            ruleList_ = fetchRuleList(billInfo_.bankUUID, swingPostRateList.get(posRateIndex));
            if (ruleList_ == null) {
                return false;
            }
            double lastDate = 0.0;
            initRuleInfo();
            for (int index = 0; index < swingCardList.size(); ++index) {
                if (swingCardList.get(index).posRate != swingPostRateList.get(posRateIndex)) {
                    continue;
                }

                RuleInfo ruleInfo = nextRule();
                if (ruleInfo == null) {
                    return false;
                }
                updateRuleInfo(ruleInfo.ruleUUID);
                updateSwingInfo(ruleInfo, swingCardList.get(index));
                double nextDateLimit = swingCardList.get(index).swingDate.getTime() / ONEDAYMILLIONSECOND - lastDate;
                lastDate = swingCardList.get(index).swingDate.getTime() / ONEDAYMILLIONSECOND;
                updateRuleCoolingTime(nextDateLimit);
            }
        }

        return true;
    }

    private void initRepayInfo() {
        PolicyInfo policyInfo = cardInfo_;
        policyInfo.useNumber = cardInfo_.repayNum;
        policyInfo.useInterval = cardInfo_.repayInterval;
        policyInfo.coolingTime = 0.0;
    }

    private void initRuleInfo() {
        Iterator<RuleInfo> iterator = ruleList_.iterator();
        while (iterator.hasNext()) {
            RuleInfo ruleInfo = iterator.next();
            PolicyInfo policyInfo = ruleInfo;
            policyInfo.useNumber = ruleInfo.ruleUseFre;
            policyInfo.useInterval = ruleInfo.ruleUseInterval;
            policyInfo.coolingTime = 0.0;
        }
    }

    private SwingList generateSwingList( ArrayList<SwingCardInfo> swingCardList, ArrayList<RepayInfo> repayList) {
        SwingList swingList = new SwingList();
        swingList.cardNO = cardInfo_.cardNO;
        if (swingCardList.size() == 0) {
            swingCardList.add(new SwingCardInfo());
        }
        swingList.swingCardList = swingCardList;
        swingList.repayList = repayList;
        return swingList;
    }

    private  SwingCardInfo generateSwingMoney(RepayInfo repayInfo, double usableMoney, boolean isLastSwing) throws Exception {
        SwingCardInfo swingCardInfo = new SwingCardInfo();
        swingCardInfo.money = (long)(usableMoney * (0.8 + random.nextDouble() * SWINGAMOUNTFIXEDLIMIT));
        if (isLastSwing) {
            swingCardInfo.money = usableMoney - random.nextDouble() * LASTFIXEDSWINGCARDMONEY;
        }
        swingCardInfo.money = (long)(swingCardInfo.money / 10.0) * 10.0;
        swingCardInfo.swingDate = repayInfo.repayDate;
        return swingCardInfo;
    }

    private SwingCardInfo updateSwingInfo(RuleInfo ruleInfo, SwingCardInfo swingCardInfo) throws Exception {
        swingCardInfo.swingTime = new Time((long)(random.nextDouble() * ((ruleInfo.swingEndTime.getTime()) - ruleInfo.swingStartTime.getTime())  + ruleInfo.swingStartTime.getTime())) ;
        swingCardInfo.ruleUUID = ruleInfo.ruleUUID;
        swingCardInfo.posUUID = ruleInfo.posUUID;
        return swingCardInfo;
    }

    private RepayInfo generateRepayInfo(double curDate) {
        PolicyInfo policyInfo = cardInfo_;
        if (policyInfo.useNumber <= 0 || policyInfo.coolingTime > 0.0) {
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
        repayInfo.money = ((long)(billInfo_.billAmount * rate / 10)) * 10.0;
        repayInfo.repayDate = new Date(billInfo_.billDate.getTime() + (long)curDate * ONEDAYMILLIONSECOND);
        return repayInfo;
    }

    private void updateRepayInfo() {
        PolicyInfo policyInfo = cardInfo_;
        policyInfo.useNumber--;
        policyInfo.coolingTime = policyInfo.useInterval;
    }

    private void updateRuleInfo(String ruleUUID) {
        for (int index = 0; index < ruleList_.size(); ++index) {
            if (ruleList_.get(index).ruleUUID != ruleUUID) {
                continue;
            }
            PolicyInfo policyInfo = ruleList_.get(index);
            policyInfo.useNumber--;
            policyInfo.coolingTime = policyInfo.useInterval;
        }
    }

    private void updateRepayCoolingTime(double nextDateLimit) {
        PolicyInfo policyInfo = cardInfo_;
        if (policyInfo.useNumber > 0) {
            policyInfo.coolingTime -= nextDateLimit;
        }
    }

    private void updateRuleCoolingTime(double nextDateLimit) {
        Iterator<RuleInfo> iterator = ruleList_.iterator();
        while (iterator.hasNext()) {
            PolicyInfo policyInfo = iterator.next();
            if (policyInfo.useNumber > 0) {
                policyInfo.coolingTime -= nextDateLimit;
            }
        }
    }

    private RuleInfo nextRule() {
        ArrayList<Integer> randomList = new ArrayList<Integer>();
        while (true) {
            if (ruleList_.size() == 0) {
                return null;
            }
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

    private double nextDateLimit(double dateLimit) {
        return (Double.max((long)(random.nextDouble() * dateLimit / cardInfo_.repayNum * REPAYDATEFIXEDLIMIT), cardInfo_.repayInterval) * 10) / 10.0;
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

    private ArrayList<HashMap<String, Object>> fetchRuleList(String bankUUID) throws Exception {
        String whereSql = new String();
        if (!UserUtils.isAdmin(salemanID)) {
            whereSql += "and rulesaleman.salemanuuid='" + salemanID + "'";
        }

        return PosDbManager.executeSql(
                "SELECT\n" +
                "ruletb.uuid\n" +
                "FROM\n" +
                "ruletb\n" +
                "LEFT JOIN swingtimetb ON swingtimetb.uuid = ruletb.swingtimeuuid\n" +
                "INNER JOIN rulebank ON rulebank.ruleuuid = ruletb.uuid\n" +
                "INNER JOIN rulesaleman ON rulesaleman.ruleuuid = ruletb.uuid\n" +
                "where rulebank.bankuuid='" + bankUUID + "' " +
                whereSql
        );
    }

    private ArrayList<RuleInfo> fetchRuleList(String bankUUID, Double posRate) throws Exception {
        ArrayList<SQLUtils.WhereCondition> whereConditions = new ArrayList<SQLUtils.WhereCondition>() {
            {
                add(new SQLUtils.WhereCondition("rulebank.bankuuid", "=", SQLUtils.ConvertToSqlString(bankUUID)));
                add(new SQLUtils.WhereCondition("rulesaleman.salemanuuid", "=", SQLUtils.ConvertToSqlString(salemanID), !UserUtils.isAdmin(salemanID)));
                add(new SQLUtils.WhereCondition("ratetb.rate", "=", posRate.toString()));
            }
        };

        String whereSql = SQLUtils.BuildWhereCondition(whereConditions);
        if (!whereSql.isEmpty()) {
            whereSql = " where " + whereSql;
        }

        return convertRuleList((ArrayList<HashMap<String, Object>>)PosDbManager.executeSql(
                "SELECT\n" +
                "ruletb.uuid,\n" +
                "swingtimetb.starttime,\n" +
                "swingtimetb.endtime,\n" +
                "ruletb.minswingmoney,\n" +
                "ruletb.maxswingmoney,\n" +
                "ruletb.posserveruuid ruleposserveruuid,\n" +
                "ruletb.industryuuid ruleindustryuuid,\n" +
                "ruletb.rateuuid rulerateuuid,\n" +
                "ruletb.mccuuid rulemccuuid,\n" +
                "ruletb.ruleusefre,\n" +
                "ruletb.ruleuseinterval,\n" +
                "rulesaleman.salemanuuid,\n" +
                "postb.uuid AS posuuid,\n" +
                "postb.industryuuid posindustryuuid,\n" +
                "postb.rateuuid posrateuuid,\n" +
                "postb.mccuuid posmccuuid,\n" +
                "postb.posserveruuid posposserveruuid\n" +
                "FROM\n" +
                "ruletb\n" +
                "LEFT JOIN swingtimetb ON swingtimetb.uuid = ruletb.swingtimeuuid\n" +
                "INNER JOIN rulebank ON rulebank.ruleuuid = ruletb.uuid\n" +
                "INNER JOIN rulesaleman ON rulesaleman.ruleuuid = ruletb.uuid\n" +
                "INNER JOIN postb\n" +
                "INNER JOIN ratetb ON postb.rateuuid = ratetb.uuid" +
                whereSql
        ));
    }

    private ArrayList<RuleInfo> convertRuleList(ArrayList<HashMap<String, Object>> ruleList) {
        ArrayList<RuleInfo> ruleInfoList = new ArrayList<RuleInfo>();
        if (ruleList != null) {
            Iterator<HashMap<String, Object>> iterator = ruleList.iterator();
            while (iterator.hasNext()) {
                RuleInfo ruleInfo = new RuleInfo();
                HashMap<String, Object> sqlRuleInfo = iterator.next();
                ruleInfo.ruleUUID = sqlRuleInfo.get("UUID").toString();
                ruleInfo.posUUID = sqlRuleInfo.get("POSUUID").toString();
                ruleInfo.swingStartTime = StringUtils.convertNullableString(sqlRuleInfo.get("STARTTIME")).isEmpty() ? Time.valueOf("00:00:00") : Time.valueOf(sqlRuleInfo.get("STARTTIME").toString());
                ruleInfo.swingEndTime = StringUtils.convertNullableString(sqlRuleInfo.get("ENDTIME")).isEmpty() ? Time.valueOf("23:59:59") : Time.valueOf(sqlRuleInfo.get("ENDTIME").toString());
                ruleInfo.minSwingMoney = StringUtils.convertNullableString(sqlRuleInfo.get("MINSWINGMONEY")).isEmpty() ? 0.0 : Double.parseDouble(sqlRuleInfo.get("MINSWINGMONEY").toString());
                ruleInfo.maxSwingMoney = StringUtils.convertNullableString(sqlRuleInfo.get("MAXSWINGMONEY")).isEmpty() ? Double.MAX_VALUE : Double.parseDouble(sqlRuleInfo.get("MAXSWINGMONEY").toString());
                ruleInfo.industryUUID = sqlRuleInfo.get("RULEINDUSTRYUUID").toString();
                ruleInfo.posServerUUID = sqlRuleInfo.get("RULEPOSSERVERUUID").toString();
                ruleInfo.rateUUID = sqlRuleInfo.get("RULERATEUUID").toString();
                ruleInfo.mccUUID = sqlRuleInfo.get("RULEMCCUUID").toString();
                ruleInfo.posIndustryUUID = sqlRuleInfo.get("POSINDUSTRYUUID").toString();
                ruleInfo.posPosServerUUID = sqlRuleInfo.get("POSPOSSERVERUUID").toString();
                ruleInfo.posRateUUID = sqlRuleInfo.get("POSRATEUUID").toString();
                ruleInfo.posMccUUID = sqlRuleInfo.get("POSMCCUUID").toString();
                ruleInfo.ruleUseFre = StringUtils.convertNullableString(sqlRuleInfo.get("RULEUSEFRE")).isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(sqlRuleInfo.get("RULEUSEFRE").toString());
                ruleInfo.ruleUseInterval = StringUtils.convertNullableString(sqlRuleInfo.get("RULEUSEINTERVAL")).isEmpty() ? 0 : Double.parseDouble(sqlRuleInfo.get("RULEUSEINTERVAL").toString());
                if (!ruleInfo.industryUUID.isEmpty() && ruleInfo.industryUUID.compareTo(ruleInfo.posIndustryUUID) != 0)
                    continue;
                if (!ruleInfo.posServerUUID.isEmpty() && ruleInfo.posServerUUID.compareTo(ruleInfo.posPosServerUUID) != 0)
                    continue;
                if (!ruleInfo.rateUUID.isEmpty() && ruleInfo.rateUUID.compareTo(ruleInfo.posRateUUID) != 0)
                    continue;
                if (!ruleInfo.mccUUID.isEmpty() && ruleInfo.mccUUID.compareTo(ruleInfo.posMccUUID) != 0)
                    continue;
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
        cardInfo.maxPosMean = Double.parseDouble(sqlCardInfo.get(0).get("MAXPOSMEAN").toString());
        cardInfo.reservedSwingMoney = Double.parseDouble(sqlCardInfo.get(0).get("RESERVEDSWINGMONEY").toString()) * cardInfo.creditAmount;
        cardInfo.reservedSwingCount = Double.parseDouble(sqlCardInfo.get(0).get("RESERVEDSWINGCOUNT").toString());
        return cardInfo;
    }

    private ArrayList<Double> fetchPosRateList() throws Exception {
        ArrayList<SQLUtils.WhereCondition> whereConditions = new ArrayList<SQLUtils.WhereCondition>() {
            {
                add(new SQLUtils.WhereCondition("salemanuuid", "=", SQLUtils.ConvertToSqlString(salemanID), !UserUtils.isAdmin(salemanID)));
            }
        };

        String whereSql = SQLUtils.BuildWhereCondition(whereConditions);
        if (!whereSql.isEmpty()) {
            whereSql = " where " + whereSql;
        }

        ArrayList<HashMap<String, Object>> posRateList = (ArrayList<HashMap<String, Object>>)PosDbManager.executeSql("SELECT DISTINCT\n" +
                "ratetb.rate\n" +
                "FROM\n" +
                "postb\n" +
                "INNER JOIN ratetb ON ratetb.uuid = postb.rateuuid" + whereSql);

        return convertPosRateList(posRateList);
    }

    private ArrayList<Double> convertPosRateList(ArrayList<HashMap<String, Object>> sqlPosRateList) throws Exception {
        if (sqlPosRateList == null || sqlPosRateList.size() <= 0)
            return null;

        ArrayList<Double> posRateList = new ArrayList<>();
        Iterator<HashMap<String, Object>> iterator = sqlPosRateList.iterator();
        while (iterator.hasNext()) {
            HashMap<String, Object> posRate = iterator.next();
            posRateList.add(Double.parseDouble(posRate.get("RATE").toString()));
        }

        return posRateList;
    }

    private String salemanID;
    private BillInfo billInfo_;
    private CardInfo cardInfo_;
    ArrayList<RuleInfo> ruleList_;
    private static Random random = new Random();
    private final double REPAYFIXEDLIMIT = 0.3;
    private final double REPAYDATEFIXEDLIMIT = 1.3;
    private final double SWINGAMOUNTFIXEDLIMIT = 0.1;
    private final long ONEDAYMILLIONSECOND = 24 * 60 * 60 * 1000;
    private final double LASTFIXEDSWINGCARDMONEY = 20;
    private String lastError;
}