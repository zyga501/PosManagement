package com.posmanagement.policy;

import com.posmanagement.utils.PosDbManager;

import java.sql.Date;
import java.util.*;

public class SalemanFeePolicy {
    public static void main(String[] args) throws Exception {
        SalemanFeePolicy policy = new SalemanFeePolicy();
        policy.updateSalemanFee();
    }

    private class SalemanFeeInfo
    {
        public String uuid;
        public Date paymenttm;
        double feeqk;
        double feeper;
        boolean status;
    };

    public void updateSalemanFee() throws Exception {
        ArrayList<SalemanFeeInfo> salemanFeeInfoList = convertSalemanFeeInfo(fetchSalemanFeeInfo());
        if (salemanFeeInfoList.size() <= 0) {
            return;
        }

        Calendar lastOfDate = Calendar.getInstance();
        lastOfDate.set(Calendar.DAY_OF_MONTH, lastOfDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        Calendar curDate = Calendar.getInstance();
        curDate.setTime(new java.util.Date());
        boolean isLastDate = lastOfDate.get(Calendar.DATE) == curDate.get(Calendar.DATE);
        Date sqlDate = new Date(curDate.getTime().getTime());

        Iterator<SalemanFeeInfo>  iterator = salemanFeeInfoList.iterator();
        while (iterator.hasNext()) {
            SalemanFeeInfo salemanFeeInfo = iterator.next();
            Calendar paymentDate = Calendar.getInstance();
            paymentDate.setTime(salemanFeeInfo.paymenttm);
            if (paymentDate.get(Calendar.DATE) == curDate.get(Calendar.DATE)
                || (isLastDate && paymentDate.get(Calendar.DATE) > curDate.get(Calendar.DATE))) {
                if (salemanFeeInfo.feeqk - salemanFeeInfo.feeper > 0) {
                    salemanFeeInfo.feeqk -= salemanFeeInfo.feeper;
                    salemanFeeInfo.status = true;
                }
                else {
                    salemanFeeInfo.status = false;
                }
                updateSalemanFeeInfo(salemanFeeInfo);
            }
        }
    }

    private ArrayList<HashMap<String, Object>> fetchSalemanFeeInfo() throws Exception {
        return (ArrayList<HashMap<String, Object>>) PosDbManager.executeSql("select * from salesmantb where status='enable'");
    }

    private ArrayList<SalemanFeeInfo> convertSalemanFeeInfo(ArrayList<HashMap<String, Object>> sqlSalemanFeeInfoList) {
        ArrayList<SalemanFeeInfo> salemanFeeInfoList = new ArrayList<SalemanFeeInfo>();
        if (salemanFeeInfoList != null) {
            Iterator<HashMap<String, Object>> iterator = sqlSalemanFeeInfoList.iterator();
            while (iterator.hasNext()) {
                SalemanFeeInfo salemanFeeInfo = new SalemanFeeInfo();
                HashMap<String, Object> sqlSalemanFeeInfo = iterator.next();
                salemanFeeInfo.uuid = sqlSalemanFeeInfo.get("UID").toString();
                salemanFeeInfo.paymenttm = new Date(Date.valueOf(sqlSalemanFeeInfo.get("PAYMENTTM").toString()).getTime() + ONEDAYMILLIONSECOND);
                salemanFeeInfo.feeqk = Double.valueOf(sqlSalemanFeeInfo.get("FEEQK").toString());
                salemanFeeInfo.feeper = Double.valueOf(sqlSalemanFeeInfo.get("FEEPER").toString());
                salemanFeeInfo.status = true;
                salemanFeeInfoList.add(salemanFeeInfo);
            }
        }

        return salemanFeeInfoList;
    }

    private boolean updateSalemanFeeInfo(SalemanFeeInfo salemanFeeInfo) throws Exception {
        Map parametMap = new HashMap();
        parametMap.put(1, salemanFeeInfo.status ? "enable" : "disable");
        parametMap.put(2, salemanFeeInfo.feeqk);
        parametMap.put(3, salemanFeeInfo.uuid);
        return PosDbManager.executeUpdate("" +
                "update salesmantb\n" +
                "set `status`=?,\n" +
                "feeqk=?\n" +
                "WHERE uid=?", (HashMap<Integer, Object>)parametMap);
    }

    private final long ONEDAYMILLIONSECOND = 24 * 60 * 60 * 1000;
}
